package com.linkagent.demo.web;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class SecurityHeadersFilter implements Filter {
  @Override public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {
    HttpServletResponse r = (HttpServletResponse) res;
    r.setHeader("X-Content-Type-Options", "nosniff");
    r.setHeader("X-Frame-Options", "DENY");
    r.setHeader("Referrer-Policy", "no-referrer");
    r.setHeader("X-XSS-Protection", "0");
    r.setHeader("Content-Security-Policy", "default-src 'none'");
    chain.doFilter(req, res);
  }
}
