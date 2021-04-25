package pl.mzlnk.autoconfigure.oauth2.server.resource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.AuthenticationProviderProperties;
import pl.mzlnk.autoconfigure.oauth2.server.resource.resolver.MultitenantAuthenticationManagerResolver;

import java.util.List;

@Configuration
public class AutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "oauth2.resource.server")
    public AuthenticationProviderProperties authenticationProviderProperties() {
        return new AuthenticationProviderProperties();
    }

    @Bean
    public MultitenantAuthenticationManagerResolver multitenantAuthenticationManagerResolver(AuthenticationProviderProperties properties,
                                                                                             List<AuthenticationProviderMatcher> matchers) {
        return new MultitenantAuthenticationManagerResolver(properties, matchers);
    }

}
