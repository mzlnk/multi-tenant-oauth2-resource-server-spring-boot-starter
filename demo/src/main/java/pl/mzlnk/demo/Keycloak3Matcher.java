package pl.mzlnk.demo;

import pl.mzlnk.autoconfigure.oauth2.server.resource.api.MatcherFactory;
import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.AuthenticationTenantDetails;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.Matcher;

import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

@MatcherFactory
public class Keycloak3Matcher implements AuthenticationTenantMatcher.Factory {

    @Override
    public String getType() {
        return "TEST";
    }

    @Override
    public AuthenticationTenantMatcher create(String providerId, AuthenticationTenantDetails.MatcherDetails matcherDetails) {
        return new AuthenticationTenantMatcher() {
            @Override
            public String getProviderId() {
                return providerId;
            }

            @Override
            public boolean matches(HttpServletRequest request) {
                var method = request.getMethod();
                var props = matcherDetails.getProperty("method");
                return method.equals(props);
            }
        };
    }
}
