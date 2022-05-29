package com.kep.schoolmeal.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {
    @GetMapping("/health")
    fun health(): String {
        return "ok"
    }
}