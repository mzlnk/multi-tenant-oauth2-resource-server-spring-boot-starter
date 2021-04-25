package pl.mzlnk.autoconfigure.oauth2.server.resource.api;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationProviderMatcher {

    String getProviderId();
    boolean matches(HttpServletRequest request);

}
