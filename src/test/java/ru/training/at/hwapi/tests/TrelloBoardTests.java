package ru.training.at.hwapi.tests;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.startsWith;
import static ru.training.at.hwapi.data.Constants.DESCRIPTION;
import static ru.training.at.hwapi.data.Constants.UPDATED;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.createTestBoard;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.deleteBoard;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.getBoardById;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.getDeletedBoard;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.updateBoardsDescriptionById;
import static ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps.updateBoardsNameById;

import io.restassured.RestAssured;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.CoreMatchers;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.training.at.hwapi.beans.TrelloBoard;
import ru.training.at.hwapi.data.Constants;
import ru.training.at.hwapi.dataproviders.TrelloBoardTestDataProvider;

public class TrelloBoardTests {

    private static final List<String> boardsIDs = new ArrayList<>();
    private TrelloBoard testBoard;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    @Test(dataProviderClass = TrelloBoardTestDataProvider.class,
          dataProvider = "boardName")
    public void testIfBoardsWereCreated(String boardName) {
        testBoard = createTestBoard(boardName);
        boardsIDs.add(testBoard.getId());
        assertThat(testBoard.getName(), CoreMatchers.equalTo(boardName));
    }

    @Test(dependsOnMethods = "testIfBoardsWereCreated",
          dataProviderClass = TrelloBoardTestDataProvider.class,
          dataProvider = "boardsIDs")
    public void testIfBoardsAreAvailableById(String id) {
        assertThat(getBoardById(id).getId().length(), greaterThan(0));
    }

    @Test(dependsOnMethods = "testIfBoardsAreAvailableById",
          dataProviderClass = TrelloBoardTestDataProvider.class,
          dataProvider = "boardsIDs")
    public void testIfBoardsNamesWereUpdated(String id) {
        String boardUpdatedName = "Updated" + getBoardById(id).getName();
        testBoard = updateBoardsNameById(boardUpdatedName, id);
        assertThat(boardUpdatedName, equalTo(getBoardById(id).getName()));
    }

    @Test(dependsOnMethods = "testIfBoardsAreAvailableById",
          dataProviderClass = TrelloBoardTestDataProvider.class,
          dataProvider = "boardsIDs")
    public void testIfBoardsDescriptionsWereUpdated(String id) {
        testBoard = updateBoardsDescriptionById(DESCRIPTION, id);
        assertThat(DESCRIPTION, equalTo(testBoard.getDesc()));
    }

    @Test(dependsOnMethods = {"testIfBoardsNamesWereUpdated", "testIfBoardsDescriptionsWereUpdated"},
          dataProviderClass = TrelloBoardTestDataProvider.class,
          dataProvider = "boardsIDs")
    public void testIfUpdatedBoardsAreAvailableById(String id) {
        assertThat(getBoardById(id).getName(), startsWith(UPDATED));
        assertThat(getBoardById(id).getDesc(), equalTo(DESCRIPTION));
    }

    @Test(dependsOnMethods = "testIfUpdatedBoardsAreAvailableById",
          dataProviderClass = TrelloBoardTestDataProvider.class,
          dataProvider = "boardsIDs")
    public void testIfBoardsWereDeleted(String id) {
        deleteBoard(id);
        getDeletedBoard(id);
    }

    public static List<String> getBoardsIDs() {
        return boardsIDs;
    }
}
