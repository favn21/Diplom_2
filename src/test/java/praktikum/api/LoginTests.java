package praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginTests extends BaseApiTest {

    @Test
    @DisplayName("Логин с неверными данными")
    public void loginWithWrongPassword() {
        String body = "{ \"email\": \"wrong@mail.ru\", \"password\": \"wrongpass\" }";

        given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(401)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @DisplayName("Успешный логин под существующим пользователем")
    public void loginWithValidUser() {

        String email = "test" + System.currentTimeMillis() + "@mail.ru";
        String password = "123456";
        String name = "TestUser";


        given()
                .header("Content-type", "application/json")
                .body("{\"email\":\"" + email + "\", \"password\":\"" + password + "\", \"name\":\"" + name + "\"}")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));


        given()
                .header("Content-type", "application/json")
                .body("{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }
}


