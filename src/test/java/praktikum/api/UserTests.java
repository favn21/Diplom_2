package praktikum.api;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;

import net.datafaker.Faker;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.client.LoginClient;
import praktikum.api.client.UserClient;
import praktikum.api.model.User;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

@Epic("Пользователи")
@Feature("Регистрация")
public class UserTests extends BaseApiTest {

    private final UserClient userClient = new UserClient();
    private final LoginClient loginClient = new LoginClient();
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
    @Story("Регистрация нового уникального пользователя")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Создание уникального пользователя")
    @Description("Проверяем, что API позволяет зарегистрировать пользователя с уникальными данными")
    public void createUniqueUser() {
        String uniqueEmail = "user" + System.currentTimeMillis() + "@mail.ru";
        String uniquePassword = "Pass" + faker.number().digits(6);
        String uniqueName = faker.name().firstName();

        User user = new User(uniqueEmail, uniquePassword, uniqueName);

        userClient.registerUser(user)
                .statusCode(SC_OK)
                .body("success", equalTo(true));
    }

    @Test
    @Story("Ошибка при регистрации без email")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание пользователя без email")
    @Description("Проверяем, что регистрация без email невозможна и возвращается ошибка 403")
    public void createUserWithoutEmail() {
        User userWithoutEmail = new User(null, password, name);

        userClient.registerUser(userWithoutEmail)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Ошибка при регистрации без имени")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание пользователя без имени")
    @Description("Проверяем, что регистрация без имени невозможна и возвращается ошибка 403")
    public void createUserWithoutName() {
        User userWithoutName = new User(email, password, null);

        userClient.registerUser(userWithoutName)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Ошибка при регистрации без пароля")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверяем, что регистрация без пароля невозможна и возвращается ошибка 403")
    public void createUserWithoutPassword() {
        User userWithoutPassword = new User(email, null, name);

        userClient.registerUser(userWithoutPassword)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @Story("Ошибка при регистрации уже существующего пользователя")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Проверяем, что API не позволяет зарегистрировать пользователя с существующим email")
    public void createExistingUser() {
        String existingEmail = "existing" + System.currentTimeMillis() + "@mail.ru";

        User firstUser = new User(existingEmail, password, name);
        User duplicateUser = new User(existingEmail, password, name);

        userClient.registerUser(firstUser)
                .statusCode(SC_OK)
                .body("success", equalTo(true));

        userClient.registerUser(duplicateUser)
                .statusCode(SC_FORBIDDEN)
                .body("message", equalTo("User already exists"));
    }
}

