package pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher;

import org.springframework.util.Assert;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.MatcherFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class CookieAuthenticationProviderMatcher extends AbstractAuthenticationProviderMatcher {

    private final String cookieName;
    private final String cookieValue;

    public CookieAuthenticationProviderMatcher(String providerId, String cookieName, String cookieValue) {
        super(providerId);

        this.cookieName = cookieName;
        this.cookieValue = cookieValue;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return Stream.ofNullable(request.getCookies())
                .flatMap(Arrays::stream)
                .anyMatch(cookie -> cookie.getName().equals(this.cookieName) && cookie.getValue().equals(this.cookieValue));
    }

    @MatcherFactory
    public static class Factory implements AuthenticationProviderMatcherFactory {

        @Override
        public String getType() {
            return "COOKIE";
        }
        @Override
        public AuthenticationProviderMatcher create(String providerId, Map<String, String> properties) {
            var cookieName = properties.get("cookieName");
            var cookieValue = properties.get("cookieValue");

            Assert.notNull(cookieName, "Property cookieName cannot be null");
            Assert.notNull(cookieValue, "Property cookieValue cannot be null");

            return new CookieAuthenticationProviderMatcher(providerId, cookieName, cookieValue);
        }
    }

}
