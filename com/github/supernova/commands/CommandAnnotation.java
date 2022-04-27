package com.github.supernova.commands;

import com.github.supernova.modules.Category;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandAnnotation {

    String name();

    String description() default "";

    String[] usages();

}
