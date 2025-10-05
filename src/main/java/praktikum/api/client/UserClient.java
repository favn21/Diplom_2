package praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import praktikum.api.model.User;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;


public class UserClient {

    @Step("Регистрируем пользователя: {email}")
    public ValidatableResponse registerUser(String email, String password, String name) {
        Map<String, String> body = new HashMap<>();
        if (email != null) body.put("email", email);
        if (password != null) body.put("password", password);
        if (name != null) body.put("name", name);

        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(Endpoints.REGISTER)
                .then();
    }
    public ValidatableResponse registerUser(User user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .when()
                .post("/auth/register")
                .then();
    }

    public ValidatableResponse deleteUser(String accessToken) {
        return given()
                .header("Authorization", accessToken)
                .when()
                .delete("/auth/user")
                .then();
    }
}


