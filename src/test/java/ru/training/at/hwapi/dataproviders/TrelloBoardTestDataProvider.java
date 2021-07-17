package ru.training.at.hwapi.dataproviders;

import java.util.Arrays;
import org.testng.annotations.DataProvider;
import ru.training.at.hwapi.tests.TrelloBoardTest;

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
        return TrelloBoardTest.getBoardsIDs()
                              .stream()
                              .map(id -> new Object[] { id })
                              .toArray(Object[][]::new);
    }
}
