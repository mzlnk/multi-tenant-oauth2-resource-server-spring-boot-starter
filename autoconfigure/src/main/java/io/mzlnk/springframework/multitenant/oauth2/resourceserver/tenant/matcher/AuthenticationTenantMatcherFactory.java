package io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AuthenticationTenantMatcherFactory {

    private final Map<String, AuthenticationTenantMatcher.Factory> factories;

    public AuthenticationTenantMatcherFactory(List<AuthenticationTenantMatcher.Factory> factories) {
        this.factories = factories.stream()
                .collect(Collectors.toMap(AuthenticationTenantMatcher.Factory::getType, f -> f));
    }

    public AuthenticationTenantMatcher fromMatcherDetails(AuthenticationTenantDetails.MatcherDetails matcherDetails) {
        return factories.get(matcherDetails.getType()).create(null, matcherDetails);
    }

}
