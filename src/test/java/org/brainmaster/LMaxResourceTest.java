package org.brainmaster;

import static io.restassured.RestAssured.given;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class LMaxResourceTest {

  @Test
  public void testHelloEndpoint() {
    given().when().get("/hello").then().statusCode(200);
  }

}
