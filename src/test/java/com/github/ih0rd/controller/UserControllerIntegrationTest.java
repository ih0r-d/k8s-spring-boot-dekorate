package com.github.ih0rd.controller;

import com.github.ih0rd.AbstractTestContainersIntegrationTest;
import com.github.ih0rd.entity.User;
import com.github.ih0rd.repository.UserRepository;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest extends AbstractTestContainersIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        postgres.start();

        RestAssured.port = port;
        userRepository.deleteAll();
    }

    @Test
    public void whenGetAllUsers_thenStatus200() {
        createTestUser();

        given()
                .when().get("/api/users")
                .then()
                .statusCode(200)
                .body("", hasSize(1));
    }

    @Test
    public void whenGetUserById_thenStatus200() {
        User user = createTestUser();

        given()
                .when().get("/api/users/{id}", user.getId())
                .then()
                .statusCode(200)
                .body("firstName", equalTo("John"));
    }

    @Test
    public void whenCreateUser_thenStatus201() {
        User user = new User(UUID.randomUUID(), "Jane", "Doe", 28, "jane.doe@example.com", Instant.now());

        given()
                .contentType(ContentType.JSON)
                .body(user)
                .when().post("/api/users")
                .then()
                .statusCode(201)
                .body("firstName", equalTo("Jane"));
    }

    @Test
    public void whenUpdateUser_thenStatus200() {
        User user = createTestUser();
        User updatedUser = new User(user.getId(), "Johnny", "Doe", 30, "john.doe@example.com", Instant.now());

        given()
                .contentType(ContentType.JSON)
                .body(updatedUser)
                .when().put("/api/users/{id}", user.getId())
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Johnny"));
    }

    @Test
    public void whenDeleteUser_thenStatus204() {
        User user = createTestUser();

        given()
                .when().delete("/api/users/{id}", user.getId())
                .then()
                .statusCode(204);
    }

    @AfterAll
    public static void tearDown() {
        if (postgres != null && postgres.isRunning()) {
            postgres.stop();
        }
    }

    private User createTestUser() {
        User user = new User(UUID.randomUUID(), "John", "Doe", 30, "john.doe@example.com", Instant.now());
        return userRepository.save(user);
    }
}
