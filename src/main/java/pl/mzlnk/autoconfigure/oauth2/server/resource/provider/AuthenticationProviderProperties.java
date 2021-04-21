package pl.mzlnk.autoconfigure.oauth2.server.resource.provider;

import java.util.Map;

public class AuthenticationProviderProperties {

    private Map<String, AuthenticationProvider> providers;

    public Map<String, AuthenticationProvider> getProviders() {
        return providers;
    }

}
