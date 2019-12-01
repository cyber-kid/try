package com.oauth2.client.controller;

import com.oauth2.client.service.Oauth2Service;
import com.oauth2.client.service.model.TokenModel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class Oauth2Controller {
    private Oauth2Service oauth2Service;

    @Autowired
    public Oauth2Controller(Oauth2Service oauth2Service) {
        this.oauth2Service = oauth2Service;
    }

    @GetMapping("/callback")
    public String callback(Model model, @RequestParam("code") String code, HttpServletResponse response, HttpServletRequest request) {

        if (request.getCookies() != null) {
            Stream.of(request.getCookies()).forEach(c -> log.info(c.getName()));
        }

        TokenModel tokenModel = oauth2Service.getTokenCodeFlow(code);

        Cookie cookie = getCookie(tokenModel);

        response.addCookie(cookie);

        return "redirect:/";
    }

    private Cookie getCookie(TokenModel tokenModel) {
        Cookie cookie = new Cookie("access-token", tokenModel.getAccessToken());
        int expiry = tokenModel.getExpiresIn();
        cookie.setMaxAge(expiry);
//        cookie.setSecure(true);
        return cookie;
    }
}
