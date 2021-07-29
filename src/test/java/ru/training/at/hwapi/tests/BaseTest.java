package ru.training.at.hwapi.tests;

import io.restassured.RestAssured;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import ru.training.at.hwapi.beans.TrelloBoard;
import ru.training.at.hwapi.data.Constants;
import ru.training.at.hwapi.tests.steps.TrelloBoardTestSteps;

public class BaseTest {

    TrelloBoard board;

    @BeforeClass
    public void setUp() {
        RestAssured.baseURI = Constants.BASE_URL;
    }

    @AfterMethod()
    public void tearDown() {
        TrelloBoardTestSteps.deleteAllBoards();
    }
}
