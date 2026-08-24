package me.hkaibni.login;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
class UserLoginTest {

    @Test
    void loginWithInvalidPasswordShouldReturn401() {

        given()
                .contentType(ContentType.JSON)
                .body("""
                {
                    "ssn": "1",
                    "password": "EXINPH6H0zxC0+oVtXcF5w=="
                }
            """)
                .when()
                .post("/auth/users/login")
                .then()
                .statusCode(401);
    }
    @Test
    void validLoginShouldReturnToken() { // correctPassword

            given()
                    .contentType(ContentType.JSON)
                    .body("""
            {
                "ssn": "1",
                "password": "SQfON1bFo0UWifgbHCr46g=="
            }
        """)
                    .when()
                    .post("/auth/users/login")
                    .then()
                    .statusCode(200)
                    .body("token", notNullValue());
        }
    @Test
    void LoginEmptySSNReturnToken() { // Empty SSN

        given()
                .contentType(ContentType.JSON)
                .body("""
            {
                "ssn": "",
                "password": "SQfON1bFo0UWifgbHCr46g=="
            }
        """)
                .when()
                .post("/auth/users/login")
                .then()
                .statusCode(400);}
    @Test
    void LoginEmptyPasswordReturnToken() { // Empty SSN

        given()
                .contentType(ContentType.JSON)
                .body("""
            {
                "ssn": "1",
                "password": ""
            }
        """)
                .when()
                .post("/auth/users/login")
                .then()
                .statusCode(400);}

}