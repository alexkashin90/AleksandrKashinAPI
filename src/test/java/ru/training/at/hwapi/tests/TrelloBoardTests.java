package ru.training.at.hwapi.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.getBoard;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.goodResponseSpec;
import static ru.training.at.hwapi.core.TrelloBoardServiceObj.requestBuilder;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.createBoard;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.deleteBoardById;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.getBoardById;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.getDeletedBoardById;

import com.github.javafaker.Faker;
import io.restassured.http.Method;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.Test;
import ru.training.at.hwapi.beans.TrelloBoard;
import ru.training.at.hwapi.data.Background;
import ru.training.at.hwapi.data.Constants;
import ru.training.at.hwapi.data.PermissionLevel;
import ru.training.at.hwapi.dataproviders.TrelloBoardTestDataProvider;
import ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps;

public class TrelloBoardTests extends BaseTest {

    @Test(dataProviderClass = TrelloBoardTestDataProvider.class,
          dataProvider = "boardNames")
    public void testIfBoardsWithSpecificNamesWereCreated(String boardName) {
        TrelloBoard board = getBoard(requestBuilder()
            .setName(boardName)
            .setMethod(Method.POST)
            .buildRequest()
            .sendRequest(Constants.BOARDS_URL)
            .then()
            .assertThat()
            .spec(goodResponseSpec())
            .extract().response()
        );
        assertThat(board.getName(), CoreMatchers.equalTo(boardName));
    }

    @Test(dataProviderClass = TrelloBoardTestDataProvider.class,
          dataProvider = "severalBoardParameters")
    public void testIfBoardsWithSeveralParametersWereCreated(String boardName,
                                                             Background background,
                                                             PermissionLevel permissionLevel) {
        TrelloBoard board = getBoard(requestBuilder()
            .setName(boardName)
            .setBackground(background)
            .setPermissionLevel(permissionLevel)
            .setMethod(Method.POST)
            .buildRequest()
            .sendRequest(Constants.BOARDS_URL)
            .then()
            .assertThat()
            .spec(goodResponseSpec())
            .extract().response()
        );
        assertThat(board.getName(), CoreMatchers.equalTo(boardName));
        assertThat(board.getPrefs().getBackground(), CoreMatchers.equalTo(background.background));
        assertThat(board.getPrefs().getPermissionLevel(), CoreMatchers.equalTo(permissionLevel.permissionLevel));
    }

    @Test()
    public void testIfBoardIsAvailableById() {
        board = createBoard();
        assertThat(getBoardById(board.getId()).getId().length(), greaterThan(0));
    }

    @Test()
    public void testIfNameOfTheBoardWasUpdated() {
        board = createBoard();
        String boardUpdatedName = "Updated" + getBoardById(board.getId()).getName();
        board = getBoard(
            requestBuilder()
                .setMethod(Method.PUT)
                .setName(boardUpdatedName)
                .buildRequest()
                .sendRequest(Constants.BOARDS_URL + board.getId())
                .then()
                .assertThat()
                .spec(goodResponseSpec())
                .extract().response()
        );
        assertThat(boardUpdatedName, equalTo(getBoardById(board.getId()).getName()));
    }

    @Test()
    public void testIfDescriptionOfTheBoardWasUpdated() {
        Faker faker = new Faker();
        String description = faker.lorem().sentence(5);
        board = createBoard();
        board = getBoard(
            requestBuilder()
                .setMethod(Method.PUT)
                .setDescription(description)
                .buildRequest()
                .sendRequest(Constants.BOARDS_URL + board.getId())
                .then()
                .assertThat()
                .spec(goodResponseSpec())
                .extract().response()
        );
        assertThat(description, equalTo(board.getDesc()));
    }

    @Test()
    public void testIfBoardWasDeletedById() {
        board = createBoard();
        String id = board.getId();
        deleteBoardById(id);
        getDeletedBoardById(id);
    }

    @Test()
    public void testIfAllBoardsWereDeleted() {
        List<TrelloBoard> boards = new ArrayList<>();
        List<String> boardsIDs = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            board = createBoard();
            boards.add(board);
            boardsIDs.add(board.getId());
        }
        TrelloBoardTestSteps.deleteAllBoards();
        for (String boardID : boardsIDs) {
            deleteBoardById(boardID);
            getDeletedBoardById(boardID);
        }
    }
}
