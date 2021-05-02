package io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.TokenType;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcherFactory;

public abstract class AuthenticationTenant {

    private final String providerId;
    private final TokenType tokenType;
    private final String issuer;

    public AuthenticationTenant(String providerId, TokenType tokenType, String issuer) {
        this.providerId = providerId;
        this.tokenType = tokenType;
        this.issuer = issuer;
    }

    public String getProviderId() {
        return providerId;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getIssuer() {
        return issuer;
    }

    @JsonIgnore
    public boolean isRelatedToOpaqueToken() {
        return this.tokenType == TokenType.OPAQUE;
    }

    @JsonIgnore
    public boolean isRelatedToJwtToken() {
        return this.tokenType == TokenType.JWT;
    }


    public interface Factory {

        TokenType getType();

        AuthenticationTenant fromDetails(AuthenticationTenantDetails tenant, AuthenticationTenantMatcherFactory matcherFactory);

    }

}
