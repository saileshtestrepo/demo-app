package com.example.demo.routes;

import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationRoutes extends RouteBuilder {


  @Override
  public void configure() {
    from("direct:validateDataModel")
        .doTry()
        .to("json-validator:json-schema/entity/DataRequestSchema.json")
        .log("Data Request Validation Success")
        .endDoTry()
        .doCatch(Exception.class)
        .log("Data Request Validation Failed")
        .to("direct:respondDataModelException")
        .stop()
        .end();

    from("direct:validateDataDtoRequest")
        .doTry()
        .to("json-validator:json-schema/mock/model/DataDtoSchema.json")
        .log("Data DTO Request Validation Success")
        .endDoTry()
        .doCatch(Exception.class)
        .log("Data DTO Response Validation Failed")
        .to("direct:respondDataDtoException")
        .stop()
        .end();
  }
}
