package io.mzlnk.springframework.multitenant.oauth2.resourceserver.tenant.matcher;

import io.mzlnk.springframework.multitenant.oauth2.resourceserver.properties.AuthenticationTenantDetails;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationTenantMatcher {

    String getProviderId();

    boolean matches(HttpServletRequest request);


    interface Factory {

        String getType();

        AuthenticationTenantMatcher create(String providerId, AuthenticationTenantDetails.MatcherDetails matcherDetails);

    }

}
