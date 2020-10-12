package com.github.brave2chen.springbooteasy.config.filter;

import cn.hutool.core.lang.UUID;
import com.github.brave2chen.springbooteasy.constant.SystemConstant;
import com.github.brave2chen.springbooteasy.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
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
import java.util.HashMap;
import java.util.Objects;
import java.util.Optional;

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
        return Ordered.HIGHEST_PRECEDENCE;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 设置traceId
        this.setMDCTraceId(request);

        String path = request.getRequestURI();
        if (!isRequestValid(request) || this.pathMatcher.match(IGNORE_TRACE_PATH, path)) {
            try {
                filterChain.doFilter(request, response);
            } finally {
                clearMDC();
            }
            return;
        }
        if (Objects.equals(IGNORE_CONTENT_TYPE, request.getContentType())) {
            try {
                log.trace("Start Multipart Request: {} \"{}\"", request.getMethod().toUpperCase(), path);
                filterChain.doFilter(request, response);
                log.trace("End Multipart Request: {} \"{}\"", request.getMethod().toUpperCase(), path);
            } finally {
                clearMDC();
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
        try {
            log.trace("Start Request: {} \"{}\"", request.getMethod().toUpperCase(), path);
            filterChain.doFilter(request, response);
        } finally {
            log.trace("End Request: {} \"{}\"", request.getMethod().toUpperCase(), path);
            long latency = System.currentTimeMillis() - startTime;
            boolean isRequestBody = request.getContentType() != null && request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE);
            log.debug("{} \"{}\" TimeTaken: {} ms , {}: {}, ResponseBody: {}",
                    request.getMethod().toUpperCase(),
                    path,
                    latency,
                    isRequestBody ? "RequestBody" : "Parameter",
                    isRequestBody ? getRequestBody(request) : JsonUtil.stringify(request.getParameterMap()),
                    getResponseBody(response)
            );
            updateResponse(response);

            clearMDC();
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
                requestBody = new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
            } catch (IOException e) {
                // NOOP
            }
            HashMap map = JsonUtil.parse(requestBody, HashMap.class);
            requestBody = JsonUtil.stringify(map);
        }
        return requestBody;
    }

    private String getResponseBody(HttpServletResponse response) {
        String responseBody = "{}";
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            try {
                responseBody = new String(wrapper.getContentAsByteArray(), wrapper.getCharacterEncoding());
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
    private void setMDCTraceId(HttpServletRequest request) {
        // 从请求头部获取traceId
        String traceId = request.getHeader(SystemConstant.TRACE_ID_REQUEST_HEADER);
        // 没有traceId则UUID生成一个，最好使用hutool的UUID来生成，其内部使用SecureRandom实现
        traceId = Optional.ofNullable(traceId).orElse(UUID.randomUUID().toString(true));
        // 将traceId放到MDC中
        MDC.put(TRACE_ID, traceId);
        log.trace("MDC set traceId = {}", traceId);
    }

    /**
     * 清除traceId, user 等
     */
    private void clearMDC() {
        MDC.clear();
        log.trace("MDC cleared");
    }
}
