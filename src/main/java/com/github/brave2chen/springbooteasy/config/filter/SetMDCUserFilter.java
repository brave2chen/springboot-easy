package com.github.brave2chen.springbooteasy.config.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SetMDCUserFilter
 *
 * @author brave2chen
 * @date 2020-06-23
 */
@Slf4j
public class SetMDCUserFilter extends OncePerRequestFilter {
    private static final String USER = "user";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            setUser();
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * 设置 user
     */
    private void setUser() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        MDC.put(USER, name);
    }

}
