package io.mzlnk.springframework.multitenant.oauth2.resourceserver;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.AuthenticationTenant;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.JwtAuthenticationTenant;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.OpaqueAuthenticationTenant;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.AuthenticationTenantMatcher;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.CookieAuthenticationTenantMatcher;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher.HeaderAuthenticationTenantMatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FactoriesAutoConfiguration {

    @Bean
    public AuthenticationTenant.Factory jwtAuthenticationTenantFactory() {
        return new JwtAuthenticationTenant.Factory();
    }

    @Bean
    public AuthenticationTenant.Factory opaqueAuthenticationTenantFactory() {
        return new OpaqueAuthenticationTenant.Factory();
    }

    @Bean
    public AuthenticationTenantMatcher.Factory cookieAuthenticationTenantMatcherFactory() {
        return new CookieAuthenticationTenantMatcher.Factory();
    }

    @Bean
    public AuthenticationTenantMatcher.Factory headerAuthenticationTenantMatcherFactory() {
        return new HeaderAuthenticationTenantMatcher.Factory();
    }

}
