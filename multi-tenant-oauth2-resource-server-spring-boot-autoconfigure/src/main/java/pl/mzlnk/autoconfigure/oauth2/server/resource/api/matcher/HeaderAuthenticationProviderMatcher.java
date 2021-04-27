package pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class HeaderAuthenticationProviderMatcher extends AbstractAuthenticationProviderMatcher {

    private final String headerName;
    private final String headerValue;

    public HeaderAuthenticationProviderMatcher(String providerId, String headerName, String headerValue) {
        super(providerId);
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(this.headerName))
                .map(this.headerValue::equals)
                .orElse(false);
    }

}
