package io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.jwt;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;

import javax.servlet.http.HttpServletRequest;
import java.security.interfaces.RSAPublicKey;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JwtAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private final List<AuthenticationManagerResolver<HttpServletRequest>> resolvers;

    @SafeVarargs
    private JwtAuthenticationManagerResolver(AuthenticationManagerResolver<HttpServletRequest>... resolvers) {
        this.resolvers = List.of(resolvers);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest request) {
        for (var resolver : this.resolvers) {
            try {
                return resolver.resolve(request);
            } catch (InvalidBearerTokenException ignored) {
            }
        }

        throw new InvalidBearerTokenException("Invalid issuer");
    }

    public static class Builder {

        private AuthenticationManagerResolver<HttpServletRequest> publicKeyResolver;
        private AuthenticationManagerResolver<HttpServletRequest> trustedIssuerResolver;

        public Builder withPublicKeyResolver(Map<String, RSAPublicKey> publicKeys) {
            this.publicKeyResolver = new JwtIssuerAuthenticationManagerResolver(new PublicKeyJwtAuthenticationManagerResolver(publicKeys));
            return this;
        }

        public Builder withTrustedIssuerResolver(Collection<String> trustedIssuers) {
            this.trustedIssuerResolver = new JwtIssuerAuthenticationManagerResolver(trustedIssuers);
            return this;
        }

        public JwtAuthenticationManagerResolver build() {
            return new JwtAuthenticationManagerResolver(this.publicKeyResolver, this.trustedIssuerResolver);
        }

    }

}
