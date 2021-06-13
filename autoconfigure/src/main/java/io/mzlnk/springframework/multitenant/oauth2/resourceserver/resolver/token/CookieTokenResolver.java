package io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

public class CookieTokenResolver implements TokenResolver {

    private final String cookieName;

    public CookieTokenResolver(String cookieName) {
        this.cookieName = cookieName;
    }

    @Override
    public String resolve(HttpServletRequest httpRequest) {
        return Arrays.stream(httpRequest.getCookies())
                .filter(cookie -> cookie.getName().equals(this.cookieName))
                .findAny()
                .map(Cookie::getValue)
                .orElse(null);
    }

}
