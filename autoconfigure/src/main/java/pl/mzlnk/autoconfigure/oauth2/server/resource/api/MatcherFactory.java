package pl.mzlnk.autoconfigure.oauth2.server.resource.api;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface MatcherFactory {

    @AliasFor(annotation = Component.class)
    String value() default "";

}
