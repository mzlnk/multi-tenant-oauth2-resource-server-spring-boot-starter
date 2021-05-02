# Multi-Tenant OAuth2 Resource Server Spring Boot Starter

[![Licence: MIT](https://img.shields.io/badge/Licence-MIT-blue.svg)](https://shields.io/)
[![Version: BETA-1.0](https://img.shields.io/badge/version-1.0--beta-yellow.svg)](https://shields.io/)
[![Open Source](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)](https://github.com/ellerbrock/open-source-badges/)
[![Java : 15](https://img.shields.io/badge/Java-15-orange.svg)](https://jdk.java.net/15/)

## About

Have you tried to secure you Spring Boot application with OAuth2 but I haven't found any clear and quick solution for it? If so, this starter is for you!
The Multi-Tenant OAuth2 Resource Server is a Spring Boot starter created to configure multiple authorization tenants out of the box - just by adding them
in Spring Boot configuration file ;)


## Releases

🚧 The project is currently in BETA. There can be lack of some features or some bugs may still appear. However, we do our best to continuously improve
and develop the starter ;)

## Getting started!

### Include Maven dependency:

If you want to use the starter in your project - just include proper dependency in your `pom.xml` file.
```xml
<dependency>
  <groupId>io.mzlnk.springframework</groupId>
  <artifactId>multi-tenant-oauth2-resource-server-spring-boot-starter</artifactId>
  <version>1.0</version>
</dependency>
```

### Configure Spring Security Configuration:

To enable resolving bearer tokens from multiple tenants, you have to attach provided by starter `AuthenticationManagerResolver`. You can do it just by 
overriding method from `WebSecurityConfigurerAdapter`. Here is quick example how to do it ;)
```java
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final MultitenantAuthenticationManagerResolver resolver;
    
    public SecurityConfiguration(MultitenantAuthenticationManagerResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2ResourceServer()
                .authenticationManagerResolver(resolver);
    }

}
```

### Add tenants via application configuration

If you want to add tenants to your Spring Boot application, you have to edit your `application.yml` file (or corresponding `application.properties` file). Here is 
sample YAML config for adding three different tenants:
```yaml
oauth2:
  resource:
    server:
      tenants:
        - provider-id: auth-provider-1
          token-type: JWT
          issuer: "http://localhost:10001/auth/realms/auth-provider-1"
          jwt-public-key: "classpath:keys/auth-provider-1.pub"

        - provider-id: auth-provider-2
          token-type: JWT
          issuer: "http://localhost:10002/auth/realms/auth-provider-2"
          jwt-issuer-uri: "http://localhost:10002/auth/realms/auth-provider-2"

        - provider-id: auth-provider-3
          token-type: OPAQUE
          issuer: "http://localhost:10003/auth/realms/auth-provider-3"
          client-id: oauth2-demo
          client-secret: XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
          introspect-uri: "http://localhost:10003/auth/realms/auth-provider-3/protocol/openid-connect/token/introspect"
          matchers:
            - type: COOKIE
              cookie-name: "issuer"
              cookie-value: "auth-provider-3"
            - type: HEADER
              header-name: "Host"
              header-value: "mzlnk.io"
            - type: METHOD
              method: POST
```

Let's explain what all these properties mean.

First of all, the starter can handle both JWT and opaque tokens - that's why for different tokens we will need to provide different information.
The table below describes which properties are required both types of tokens.

| property       | JWT        | opaque     |
| -------------- | ---------- | ---------- |
| provider-id    | *required* | *required* |
| token-type     | *required* | *required* |
| issuer         | *required* | *required* |
| jwt-issuer-uri | *required* |            |
| jwt-public-key | *required* |            |
| client-id      |            | *required* |
| client-secret  |            | *required* |
| introspect-uri |            | *required* |
| matchers       |            | *required* |

#### property-id:

This is id for given authentication tenant. It can by any string but (what's important) it must be unique value among all declared tenants.


#### token-type:

This property defines what type of token given tenant can handle. The value for this property should be one of:
- `JWT`
- `OPAQUE`


#### issuer:

This property defines the name of the issuer (here our configured tenant). This is value unique for given provider and determined by the provider itself
(e.g. for Keycloak selfhosted on port 10000: `http://localhost:10002/auth/realms/auth-provider-2`). You have to check what is the value for given provider in their
documentation.


#### jwt-issuer-uri:

This property points to the base Authorization Server URI. This value can also be used to verify the iss claim in provided JWT token.


#### jwt-public-key:

This property points to JWT public key which is used to verify JWT tokens. It can be file location or key string representation itself.


#### client-id:

This property defines the client ID (from pair client ID/ client secret) which can be obtained in authorization provider.


#### client-secret:

This property defines the client secret (from pair client ID/ client secret) which can be obtained in authorization provider.


#### introspect-uri:

This property points to URI provided by authorization server where the opaque tokens can be verfied.


#### matchers:

Because of the fact that resource server cannot retrieve issuer directly from opaque token you have to provide additional information (here: *matcher*) which will be used
to determine which authentication tenant should be used to verify incoming opaque token. You can use built-in matchers or create a custom one. For more information
continue reading :D


### Matchers for Authentication Tenant

As mentioned before, each opaque token authentication tenant must have at least one declared matcher. It can be cookie, header value, request method,
some path - there are lots of possibilities! That's why you can create totally custom matcher against incoming request or just use one of most common
matchers.


#### Built-in matchers:

There are three built-in matchers provided by the starter:

#### Default matcher

If you want to use given tenant for all incoming opaque tokens (e.g. you have provided only one authentication tenant), you can use this default built-in matcher which simply matches all incoming request. Here is sample configuration:
```yaml
matchers:
  - type: DEFAULT
```


#### Matcher against cookie

This matcher can be added via configuration file under `matchers` property. You have to provide cookie name and the value which incoming request have to provide
with to verify token using given tenant. Here is sample configuration:
```yaml
matchers:
  - type: COOKIE
    cookie-name: "issuer"
    cookie-value: "auth-provider-1"
```

In above example given tenant will be used to verify token if incoming request will provide cookie with name `issuer` and its value equal to `auth-provider-1`.

#### Matcher against header:

This matcher can be added via configuration file under `matchers` property. You have to provide header name and the value which incoming request have to provide 
with to verify token using given tenant. Here is sample configuration:
```yaml
matchers:
  - type: HEADER
    header-name: "Host"
    header-value: "mzlnk.io"
```

In above example given tenant will be used to verify token if incoming request will provide `Host` header and its value equal to `mzlnk.io`.


#### Custom matchers:

If built-in matchers are not enough for you, you can easily create a custom one which fits your needs ;) To do it, you have to just create a class which implements
`AuthenticationTenantMatcher` interface and is annotated with `@Matcher`. Here is also quick example:
```java
@Matcher
public class AuthProvider3Matcher implements AuthenticationTenantMatcher {

    @Override
    public String getProviderId() {
        return "auth-provider-3";
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return request.getQueryString().contains("iss=auth-provider-3");
    }

}
```

In example above, we are creating matcher for authentication tenant defined in configuration file with `provider-id` equal to `auth-provider-3`.

#### Custom matcher factory:

What's more you can create custom matchers which can be used for multiple tenants similarly to the built-in ones. To achieve it, you have to create a class
which implements `AuthenticationTenantMatcher.Factory` interface and is annotated with `@MatcherFactory`. Then, add the matcher with required properties in
the configuration file under `matchers` property for given tenants. Here is quick example how to create custom matcher against request method.

**AuthenticationMatcherFactory:**
```java
@MatcherFactory
public class HttpMethodMatcherFactory implements AuthenticationTenantMatcher.Factory {

    private static final String TYPE = "METHOD";
    private static final String METHOD_PROPERTY_KEY = "method";

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public AuthenticationTenantMatcher create(String providerId, AuthenticationTenantDetails.MatcherDetails matcherDetails) {
        return new HttpMethodMatcher(providerId, matcherDetails.getProperty(METHOD_PROPERTY_KEY));
    }

    public static class HttpMethodMatcher extends AbstractAuthenticationTenantMatcher {

        private final String method;

        public HttpMethodMatcher(String providerId, String method) {
            super(providerId);
            this.method = method;
        }

        @Override
        public boolean matches(HttpServletRequest request) {
            return request.getMethod().equals(this.method);
        }
    }

}
```

**Configuration file:**
```yaml
// ...
- provider-id: auth-provider-1
  // ...
  matchers:
    - type: METHOD
      method: POST

- provider-id: auth-provider-2
  // ...
  matchers:
    - type: METHOD
      method: GET
```

## Demo

To gather all things in one place, there is a simple demo provided in this project [here](https://github.com/mzlnk/spring-boot-multi-tenant-oauth2-resource-server/tree/master/demo).
It is simple Spring Boot application which uses the starter and all mentioned features so you can check one more time how everything works together :D

## Want to contribute?

Feel free to fork this repository and request changes or add features to it. The whole project is built with Maven and Java 15 so these two tools are required
to run the code locally ;)

## Credits

This starter is under MIT licence so feel free to use it for your personal or even commercial use ;)

Created by Marcin Zielonka

