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
import java.util.Collections;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Slf4j
@Component
public class CustomRequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Only log requests of type DispatcherType.REQUEST to avoid duplicates on dispatch types like FORWARD or ERROR
        if (DispatcherType.REQUEST.equals (request.getDispatcherType ())) {
            String requestId = generateRequestId ();
            log.info ("Request ID: {}, Incoming Request: method={}, uri={}, params={}, headers={}",
                    requestId,
                    request.getMethod (),
                    request.getRequestURI (),
                    request.getQueryString (),
                    getHeaders (request));

            filterChain.doFilter (request, response);

            log.info ("Request ID: {}, Outgoing Response: status={}", requestId, response.getStatus ());
        } else {
            filterChain.doFilter (request, response);
        }
    }

    private String getHeaders (HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames ();
        if (headerNames == null) {
            return "No Headers";
        }
        return Collections.list (headerNames).stream ()
                .map (name -> name + ": " + request.getHeader (name))
                .collect (Collectors.joining (", "));
    }

    private String generateRequestId () {
        return java.util.UUID.randomUUID ().toString ();
    }
}
