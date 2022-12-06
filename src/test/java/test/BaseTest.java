package test;

import consts.BoardEndpoints;
import consts.BoardTestData;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    @BeforeAll
    public static void setBaseUrl() {
        RestAssured.baseURI = BoardEndpoints.BASE_URL;
    }

    protected RequestSpecification requestWithAuth() {
        return RestAssured.given()
                .queryParams(BoardTestData.USER_KEY_TOKEN);
    }

    protected RequestSpecification requestWithoutAuth() {
        return RestAssured.given();
    }
}
