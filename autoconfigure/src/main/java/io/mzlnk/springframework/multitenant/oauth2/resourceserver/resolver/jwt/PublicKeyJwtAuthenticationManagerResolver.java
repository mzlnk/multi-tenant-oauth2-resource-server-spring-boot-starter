package io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.jwt;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.util.Assert;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;
import java.util.stream.Collectors;

public class PublicKeyJwtAuthenticationManagerResolver implements AuthenticationManagerResolver<String> {

    private final Log logger = LogFactory.getLog(this.getClass());
    private final Map<String, AuthenticationManager> authenticationManagers;

    public PublicKeyJwtAuthenticationManagerResolver(Map<String, RSAPublicKey> issuersPublicKeys) {
        this.authenticationManagers = issuersPublicKeys.entrySet()
                .stream()
                .collect(Collectors.toConcurrentMap(this::retrieveIssuer, this::retrieveAuthenticationManager));
    }

    @Override
    public AuthenticationManager resolve(String issuer) {
        if (!this.authenticationManagers.containsKey(issuer)) {
            this.logger.debug("Did not resolve AuthenticationManager since public key had not been provided for this issuer");
            return null;
        }

        return this.authenticationManagers.get(issuer);
    }

    private String retrieveIssuer(Map.Entry<String, RSAPublicKey> entry) {
        return entry.getKey();
    }

    private AuthenticationManager retrieveAuthenticationManager(Map.Entry<String, RSAPublicKey> entry) {
        var jwtDecoder = NimbusJwtDecoder.withPublicKey(entry.getValue()).build();
        var provider = new JwtAuthenticationProvider(jwtDecoder);

        return provider::authenticate;
    }

}
