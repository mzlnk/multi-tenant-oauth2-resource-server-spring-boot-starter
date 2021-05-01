package pl.mzlnk.autoconfigure.oauth2.server.resource.resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.OpaqueTokenAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.AuthenticationProviderProperties;
import pl.mzlnk.autoconfigure.oauth2.server.resource.resolver.jwt.JwtAuthenticationManagerResolver;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.AuthenticationTenant;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.AuthenticationTenantFactory;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.JwtAuthenticationTenant;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.OpaqueAuthenticationTenant;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MultitenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private final Log log = LogFactory.getLog(this.getClass());

    private final List<AuthenticationTenant> tenants;
    private final Map<String, AuthenticationProvider> opaqueProviders;
    private final JwtAuthenticationManagerResolver jwtResolver;

    public MultitenantAuthenticationManagerResolver(AuthenticationProviderProperties tenantsProperties,
                                                    List<AuthenticationTenantMatcher> externalMatchers,
                                                    AuthenticationTenantFactory tenantFactory) {

        this.tenants = tenantFactory.create(tenantsProperties.getTenants(), externalMatchers);
        this.opaqueProviders = opaqueTokenAuthenticationProviders();
        this.jwtResolver = jwtResolver();
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest httpRequest) {
        return request -> this.tenants.stream()
                .filter(AuthenticationTenant::isRelatedToOpaqueToken)
                .filter(p -> p.getMatchers().stream().anyMatch(matcher -> matcher.matches(httpRequest)))
                .findAny()
                .map(AuthenticationTenant::getProviderId)
                .map(this::getOpaqueProvider)
                .map(p -> p.authenticate(request))
                .orElseGet(() -> this.jwtResolver.resolve(httpRequest).authenticate(request));
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
                .filter(p -> p.getJwtIssuer() != null)
                .filter(p -> p.getJwtPublicKey() != null)
                .collect(Collectors.toMap(JwtAuthenticationTenant::getJwtIssuer, JwtAuthenticationTenant::getJwtPublicKey));

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
