package pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher;

import org.springframework.util.Assert;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.MatcherFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Optional;

public class HeaderAuthenticationProviderMatcher extends AbstractAuthenticationProviderMatcher {

    private final String headerName;
    private final String headerValue;

    public HeaderAuthenticationProviderMatcher(String providerId, String headerName, String headerValue) {
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

    @MatcherFactory
    public static class Factory implements AuthenticationProviderMatcherFactory {

        @Override
        public String getType() {
            return "HEADER";
        }

        @Override
        public AuthenticationProviderMatcher create(String providerId, Map<String, String> properties) {
            var headerName = properties.get("headerName");
            var headerValue = properties.get("headerValue");

            Assert.notNull(headerName, "Property headerName cannot be null");
            Assert.notNull(headerValue, "Property headerValue cannot be null");

            return new HeaderAuthenticationProviderMatcher(providerId, headerName, headerValue);
        }
    }

}
