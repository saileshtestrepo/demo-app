package com.example.demo.converter;

import com.example.demo.entity.request.DataRequest;
import com.example.demo.entity.response.Data;
import com.example.demo.mock.model.DataDtoRequest;
import com.example.demo.mock.model.DataDtoResponse;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.apache.camel.Converter;
import org.apache.camel.TypeConverters;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class DataConverters implements TypeConverters {
  private final Gson gson;

  @Converter
  public InputStream convertDataRequestToInputStream(DataRequest dataRequest) {
    return new ByteArrayInputStream(gson.toJson(dataRequest).getBytes());
  }


  @Converter
  public InputStream convertDataDtoRequestToInputStream(DataDtoRequest data) {
    return new ByteArrayInputStream(gson.toJson(data).getBytes());
  }

  @Converter
  public DataDtoRequest convertDataRequestToDataDtoRequest(DataRequest dataRequest) {
    final DataDtoRequest.DataDtoRequestBuilder builder = DataDtoRequest.builder();
    if (StringUtils.isNotEmpty(dataRequest.getValue())) {
      builder.data(dataRequest.getValue());
    }
    if (StringUtils.isNotEmpty(dataRequest.getId())) {
      builder.id(dataRequest.getId());
    }
    return builder.build();
  }

  @Converter
  public Data convertDataDtoResponseToData(DataDtoResponse dataDtoResponse) {
    final Data.DataBuilder builder = Data.builder();
    if (StringUtils.isNotEmpty(dataDtoResponse.getValue())) {
      builder.value(dataDtoResponse.getValue());
    }
    return builder.build();
  }
}
