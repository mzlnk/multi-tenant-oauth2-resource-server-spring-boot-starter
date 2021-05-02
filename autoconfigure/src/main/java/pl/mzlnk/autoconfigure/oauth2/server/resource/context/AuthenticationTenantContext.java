package pl.mzlnk.autoconfigure.oauth2.server.resource.context;

import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.AuthenticationTenant;

import java.io.Serializable;

public interface AuthenticationTenantContext extends Serializable {

    AuthenticationTenant getAuthenticationTenant();

    void setAuthenticationTenant(AuthenticationTenant tenant);

}
