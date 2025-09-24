package praktikum.api;

import io.restassured.RestAssured;
import org.junit.Before;

public class BaseApiTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }
}
