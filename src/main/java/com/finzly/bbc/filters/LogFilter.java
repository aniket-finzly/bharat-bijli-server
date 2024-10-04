package com.finzly.bbc.filters;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class LogFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (DispatcherType.REQUEST.equals (request.getDispatcherType ())) {
            String clientIp = request.getRemoteAddr ();
            String userAgent = request.getHeader ("User-Agent");

            log.info ("Request: {} {} from {} - {}", request.getMethod (), request.getRequestURI (), clientIp, userAgent);

            try {
                filterChain.doFilter (request, response);
                log.info ("Response: {}", response.getStatus ());
            } catch (Exception e) {
                log.error ("Exception: {}", e.getMessage ());
                response.setStatus (HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter ().write ("Internal Server Error");
            }
        } else {
            filterChain.doFilter (request, response);
        }
    }
}
