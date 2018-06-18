package pers.hdh.b_mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pers.hdh.b_mvc.config.ApplicationException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ErrorController class<br/>
 * 通用错误处理
 * @author hdonghong
 * @date 2018/06/18
 */
@Controller
@Slf4j
public class ErrorController extends AbstractErrorController {

    @Autowired
    private ObjectMapper objectMapper;

    public ErrorController() {
        super(new DefaultErrorAttributes());
    }

    @RequestMapping("/error")
    public ModelAndView getErrorPath(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = Collections.unmodifiableMap(super.getErrorAttributes(request, false));

        // 获取异常
        Throwable cause = getCause(request);
        int status = (Integer) model.get("status");

        // 错误信息
        String message = (String) model.get("message");

        // 友好提示
        String errorMessage = getErrorMessage(cause);

        // 后台打印日志
        log.info(status + ", " + message, cause);

        response.setStatus(status);

        // 如果不是JSON请求
        if (! isJsonRequest(request)) {
            ModelAndView view = new ModelAndView("");
            view.addAllObjects(model);
            view.addObject("errorMessage", errorMessage);
            view.addObject("status", status);
            view.addObject("cause", cause);
            return view;
        }

        Map<String, Object> error = new HashMap<>(16);
        error.put("success", false);
        error.put("errorMessage", errorMessage);
        error.put("message", message);
        writeJson(response, error);

        return null;
    }

    /**
     * 获取应用系统异常
     * @param request 请求
     * @return 错误
     */
    private Throwable getCause(HttpServletRequest request) {
        Throwable error = (Throwable) request.getAttribute("javax.servlet.error.exception");
        if (error != null) {
            // MVC有可能会封装异常成SevletException，需要调用getCause获取真正的异常
            while (error instanceof ServletException && error.getCause() != null) {
                error = ((ServletException) error).getCause();
            }
        }
        return error;
    }

    /**
     * 返回友好的错误信息
     * @param ex 异常
     * @return 错误信息
     */
    private String getErrorMessage(Throwable ex) {
        if (ex instanceof ApplicationException) {
            // 如果是自己设定的应用的异常
            return ((ApplicationException) ex).getMessage();
        }
        return "服务器错误，请联系管理员";
    }

    /**
     * 判断是否JSON请求
     * @param request 请求
     * @return 是否
     */
    private boolean isJsonRequest(HttpServletRequest request) {
        String requestUri = (String) request.getAttribute("javax.servlet.error.request_uri");
        boolean result = (requestUri != null && requestUri.endsWith(".json")) ||
                request.getHeader("Accept").contains("application/json");
        return result;
    }

    /**
     * 转成JSON
     * @param response 响应
     * @param error 错误对象
     */
    private void writeJson(HttpServletResponse response,Map error){
        response.setContentType("application/json;charset=utf-8");
        try {
            response.getWriter().write(objectMapper.writeValueAsString(error));
        } catch (IOException e) {
            // ignore
        }
    }

    @Override
    public String getErrorPath() {
        // nothing
        return null;
    }
}
