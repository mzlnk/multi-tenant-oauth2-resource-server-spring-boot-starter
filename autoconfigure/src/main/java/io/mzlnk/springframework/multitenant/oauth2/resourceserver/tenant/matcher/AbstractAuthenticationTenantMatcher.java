package io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher;

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
