package kuleuven.group2;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates a change to the original Junit project done by our team.
 * 
 * When a method was changed the annotation will be used on the method. When a
 * class was significantly changed the annotation will be used on the class.
 * 
 * This annotation should also be accompanied with additional comments on what
 * was changed and why the change was made.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Kuleuven {

}
