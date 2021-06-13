package io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver;

import com.nimbusds.jwt.JWTParser;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationProviderProperties;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.TokenType;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.jwt.JwtAuthenticationManagerResolver;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.token.TokenResolver;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.AuthenticationTenant;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.AuthenticationTenantFactory;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.JwtAuthenticationTenant;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.OpaqueAuthenticationTenant;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import javax.servlet.http.HttpServletRequest;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MultitenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private final List<AuthenticationTenant> tenants;
    private final Map<String, AuthenticationTenant> tenantsByIssuer;

    private final Map<String, AuthenticationProvider> opaqueProviders;
    private final JwtAuthenticationManagerResolver jwtResolver;

    private final TokenResolver tokenResolver;

    public MultitenantAuthenticationManagerResolver(AuthenticationProviderProperties tenantsProperties,
                                                    List<AuthenticationTenantMatcher> externalMatchers,
                                                    AuthenticationTenantFactory tenantFactory,
                                                    TokenResolver tokenResolver) {

        this.tenants = tenantFactory.create(tenantsProperties.getTenants(), externalMatchers);
        this.tenantsByIssuer = this.tenants.stream().collect(Collectors.toMap(AuthenticationTenant::getIssuer, a -> a));
        this.opaqueProviders = opaqueTokenAuthenticationProviders();
        this.jwtResolver = jwtResolver();
        this.tokenResolver = tokenResolver;
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest httpRequest) {
        AuthenticationManager jwtResolver = request -> this.jwtResolver.resolve(httpRequest).authenticate(request);

        AuthenticationManager opaqueResolver = request -> this.tenants.stream()
                .filter(AuthenticationTenant::isRelatedToOpaqueToken)
                .map(OpaqueAuthenticationTenant.class::cast)
                .filter(p -> p.getMatchers().stream().anyMatch(matcher -> matcher.matches(httpRequest)))
                .findAny()
                .map(AuthenticationTenant::getProviderId)
                .map(this::getOpaqueProvider)
                .map(p -> p.authenticate(request))
                .orElseThrow(() -> new AuthenticationServiceException("No authentication provider matches request"));

        return switch (getTokenType(httpRequest)) {
            case JWT -> jwtResolver;
            case OPAQUE -> opaqueResolver;
        };
    }

    public AuthenticationTenant getTenantByIssuer(URL issuer) {
        return this.tenantsByIssuer.get(issuer.toString());
    }

    private TokenType getTokenType(HttpServletRequest httpRequest) {
        String token = this.tokenResolver.resolve(httpRequest);

        try {
            JWTParser.parse(token);

            return TokenType.JWT;
        } catch (Exception ignored) {
        }

        return TokenType.OPAQUE;
    }

    private AuthenticationProvider getOpaqueProvider(String providerId) {
        var result = this.opaqueProviders.get(providerId);
        return result;
    }

    private Map<String, AuthenticationProvider> opaqueTokenAuthenticationProviders() {
        return this.tenants.stream()
                .filter(AuthenticationTenant::isRelatedToOpaqueToken)
                .map(OpaqueAuthenticationTenant.class::cast)
                .collect(Collectors.toMap(
                        OpaqueAuthenticationTenant::getProviderId,
                        tenant -> {
                            var introspector = new NimbusOpaqueTokenIntrospector(
                                    tenant.getIntrospectUri(),
                                    tenant.getClientId(),
                                    tenant.getClientSecret()
                            );

                            return new OpaqueTokenAuthenticationProvider(introspector);
                        }
                ));
    }

    private JwtAuthenticationManagerResolver jwtResolver() {
        var jwtTenants = this.tenants.stream()
                .filter(AuthenticationTenant::isRelatedToJwtToken)
                .map(JwtAuthenticationTenant.class::cast)
                .collect(Collectors.toList());

        var publicKeys = jwtTenants.stream()
                .filter(p -> p.getIssuer() != null)
                .filter(p -> p.getJwtPublicKey() != null)
                .collect(Collectors.toMap(JwtAuthenticationTenant::getIssuer, JwtAuthenticationTenant::getJwtPublicKey));

        var trustedIssuers = jwtTenants.stream()
                .map(JwtAuthenticationTenant::getJwtIssuerUri)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return JwtAuthenticationManagerResolver.builder()
                .withPublicKeyResolver(publicKeys)
                .withTrustedIssuerResolver(trustedIssuers)
                .build();
    }

}
