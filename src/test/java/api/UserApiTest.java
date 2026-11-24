package api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import models.Post;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class UserApiTest {
    private String url = "https://jsonplaceholder.typicode.com";

    @Test
    public void getUser() {
        given()
                .baseUri(url)
                .when()
                .get("/users/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Leanne Graham"))
                .body("address.city", equalTo("Gwenborough"))
                .body("company.name", equalTo("Romaguera-Crona"));

    }

    @Test
    public void createPost() {
        given().contentType(ContentType.JSON)
                .baseUri(url)
                .body("""
                         {
                          "title": "My first post",
                          "body": "Testing with REST Assured",
                          "userId": 1
                        }
                        """)
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .body("title", equalTo("My first post"))
                .body("id", equalTo(101));
    }

    @Test
    public void updatePost() {
        given().contentType(ContentType.JSON)
                .baseUri(url)
                .body("""
                          {
                          "title": "My first updated post",
                          "body": "Testing with REST Assured",
                          "userId": 1
                        }
                        """)
                .when()
                .put("/posts/1")
                .then()
                .log().body()
                .statusCode(200)
                .body("title", equalTo("My first updated post"));

    }

    @Test
    public void notUser() {
        given()
                .baseUri(url)
                .when()
                .get("/users/999")
                .then()
                .statusCode(404);

    }

    @Test
    public void deletePost() {
        given()
                .baseUri(url)
                .when()
                .delete("/posts/1")
                .then()
                .log().body()
                .statusCode(200)
                .body(equalTo("{}"));

    }

    @Test
    public void createPostWithPojo() {
        Post newPost = new Post("My first post", "Testing with REST Assured", 1);

        Post createdPost = given()
                .baseUri(url)
                .contentType(ContentType.JSON)
                .body(newPost)
                .when()
                .post("/posts")
                .then()
                .log().body()
                .statusCode(201)
                .extract().as(Post.class);

        Assert.assertEquals(createdPost.getTitle(),newPost.getTitle());
        Assert.assertEquals(createdPost.getBody(),newPost.getBody());
        Assert.assertEquals(createdPost.getUserId(),newPost.getUserId());
        Assert.assertEquals(createdPost.getId(),101);
    }
}
