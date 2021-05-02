package pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher;

import org.springframework.util.Assert;
import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.AuthenticationTenantDetails;

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
            var headerName = matcherDetails.getProperty("headerName");
            var headerValue = matcherDetails.getProperty("headerValue");

            Assert.notNull(headerName, "Property headerName cannot be null");
            Assert.notNull(headerValue, "Property headerValue cannot be null");

            return new HeaderAuthenticationTenantMatcher(providerId, headerName, headerValue);
        }
    }

}
