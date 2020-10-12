package com.example.demo.routes.helper;

import com.example.demo.entity.response.Data;
import com.example.demo.entity.response.DataResponse;
import com.example.demo.entity.response.Info;
import com.example.demo.filter.context.CorrelationRequest;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class ResponseHelper {

  public DataResponse prepareDemoDataResponse(final Data data) {
    return prepareDemoDataResponse(data, null);
  }

  public DataResponse prepareDemoDataResponse(final List<String> errors) {
    return prepareDemoDataResponse(null, errors);
  }

  public DataResponse prepareDemoDataResponse(final String errorMessage) {
    return prepareDemoDataResponse(null, Collections.singletonList(errorMessage));
  }

  private DataResponse prepareDemoDataResponse(final Data data, final List<String> errors) {
    DataResponse.DataResponseBuilder demoDataResponseBuilder = DataResponse.builder();
    final Info info =
        Info.builder()
            .correlationId(CorrelationRequest.getCorrelationId())
            .timestamp(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()))
            .build();
    demoDataResponseBuilder.info(info);
    if (Objects.nonNull(data)) {
      demoDataResponseBuilder.data(data);
    }
    demoDataResponseBuilder.errors(
        CollectionUtils.isNotEmpty(errors) ? errors : Collections.emptyList());
    return demoDataResponseBuilder.build();
  }
}
