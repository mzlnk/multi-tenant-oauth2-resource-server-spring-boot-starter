package pl.mzlnk.autoconfigure.oauth2.server.resource.context;

import org.springframework.security.core.context.SecurityContext;

public interface AuthenticationTenantContextHolderStrategy {

    String getName();

    void clearContext();

    AuthenticationTenantContext getContext();

    void setContext(AuthenticationTenantContext context);

    AuthenticationTenantContext createEmptyContext();

}
