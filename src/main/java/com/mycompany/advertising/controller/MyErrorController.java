package com.mycompany.advertising.controller;

import com.mycompany.advertising.components.utils.AViewableException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Amir on 5/7/2021.
 */
@Controller
public class MyErrorController implements ErrorController {
    private static final Logger logger = Logger.getLogger(MyErrorController.class);
    @Value("${amir.error.folder}")
    String errorfolder;
    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String handleError(Model model, HttpServletRequest request) {
        //Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
 /*       ServletWebRequest servletWebRequest = new ServletWebRequest(request);
        Map<String, Object> errors = errorAttributes.getErrorAttributes(servletWebRequest, true);
        if (errors != null) {
            Object uri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
            Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
            logger.error("request for: " + uri.toString() + "\tgot Error: " + exception.toString());
            model.addAttribute("error_code", status.toString());
            //Object exceptiontype = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
            Integer statusCode = Integer.valueOf(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return errorfolder + "error-404";
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
                if (exception instanceof AViewableException)
                    model.addAttribute("error_msg", request.getAttribute(RequestDispatcher.ERROR_MESSAGE));
                return errorfolder + "error-500";
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return errorfolder + "error-403";
            }
        }*/
        return errorfolder + "error";
    }

    @Override
    public String getErrorPath() {
        //it set in application.properties server.error.path=/error
        return null;
    }
}
