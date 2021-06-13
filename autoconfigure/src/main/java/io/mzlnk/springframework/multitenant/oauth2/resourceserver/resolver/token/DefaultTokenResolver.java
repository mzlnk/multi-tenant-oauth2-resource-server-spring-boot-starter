package io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.token;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;

import javax.servlet.http.HttpServletRequest;

public class DefaultTokenResolver implements TokenResolver {

    private final BearerTokenResolver resolver = new DefaultBearerTokenResolver();

    @Override
    public String resolve(HttpServletRequest httpRequest) {
        return resolver.resolve(httpRequest);
    }

}
