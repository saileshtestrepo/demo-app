package com.example.demo.routes;

import com.example.demo.entity.request.DataRequest;
import com.example.demo.entity.response.Data;
import com.example.demo.exceptions.DataException;
import com.example.demo.mock.model.DataDtoRequest;
import com.example.demo.mock.model.DataDtoResponse;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServiceRoutes extends RouteBuilder {
  @Value("${demo.app.value.url:rest}")
  private String url;

  private final Gson gson;

  @Override
  public void configure() {

    from("direct:performPostDemoData")
        .id("performPostDemoData")
        .log("Data Request: ${body}")
        .to("direct:validateDataModel")
        .to("direct:processBusinessLogic")
        .convertBodyTo(DataDtoRequest.class)
        .log("Data Dto Request: ${body}")
        .to("direct:validateDataDtoRequest")
        .log("Calling Mock Service")
        .setHeader(Exchange.HTTP_METHOD, simple("POST"))
        .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
        .setHeader("Accept", constant("application/json"))
                .to(url + "?bridgeEndpoint=true")
//        .to("direct:mock-service")
        .convertBodyTo(String.class)
        .log("Data DTO Response from Mock Service: ${body}")
        .unmarshal()
        .json(JsonLibrary.Jackson, DataDtoResponse.class)
        .log("Data DTO Response from mock server Unmarshalled")
        .convertBodyTo(Data.class)
        .log("Data DTO Response converted to Data")
        .to("direct:respondSuccessResponse");

    //    from("direct:mock-service")
    //        .process(
    //            e ->
    //                e.getIn()
    //                    .setBody(
    //                        gson.toJson(DataDtoResponse.builder().value("Some
    // Response").build())));

    from("direct:processBusinessLogic")
        .doTry()
        .process(
            e -> {
              final DataRequest body = (DataRequest) e.getIn().getBody();
              body.setId(UUID.randomUUID().toString());
              if (body.getValue().equals("exception")) {
                throw new DataException("my custom Exception");
              }
              e.getIn().setBody(body);
            })
        .log("Random Id populated")
        .endDoTry()
        .doCatch(DataException.class)
        .log(LoggingLevel.ERROR, "Demo Data Exception ${exception}")
        .to("direct:respondDemoDataException")
        .stop()
        .end();
  }
}
