package io.mzlnk.springframework.multitenant.oauth2.resourceserver;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationProviderProperties;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.MultitenantAuthenticationManagerResolver;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.token.TokenResolver;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.AuthenticationTenant;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcher;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.context.AuthenticationTenantContextFilter;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.AuthenticationTenantFactory;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcherFactory;

import java.util.List;

@Configuration
public class AutoConfiguration {

    @Bean
    public AuthenticationTenantMatcherFactory authenticationTenantMatcherFactory(List<AuthenticationTenantMatcher.Factory> factories) {
        return new AuthenticationTenantMatcherFactory(factories);
    }

    @Bean
    public AuthenticationTenantFactory authenticationTenantFactory(List<AuthenticationTenant.Factory> factories,
                                                                   AuthenticationTenantMatcherFactory matcherFactory) {
        return new AuthenticationTenantFactory(factories, matcherFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "oauth2.resource.server")
    public AuthenticationProviderProperties authenticationProviderProperties() {
        return new AuthenticationProviderProperties();
    }

    @Bean
    public MultitenantAuthenticationManagerResolver multitenantAuthenticationManagerResolver(AuthenticationProviderProperties properties,
                                                                                             List<AuthenticationTenantMatcher> matchers,
                                                                                             AuthenticationTenantFactory tenantFactory,
                                                                                             TokenResolver tokenResolver) {
        return new MultitenantAuthenticationManagerResolver(properties, matchers, tenantFactory, tokenResolver);
    }

    @Bean
    public AuthenticationTenantContextFilter authenticationTenantContextFilter(MultitenantAuthenticationManagerResolver resolver) {
        return new AuthenticationTenantContextFilter(resolver);
    }

}
