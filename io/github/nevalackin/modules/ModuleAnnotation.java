package io.github.nevalackin.modules;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ModuleAnnotation {

	String name();

	String displayName();

	String description() default "Empty description";

	int keyBind() default 0;

	Category category();

	boolean visible() default true;

}
