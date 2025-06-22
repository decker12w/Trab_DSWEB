package org.example.trab_dsweb.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
public class PagesController {

    @GetMapping()
    public String index() {
        return "index";
    }
}
