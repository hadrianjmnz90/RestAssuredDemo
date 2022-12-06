package test.get;

import consts.BoardEndpoints;
import consts.BoardTestData;
import consts.StatusCodes;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.BaseTest;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

public class GetBoardsTest extends BaseTest {

    private static String createdBoardId;
    private static String boardName = "Create Board Test";
    private static String cardName = "this is a new card";

    @Test
    public void checkGetAllBoards() {
        Response response = requestWithAuth()
                .queryParam("fields", "id,name")
                .pathParam("member", BoardTestData.MEMBER)
                .get(BoardEndpoints.GET_ALL_MEMBER_BOARDS_URL);
        response
                .then()
                .statusCode(StatusCodes.CODE200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/get_boards.json"));
        System.out.println("****" + response.body().asString());
    }

    @Test
    public void checkGetSingleBoard() {
        createdBoardId = createBoard();
        requestWithAuth().pathParam("id", createdBoardId)
                .get(BoardEndpoints.GET_BOARD_URL)
                .then()
                .statusCode(StatusCodes.CODE200)
                .body("name", equalTo(boardName));
    }

    @Test
    public void checkGetListsFromSpecificBoard() {
        createdBoardId = createBoard();
        requestWithAuth().
                pathParam("id", createdBoardId)
                .get(BoardEndpoints.GET_BOARD_LISTS)
                .then()
                .statusCode(StatusCodes.CODE200)
                .body("name[0]", equalTo("To Do"))
                .body("name[1]", equalTo("Doing"))
                .body("name[2]", equalTo("Done"))
                .log().body();
    }

    @Test
    public void checkGetAllCardsFromAList() {
        createdBoardId = createBoard();
        String listId = getFirstListIdFromBoard(createdBoardId);
        requestWithAuth().
                pathParam("id", listId)
                .get(BoardEndpoints.GET_BOARD_CARDS)
                .then()
                .statusCode(StatusCodes.CODE200)
                .body("", hasSize(0))
                .log().body();
    }

    @Test
    public void checkGetSingleCardFromAList() {
        createdBoardId = createBoard();
        String cardId = getCreateNewCardId(createdBoardId);
        requestWithAuth().
                pathParam("id", cardId) // card id
                .get(BoardEndpoints.GET_SINGLE_CARD)
                .then()
                .statusCode(StatusCodes.CODE200)
                .body("name", equalTo(cardName))
                .log().body();
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
                System.out.println("Deleting board: " + boardId);
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

    public String createBoard() {
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
                .statusCode(200)
                .body("name", equalTo(boardName));

        return boardId;
    }


    public String getFirstListIdFromBoard(String boardId) {
        Response response = requestWithAuth().
                pathParam("id", boardId) //board id
                .get(BoardEndpoints.GET_BOARD_LISTS);
        response
                .then()
                .statusCode(200);
        String listId = response.body().jsonPath().get("id[0]");
        System.out.println("list id: " + listId);
        return listId;
    }


    public String getCreateNewCardId(String boardId) {
        Response response = requestWithAuth()
                .queryParam("idList", getFirstListIdFromBoard(boardId))
                .body(Map.of("name", cardName))
                .contentType(ContentType.JSON)
                .post(BoardEndpoints.CREATE_CARD_FOR_A_LIST);
        cardName = response.body().jsonPath().get("name");
        String cardId = response.body().jsonPath().get("id");
        System.out.println("CARD NAME: " + cardName);
        System.out.println("CARD ID: " + cardId);
        response
                .then()
                .statusCode(200).log().body();
        return cardId;
    }
}