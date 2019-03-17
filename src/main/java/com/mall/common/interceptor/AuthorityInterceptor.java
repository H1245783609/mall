package com.mall.common.interceptor;

import com.google.common.collect.Maps;
import com.mall.common.Const;
import com.mall.common.ServerResponse;
import com.mall.pojo.User;
import com.mall.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;

/**
 * SpringMVC权限拦截类
 * @author panjing
 */
@Slf4j
public class AuthorityInterceptor implements HandlerInterceptor {
    /**
     * controller执行之前执行
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        log.info("preHandle");
        //请求中Controller中的方法名
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        //解析
        //获取方法名
        String methodName = handlerMethod.getMethod().getName();
        //获取简单类名
        String className = handlerMethod.getBean().getClass().getSimpleName();

        //解析参数，具体的参数key以及value是什么，打印日志
        StringBuilder requestParamBuffer = new StringBuilder();
        //获取参数集合
        Map paramMap = httpServletRequest.getParameterMap();
        Iterator it = paramMap.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            String mapKey = (String) entry.getKey();

            String mapValue = StringUtils.EMPTY;

            //request这个参数的map，里面的value返回的是一个String[]
            Object obj = entry.getValue();
            if(obj instanceof String[]){
                String[] strs = (String[]) obj;
                mapValue = Arrays.toString(strs);
            }
            requestParamBuffer.append(mapKey).append("=").append(mapValue);
        }

        //放行UserManageController中的login方法
        if(StringUtils.equals(className, "UserManageController") && (StringUtils.equals(methodName, "login"))){
            log.info("权限拦截器拦截到请求，className:{},methodName:{},param:{}", className, methodName, requestParamBuffer.toString());
            //如果是拦截到登录请求，不打印参数，因为参数里面有密码，全部会打印到日志中，防止泄露
            return true;
        }

        //获取当前登录对象
        User user = (User)httpServletRequest.getSession().getAttribute(Const.CURRENT_USER);
        if(user == null || (user.getRole().intValue() != Const.Role.ROLE_ADMIN)){
            //返回false，即不会调用controller的方法
            httpServletResponse.reset(); //这里要添加reset，否则会报异常getwritter() has already...
            httpServletResponse.setCharacterEncoding("UTF-8"); //这里要设置编码，否则会乱码
            httpServletResponse.setContentType("application/json;charset=utf-8"); //这里要设置返回值的类型，因为全部是json接口

            PrintWriter out = httpServletResponse.getWriter();

            // 上传由于富文本控件的要求，要特殊处理返回值，这里面区分是否登录以及是否有权限
            if(user == null){
                if(StringUtils.equals(className, "ProductManageController ") && (StringUtils.equals(methodName, "richtextImgUpload"))){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "请登录管理员");
                    out.println(JsonUtil.obj2String(resultMap));
                }else{
                    out.println(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户未登录")));
                }
            }else{
                if(StringUtils.equals(className, "ProductManageController ") && (StringUtils.equals(methodName, "richtextImgUpload"))){
                    Map resultMap = Maps.newHashMap();
                    resultMap.put("success", false);
                    resultMap.put("msg", "无权限操作");
                    out.println(JsonUtil.obj2String(resultMap));
                }else{
                    out.println(JsonUtil.obj2String(ServerResponse.createByErrorMessage("拦截器拦截，用户无权限操作")));
                }
            }
            out.flush();
            out.close();
            return false;
        }
        return true;
    }

    /**
     * controller执行之后执行
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle");
    }

    /**
     * controller执行之后执行
     * @param httpServletRequest
     * @param httpServletResponse
     * @param handler
     * @param e
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler, Exception e) throws Exception {
        log.info("afterCompletion");
    }
}
