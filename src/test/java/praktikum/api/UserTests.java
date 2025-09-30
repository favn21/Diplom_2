package praktikum.api;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
@Epic("Пользователи")
@Feature("Регистрация")
public class UserTests extends BaseApiTest {

    @Test
    @Story("Регистрация нового уникального пользователя")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Создание уникального пользователя")
    @Description("Проверяем, что API позволяет зарегистрировать пользователя с уникальными данными")
    public void createUniqueUser() {
        String body = "{ \"email\": \"test" + System.currentTimeMillis() + "@mail.ru\", " +
                "\"password\": \"123456\", \"name\": \"TestUser\" }";

        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @Story("Ошибка при регистрации без email")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание пользователя без email")
    @Description("Проверяем, что регистрация без email невозможна и возвращается ошибка 403")
    public void createUserWithoutEmail() {
        String body = "{ \"password\": \"123456\", \"name\": \"TestUser\" }";

        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/auth/register")
                .then()
                .statusCode(403)
                .body("message", equalTo("Email, password and name are required fields"));
    }
    @Test
    @Story("Ошибка при регистрации уже существующего пользователя")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверяем, что API не позволяет зарегистрировать пользователя с существующим email")
    public void createExistingUser() {
        String email = "existing" + System.currentTimeMillis() + "@mail.ru";
        String password = "123456";
        String name = "TestUser";


        given()
                .header("Content-type", "application/json")
                .body("{\"email\":\"" + email + "\", \"password\":\"" + password + "\", \"name\":\"" + name + "\"}")
                .post("/auth/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));


        given()
                .header("Content-type", "application/json")
                .body("{\"email\":\"" + email + "\", \"password\":\"" + password + "\", \"name\":\"" + name + "\"}")
                .post("/auth/register")
                .then()
                .statusCode(403)
                .body("message", equalTo("User already exists"));
    }

}

