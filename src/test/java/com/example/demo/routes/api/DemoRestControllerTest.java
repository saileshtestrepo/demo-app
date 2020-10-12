package com.example.demo.routes.api;

import com.example.demo.DemoApplication;
import com.example.demo.entity.request.DataRequest;
import com.example.demo.entity.response.DataResponse;
import com.example.demo.mock.model.DataDtoResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(
    classes = DemoApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Slf4j
public class DemoRestControllerTest {
  private static final String URL = "http://localhost";
  @Autowired private Gson gson;

  @LocalServerPort private int port;

  private TestRestTemplate restTemplate;
  private HttpHeaders headers;
  private static ClientAndServer mockServer;

  @BeforeClass
  public static void startServer() {
    mockServer = ClientAndServer.startClientAndServer(1080);
  }

  @AfterClass
  public static void stopServer() {
    mockServer.stop();
  }

  @Before
  public void setUp() {
    restTemplate = new TestRestTemplate();
    headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    new MockServerClient("127.0.0.1", 1080)
        .when(
            HttpRequest.request()
                .withMethod("POST")
                .withPath("/test/api/data")
                .withHeader("\"Content-type\", \"application/json\""))
        .respond(
            HttpResponse.response()
                .withStatusCode(200)
                .withHeaders(
                    new Header("Content-Type", "application/json; charset=utf-8"),
                    new Header("Cache-Control", "public, max-age=86400"))
                .withBody(
                    gson.toJson(DataDtoResponse.builder().value("mock service response").build()))
                .withDelay(TimeUnit.SECONDS, 1));
  }

  @Test
  public void when_postData_then_test_success() {

    final String correlationId = UUID.randomUUID().toString();
    headers.add("X-Correlation-Id", correlationId);
    try {
      final ResponseEntity<DataResponse> responseEntity =
          restTemplate.postForEntity(
              URL.concat(":").concat(String.valueOf(port)).concat("/camel/api/data"),
              new HttpEntity<>(getDemoData(), headers),
              DataResponse.class);
      log.debug(
          "Response: {}", new GsonBuilder().setPrettyPrinting().create().toJson(responseEntity));
      Assert.assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
      Assert.assertEquals(
          correlationId,
          Objects.requireNonNull(responseEntity.getBody()).getInfo().getCorrelationId());
      Assert.assertEquals(
          "mock service response",
          Objects.requireNonNull(responseEntity.getBody()).getData().getValue());
    } catch (Exception e) {
      log.error("Unhandled Exception", e);
      Assert.fail();
    }
  }

  @Test
  public void when_postData_then_test_failure() {
    final String correlationId = UUID.randomUUID().toString();
    headers.add("X-Correlation-Id", correlationId);
    try {
      final ResponseEntity<DataResponse> responseEntity =
          restTemplate.postForEntity(
              URL.concat(":").concat(String.valueOf(port)).concat("/camel/api/data"),
              new HttpEntity<>(getExceptionValueData(), headers),
              DataResponse.class);
      log.debug(
          "Response: {}", new GsonBuilder().setPrettyPrinting().create().toJson(responseEntity));
      Assert.assertTrue(responseEntity.getStatusCode().is5xxServerError());
      Assert.assertEquals(
          correlationId,
          Objects.requireNonNull(responseEntity.getBody()).getInfo().getCorrelationId());
      Assert.assertTrue(
          Objects.requireNonNull(responseEntity.getBody())
              .getErrors()
              .contains("my custom Exception"));
    } catch (Exception e) {
      log.error("Unhandled Exception", e);
      Assert.fail();
    }
  }

  private DataRequest getDemoData() {
    return DataRequest.builder().value("some data").build();
  }

  private DataRequest getExceptionValueData() {
    return DataRequest.builder().value("exception").build();
  }
}
