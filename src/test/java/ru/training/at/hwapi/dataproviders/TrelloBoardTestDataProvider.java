package ru.training.at.hwapi.dataproviders;

import org.testng.annotations.DataProvider;
import ru.training.at.hwapi.tests.TrelloBoardTests;

public class TrelloBoardTestDataProvider {

    @DataProvider
    public static Object[][] boardName() {
        return new Object[][]{
            {"FirstTestBoardName"},
            {"SecondTestBoardName"},
            {"ThirdTestBoardName"}
        };
    }

    @DataProvider
    public static Object[][] boardsIDs() {
        return TrelloBoardTests.getBoardsIDs()
                               .stream()
                               .map(id -> new Object[] { id })
                               .toArray(Object[][]::new);
    }
}
