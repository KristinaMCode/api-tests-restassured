package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.Todo;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class TodoApiTest {
    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test
    public void getTodoById() {
        Todo todo = new Todo(1, "delectus aut autem", false);

        Todo getTodo = given()
                .when()
                .get("/todos/1")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(Todo.class);

        Assert.assertEquals(getTodo.getUserId(), todo.getUserId());
        Assert.assertEquals(getTodo.getTitle(), todo.getTitle());
        Assert.assertEquals(getTodo.isCompleted(), todo.isCompleted());
        Assert.assertEquals(getTodo.getId(), 1);
    }

    @Test
    public void createTodo() {
        Todo postTodo = given().contentType(ContentType.JSON).body(
                        """
                                  {
                                  "userId": 2,
                                  "title": "Title 2",
                                  "completed": false
                                }
                                """
                ).when()
                .post("/todos")
                .then()
                .log().body()
                .statusCode(201)
                .extract().as(Todo.class);

        Assert.assertEquals(postTodo.getUserId(), 2);
        Assert.assertEquals(postTodo.getTitle(), "Title 2");
        Assert.assertEquals(postTodo.isCompleted(), false);
        Assert.assertEquals(postTodo.getId(), 201);

    }

    @Test
    public void updateTodo() {
        Todo todo = new Todo(1, "delectus aut autem", true);
        Todo putTodo = given()
                .contentType(ContentType.JSON)
                .body(todo)
                .when()
                .put("/todos/1")
                .then()
                .log().body()
                .statusCode(200)
                .extract().as(Todo.class);

        Assert.assertEquals(putTodo.getUserId(), todo.getUserId());
        Assert.assertEquals(putTodo.getTitle(), todo.getTitle());
        Assert.assertEquals(putTodo.isCompleted(), todo.isCompleted());
        Assert.assertEquals(putTodo.getId(), 1);
    }

    @Test
    public void deleteTodo() {
        given()
                .when()
                .delete("todos/1")
                .then()
                .log().body()
                .statusCode(200)
                .body(equalTo("{}"));

    }

    @Test
    public void getNonExistentTodoReturns404() {
        given()
                .when()
                .get("todos/999")
                .then()
                .log().body()
                .statusCode(404)
                .body(equalTo("{}"));
    }
}
