package test.put;

import consts.BoardEndpoints;
import consts.StatusCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import test.BaseTest;
import test.CommonRequests;

import static org.hamcrest.Matchers.equalTo;

public class UpdateBoardTest extends BaseTest {

    final String boardName= "New Board";
    final String updatedBoardName = "Updated Board Test";
    CommonRequests commonRequests = new CommonRequests();
    @Test
    public void updateBoardNameTest() {

        String boardId = commonRequests.createBoard(boardName);
        requestWithAuth()
                .pathParam("id", boardId)
                .queryParam("name", updatedBoardName)
                .put(BoardEndpoints.UPDATE_BOARD)
                .then().statusCode(StatusCodes.CODE200)
                .body("name", equalTo(updatedBoardName));
    }

    @BeforeEach
    public void deleteAllBoards(){
        commonRequests.deleteAllBoards();
    }

}
