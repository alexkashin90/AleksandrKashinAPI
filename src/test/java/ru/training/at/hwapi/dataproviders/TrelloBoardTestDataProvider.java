package ru.training.at.hwapi.dataproviders;

import com.github.javafaker.Faker;
import org.testng.annotations.DataProvider;
import ru.training.at.hwapi.data.Background;
import ru.training.at.hwapi.data.PermissionLevel;

public class TrelloBoardTestDataProvider {

    @DataProvider
    public static Object[][] boardNames() {
        Faker faker = new Faker();
        return new Object[][]{
            {faker.animal().name()},
            {faker.ancient().hero()},
            {faker.artist().name()}
        };
    }

    @DataProvider
    public static Object[][] severalBoardParameters() {
        Faker faker = new Faker();
        return new Object[][]{
            {faker.job().title(), Background.BLUE, PermissionLevel.PUBLIC},
            {faker.company().name(), Background.GREEN, PermissionLevel.ORG},
            {faker.aviation().airport(), Background.PINK, PermissionLevel.PRIVATE}
        };
    }
}
