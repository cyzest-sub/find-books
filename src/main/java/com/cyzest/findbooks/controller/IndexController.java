package com.cyzest.findbooks.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class IndexController {

    @RequestMapping("/")
    public String index(
            HttpSession session, Model model,
            @RequestParam(value = "login_error", required = false) String loginError) {

        Object securityContextObject =
                session.getAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);

        if (securityContextObject != null) {
            return "redirect:/search";
        } else {
            model.addAttribute("loginError", loginError);
            return "index";
        }
    }

}
