package pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher;

import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;

public abstract class AbstractAuthenticationProviderMatcher implements AuthenticationProviderMatcher {

    private final String providerId;

    public AbstractAuthenticationProviderMatcher(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public String getProviderId() {
        return this.providerId;
    }

}
