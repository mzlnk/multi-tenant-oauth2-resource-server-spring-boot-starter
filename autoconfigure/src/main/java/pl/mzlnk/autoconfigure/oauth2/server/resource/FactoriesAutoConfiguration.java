package pl.mzlnk.autoconfigure.oauth2.server.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.AuthenticationTenant;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.JwtAuthenticationTenant;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.OpaqueAuthenticationTenant;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.AuthenticationTenantMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.CookieAuthenticationTenantMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.matcher.HeaderAuthenticationTenantMatcher;

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
