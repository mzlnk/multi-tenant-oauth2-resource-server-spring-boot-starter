package io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;

import javax.servlet.http.HttpServletRequest;

public class DefaultAuthenticationTenantMatcher extends AbstractAuthenticationTenantMatcher {

    public DefaultAuthenticationTenantMatcher(String providerId) {
        super(providerId);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return true;
    }


    public static class Factory implements AuthenticationTenantMatcher.Factory {

        @Override
        public String getType() {
            return "DEFAULT";
        }

        @Override
        public AuthenticationTenantMatcher create(String providerId, AuthenticationTenantDetails.MatcherDetails matcherDetails) {
            return new DefaultAuthenticationTenantMatcher(providerId);
        }

    }

}
