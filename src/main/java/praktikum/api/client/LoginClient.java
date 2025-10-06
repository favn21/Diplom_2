package praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;


import praktikum.api.model.Login;
import praktikum.api.model.User;


public class LoginClient {

    @Step("Регистрируем пользователя: {email}")
    public ValidatableResponse registerUser(String email, String password, String name) {
        User user = new User(email, password, name);

        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(Endpoints.REGISTER)
                .then();
    }

    @Step("Логинимся под пользователем: {email}")
    public ValidatableResponse loginUser(String email, String password) {
        Login login = new Login(email, password);

        return given()
                .header("Content-type", "application/json")
                .body(login)
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
