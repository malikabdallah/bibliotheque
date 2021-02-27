package com.bookstore.projet.controlleur;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ControlleurErreur implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        //do something like logging
        return "echec";
    }

    @Override
    public String getErrorPath() {
        return "/echec";
    }
}