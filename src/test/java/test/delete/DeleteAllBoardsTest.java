package test.delete;

import consts.BoardEndpoints;
import consts.BoardTestData;
import consts.StatusCodes;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.BaseTest;

import java.util.List;

public class DeleteAllBoardsTest extends BaseTest {

    @Test
    public void testDeleteAllBoards() {
        Response response = requestWithAuth()
                .queryParam("fields", "id,name")
                .get(BoardEndpoints.GET_ALL_MEMBER_BOARDS_URL);
        response
                .then()
                .statusCode(StatusCodes.CODE200);

        List<String> boardIds = response.body().jsonPath().get("id");
        if (!boardIds.isEmpty()) {
            System.out.println("There are boards to delete, deleting now...");
            for (String boardId : boardIds) {
                requestWithAuth()
                        .pathParam("id", boardId)
                        .delete(BoardEndpoints.DELETE_BOARD_URL)
                        .then()
                        .statusCode(StatusCodes.CODE200);
                System.out.println("deleted board id: " + boardId);
            }
        } else {
            System.out.println("No boards to delete");
        }
        boardIds = response.body().jsonPath().get("id");
        Assertions.assertTrue(
                boardIds == null || boardIds.isEmpty(),
                "Se esperaban 0 boards, pero se encontraron: " + (boardIds != null ? boardIds.size() : "null")
        );
    }

}
