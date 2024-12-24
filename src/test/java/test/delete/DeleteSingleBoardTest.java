package test.delete;

import consts.BoardEndpoints;
import consts.StatusCodes;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import test.BaseTest;

import java.util.List;

public class DeleteSingleBoardTest extends BaseTest {

    @Test
    public void testDeleteSingleBoard() {
        Response response = requestWithAuth().queryParam("fields", "id,name").
                get(BoardEndpoints.GET_ALL_MEMBER_BOARDS_URL);
        response.then().statusCode(StatusCodes.CODE200);

        List<String> boardIds = response.body().jsonPath().get("id");
        String boardId = boardIds.get(0);
        System.out.println(boardId);

        requestWithAuth().pathParam("id", boardId)
                .delete(BoardEndpoints.DELETE_BOARD_URL)
                .then().statusCode(StatusCodes.CODE200);
        System.out.println("deleted board id: " + boardId);

    }
}
