package praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.api.client.Endpoints;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class LoginClient {

    @Step("Регистрируем пользователя: {email}")
    public ValidatableResponse registerUser(String email, String password, String name) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);
        body.put("name", name);

        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(Endpoints.REGISTER)
                .then();
    }

    @Step("Логинимся под пользователем: {email}")
    public ValidatableResponse loginUser(String email, String password) {
        Map<String, String> body = new HashMap<>();
        body.put("email", email);
        body.put("password", password);

        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(Endpoints.LOGIN)
                .then();
    }

    @Step("Удаляем пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .delete(Endpoints.DELETE_USER)
                .then();
    }
}
