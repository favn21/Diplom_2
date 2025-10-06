package praktikum.api.client;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;




import static io.restassured.RestAssured.given;

import praktikum.api.model.Order;

public class OrderClient {

    @Step("Создание заказа с авторизацией")
    public ValidatableResponse createOrder(String accessToken, Order order) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(order)
                .when()
                .post(Endpoints.ORDERS)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuth(Order order) {
        return given()
                .header("Content-type", "application/json")
                .body(order)
                .when()
                .post(Endpoints.ORDERS)
                .then();
    }
}



