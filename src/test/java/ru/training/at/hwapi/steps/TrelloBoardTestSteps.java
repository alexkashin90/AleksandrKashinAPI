package ru.training.at.hwapi.steps;

import static ru.training.at.hwapi.core.TrelloBoardServiceObj.getBoard;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.goodResponseSpec;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.notFoundResponseSpec;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.requestBuilder;

import io.qameta.allure.Step;
import io.restassured.http.Method;
import ru.training.at.hwapi.beans.TrelloBoard;
import ru.training.at.hwapi.core.TrelloBoardServiceObj;
import ru.training.at.hwapi.data.Constants;

public class TrelloBoardTestSteps {

    @Step("Create test board with chosen name")
    public static TrelloBoard createTestBoard(String boardName) {
        return getBoard(requestBuilder()
            .setName(boardName)
            .setMethod(Method.POST)
            .buildRequest()
            .sendRequest(Constants.BOARDS_URL)
            .then()
            .assertThat()
            .spec(goodResponseSpec())
            .extract().response()
        );
    }

    @Step("Get board by specific ID")
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

    @Step("Update board's name")
    public static TrelloBoard updateBoardsNameById(String name, String id) {
        return getBoard(
            requestBuilder()
                .setMethod(Method.PUT)
                .setName(name)
                .buildRequest()
                .sendRequest(Constants.BOARDS_URL + id)
                .then()
                .assertThat()
                .spec(goodResponseSpec())
                .extract().response()
        );
    }

    @Step("Update board's description")
    public static TrelloBoard updateBoardsDescriptionById(String description, String id) {
        return getBoard(
            requestBuilder()
                .setMethod(Method.PUT)
                .setDescription(description)
                .buildRequest()
                .sendRequest(Constants.BOARDS_URL + id)
                .then()
                .assertThat()
                .spec(goodResponseSpec())
                .extract().response()
        );
    }

    @Step("Delete board")
    public static void deleteBoard(String boardId) {
        requestBuilder()
            .setMethod(Method.DELETE)
            .buildRequest()
            .sendRequest(Constants.BOARDS_URL + boardId)
            .andReturn();
    }

    @Step("Get deleted board")
    public static void getDeletedBoard(String boardId) {
        requestBuilder()
            .setMethod(Method.GET)
            .setBoardId(boardId)
            .buildRequest()
            .sendRequest(Constants.BOARDS_URL + boardId)
            .then()
            .assertThat()
            .spec(notFoundResponseSpec())
            .extract().response();
    }
}
