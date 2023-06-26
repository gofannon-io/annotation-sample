package io.gofannon.apl.annotation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface TheConfParam {
    String name();

    String defaultValue();

    String description();
}
