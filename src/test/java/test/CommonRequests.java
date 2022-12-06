package test;

import consts.BoardEndpoints;
import consts.BoardTestData;
import consts.StatusCodes;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

public class CommonRequests extends BaseTest{

    public  String createBoard(String boardName) {
        String boardId;
        String responseBoardName;
        Response response = requestWithAuth()
                .body(Map.of("name", boardName))
                .contentType(ContentType.JSON)
                .post(BoardEndpoints.CREATE_BOARD_URL);
        boardId = response.body().jsonPath().get("id");
        System.out.println("BOARD ID: " + boardId);
        responseBoardName = response.body().jsonPath().get("name");
        System.out.println("BOARD NAME FROM RESPONSE JSON: " + responseBoardName );
        response
                .then()
                .statusCode(StatusCodes.CODE200)
                .body("name", equalTo(boardName));

        return boardId;
    }

    public void deleteAllBoards() {
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
}
