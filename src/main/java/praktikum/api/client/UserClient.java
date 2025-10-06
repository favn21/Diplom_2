package praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.api.model.User;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("Регистрируем пользователя: {user.email}")
    public ValidatableResponse registerUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post(Endpoints.REGISTER)
                .then();
    }

    @Step("Удаляем пользователя")
    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete(Endpoints.DELETE_USER)
                .then();
    }
}




