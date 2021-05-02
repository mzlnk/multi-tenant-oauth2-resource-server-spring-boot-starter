package pl.mzlnk.autoconfigure.oauth2.server.resource.context;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.mzlnk.autoconfigure.oauth2.server.resource.resolver.MultitenantAuthenticationManagerResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

public class AuthenticationTenantContextFilter extends OncePerRequestFilter {

    private final MultitenantAuthenticationManagerResolver resolver;

    public AuthenticationTenantContextFilter(MultitenantAuthenticationManagerResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public void doFilterInternal(HttpServletRequest httpRequest,
                                 HttpServletResponse httpResponse,
                                 FilterChain filterChain) throws IOException, ServletException {

        Optional.ofNullable(this.retrieveIssuer(SecurityContextHolder.getContext().getAuthentication().getPrincipal()))
                .map(this.resolver::getTenantByIssuer)
                .ifPresent(tenant -> {
                    var context = AuthenticationTenantContextHolder.createEmptyContext();
                    context.setAuthenticationTenant(tenant);
                    AuthenticationTenantContextHolder.setContext(context);
                });

        try {
            filterChain.doFilter(httpRequest, httpResponse);
        } finally {
            AuthenticationTenantContextHolder.clearContext();
        }

    }

    @Override
    public void destroy() {

    }

    private URL retrieveIssuer(Object principal) {
        if (principal == null) {
            return null;
        }

        if (principal instanceof OAuth2IntrospectionAuthenticatedPrincipal) {
            return ((OAuth2IntrospectionAuthenticatedPrincipal) principal).getIssuer();
        }

        if (principal instanceof Jwt) {
            return ((Jwt) principal).getIssuer();
        }

        return null;
    }

}
