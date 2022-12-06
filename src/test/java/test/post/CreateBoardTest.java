package test.post;

import consts.BoardEndpoints;
import consts.BoardTestData;
import consts.StatusCodes;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.BaseTest;

import java.util.List;
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

    @BeforeEach
    public void testDeleteAllBoards() {
        Response response = requestWithAuth()
                .queryParam("fields", "id,name")
                .pathParam("member", BoardTestData.MEMBER)
                .get(BoardEndpoints.GET_ALL_MEMBER_BOARDS_URL);
        response
                .then()
                .statusCode(StatusCodes.CODE200);

        List<String> boardIds = response.body().jsonPath().get("id");
        if (!boardIds.isEmpty()) {
            for (String boardId : boardIds) {
                requestWithAuth()
                        .pathParam("id", boardId)
                        .delete(BoardEndpoints.DELETE_BOARD_URL)
                        .then()
                        .statusCode(StatusCodes.CODE200);
            }
        } else {
            System.out.println("No boards to delete");
        }
    }

    //  @Test
    public void deleteCreatedBoard() {
        requestWithAuth()
                .pathParam("id", createdBoardId)
                .delete(BoardEndpoints.DELETE_BOARD_URL)
                .then()
                .statusCode(StatusCodes.CODE200);
    }
}