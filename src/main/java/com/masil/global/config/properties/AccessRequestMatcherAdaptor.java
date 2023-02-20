package com.masil.global.config.properties;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessRequestMatcherAdaptor {

    public RequestMatcher getRequestMatcher() {
        AntPathMatcher pathMatcher = new AntPathMatcher();
        pathMatcher.setCaseSensitive(false); // 대소문자 구분 안함

        List<String> patterns = Arrays.asList(
                "/auth/**",
                "/board/**/posts",
                "/posts/**",
                "/addresses/search",
                "/posts/**/comments"
        );

        return new OrRequestMatcher(patterns.stream()
                .map(pattern -> new AntPathRequestMatcher(pattern))
                .collect(Collectors.toList()));
    }
}
