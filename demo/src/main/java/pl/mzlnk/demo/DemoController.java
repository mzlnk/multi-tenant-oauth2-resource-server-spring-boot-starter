package pl.mzlnk.demo;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.mzlnk.autoconfigure.oauth2.server.resource.context.AuthenticationTenantContextHolder;
import pl.mzlnk.autoconfigure.oauth2.server.resource.tenant.AuthenticationTenant;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class DemoController {

    @GetMapping("/auth/callback")
    public String showAuthorizationCode(@RequestParam String code) {
        return code;
    }

    @GetMapping("/authorized/me")
    public String me(Principal principal) {
        var opaquePrincipal = (OAuth2IntrospectionAuthenticatedPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var iss = opaquePrincipal.getAttributes().get("iss");

        return "User: " + principal.getName() + ", issuer: " + iss;
    }

    @GetMapping("/authorized/tenant")
    public AuthenticationTenant getTenant() {
        var tenant = AuthenticationTenantContextHolder.getContext().getAuthenticationTenant();
        return tenant;
    }

    @GetMapping("/non-authorized")
    public String helloWorld() {
        return "Hello World from non authorized URI!";
    }

    @GetMapping("/non-authorized/cookies")
    public List<Cookie> getCookies(HttpServletRequest request) {
        return Stream.of(request.getCookies())
                .collect(Collectors.toList());
    }

}
