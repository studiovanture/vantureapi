package me.vanturestudio.vantureapi.commands.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Cooldown {
	int seconds() default 0;
}
