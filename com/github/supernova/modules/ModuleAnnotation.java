package com.github.supernova.modules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleAnnotation {


	String name();

	String displayName();

	String description() default "Empty description";

	int keyBind() default 0;

	Category category() default Category.OTHER;

	boolean visible() default true;

}
