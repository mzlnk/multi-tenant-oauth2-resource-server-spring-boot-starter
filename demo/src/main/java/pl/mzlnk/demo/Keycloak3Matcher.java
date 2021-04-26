package pl.mzlnk.demo;

import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.Matcher;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

@Matcher
public class Keycloak3Matcher implements AuthenticationProviderMatcher {

    @Override
    public String getProviderId() {
        return "keycloak-auth-provider-3";
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        if(request.getCookies() == null) {
            return false;
        }

        return Stream.of(request.getCookies())
                .anyMatch(cookie -> cookie.getName().equals("issuer") && cookie.getValue().equals("keycloak-3"));
    }

}
