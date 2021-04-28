package pl.mzlnk.autoconfigure.oauth2.server.resource.provider;

import org.springframework.core.convert.converter.Converter;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.AuthenticationProviderMatcher;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher.AuthenticationProviderMatcherFactory;
import pl.mzlnk.autoconfigure.oauth2.server.resource.api.matcher.CookieAuthenticationProviderMatcher;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthenticationProviderMatcherConverter implements Converter<Map<String, String>, AuthenticationProviderMatcher> {

    private final Map<String, AuthenticationProviderMatcherFactory> factories;

    public AuthenticationProviderMatcherConverter(List<AuthenticationProviderMatcherFactory> factories) {
        this.factories = factories.stream().collect(Collectors.toMap(f -> f.getType(), f -> f));
    }

    @Override
    public AuthenticationProviderMatcher convert(Map<String, String> properties) {
        String type = properties.get("type");

        return Optional.ofNullable(this.factories.get(type))
                .map(f -> f.create(null, properties))
                .orElseThrow(() -> new IllegalArgumentException("AuthenticationProviderMatcherFactory is not declared for given type"));
    }

}
