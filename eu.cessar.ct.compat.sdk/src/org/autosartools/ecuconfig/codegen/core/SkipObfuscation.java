package org.autosartools.ecuconfig.codegen.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation marker that can be used to signal the obfuscator that the
 * specified Java element should not be obfuscated
 * 
 * 
 * 
 */
@Target({ElementType.PACKAGE, ElementType.TYPE, ElementType.CONSTRUCTOR, ElementType.FIELD,
	ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
public @interface SkipObfuscation
{
	// no implementation
}
