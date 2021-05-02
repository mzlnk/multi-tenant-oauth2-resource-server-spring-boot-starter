package io.mzlnk.springframework.multitenant.oauth2.resourceserver.demo.api;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.context.AuthenticationTenantContextHolder;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.AuthenticationTenant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/authorized")
public class AuthorizedController {

    @GetMapping("/me")
    public ResponseEntity<Principal> getPrincipal(Principal principal) {
        return ResponseEntity.ok(principal);
    }

    @GetMapping("/tenant")
    public ResponseEntity<AuthenticationTenant> getTenant() {
        var tenant = AuthenticationTenantContextHolder.getContext().getAuthenticationTenant();

        return ResponseEntity.ok(tenant);
    }



}
