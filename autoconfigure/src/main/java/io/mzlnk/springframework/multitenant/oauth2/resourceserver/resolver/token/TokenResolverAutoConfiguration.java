package io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.token;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class TokenResolverAutoConfiguration {

    @Bean
    @Primary
    @ConditionalOnProperty(
            prefix = "oauth2.resource.server.token-resolver",
            name = "type",
            havingValue = "COOKIE")
    public TokenResolver cookieTokenResolver(@Value("${oauth2.resource.server.token-resolver.cookie-name:access_token}") String cookieName) {
        return new CookieTokenResolver(cookieName);
    }

    @Bean
    public TokenResolver defaultTokenResolver() {
        return new DefaultTokenResolver();
    }

}
