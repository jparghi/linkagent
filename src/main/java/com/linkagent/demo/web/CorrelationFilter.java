package com.linkagent.demo.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationFilter implements Filter {
  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
    try {
      String corr = UUID.randomUUID().toString();
      MDC.put("corrId", corr);
      chain.doFilter(request, response);
      if (response instanceof HttpServletResponse resp) {
        resp.setHeader("X-Correlation-Id", corr);
      }
    } finally {
      MDC.remove("corrId");
    }
  }
}
