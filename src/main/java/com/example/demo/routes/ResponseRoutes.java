package com.example.demo.routes;

import com.example.demo.entity.response.Data;
import com.example.demo.exceptions.DataException;
import com.example.demo.routes.helper.ResponseHelper;
import com.networknt.schema.ValidationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jsonvalidator.JsonValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class ResponseRoutes extends RouteBuilder {

  private final ResponseHelper responseHelper;

  @Override
  public void configure() {
    /*
    from("direct:respondDataModelValidationException")
    .process(
        e -> {
          final ValidationException validationException =
              (ValidationException) e.getProperty(Exchange.EXCEPTION_CAUGHT);
          e.getIn()
              .setBody(
                  responseRouteHelper.prepareDemoDataResponse(
                      validationException.getAllMessages()));
        })
    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.BAD_REQUEST.value()));
    */
    from("direct:respondDataModelException")
        .process(
            e -> {
              Exception exception = (Exception) e.getProperty(Exchange.EXCEPTION_CAUGHT);
              if (exception instanceof JsonValidationException) {
                final JsonValidationException jsonValidationException =
                    (JsonValidationException) e.getProperty(Exchange.EXCEPTION_CAUGHT);
                e.getIn()
                    .setBody(
                        responseHelper.prepareDemoDataResponse(
                            jsonValidationException.getErrors().stream()
                                .map(ValidationMessage::getMessage)
                                .collect(Collectors.toList())));
              } else {
                log.error("Exception ", exception);
                e.getIn()
                    .setBody(responseHelper.prepareDemoDataResponse(exception.getMessage()));
              }
            })
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.BAD_REQUEST.value()));

    //    from("direct:respondDataModelUnrecognizedPropertyException")
    //        .process(
    //            e -> {
    //              final UnrecognizedPropertyException validationException =
    //                  (UnrecognizedPropertyException) e.getProperty(Exchange.EXCEPTION_CAUGHT);
    //              e.getIn()
    //                  .setBody(
    //                      responseRouteHelper.prepareDemoDataResponse(
    //                          validationException.getMessage()));
    //            })
    //        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.BAD_REQUEST.value()));

    from("direct:respondDataDtoException")
        .process(
            e -> {
              Exception exception = (Exception) e.getProperty(Exchange.EXCEPTION_CAUGHT);

              log.error("Exception ", exception);
              e.getIn()
                  .setBody(responseHelper.prepareDemoDataResponse(exception.getMessage()));
            })
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    from("direct:respondDemoDataException")
        .process(
            e -> {
              final DataException dataException =
                  (DataException) e.getProperty(Exchange.EXCEPTION_CAUGHT);
              e.getIn()
                  .setBody(
                      responseHelper.prepareDemoDataResponse(dataException.getMessage()));
            })
        .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    from("direct:respondSuccessResponse")
        .process(
            e -> {
              final Data body = (Data) e.getIn().getBody();
              e.getIn().setBody(responseHelper.prepareDemoDataResponse(body));
            });
  }
}
