package com.demo.flowable.config.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : gr
 * @date : 2024/3/7 9:25
 */
@Configuration
@Slf4j
public class FeignRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            //设置请求token信息
//            requestTemplate.header(HttpHeaders.AUTHORIZATION, request.getHeader("Authorization"));
        } catch (IllegalStateException e) {
            log.error(e.getMessage());
        }
    }
}