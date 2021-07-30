package ru.training.at.hwapi.tests.steps;

import static ru.training.at.hwapi.core.TrelloBoardServiceObj.getBoard;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.getBoards;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.goodResponseSpec;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.notFoundResponseSpec;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.requestBuilder;

import com.github.javafaker.Faker;
import io.restassured.http.Method;
import java.util.List;
import ru.training.at.hwapi.beans.TrelloBoard;
import ru.training.at.hwapi.data.Constants;

public class TrelloBoardTestSteps {

    public static TrelloBoard createBoard() {
        Faker faker = new Faker();
        return getBoard(requestBuilder()
            .setName(faker.name().username())
            .setMethod(Method.POST)
            .buildRequest()
            .sendRequest(Constants.BOARDS_URL)
            .then()
            .assertThat()
            .spec(goodResponseSpec())
            .extract().response()
        );
    }

    public static TrelloBoard getBoardById(String id) {
        return getBoard(
            requestBuilder()
                .buildRequest()
                .sendRequest(Constants.BOARDS_URL + id)
                .then()
                .assertThat()
                .spec(goodResponseSpec())
                .extract().response()
        );
    }

    public static void getDeletedBoardById(String id) {
            requestBuilder()
                .buildRequest()
                .sendRequest(Constants.BOARDS_URL + id)
                .then()
                .assertThat()
                .spec(notFoundResponseSpec())
                .extract().response();
    }

    public static void deleteBoardById(String boardId) {
        requestBuilder()
            .setMethod(Method.DELETE)
            .buildRequest()
            .sendRequest(Constants.BOARDS_URL + boardId)
            .andReturn();
    }

    public static void deleteAllBoards() {
        List<TrelloBoard> myBoards = getBoards(requestBuilder()
            .buildRequest()
            .sendRequest(Constants.ALL_MY_BOARDS)
            .then()
            .assertThat()
            .spec(goodResponseSpec())
            .extract().response()
        );
        for (TrelloBoard board : myBoards) {
            TrelloBoardTestSteps.deleteBoardById(board.getId());
        }
    }
}
