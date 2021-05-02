package io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.TokenType;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcher;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcherFactory;

import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationTenant extends AuthenticationTenant {

    private final String jwtIssuerUri;
    private final RSAPublicKey jwtPublicKey;

    public JwtAuthenticationTenant(String providerId,
                                   TokenType tokenType,
                                   String issuer,
                                   String jwtIssuerUri,
                                   RSAPublicKey jwtPublicKey) {

        super(providerId, tokenType, issuer);
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
                    tenant.getJwtIssuerUri(),
                    tenant.getJwtPublicKey()
            );
        }
    }

}
