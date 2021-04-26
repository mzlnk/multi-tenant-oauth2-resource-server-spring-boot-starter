package pl.mzlnk.autoconfigure.oauth2.server.resource.provider;

import java.util.List;

public class AuthenticationProviderProperties {

    private List<OAuth2Provider> providers;

    public List<OAuth2Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<OAuth2Provider> providers) {
        this.providers = providers;
    }

}
