package io.mzlnk.springframework.multitenant.oauth2.resourceserver.demo.application.security;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.api.MatcherFactory;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AbstractAuthenticationTenantMatcher;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcher;

import javax.servlet.http.HttpServletRequest;

@MatcherFactory
public class HttpMethodMatcherFactory implements AuthenticationTenantMatcher.Factory {

    private static final String TYPE = "METHOD";
    private static final String METHOD_PROPERTY_KEY = "method";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public AuthenticationTenantMatcher create(String providerId, AuthenticationTenantDetails.MatcherDetails matcherDetails) {
        return new HttpMethodMatcher(providerId, matcherDetails.getProperty(METHOD_PROPERTY_KEY));
    }

    public static class HttpMethodMatcher extends AbstractAuthenticationTenantMatcher {

        private final String method;

        public HttpMethodMatcher(String providerId, String method) {
            super(providerId);
            this.method = method;
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            return request.getMethod().equals(this.method);
        }
    }

}
