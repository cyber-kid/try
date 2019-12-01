package com.oauth2.client.controller;

import com.oauth2.client.service.Oauth2Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class ClientController {
    @Autowired
    private Oauth2Service service;

    @GetMapping("/")
    public String getHome(Model model, HttpServletRequest request) {
        String template = "http://127.0.0.1:8082/auth/oauth/authorize?response_type=code&client_id=testClientId&redirect_uri=http://127.0.0.1:8080/client/callback&scope=write";

        model.addAttribute("url", template);

        boolean authFlag = isAuthenticated(request);

        if (authFlag) {
            model.addAttribute("message", "secured!");
        } else {
            model.addAttribute("message", "not secured!");
        }

        return "index";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response, HttpServletRequest request) {

//        Stream.of(request.getCookies()).forEach(c -> log.info(c.getName()));


//        while (session.getAttributeNames().hasMoreElements()) {
//            String element = session.getAttributeNames().nextElement();
//
//            log.info(element);
//        }

//        request.getSession().invalidate();

        service.logoutAuth();

        response.addCookie(getNullCookie());

        return "redirect:/";
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies() == null ? new Cookie[0] : request.getCookies();

        Optional<Cookie> accessTokenCookie = Stream.of(cookies)
                .filter(cookie -> cookie.getName().equalsIgnoreCase("access-token"))
                .findAny();

        return accessTokenCookie.isPresent();
    }

    private Cookie getNullCookie() {
        Cookie cookie = new Cookie("access-token", null);
        cookie.setMaxAge(0);
        return cookie;
    }
}
