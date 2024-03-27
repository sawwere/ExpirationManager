package com.mycompany.ExpirationManagerWeb.controllers;

import com.mycompany.ExpirationManagerWeb.exceptions.ErrorInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;

/**
 * Служит для перехвата возникающих ошибок
 */
@RequiredArgsConstructor
@Controller
public class CustomErrorController implements ErrorController {
    private static final String ERROR_PATH = "/error";
    private final ErrorAttributes errorAttributes;

    /**
     * Перехватывает возникающие ошибки
     * @param webRequest текущий запрос
     * @return ResponseEntity с телом, содержашим информацию об ошибке в виде ErrorInfo
     */
    @RequestMapping(CustomErrorController.ERROR_PATH)
    public ResponseEntity<ErrorInfo> error(WebRequest webRequest) {
        var attributes = errorAttributes.getErrorAttributes(webRequest,
                ErrorAttributeOptions.of(ErrorAttributeOptions.Include.EXCEPTION, ErrorAttributeOptions.Include.MESSAGE)
        );
        return ResponseEntity
                .status((Integer)attributes.get("status"))
                .body(ErrorInfo.builder()
                        .error((String) attributes.get("error"))
                        .description((String) attributes.get("message"))
                        .build()
                );
    }
}
