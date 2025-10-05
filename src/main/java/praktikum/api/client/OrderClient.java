package praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;


import java.util.List;
import java.util.Map;
import java.util.HashMap;

import static io.restassured.RestAssured.given;


public class OrderClient {

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(String accessToken, List<String> ingredients) {
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", ingredients);

        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(body)
                .when()
                .post(Endpoints.ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(List<String> ingredients) {
        Map<String, Object> body = new HashMap<>();
        body.put("ingredients", ingredients);

        return given()
                .header("Content-type", "application/json")
                .body(body)
                .when()
                .post(Endpoints.ORDERS)
                .then();
    }
}



