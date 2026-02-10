package dev.prpatel.confbot.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/chat")
    public String chat(@RequestParam String message, Model model) {
        String response = chatClient.prompt()
                .user(message)
                .call()
                .content();
        model.addAttribute("question", message);
        model.addAttribute("response", response);
        return "fragments/response :: responseFragment";
    }
}
