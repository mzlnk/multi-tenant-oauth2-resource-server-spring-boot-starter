package io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties;

import java.util.List;

public class AuthenticationProviderProperties {

    private List<AuthenticationTenantDetails> tenants;

    public List<AuthenticationTenantDetails> getTenants() {
        return tenants;
    }

    public void setTenants(List<AuthenticationTenantDetails> tenants) {
        this.tenants = tenants;
    }

}
