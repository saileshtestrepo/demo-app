package com.example.demo.filter;

import com.example.demo.filter.context.CorrelationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
@Slf4j
public class CorrelationIdFilter implements Filter {

  public static final String CORRELATION_ID = "CorrelationId";

  @Override
  public void doFilter(
      ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
      throws IOException, ServletException {
    final HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
    final String correlationId = httpServletRequest.getHeader("X-Correlation-Id");
    try {
      if (!currentRequestIsAsyncDispatcher(httpServletRequest)) {

        if (StringUtils.isBlank(correlationId)) {
          final String newCorrelationId = UUID.randomUUID().toString();
          MDC.put(CORRELATION_ID, newCorrelationId);
          CorrelationRequest.setCorrelationId(newCorrelationId);
          log.info("Generated correlationId : " + newCorrelationId);
        } else {
          MDC.put(CORRELATION_ID, correlationId);
          CorrelationRequest.setCorrelationId(correlationId);
          log.info("correlationId from request : " + correlationId);
        }
      }
      filterChain.doFilter(servletRequest, servletResponse);
    } finally {
      MDC.remove(CORRELATION_ID);
    }
  }

  private boolean currentRequestIsAsyncDispatcher(HttpServletRequest httpServletRequest) {
    return httpServletRequest.getDispatcherType().equals(DispatcherType.ASYNC);
  }
}
