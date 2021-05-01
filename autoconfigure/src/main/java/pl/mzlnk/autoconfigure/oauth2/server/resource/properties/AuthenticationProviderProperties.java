package pl.mzlnk.autoconfigure.oauth2.server.resource.properties;

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
