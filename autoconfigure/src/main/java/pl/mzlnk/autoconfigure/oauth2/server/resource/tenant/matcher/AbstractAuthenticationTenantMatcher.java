package pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher;

public abstract class AbstractAuthenticationTenantMatcher implements AuthenticationTenantMatcher {

    private final String providerId;

    public AbstractAuthenticationTenantMatcher(String providerId) {
        this.providerId = providerId;
    }

    @Override
    public String getProviderId() {
        return this.providerId;
    }

}
