package ru.training.at.hwapi.core;

import static org.hamcrest.Matchers.lessThan;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.http.HttpStatus;
import ru.training.at.hwapi.beans.TrelloBoard;
import ru.training.at.hwapi.data.Background;
import ru.training.at.hwapi.data.Constants;
import ru.training.at.hwapi.data.PermissionLevel;

public class TrelloBoardServiceObj {

    private Method requestMethod;
    private static Map<String, String> parameters;

    private TrelloBoardServiceObj(Map<String, String> parameters, Method method) {
        this.parameters = parameters;
        this.requestMethod = method;
    }

    public static ApiRequestBuilder requestBuilder() {
        return new ApiRequestBuilder();
    }

    public static class ApiRequestBuilder {
        private Map<String, String> parameters = new HashMap<>();
        private Map<String, String> path = new HashMap<>();
        private Method requestMethod = Method.GET;

        public ApiRequestBuilder setMethod(Method method) {
            requestMethod = method;
            return this;
        }

        public ApiRequestBuilder setBoardId(String id) {
            path.put("id", id);
            return this;
        }

        public ApiRequestBuilder setName(String name) {
            parameters.put("name", name);
            return this;
        }

        public ApiRequestBuilder setPermissionLevel(PermissionLevel permissionLevel) {
            parameters.put("prefs_permissionLevel", permissionLevel.permissionLevel);
            return this;
        }

        public ApiRequestBuilder setBackground(Background background) {
            parameters.put("prefs_background", background.background);
            return this;
        }

        public ApiRequestBuilder setDescription(String desc) {
            parameters.put("desc", desc);
            return this;
        }

        public TrelloBoardServiceObj buildRequest() {
            parameters.put("key", Constants.API_KEY);
            parameters.put("token", Constants.TOKEN);
            return new TrelloBoardServiceObj(parameters, requestMethod);
        }
    }

    public Response sendRequest(String pathUri) {
        return RestAssured
            .given(requestSpecification()).log().all()
            .queryParams(parameters)
            .request(requestMethod, pathUri)
            .prettyPeek();
    }

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .setAccept(ContentType.JSON)
            .build();
    }

    public static ResponseSpecification goodResponseSpec() {
        return new ResponseSpecBuilder()
            .expectContentType(ContentType.JSON)
            .expectResponseTime(lessThan(5000L))
            .expectStatusCode(org.apache.http.HttpStatus.SC_OK)
            .build();
    }

    public static ResponseSpecification notFoundResponseSpec() {
        return new ResponseSpecBuilder()
            .expectContentType(ContentType.TEXT)
            .expectResponseTime(lessThan(10000L))
            .expectStatusCode(HttpStatus.SC_NOT_FOUND)
            .build();
    }

    public static TrelloBoard getBoard(Response response) {
        return new Gson().fromJson(response
                .asString()
                .trim(),
            new TypeToken<TrelloBoard>() {}.getType());
    }

    public static List<TrelloBoard> getBoards(Response response) {
        return new Gson().fromJson(response
                .asString()
                .trim(),
            new TypeToken<List<TrelloBoard>>() {}.getType());
    }
}
