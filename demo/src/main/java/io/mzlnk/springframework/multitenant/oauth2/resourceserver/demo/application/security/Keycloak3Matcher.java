package io.mzlnk.springframework.multitenant.oauth2.resourceserver.demo.application.security;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.api.MatcherFactory;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcher;

import javax.servlet.http.HttpServletRequest;

@MatcherFactory
public class Keycloak3Matcher implements AuthenticationTenantMatcher.Factory {

    @Override
    public String getType() {
        return "TEST";
    }

    @Override
    public AuthenticationTenantMatcher create(String providerId, AuthenticationTenantDetails.MatcherDetails matcherDetails) {
        return new AuthenticationTenantMatcher() {
            @Override
            public String getProviderId() {
                return providerId;
            }

            @Override
            public boolean matches(HttpServletRequest request) {
                var method = request.getMethod();
                var props = matcherDetails.getProperty("method");
                return method.equals(props);
            }
        };
    }
}
