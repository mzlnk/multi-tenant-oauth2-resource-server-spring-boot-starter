package pl.mzlnk.autoconfigure.oauth2.server.resource.provider;

import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher.CookieAuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher.HeaderAuthenticationProviderMatcher;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OAuth2Provider {

    private static final AuthenticationProviderMatcherConverter converter;

    // TODO: refactor to use Spring Context
    static {
        converter = new AuthenticationProviderMatcherConverter(List.of(
                new CookieAuthenticationProviderMatcher.Factory(),
                new HeaderAuthenticationProviderMatcher.Factory()
        ));
    }

    private String providerId;
    private TokenType tokenType;

    private String clientId;
    private String clientSecret;

    private String jwtIssuer;
    private String jwtIssuerUri;
    private RSAPublicKey jwtPublicKey;

    private String introspectUri;
    private List<AuthenticationProviderMatcher> matchers = new ArrayList<>();

    public String getProviderId() {
        return providerId;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getJwtIssuer() {
        return jwtIssuer;
    }

    public String getJwtIssuerUri() {
        return jwtIssuerUri;
    }

    public RSAPublicKey getJwtPublicKey() {
        return jwtPublicKey;
    }

    public String getIntrospectUri() {
        return introspectUri;
    }

    public List<AuthenticationProviderMatcher> getMatchers() {
        return Collections.unmodifiableList(this.matchers);
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public void setJwtIssuer(String jwtIssuer) {
        this.jwtIssuer = jwtIssuer;
    }

    public void setJwtIssuerUri(String jwtIssuerUri) {
        this.jwtIssuerUri = jwtIssuerUri;
    }

    public void setJwtPublicKey(RSAPublicKey jwtPublicKey) {
        this.jwtPublicKey = jwtPublicKey;
    }

    public void setIntrospectUri(String introspectUri) {
        this.introspectUri = introspectUri;
    }

    public void setMatchers(List<Map<String, String>> matchers) {
        this.matchers = matchers.stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    public void addMatcher(AuthenticationProviderMatcher matcher) {
        this.matchers.add(matcher);
    }

    public boolean isRelatedToOpaqueToken() {
        return this.tokenType == TokenType.OPAQUE;
    }

    public boolean isRelatedToJwtToken() {
        return this.tokenType == TokenType.JWT;
    }

}
