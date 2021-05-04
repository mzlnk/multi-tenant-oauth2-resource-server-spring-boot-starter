package io.mzlnk.springframework.multitenant.oauth2.resourceserver.demo.application.security;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.api.Matcher;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Matcher
public class AuthProvider3Matcher implements AuthenticationTenantMatcher {

    @Override
    public String getProviderId() {
        return "auth-provider-3";
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return Optional.ofNullable(request.getQueryString())
                .map(query -> query.contains("iss=auth-provider-3"))
                .orElse(false);
    }

}
