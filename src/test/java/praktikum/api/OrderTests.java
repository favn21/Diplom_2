package praktikum.api;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.client.LoginClient;
import praktikum.api.client.OrderClient;
import praktikum.api.client.UserClient;
import praktikum.api.model.Order;
import praktikum.api.model.User;

import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@Epic("Заказы")
@Feature("Создание заказа")
public class OrderTests extends BaseApiTest {

    private final LoginClient loginClient = new LoginClient();
    private final OrderClient orderClient = new OrderClient();
    private final UserClient userClient = new UserClient();
    private final Faker faker = new Faker();

    private String email;
    private String password;
    private String name;
    private String accessToken;

    @Before
    public void setUpExistingUser() {
        email = "existing" + System.currentTimeMillis() + "@mail.ru";
        password = "123456";
        name = "TestUser";

        User user = new User(email, password, name);

        accessToken = userClient.registerUser(user)
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .extract()
                .path("accessToken");

        if (!accessToken.startsWith("Bearer ")) {
            accessToken = "Bearer " + accessToken;
        }
    }


    @After
    public void tearDown() {
        if (accessToken != null) {
            loginClient.deleteUser(accessToken)
                    .statusCode(SC_ACCEPTED);
        }
    }

    @Test
    @Story("Ошибка при отсутствии ингредиентов")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверяем, что API возвращает ошибку при попытке создать заказ без указания ингредиентов")
    public void createOrderWithoutIngredients() {
        Order emptyOrder = new Order(List.of());
        orderClient.createOrderWithoutAuth(emptyOrder)
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @Test
    @Story("Создание заказа с авторизацией")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание заказа с авторизованным пользователем")
    @Description("Регистрируем пользователя, авторизуемся и создаем заказ с ингредиентами")
    public void createOrderWithAuth() {
        Order order = new Order(List.of(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f",
                "61c0c5a71d1f82001bdaaa72"
        ));

        orderClient.createOrder(accessToken, order)
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @Story("Создание заказа без авторизации")
    @Severity(SeverityLevel.MINOR)
    @DisplayName("Создание заказа без токена доступа")
    @Description("Проверяем, что API не позволяет создавать заказ без авторизации")
    public void createOrderWithoutAuth() {
        Order order = new Order(List.of("60d3b41abdacab0026a733c6"));

        orderClient.createOrderWithoutAuth(order)
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @Story("Ошибка при неверном ингредиенте")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание заказа с неверным хешем ингредиентов")
    @Description("Проверяем, что при передаче некорректного идентификатора ингредиента возвращается ошибка 500")
    public void createOrderWithInvalidIngredient() {
        Order invalidOrder = new Order(List.of("invalidhash"));

        orderClient.createOrderWithoutAuth(invalidOrder)
                .statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @Story("Создание заказа с ингредиентами и авторизацией")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Успешное создание заказа с ингредиентами")
    @Description("Регистрируем пользователя, авторизуемся и проверяем успешное создание заказа с валидными ингредиентами")
    public void createOrderWithIngredients() {
        Order order = new Order(List.of(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f",
                "61c0c5a71d1f82001bdaaa72"
        ));

        orderClient.createOrder(accessToken, order)
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }
}


