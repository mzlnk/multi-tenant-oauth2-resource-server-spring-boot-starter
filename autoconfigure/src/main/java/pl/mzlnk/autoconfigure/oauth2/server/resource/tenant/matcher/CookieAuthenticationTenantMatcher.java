package pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher;

import org.springframework.util.Assert;
import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.AuthenticationTenantDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.stream.Stream;

public class CookieAuthenticationTenantMatcher extends AbstractAuthenticationTenantMatcher {

    private final String cookieName;
    private final String cookieValue;

    public CookieAuthenticationTenantMatcher(String providerId, String cookieName, String cookieValue) {
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

    public static class Factory implements AuthenticationTenantMatcher.Factory {

        @Override
        public String getType() {
            return "COOKIE";
        }

        @Override
        public AuthenticationTenantMatcher create(String providerId, AuthenticationTenantDetails.MatcherDetails matcherDetails) {
            var cookieName = matcherDetails.getProperty("cookieName");
            var cookieValue = matcherDetails.getProperty("cookieValue");

            Assert.notNull(cookieName, "Property cookieName cannot be null");
            Assert.notNull(cookieValue, "Property cookieValue cannot be null");

            return new CookieAuthenticationTenantMatcher(providerId, cookieName, cookieValue);
        }
    }

}
