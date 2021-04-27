package pl.mzlnk.autoconfigure.oauth2.server.resource.resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.AuthenticationProviderProperties;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.OAuth2Provider;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.TokenType;
import pl.mzlnk.autoconfigure.oauth2.server.resource.security.jwt.JwtAuthenticationManagerResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MultitenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private final Log log = LogFactory.getLog(this.getClass());

    private final List<OAuth2Provider> providers;

    public MultitenantAuthenticationManagerResolver(AuthenticationProviderProperties providerProperties,
                                                    List<AuthenticationProviderMatcher> matchers) {

        this.providers = combine(providerProperties.getProviders(), matchers);
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest httpRequest) {
        return request -> this.providers.stream()
                .filter(OAuth2Provider::isRelatedToOpaqueToken)
                .filter(p -> p.getMatchers().stream().anyMatch(matcher -> matcher.matches(httpRequest)))
                .findAny()
                .map(this::opaqueTokenAuthenticationProvider)
                .map(p -> p.authenticate(request))
                .orElseGet(() -> jwt().resolve(httpRequest).authenticate(request));
    }

    private List<OAuth2Provider> combine(List<OAuth2Provider> providers, List<AuthenticationProviderMatcher> matchers) {
        var providersMap = providers.stream().collect(Collectors.toMap(OAuth2Provider::getProviderId, p -> p));

        matchers.stream()
                .filter(matcher -> {
                    if (!providersMap.containsKey(matcher.getProviderId())) {
                        log.warn("Found matcher for provider which has not been declared");
                        return false;
                    }
                    return true;
                })
                .forEach(matcher -> providersMap.get(matcher.getProviderId()).addMatcher(matcher));

        return new ArrayList<>(providersMap.values());
    }

    private AuthenticationProvider opaqueTokenAuthenticationProvider(OAuth2Provider provider) {
        var introspector = new NimbusOpaqueTokenIntrospector(
                provider.getIntrospectUri(),
                provider.getClientId(),
                provider.getClientSecret()
        );

        return new OpaqueTokenAuthenticationProvider(introspector);
    }

    private JwtAuthenticationManagerResolver jwt() {
        var publicKeys = this.providers
                .stream()
                .filter(p -> p.getJwtIssuer() != null)
                .filter(p -> p.getJwtPublicKey() != null)
                .collect(Collectors.toMap(OAuth2Provider::getJwtIssuer, OAuth2Provider::getJwtPublicKey));

        var trustedIssuers = this.providers.stream()
                .filter(p -> p.getTokenType() == TokenType.JWT)
                .map(OAuth2Provider::getJwtIssuerUri)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return JwtAuthenticationManagerResolver.builder()
                .withPublicKeyResolver(publicKeys)
                .withTrustedIssuerResolver(trustedIssuers)
                .build();
    }

}
