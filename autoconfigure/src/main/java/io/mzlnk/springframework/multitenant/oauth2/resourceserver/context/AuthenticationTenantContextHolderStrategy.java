package io.mzlnk.springframework.multitenant.oauth2.resourceserver.context;

public interface AuthenticationTenantContextHolderStrategy {

    String getName();

    void clearContext();

    AuthenticationTenantContext getContext();

    void setContext(AuthenticationTenantContext context);

    AuthenticationTenantContext createEmptyContext();

}
