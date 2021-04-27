package pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
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

}
