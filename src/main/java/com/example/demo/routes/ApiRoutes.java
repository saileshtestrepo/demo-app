package com.example.demo.routes;

import com.example.demo.entity.request.DataRequest;
import com.example.demo.entity.response.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApiRoutes extends RouteBuilder {

  @Override
  public void configure() {
    restConfiguration().component("servlet").bindingMode(RestBindingMode.json);

    rest("/api/data")
        .post("/")
        .id("post-demo-data")
        .consumes(MediaType.APPLICATION_JSON_VALUE)
        .produces(MediaType.APPLICATION_JSON_VALUE)
        .type(DataRequest.class)
        .outType(DataResponse.class)
        .route()
        .log("Started")
        .to("direct:performPostDemoData")
        .log("Completed")
        .endRest();
  }
}
