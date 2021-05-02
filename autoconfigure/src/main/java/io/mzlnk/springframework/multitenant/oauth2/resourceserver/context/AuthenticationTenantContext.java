package io.mzlnk.springframework.multitenant.oauth2.resourceserver.context;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.AuthenticationTenant;

import java.io.Serializable;

public interface AuthenticationTenantContext extends Serializable {

    AuthenticationTenant getAuthenticationTenant();

    void setAuthenticationTenant(AuthenticationTenant tenant);

}
