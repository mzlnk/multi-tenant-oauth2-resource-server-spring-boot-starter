package pl.mzlnk.autoconfigure.oauth2.server.resource.resolver;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationManagerResolver;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.AuthenticationProvider;
import pl.mzlnk.autoconfigure.oauth2.server.resource.provider.AuthenticationProviderProperties;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class MultitenantAuthenticationManagerResolver implements AuthenticationManagerResolver<HttpServletRequest> {

    private final Map<String, AuthenticationProvider> providers;

    public MultitenantAuthenticationManagerResolver(AuthenticationProviderProperties providerProperties) {
        this.providers = providerProperties.getProviders();
    }

    @Override
    public AuthenticationManager resolve(HttpServletRequest request) {
        return null;
    }
}
