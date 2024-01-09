package sample.coffeekiosk.config.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import sample.coffeekiosk.api.ApiResponse;

@Aspect
@Component
public class CustomValidatorAspect {

    @Pointcut("@annotation(sample.coffeekiosk.config.aop.AutoValidator))")
    public void autoValidator() {

    }

    @Around("autoValidator()")
    public Object validationCheck(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if ( arg instanceof BindingResult ) {
                BindingResult bindingResult = (BindingResult) arg;

                if ( bindingResult.hasErrors()) {
                    return ApiResponse.bindError("잘못된 요청입니다.", bindingResult);
                }
            }
        }
        return joinPoint.proceed();
    }
}
