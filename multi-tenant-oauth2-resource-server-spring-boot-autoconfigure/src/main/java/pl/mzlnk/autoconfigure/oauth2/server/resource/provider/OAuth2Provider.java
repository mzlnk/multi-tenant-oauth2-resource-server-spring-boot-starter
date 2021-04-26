package pl.mzlnk.autoconfigure.oauth2.server.resource.provider;

public class OAuth2Provider {

    private String providerId;
    private TokenType tokenType;
    private String clientId;
    private String clientSecret;
    private String jwtIssuerUri;
    private String introspectUri;

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

    public String getJwtIssuerUri() {
        return jwtIssuerUri;
    }

    public String getIntrospectUri() {
        return introspectUri;
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

    public void setJwtIssuerUri(String jwtIssuerUri) {
        this.jwtIssuerUri = jwtIssuerUri;
    }

    public void setIntrospectUri(String introspectUri) {
        this.introspectUri = introspectUri;
    }
}
