package pl.mzlnk.autoconfigure.oauth2.server.resource.tenant;

import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.AuthenticationTenantDetails;
import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.TokenType;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcherFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class AuthenticationTenantFactory {

    private final Map<TokenType, AuthenticationTenant.Factory> factories;
    private final AuthenticationTenantMatcherFactory matcherFactory;

    public AuthenticationTenantFactory(List<AuthenticationTenant.Factory> factories,
                                       AuthenticationTenantMatcherFactory matcherFactory) {
        this.matcherFactory = matcherFactory;
        this.factories = factories.stream()
                .collect(Collectors.toMap(AuthenticationTenant.Factory::getType, f -> f));
    }

    public List<AuthenticationTenant> create(List<AuthenticationTenantDetails> tenants, List<AuthenticationTenantMatcher> externalMatchers) {
        var externalMatchersByProvider = externalMatchers.stream()
                .collect(groupingBy(AuthenticationTenantMatcher::getProviderId));

        return tenants.stream()
                .map(this::create)
                .peek(tenant -> tenant.addMatchers(externalMatchersByProvider.getOrDefault(tenant.getProviderId(), Collections.emptyList())))
                .collect(Collectors.toList());
    }

    public AuthenticationTenant create(AuthenticationTenantDetails tenant) {
        return this.factories.get(tenant.getTokenType()).fromDetails(tenant, this.matcherFactory);
    }

}
