package dev.prpatel.confbot.controller;

import dev.prpatel.confbot.service.ConferenceDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ConferenceDataService conferenceDataService;

    public AdminController(ConferenceDataService conferenceDataService) {
        this.conferenceDataService = conferenceDataService;
    }

    @GetMapping
    public String adminPage() {
        return "admin";
    }

    @PostMapping("/load")
    @ResponseBody
    public String loadData() {
        try {
            conferenceDataService.loadData();
            return "Data loaded successfully";
        } catch (IOException e) {
            return "Error loading data: " + e.getMessage();
        }
    }
}
