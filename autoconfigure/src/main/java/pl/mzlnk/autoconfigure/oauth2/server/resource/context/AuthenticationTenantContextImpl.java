package pl.mzlnk.autoconfigure.oauth2.server.resource.context;

import org.springframework.util.ObjectUtils;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.AuthenticationTenant;

import java.io.Serial;

public class AuthenticationTenantContextImpl implements AuthenticationTenantContext {

    @Serial
    private static final long serialVersionUID = 540L;
    private static final String TO_STRING_PATTERN = "%s [Tenant=%s]";

    private AuthenticationTenant tenant;

    public AuthenticationTenantContextImpl() {
    }

    public AuthenticationTenantContextImpl(AuthenticationTenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public AuthenticationTenant getAuthenticationTenant() {
        return this.tenant;
    }

    @Override
    public void setAuthenticationTenant(AuthenticationTenant tenant) {
        this.tenant = tenant;
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.tenant);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof AuthenticationTenantContextImpl) {
            var context = (AuthenticationTenantContextImpl) object;

            if (this.getAuthenticationTenant() == null && context.getAuthenticationTenant() == null) {
                return true;
            }

            var tenant = this.getAuthenticationTenant();
            var otherTenant = context.getAuthenticationTenant();

            if (tenant != null && tenant.equals(otherTenant)) {
                return true;
            }
        }

        return false;
    }

    public String toString() {
        return String.format(
                TO_STRING_PATTERN,
                this.getClass().getSimpleName(),
                this.tenant != null ? this.tenant : "null"
        );
    }

}
