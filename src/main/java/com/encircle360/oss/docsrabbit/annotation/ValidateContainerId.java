package com.encircle360.oss.docsrabbit.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ContainerIdValidator.class})
public @interface ValidateContainerId {

    String message() default "Please provide a containerId for this render format.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
