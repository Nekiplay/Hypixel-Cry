package com.nekiplay.hypixelcry.config;

import cc.polyfrost.oneconfig.config.annotations.CustomOption;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@CustomOption(id = "imageCheckbox")
public @interface ImageCheckboxAnnotation {
    String category();
    String subcategory();
    String image();
}
