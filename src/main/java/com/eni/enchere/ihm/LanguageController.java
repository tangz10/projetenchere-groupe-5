package com.eni.enchere.ihm;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@RestController
public class LanguageController {

    @Autowired
    private LocaleResolver localeResolver;

    @PostMapping("/changeLanguage")
    public void changeLanguage(@RequestParam("lang") String lang, HttpServletRequest request, HttpServletResponse response) {
        Locale newLocale = new Locale(lang);
        localeResolver.setLocale(request, response, newLocale);
    }
}
