package io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.token;

import javax.servlet.http.HttpServletRequest;

public interface TokenResolver {

    String resolve(HttpServletRequest httpRequest);

}
