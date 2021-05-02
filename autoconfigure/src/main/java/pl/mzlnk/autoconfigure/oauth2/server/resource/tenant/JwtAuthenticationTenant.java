package pl.mzlnk.autoconfigure.oauth2.server.resource.tenant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.AuthenticationTenantDetails;
import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.TokenType;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcherFactory;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationTenant extends AuthenticationTenant {

    private final String jwtIssuerUri;
    private final RSAPublicKey jwtPublicKey;

    public JwtAuthenticationTenant(String providerId,
                                   TokenType tokenType,
                                   String issuer,
                                   List<AuthenticationTenantMatcher> matchers,
                                   String jwtIssuerUri,
                                   RSAPublicKey jwtPublicKey) {

        super(providerId, tokenType, issuer, matchers);
        this.jwtIssuerUri = jwtIssuerUri;
        this.jwtPublicKey = jwtPublicKey;
    }

    public String getJwtIssuerUri() {
        return jwtIssuerUri;
    }

    @JsonIgnore
    public RSAPublicKey getJwtPublicKey() {
        return jwtPublicKey;
    }

    public static class Factory implements AuthenticationTenant.Factory {

        @Override
        public TokenType getType() {
            return TokenType.JWT;
        }

        @Override
        public AuthenticationTenant fromDetails(AuthenticationTenantDetails tenant, AuthenticationTenantMatcherFactory matcherFactory) {
            return new JwtAuthenticationTenant(
                    tenant.getProviderId(),
                    tenant.getTokenType(),
                    tenant.getIssuer(),
                    tenant.getMatchers().stream().map(matcherFactory::fromMatcherDetails).collect(Collectors.toList()),
                    tenant.getJwtIssuerUri(),
                    tenant.getJwtPublicKey()
            );
        }
    }

}
