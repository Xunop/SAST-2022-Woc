package com.example.woc.interceptor;


import com.example.woc.annotation.CheckRole;
import com.example.woc.annotation.PassToken;
import com.example.woc.entity.User;
import com.example.woc.enums.ErrorEnum;
import com.example.woc.exception.LocalRuntimeException;
import com.example.woc.mapper.UserMapper;
import com.example.woc.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @Author xun
 * @create 2023/1/3 14:15
 */
@Component
public class UserInterceptor implements HandlerInterceptor {
    public static ThreadLocal<User> userHolder = new ThreadLocal<>();

    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    
    public UserInterceptor(JWTUtil jwtUtil, UserMapper userMapper) {
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (!(handler instanceof HandlerMethod))
            return true;
        Method method = ((HandlerMethod) handler).getMethod();
        boolean isAnonymous = allowNoToken(method);
        String token = request.getHeader("Token");
        if (!StringUtils.hasLength(token)) {
            // 判断接口是否允许没有Token
            if (isAnonymous) return true;
            throw new LocalRuntimeException(ErrorEnum.NO_TOKEN);
        }

        String password = jwtUtil.getPassword(token);
        Integer id = jwtUtil.getId(token);
        if (password == null)
            throw new LocalRuntimeException(ErrorEnum.TOKEN_ERROR);
        // 登录过期
        if (jwtUtil.isExpired(id))
            throw new LocalRuntimeException(ErrorEnum.EXPIRED_LOGIN);
        User userFromDB = userMapper.findById(id);
        // 判断是否是已知用户
        if (userFromDB == null)
            throw new LocalRuntimeException(ErrorEnum.EXPIRED_LOGIN);
        // 检查权限
        if (!isAnonymous && !checkPermission(method, userFromDB))
            throw new LocalRuntimeException(ErrorEnum.NO_ROLE);
        userHolder.set(userFromDB);
        // 更新redis中的token持续时间
        jwtUtil.reFreshToken(id);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,  HttpServletResponse response,
                                Object handler, Exception ex) {
        userHolder.remove();
    }

    private boolean allowNoToken(Method method) {
        PassToken passToken = getAnnotation(method, PassToken.class);
        return passToken != null && passToken.required();
    }

    private boolean checkPermission(Method method, User user) {
        CheckRole checkRole = getAnnotation(method, CheckRole.class);
        if (checkRole == null) return true;
        return user.getRole() >= checkRole.role().getRole();
    }


    /**
     * 获得方法或类上的注解
     * @param method 方法
     * @param annotationClass 注解
     */
    public static <T extends Annotation> T getAnnotation(Method method,
                                                         Class<T> annotationClass) {
        if (method == null) return null;
        if (method.isAnnotationPresent(annotationClass)) {
            return AnnotationUtils.getAnnotation(method, annotationClass);
        } else {
            return AnnotationUtils.getAnnotation(method.getDeclaringClass(), annotationClass);
        }
    }
}
