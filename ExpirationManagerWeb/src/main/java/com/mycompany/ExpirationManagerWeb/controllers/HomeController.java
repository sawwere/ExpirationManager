package com.mycompany.ExpirationManagerWeb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Контроллер, отвечающий за обработку всех входящих запросов для домашней страницы
 */
@Controller
public class HomeController {

    /**
     * Возвращает домашнюю страницу
     * @return домашняя страница
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }
}
