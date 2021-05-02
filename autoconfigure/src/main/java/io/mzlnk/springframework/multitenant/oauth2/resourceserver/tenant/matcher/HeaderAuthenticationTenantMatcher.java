package io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class HeaderAuthenticationTenantMatcher extends AbstractAuthenticationTenantMatcher {

    private final String headerName;
    private final String headerValue;

    public HeaderAuthenticationTenantMatcher(String providerId, String headerName, String headerValue) {
        super(providerId);
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(this.headerName))
                .map(this.headerValue::equals)
                .orElse(false);
    }

    public static class Factory implements AuthenticationTenantMatcher.Factory {

        @Override
        public String getType() {
            return "HEADER";
        }

        @Override
        public AuthenticationTenantMatcher create(String providerId, AuthenticationTenantDetails.MatcherDetails matcherDetails) {
            var headerName = matcherDetails.getProperty("header-name");
            var headerValue = matcherDetails.getProperty("header-value");

            Assert.notNull(headerName, "Property header-name cannot be null");
            Assert.notNull(headerValue, "Property header-value cannot be null");

            return new HeaderAuthenticationTenantMatcher(providerId, headerName, headerValue);
        }
    }

}
