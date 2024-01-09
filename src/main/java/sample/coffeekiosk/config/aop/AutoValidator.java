package sample.coffeekiosk.config.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)  // 메서드에만 어노테이션을 적용할 수 있도록 설정
@Retention(RetentionPolicy.RUNTIME)  // 런타임시에 어노테이션 정보를 사용하기 위해 설정
public @interface AutoValidator {
    // 어노테이션 속성들을 여기에 정의할 수 있습니다.
    // 예: String value() default "";
}