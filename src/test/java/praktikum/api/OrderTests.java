package praktikum.api;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Epic("Заказы")
@Feature("Создание заказа")
public class OrderTests extends BaseApiTest {

    @Test
    @Story("Ошибка при отсутствии ингредиентов")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверяем, что API возвращает ошибку при попытке создать заказ без указания ингредиентов")
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
    @Story("Создание заказа с авторизацией")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание заказа с авторизованным пользователем")
    @Description("Регистрируем пользователя, авторизуемся и создаем заказ с ингредиентами")
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


    @Test
    @Story("Создание заказа без авторизации")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание заказа без токена доступа")
    @Description("Проверяем, что API не позволяет создавать заказ без авторизации")
    public void createOrderWithoutAuth() {
        given()
                .header("Content-type", "application/json")
                .body("{\"ingredients\": [\"60d3b41abdacab0026a733c6\"]}")
                .post("/orders")
                .then()
                .statusCode(400);
    }


    @Test
    @Story("Ошибка при неверном ингредиенте")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверяем, что при передаче некорректного идентификатора ингредиента возвращается ошибка 500")
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
    @Story("Создание заказа с ингредиентами и авторизацией")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Успешное создание заказа с ингредиентами")
    @Description("Регистрируем пользователя, авторизуемся и проверяем успешное создание заказа с валидными ингредиентами")
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


