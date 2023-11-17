//package fpt.edu.eresourcessystem.config.aspect;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import fpt.edu.eresourcessystem.dto.UserLogDto;
//import fpt.edu.eresourcessystem.model.UserLog;
//import fpt.edu.eresourcessystem.service.UserLogService;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.reflect.MethodSignature;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.*;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//
//@Aspect
//@Component
//public class GeneralInterceptorAspect {
//
//    private final UserLogService userLogService;
//    private final ObjectMapper objectMapper;
//
//    @Autowired
//    public GeneralInterceptorAspect(UserLogService userLogService, ObjectMapper objectMapper) {
//        this.userLogService = userLogService;
//        this.objectMapper = objectMapper;
//    }
//
//    @AfterReturning("execution(* fpt.edu.eresourcessystem.controller.*.*(..)) && " +
//            "( @annotation(org.springframework.web.bind.annotation.RequestMapping)" +
//            "|| @annotation(org.springframework.web.bind.annotation.GetMapping)" +
//            "|| @annotation(org.springframework.web.bind.annotation.PostMapping)" +
//            "|| @annotation(org.springframework.web.bind.annotation.PutMapping)" +
//            "|| @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
//            ")")
//    public void afterReturningAdvice(JoinPoint joinPoint) throws Throwable {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        String methodName = methodSignature.getName();
//        String requestUrl = getRequestUrl(joinPoint);
//        String httpMethod = getHttpMethod(joinPoint);
//        String parameters = getMethodParameters(joinPoint);
//
//
//        System.out.println("After Returning method: " + methodName);
//            // log user action
//            UserLogDto userLogDto = new UserLogDto(requestUrl);
//            UserLog userLog = userLogService.addUserLog(new UserLog(userLogDto));
//            System.out.println(userLog);
//
//
//    }
//
//    private String getRequestUrl(JoinPoint joinPoint) {
//        RequestMapping requestMapping = getAnnotation(joinPoint, RequestMapping.class);
//        GetMapping getMapping = getAnnotation(joinPoint, GetMapping.class);
//        PostMapping postMapping = getAnnotation(joinPoint, PostMapping.class);
//        PutMapping putMapping = getAnnotation(joinPoint, PutMapping.class);
//        DeleteMapping deleteMapping = getAnnotation(joinPoint, DeleteMapping.class);
//
//        if (requestMapping != null) {
//            return getRequestUrlFromRequestMapping(requestMapping);
//        } else if (getMapping != null) {
//            return getRequestUrlFromGetMapping(getMapping);
//        } else if (postMapping != null) {
//            return getRequestUrlFromPostMapping(postMapping);
//        } else if (putMapping != null) {
//            return getRequestUrlFromPutMapping(putMapping);
//        } else if (deleteMapping != null) {
//            return getRequestUrlFromDeleteMapping(deleteMapping);
//        }
//
//        return "";
//    }
//
//    private String getRequestUrlFromRequestMapping(RequestMapping requestMapping) {
//        return (requestMapping.value().length > 0) ? requestMapping.value()[0] : "";
//    }
//
//    private String getRequestUrlFromGetMapping(GetMapping getMapping) {
//        return (getMapping.value().length > 0) ? getMapping.value()[0] : "";
//    }
//
//    private String getRequestUrlFromPostMapping(PostMapping postMapping) {
//        return (postMapping.value().length > 0) ? postMapping.value()[0] : "";
//    }
//
//    private String getRequestUrlFromPutMapping(PutMapping putMapping) {
//        return (putMapping.value().length > 0) ? putMapping.value()[0] : "";
//    }
//
//    private String getRequestUrlFromDeleteMapping(DeleteMapping deleteMapping) {
//        return (deleteMapping.value().length > 0) ? deleteMapping.value()[0] : "";
//    }
//
//    private String getHttpMethod(JoinPoint joinPoint) {
//        RequestMapping requestMapping = getAnnotation(joinPoint, RequestMapping.class);
//        GetMapping getMapping = getAnnotation(joinPoint, GetMapping.class);
//        PostMapping postMapping = getAnnotation(joinPoint, PostMapping.class);
//        PutMapping putMapping = getAnnotation(joinPoint, PutMapping.class);
//        DeleteMapping deleteMapping = getAnnotation(joinPoint, DeleteMapping.class);
//
//        if (requestMapping != null) {
//            return "REQUEST";
//        } else if (getMapping != null) {
//            return "GET";
//        } else if (postMapping != null) {
//            return "POST";
//        } else if (putMapping != null) {
//            return "PUT";
//        } else if (deleteMapping != null) {
//            return "DELETE";
//        }
//
//        return "";
//    }
//
//    private String getMethodParameters(JoinPoint joinPoint) {
//        Object[] args = joinPoint.getArgs();
//        StringBuilder parameters = new StringBuilder();
//
//        for (Object arg : args) {
//            parameters.append(arg).append(", ");
//        }
//
//        return parameters.toString();
//    }
//
//    private <A extends Annotation> A getAnnotation(JoinPoint joinPoint, Class<A> annotationType) {
//        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
//        Method method = methodSignature.getMethod();
//        A annotation = method.getAnnotation(annotationType);
//
//        if (annotation == null) {
//            Class<?> targetClass = joinPoint.getTarget().getClass();
//            Method targetMethod = null;
//            try {
//                targetMethod = targetClass.getMethod(method.getName(), method.getParameterTypes());
//                annotation = targetMethod.getAnnotation(annotationType);
//            } catch (NoSuchMethodException e) {
//                // Handle exception if needed
//            }
//        }
//
//        return annotation;
//    }
//
//}
