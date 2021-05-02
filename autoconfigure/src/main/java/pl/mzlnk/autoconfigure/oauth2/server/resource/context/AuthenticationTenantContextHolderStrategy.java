package pl.mzlnk.autoconfigure.oauth2.server.resource.context;

public interface AuthenticationTenantContextHolderStrategy {

    String getName();

    void clearContext();

    AuthenticationTenantContext getContext();

    void setContext(AuthenticationTenantContext context);

    AuthenticationTenantContext createEmptyContext();

}
