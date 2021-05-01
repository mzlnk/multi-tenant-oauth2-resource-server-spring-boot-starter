package pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher;

import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.AuthenticationTenantDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuthenticationTenantMatcher {

    String getProviderId();
    boolean matches(HttpServletRequest request);

    interface Factory {

        String getType();
        AuthenticationTenantMatcher create(String providerId, AuthenticationTenantDetails.MatcherDetails matcherDetails);

    }

}
