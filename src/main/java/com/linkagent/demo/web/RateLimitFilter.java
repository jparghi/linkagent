package com.linkagent.demo.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter implements Filter {
  private final int limit;
  private final long windowMillis;
  private final Map<String, Deque<Long>> hits = new ConcurrentHashMap<>();

  public RateLimitFilter(
      @Value("${linkagent.ratelimit.limit:60}") int limit,
      @Value("${linkagent.ratelimit.windowMillis:60000}") long windowMillis) {
    this.limit = limit; this.windowMillis = windowMillis;
  }

  @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest req = (HttpServletRequest) request;
    HttpServletResponse res = (HttpServletResponse) response;

    if ("POST".equalsIgnoreCase(req.getMethod()) && req.getRequestURI().startsWith("/agent/")) {
      String ip = req.getRemoteAddr();
      long now = Instant.now().toEpochMilli();
      Deque<Long> q = hits.computeIfAbsent(ip, k -> new ArrayDeque<>());
      synchronized (q) {
        while (!q.isEmpty() && now - q.peekFirst() > windowMillis) q.pollFirst();
        if (q.size() >= limit) {
          res.setStatus(429);
          res.setContentType("application/json");
          res.getWriter().write("{\"error\":\"RATE_LIMIT\",\"message\":\"Too_Many_Requests\"}");
          return;
        }
        q.addLast(now);
      }
    }
    chain.doFilter(request, response);
  }
}
