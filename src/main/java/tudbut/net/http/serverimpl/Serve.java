package tudbut.net.http.serverimpl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Serve {

    String value();
    Type type() default Type.WILDCARD;

    enum Type {
        PLAIN,
        REGEX,
        WILDCARD,
        ;
    }
}
