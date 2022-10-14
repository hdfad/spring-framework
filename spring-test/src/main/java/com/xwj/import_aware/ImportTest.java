package com.xwj.import_aware;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MyImportAware.class)
public @interface ImportTest {
    String importTest() default "";
}
