package com.hope.handler.log;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.servlet.ServletUtil;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.PropertyPreFilters;
import com.hope.annotation.Log;
import com.hope.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * 注解@Log操作日志记录处理程序
 * 注解使用在具体接口方法上面
 * 如： @Log(title = "文件删除:又拍云",businessType = BusinessType.DELETE)
 * @author AoDeng
 * @date 14:48 21-8-13
 */
@Aspect
@Component
@Slf4j
public class LogAspectHandler {

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    // 配置织入点
    @Pointcut("@annotation(com.hope.annotation.Log)")
    public void logPointCut() {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult) {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult) {
        try {
            // 获取当前请求注解信息
            Log controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            //业务操作功能crud
            String businessType = controllerLog.businessType().name();
            //业务操作模块
            String title = controllerLog.title();

            //获取当前请求信息
            ServletRequestAttributes servlet = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = servlet.getRequest();
            String addr = request.getRemoteAddr();//获取ip地址
            String host=request.getRemoteHost();
            String port=request.getRemotePort()+"";
            String addrLocal=request.getLocalAddr();
            String hostLocal=request.getLocalName();
            String portLocal=request.getLocalPort()+"";
            //String userId = request.getHeader("token");//获取请求头参数
            String requestURI = request.getRequestURI();//获取请求路由地址

            //获取响应信息
            String jsonResultStr = "";
            if (ObjectUtil.isNotNull(jsonResult)) {
                jsonResultStr = StringUtils.substring(JSONObject.toJSONString(jsonResult), 0, 2000);
            }

            //请求参数
            Map<String, String[]> map = ServletUtil.getParams(request);
            String params="";
            if (StringUtils.isNotEmpty(map)) {
                params = JSONObject.toJSONString(map, excludePropertyPreFilter());
                params = StringUtils.substring(params, 0, 2000);
            } else {
                Object args = joinPoint.getArgs();
                if (StringUtils.isNotNull(args)) {
                    params = argsArrayToString(joinPoint.getArgs());
                    params = StringUtils.substring(params, 0, 2000);
                }
            }

            //TODO 日志保存数据库


        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private Log getAnnotationLog(JoinPoint joinPoint) throws Exception {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null) {
            return method.getAnnotation(Log.class);
        }
        return null;
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreFilters.MySimplePropertyPreFilter excludePropertyPreFilter() {
        return new PropertyPreFilters().addFilter().addExcludes(EXCLUDE_PROPERTIES);
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (int i = 0; i < paramsArray.length; i++) {
                if (StringUtils.isNotNull(paramsArray[i]) && !isFilterObject(paramsArray[i])) {
                    Object jsonObj = JSONObject.toJSONString(paramsArray[i], excludePropertyPreFilter());
                    params += jsonObj.toString() + " ";
                }
            }
        }
        return params.trim();
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
                return iter.next() instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Iterator iter = map.entrySet().iterator(); iter.hasNext(); ) {
                Map.Entry entry = (Map.Entry) iter.next();
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
