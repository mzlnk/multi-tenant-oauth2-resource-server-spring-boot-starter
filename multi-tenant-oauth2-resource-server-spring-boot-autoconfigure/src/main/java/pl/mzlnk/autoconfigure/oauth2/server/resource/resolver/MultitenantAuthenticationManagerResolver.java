package pl.mzlnk.autoconfigure.oauth2.server.resource.resolver;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.AuthenticationProvider;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.AuthenticationProviderProperties;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.TokenType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultitenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private final Map<String, AuthenticationProvider> providers;
    private final Map<String, AuthenticationProviderMatcher> matchers;

    public MultitenantAuthenticationManagerResolver(AuthenticationProviderProperties providerProperties,
                                                    List<AuthenticationProviderMatcher> matchers) {
        this.providers = providerProperties.getProviders();
        this.matchers = matchers.stream().collect(Collectors.toMap(AuthenticationProviderMatcher::getProviderId, m -> m));
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest request) {
        return request -> {
            var opaque = this.providers.values()
                    .stream()
        }
    }

    private JwtIssuerAuthenticationManagerResolver jwt() {
        return new JwtIssuerAuthenticationManagerResolver(
                this.providers.values()
                .stream()
                .filter(p -> p.getTokenType() == TokenType.OPAQUE)
                .map(AuthenticationProvider::getJwtIssuerUri)
                .collect(Collectors.toList())
        );
    }

}
