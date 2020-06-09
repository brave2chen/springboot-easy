package com.github.brave2chen.springbooteasy.config.filter;

import com.github.brave2chen.springbooteasy.constant.SystemConstant;
import com.github.brave2chen.springbooteasy.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Http 请求日志记录 配置类
 *
 * @author brave2chen
 * @date 2020-06-02
 */
@Slf4j
@Component
public class HttpTraceLogFilter extends OncePerRequestFilter implements Ordered {
    private static final String TRACE_ID = "traceId";

    private static final String IGNORE_TRACE_PATH = "/**/*.*";
    private static final String IGNORE_CONTENT_TYPE = "multipart/form-data";

    private PathMatcher pathMatcher = new AntPathMatcher();

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 设置traceId
        this.setTraceId(request);

        String path = request.getRequestURI();
        if (!isRequestValid(request)
                || this.pathMatcher.match(IGNORE_TRACE_PATH, path)
                || Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())
        ) {
            try {
                filterChain.doFilter(request, response);
            } finally {
                clearTraceId(request);
            }
            return;
        }
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        long startTime = System.currentTimeMillis();
        log.info("{} \"{}\" Parameter: {}, RequestBody: {}", request.getMethod().toUpperCase(), path, JsonUtil.stringify(request.getParameterMap()), (getRequestBody(request)));
        try {
            filterChain.doFilter(request, response);
        } finally {
            long latency = System.currentTimeMillis() - startTime;
            log.info("{} \"{}\" TimeTaken: {} ms, ResponseBody: {}", request.getMethod().toUpperCase(), path, latency, (getResponseBody(response)));
            updateResponse(response);

            clearTraceId(request);
        }
    }

    private boolean isRequestValid(HttpServletRequest request) {
        try {
            new URI(request.getRequestURL().toString());
            return true;
        } catch (URISyntaxException ex) {
            return false;
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        String requestBody = "{}";
        ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
        if (wrapper != null) {
            try {
                requestBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (IOException e) {
                // NOOP
            }
        }
        return requestBody;
    }

    private String getResponseBody(HttpServletResponse response) {
        String responseBody = "{}";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            try {
                responseBody = IOUtils.toString(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (IOException e) {
                // NOOP
            }
        }
        return responseBody;
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        Objects.requireNonNull(responseWrapper).copyBodyToResponse();
    }

    /**
     * 设置traceId
     */
    private void setTraceId(HttpServletRequest request) {
        // 从请求头部获取traceId
        String traceId = request.getHeader(SystemConstant.TRACE_ID_REQUEST_HEADER);
        // 没有traceId则UUID生成一个
        traceId = Optional.ofNullable(traceId).orElse(UUID.randomUUID().toString()).replace("-", "");
        // 将traceId放到MDC中
        MDC.put(TRACE_ID, traceId);
    }

    /**
     * 清除traceId
     */
    private void clearTraceId(HttpServletRequest request) {
        MDC.clear();
    }
}