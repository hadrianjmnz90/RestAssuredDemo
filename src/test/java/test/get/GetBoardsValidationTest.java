package test.get;

import arguments.holders.BoardIdValidationArgumentsHolder;
import arguments.providers.BoardIdValidationArgumentsProvider;
import consts.BoardEndpoints;
import consts.StatusCodes;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import test.BaseTest;

import java.util.Map;

public class GetBoardsValidationTest extends BaseTest {

    @ParameterizedTest
    @ArgumentsSource(BoardIdValidationArgumentsProvider.class)
    public void checkGetBoardWithInvalidId(BoardIdValidationArgumentsHolder validationArguments) {
        Response response = requestWithAuth()
                .pathParams(validationArguments.getPathParams())
                .get(BoardEndpoints.GET_BOARD_URL);
        response
                .then()
                .statusCode(validationArguments.getStatusCode());
        Assertions.assertEquals(validationArguments.getErrorMessage(), response.body().asString());

        System.out.println(validationArguments.getPathParams());
        System.out.println(validationArguments.getStatusCode());
    }

    @Test
    public void checkGetBoardWithInvalidAuth() {
        Response response = requestWithoutAuth()
                .pathParam("id", "60d847d9aad2437cb984f8e0")
                .get(BoardEndpoints.GET_BOARD_URL);
        response
                .then()
                .statusCode(StatusCodes.CODE404);
        Assertions.assertEquals("unauthorized permission requested", response.body().asString());
    }

    @Test
    public void checkGetBoardWithAnotherUserCredentials() {
        Response response = requestWithoutAuth()
                .queryParams(Map.of(
                        "key", "8b32218e6887516d17c84253faf967b6",
                        "token", "492343b8106e7df3ebb7f01e219cbf32827c852a5f9e2b8f9ca296b1cc604955"
                ))
                .pathParam("id", "60d847d9aad2437cb984f8e0")
                .get(BoardEndpoints.GET_BOARD_URL);
        response
                .then()
                .statusCode(StatusCodes.CODE401);
        Assertions.assertEquals("invalid token", response.body().asString());
    }
}