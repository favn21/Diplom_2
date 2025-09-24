package praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class UserTests extends BaseApiTest {

    @Test
    @DisplayName("Создание уникального пользователя")
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
    @DisplayName("Создание пользователя без email")
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
    @DisplayName("Создание пользователя, который уже зарегистрирован")
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

