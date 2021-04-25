package pl.mzlnk.autoconfigure.oauth2.server.resource.provider;

public class AuthenticationProvider {

    private TokenType tokenType;
    private String clientId;
    private String clientSecret;
    private String jwtIssuerUri;
    private String introspectUri;

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

}
