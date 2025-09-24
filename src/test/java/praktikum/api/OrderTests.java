package praktikum.api;

import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class OrderTests extends BaseApiTest {

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    public void createOrderWithoutIngredients() {
        String body = "{ \"ingredients\": [] }";

        given()
                .header("Content-type", "application/json")
                .body(body)
                .post("/orders")
                .then()
                .statusCode(400)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с авторизацией")
    public void createOrderWithAuth() {

        String email = "user" + System.currentTimeMillis() + "@mail.ru";
        String password = "123456";
        String name = "TestUser";

        String accessToken =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"email\":\"" + email + "\", \"password\":\"" + password + "\", \"name\":\"" + name + "\"}")
                        .post("/auth/register")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("accessToken");

        if (!accessToken.startsWith("Bearer ")) {
            accessToken = "Bearer " + accessToken;
        }


        String ingredientsBody = "{ \"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\", \"61c0c5a71d1f82001bdaaa6f\", \"61c0c5a71d1f82001bdaaa72\"] }";

        given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(ingredientsBody)
                .post("/orders")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }


    @Test @DisplayName("Создание заказа без авторизации")
    public void createOrderWithoutAuth() {
        given()
                .header("Content-type", "application/json")
                .body("{\"ingredients\": [\"60d3b41abdacab0026a733c6\"]}")
                .post("/orders")
                .then()
                .statusCode(400);
    }


    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    public void createOrderWithInvalidIngredient() {
        String ingredientsBody = "{ \"ingredients\": [\"invalidhash\"] }";

        given()
                .header("Content-type", "application/json")
                .body(ingredientsBody)
                .post("/orders")
                .then()
                .statusCode(500);
    }


    @Test
    @DisplayName("Создание заказа с ингредиентами и авторизацией")
    public void createOrderWithIngredients() {

        String email = "user" + System.currentTimeMillis() + "@mail.ru";
        String password = "123456";
        String name = "TestUser";

        String accessToken =
                given()
                        .header("Content-type", "application/json")
                        .body("{\"email\":\"" + email + "\", \"password\":\"" + password + "\", \"name\":\"" + name + "\"}")
                        .post("/auth/register")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path("accessToken");

        if (!accessToken.startsWith("Bearer ")) {
            accessToken = "Bearer " + accessToken;
        }

        String ingredientsBody = "{ \"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\", \"61c0c5a71d1f82001bdaaa6f\", \"61c0c5a71d1f82001bdaaa72\"] }";

        given()
                .header("Content-type", "application/json")
                .header("Authorization", accessToken)
                .body(ingredientsBody)
                .post("/orders")
                .then()
                .statusCode(200)
                .body("success", equalTo(true));
    }
}


