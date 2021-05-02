package io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.TokenType;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcher;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcherFactory;

import java.util.Collections;
import java.util.List;

public abstract class AuthenticationTenant {

    private final String providerId;
    private final TokenType tokenType;
    private final String issuer;
    private final List<AuthenticationTenantMatcher> matchers;

    public AuthenticationTenant(String providerId, TokenType tokenType, String issuer, List<AuthenticationTenantMatcher> matchers) {
        this.providerId = providerId;
        this.tokenType = tokenType;
        this.issuer = issuer;
        this.matchers = matchers;
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
    public List<AuthenticationTenantMatcher> getMatchers() {
        return Collections.unmodifiableList(matchers);
    }

    public void addMatcher(AuthenticationTenantMatcher matcher) {
        this.matchers.add(matcher);
    }

    public void addMatchers(List<AuthenticationTenantMatcher> matchers) {
        this.matchers.addAll(matchers);
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
