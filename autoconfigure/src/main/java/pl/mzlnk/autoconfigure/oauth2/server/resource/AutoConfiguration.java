package pl.mzlnk.autoconfigure.oauth2.server.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import pl.mzlnk.autoconfigure.oauth2.server.resource.context.AuthenticationTenantContextFilter;
import pl.mzlnk.autoconfigure.oauth2.server.resource.properties.AuthenticationProviderProperties;
import pl.mzlnk.autoconfigure.oauth2.server.resource.resolver.MultitenantAuthenticationManagerResolver;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.AuthenticationTenant;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.AuthenticationTenantFactory;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcherFactory;

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
                                                                                             AuthenticationTenantFactory tenantFactory) {
        return new MultitenantAuthenticationManagerResolver(properties, matchers, tenantFactory);
    }

    @Bean
    @Order(1)
    public AuthenticationTenantContextFilter authenticationTenantContextFilter(MultitenantAuthenticationManagerResolver resolver) {
        return new AuthenticationTenantContextFilter(resolver);
    }

}
