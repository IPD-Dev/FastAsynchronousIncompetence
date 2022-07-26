package com.sk89q.worldedit.command.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Step {

    Class clazz() default Link.class;

    double value() default 1;

}
