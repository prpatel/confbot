package dev.prpatel.confbot.evaluation;

import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RagTest {
    private final ChatClient chatClient;

    @Autowired
    public RagTest(@Qualifier("ollamaChatClient") ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Test
    void beginnerAiSessions() {
        String response = chatClient.prompt("I want to learn AI and I am a beginner, what are the best sessions to attend?")
                .call()
                .content();

        System.out.println(response);
        assertThat(response).containsIgnoringCase("AI");
    }

    @Test
    void javaPerformanceSessions() {
        String response = chatClient.prompt("I am interested in learning about Java Performance, what sessions should I attend?")
                .call()
                .content();

        System.out.println(response);
        assertThat(response).containsIgnoringCase("Performance");
    }

    @Test
    void javaChampions() {
        String response = chatClient.prompt("Who are the Java Champions speaking at the conference?")
                .call()
                .content();

        System.out.println(response);
        assertThat(response).containsIgnoringCase("Java Champion");
    }
}
