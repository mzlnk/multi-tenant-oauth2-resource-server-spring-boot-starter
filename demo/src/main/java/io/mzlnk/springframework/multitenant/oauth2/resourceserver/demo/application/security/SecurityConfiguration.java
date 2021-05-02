package io.mzlnk.springframework.multitenant.oauth2.resourceserver.demo.application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import io.mzlnk.springframework.multitenant.oauth2.resourceserver.resolver.MultitenantAuthenticationManagerResolver;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MultitenantAuthenticationManagerResolver resolver;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/auth/callback", "/non-authorized/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .authenticationManagerResolver(resolver);
    }

}
