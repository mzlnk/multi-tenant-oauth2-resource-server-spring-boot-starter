package pl.mzlnk.autoconfigure.oauth2.server.resource.tenant;

import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.AuthenticationTenantDetails;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.TokenType;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcherFactory;

import java.util.List;
import java.util.stream.Collectors;

public class OpaqueAuthenticationTenant extends AuthenticationTenant {

    private final String clientId;
    private final String clientSecret;
    private final String introspectUri;

    public OpaqueAuthenticationTenant(String providerId,
                                      TokenType tokenType,
                                      String issuer,
                                      List<AuthenticationTenantMatcher> matchers,
                                      String clientId,
                                      String clientSecret,
                                      String introspectUri) {
        super(providerId, tokenType, issuer, matchers);
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.introspectUri = introspectUri;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getIntrospectUri() {
        return introspectUri;
    }

    public static class Factory implements AuthenticationTenant.Factory {

        @Override
        public TokenType getType() {
            return TokenType.OPAQUE;
        }

        @Override
        public AuthenticationTenant fromDetails(AuthenticationTenantDetails tenant, AuthenticationTenantMatcherFactory matcherFactory) {
            return new OpaqueAuthenticationTenant(
                    tenant.getProviderId(),
                    tenant.getTokenType(),
                    tenant.getIssuer(),
                    tenant.getMatchers().stream().map(matcherFactory::fromMatcherDetails).collect(Collectors.toList()),
                    tenant.getClientId(),
                    tenant.getClientSecret(),
                    tenant.getIntrospectUri()
            );
        }
    }

}
