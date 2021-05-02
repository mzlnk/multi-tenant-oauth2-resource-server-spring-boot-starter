package io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.TokenType;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcher;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcherFactory;

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

    public List<AuthenticationTenant> create(List<AuthenticationTenantDetails> tenantsDetails, List<AuthenticationTenantMatcher> externalMatchers) {
        var externalMatchersByProvider = externalMatchers.stream()
                .collect(groupingBy(AuthenticationTenantMatcher::getProviderId));

        var tenants = tenantsDetails.stream()
                .map(this::create)
                .collect(Collectors.toList());

        tenants.stream()
                .filter(AuthenticationTenant::isRelatedToOpaqueToken)
                .map(OpaqueAuthenticationTenant.class::cast)
                .forEach(tenant -> tenant.addMatchers(externalMatchersByProvider.getOrDefault(tenant.getProviderId(), Collections.emptyList())));

        return tenants;
    }

    public AuthenticationTenant create(AuthenticationTenantDetails tenant) {
        return this.factories.get(tenant.getTokenType()).fromDetails(tenant, this.matcherFactory);
    }

}
