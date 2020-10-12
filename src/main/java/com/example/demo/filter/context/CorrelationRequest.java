package com.example.demo.filter.context;

public class CorrelationRequest {
  public static final String CORRELATION_ID = "correlationId";

  private static final ThreadLocal<String> correlationId = new ThreadLocal<>();

  public static String getCorrelationId() {
    return correlationId.get();
  }

  public static void setCorrelationId(String correlationId) {
    CorrelationRequest.correlationId.set(correlationId);
  }
}
