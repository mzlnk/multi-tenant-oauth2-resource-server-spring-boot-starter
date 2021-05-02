package pl.mzlnk.autoconfigure.oauth2.server.resource.properties;

import java.security.interfaces.RSAPublicKey;
import java.util.*;
import java.util.stream.Collectors;

public class AuthenticationTenantDetails {

    private String providerId;
    private TokenType tokenType;

    private String clientId;
    private String clientSecret;

    private String issuer;
    private String jwtIssuerUri;
    private RSAPublicKey jwtPublicKey;

    private String introspectUri;
    private List<Map<String, String>> matchers;

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

    public String getIssuer() {
        return issuer;
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

    public List<MatcherDetails> getMatchers() {
        return Optional.ofNullable(this.matchers)
                .orElseGet(Collections::emptyList)
                .stream()
                .map(MatcherDetails::fromMap)
                .collect(Collectors.toList());
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

    public void setIssuer(String issuer) {
        this.issuer = issuer;
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
        this.matchers = matchers;
    }

    public boolean isRelatedToOpaqueToken() {
        return this.tokenType == TokenType.OPAQUE;
    }

    public boolean isRelatedToJwtToken() {
        return this.tokenType == TokenType.JWT;
    }

    public static class MatcherDetails {

        public static MatcherDetails fromMap(Map<String, String> properties) {
            return new MatcherDetails(properties);
        }

        private String type;
        private Map<String, String> properties = new HashMap<>();

        private MatcherDetails(Map<String, String> properties) {
            this.type = properties.get("type");
            this.properties.putAll(properties);
        }

        public String getType() {
            return type;
        }

        public String getProperty(String key) {
            return properties.get(key);
        }

    }

}
