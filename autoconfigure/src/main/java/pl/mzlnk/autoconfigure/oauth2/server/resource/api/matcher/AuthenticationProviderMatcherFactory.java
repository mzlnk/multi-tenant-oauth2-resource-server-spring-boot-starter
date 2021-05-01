package pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher;

import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;

import java.util.Map;

public interface AuthenticationProviderMatcherFactory {

    String getType();
    AuthenticationProviderMatcher create(String providerId, Map<String, String> properties);

}
