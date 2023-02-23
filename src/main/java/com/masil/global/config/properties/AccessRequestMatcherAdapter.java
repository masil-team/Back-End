package com.masil.global.config.properties;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AccessRequestMatcherAdapter implements RequestMatcher {

    private final RequestMatcher guestAccessUri;

    public AccessRequestMatcherAdapter() {

        List<String> accessUriPattern = Arrays.asList(
                "/auth/**",
                "/boards/**/posts",
                "/posts/**",
                "/addresses/search",
                "/posts/**/comments"
        );
        List<RequestMatcher> requestMatchers = accessUriPattern.stream()
                .map(pattern -> new AntPathRequestMatcher(pattern))
                .collect(Collectors.toList());

        this.guestAccessUri = new OrRequestMatcher(requestMatchers);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return guestAccessUri.matches(request);
    }
}
