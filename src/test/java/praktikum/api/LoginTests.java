package praktikum.api;

import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.api.client.LoginClient;
import praktikum.api.client.UserClient;
import praktikum.api.model.User;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@Epic("Авторизация")
@Feature("Логин пользователя")
public class LoginTests extends BaseApiTest {

    private final LoginClient loginClient = new LoginClient();
    private final UserClient userClient = new UserClient();
    private User testUser;
    private String accessToken;
    private String email;
    private String password;
    private String name;

    private boolean needValidUser = false;



    @Before
    public void setUpExistingUser() {
        email = "existing" + System.currentTimeMillis() + "@mail.ru";
        password = "123456";
        name = "TestUser";

        testUser = new User(email, password, name);

        accessToken = userClient.registerUser(testUser)
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
            userClient.deleteUser(accessToken)
                    .statusCode(SC_ACCEPTED);
        }
    }

    @Test
    @Story("Успешный логин под существующим пользователем")
    @Severity(SeverityLevel.BLOCKER)
    @DisplayName("Успешный логин с корректными данными")
    @Description("Проверяем, что зарегистрированный пользователь может авторизоваться")
    public void loginWithValidUser() {
        loginClient.loginUser(testUser.getEmail(), testUser.getPassword())
                .statusCode(SC_OK)
                .body("success", equalTo(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @Story("Логин с неверным паролем")
    @Severity(SeverityLevel.CRITICAL)
    @DisplayName("Ошибка при логине с неверным паролем")
    @Description("Проверяем, что при неверном пароле возвращается 401 и сообщение об ошибке")
    public void loginWithWrongPassword() {
        loginClient.loginUser(testUser.getEmail(), "WrongPassword123")
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Логин без email")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Ошибка при логине без email")
    @Description("Проверяем, что при отсутствии email возвращается ошибка 401")
    public void loginWithoutEmail() {
        loginClient.loginUser(null, testUser.getPassword())
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }

    @Test
    @Story("Логин без пароля")
    @Severity(SeverityLevel.NORMAL)
    @DisplayName("Ошибка при логине без пароля")
    @Description("Проверяем, что при отсутствии пароля возвращается ошибка 401")
    public void loginWithoutPassword() {
        loginClient.loginUser(testUser.getEmail(), null)
                .statusCode(SC_UNAUTHORIZED)
                .body("message", equalTo("email or password are incorrect"));
    }
}


