package test.post;

import consts.BoardEndpoints;
import consts.StatusCodes;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import test.BaseTest;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class CreateBoardTest extends BaseTest {

    //delete request is also tested
    private String createdBoardId;

    @Test
    public void testCreateBoard() {
        String boardName = "RestAssured Create Board Test";
        Response response = requestWithAuth()
                .body(Map.of("name", boardName))
                .contentType(ContentType.JSON)
                .post(BoardEndpoints.CREATE_BOARD_URL);
        createdBoardId = response.body().jsonPath().get("id");
        System.out.println("BOARD ID: " + createdBoardId);
        response
                .then()
                .statusCode(StatusCodes.CODE200)
                .body("name", equalTo(boardName));
    }

      @Test
    public void deleteCreatedBoard() {
        createdBoardId = "676b2d3ef242fb19f034c98c";
        requestWithAuth()
                .pathParam("id", createdBoardId)
                .delete(BoardEndpoints.DELETE_BOARD_URL)
                .then()
                .statusCode(StatusCodes.CODE200);
    }
}