package pl.mzlnk.autoconfigure.oauth2.server.resource.resolver;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtIssuerAuthenticationManagerResolver;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.AuthenticationProviderProperties;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.OAuth2Provider;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.TokenType;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MultitenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private final Map<String, OAuth2Provider> providers;
    private final Map<String, AuthenticationProviderMatcher> matchers;

    public MultitenantAuthenticationManagerResolver(AuthenticationProviderProperties providerProperties,
                                                    List<AuthenticationProviderMatcher> matchers) {

        this.providers = providerProperties.getProviders().stream().collect(Collectors.toMap(OAuth2Provider::getProviderId, p -> p));
        this.matchers = matchers.stream().collect(Collectors.toMap(AuthenticationProviderMatcher::getProviderId, m -> m));
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest httpRequest) {
        return request -> this.providers.values()
                .stream()
                .filter(p -> p.getTokenType() == TokenType.OPAQUE)
                .filter(p -> this.matchers.containsKey(p.getProviderId()))
                .filter(p -> this.matchers.get(p.getProviderId()).matches(httpRequest))
                .findAny()
                .map(this::opaqueTokenAuthenticationProvider)
                .map(p -> p.authenticate(request))
                .orElse(jwt().resolve(httpRequest).authenticate(request));
    }

    private AuthenticationProvider opaqueTokenAuthenticationProvider(OAuth2Provider provider) {
        var introspector = new NimbusOpaqueTokenIntrospector(
                provider.getIntrospectUri(),
                provider.getClientId(),
                provider.getClientSecret()
        );

        return new OpaqueTokenAuthenticationProvider(introspector);
    }

    private JwtIssuerAuthenticationManagerResolver jwt() {
        return new JwtIssuerAuthenticationManagerResolver(
                this.providers.values()
                        .stream()
                        .filter(p -> p.getTokenType() == TokenType.OPAQUE)
                        .map(OAuth2Provider::getJwtIssuerUri)
                        .collect(Collectors.toList())
        );
    }

}
