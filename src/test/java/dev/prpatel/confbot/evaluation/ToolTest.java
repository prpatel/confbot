package dev.prpatel.confbot.evaluation;

import dev.prpatel.confbot.service.ConferenceTools;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ToolTest {
    private final ChatClient chatClient;

    // Two chat clients available in this repo:
    // ollamaChatClient
    // ollamaChatClientNoRAG
    // openAiChatClient
    //
    // they are used throughout the application, and configured in application.properties
    @Autowired
    public ToolTest(@Qualifier("ollamaChatClient")ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @Test
    void whenIsSpeakerPresenting() {
        String response = chatClient.prompt("When is Josh Long presenting?")
                .call()
                .content();

        assertThat(response).contains("Josh Long");
        assertThat(response).contains("Hands-On: Building Agents with Spring AI, MCP, Java, and Amazon Bedrock");
    }

    @Test
    void howManyPresentations() {
        String response = chatClient.prompt("How many presentations does Venkat Subramaniam have?")
                .call()
                .content();

        assertThat(response).contains("Venkat Subramaniam");
        assertThat(response).contains("2");
    }

    @Test
    void listSessionsBySpeaker() {
        String response = chatClient.prompt("List all sessions by Dan Vega")
                .call()
                .content();

        assertThat(response).contains("Dan Vega");
        assertThat(response).contains("Fundamentals of Software Engineering In the age of AI Workshop");
    }

    @Test
    void sessionsInRoom() {
        String response = chatClient.prompt("What sessions are in room 'Gen AI'?")
                .call()
                .content();

        assertThat(response).contains("Gen AI");
        assertThat(response).contains("Modernize your apps in days with AI Agents");
    }

    @Test
    void sessionsAtTime() {
        String response = chatClient.prompt("What sessions are happening between 2026-03-05T10:00:00 and 2026-03-05T11:00:00?")
                .call()
                .content();

        assertThat(response).contains("Life after dad joke apps");
    }

    @Test
    void sessionInfo() {
        String response = chatClient.prompt("Tell me about the session 'Bootiful Spring Security'")
                .call()
                .content();
        System.out.println(response);
        assertThat(response).contains("Bootiful Spring Security");
        assertThat(response).contains("Rob Winch");
        assertThat(response).contains("Josh Long");
    }

    @Test
    void speakerBio() {
        String response = chatClient.prompt("What is the bio for Mark Pollack?")
                .call()
                .content();

        assertThat(response).contains("Mark Pollack");
        assertThat(response).contains("Spring AI");
    }

    @Test
    void speakersByCompany() {
        String response = chatClient.prompt("Who works at Broadcom?")
                .call()
                .content();

        assertThat(response).contains("Josh Long");
        assertThat(response).contains("Dan Vega");
    }

    @Test
    void countTotalSessions() {
        String response = chatClient.prompt("How many total sessions are there?")
                .call()
                .content();

        assertThat(response).contains("117");
    }

    @Test
    void countTotalSpeakers() {
        String response = chatClient.prompt("How many total speakers are there?")
                .call()
                .content();

        assertThat(response).contains("121");
    }

    @Test
    void findSessionsByKeyword() {
        String response = chatClient.prompt("Find sessions about 'security'")
                .call()
                .content();

        System.out.println(response);
        assertThat(response).contains("Bootiful Spring Security");
    }

    @Test
    void speakerTwitter() {
        String response = chatClient.prompt("What is Josh Long's Twitter handle?")
                .call()
                .content();

        assertThat(response).contains("starbuxman");
    }

    @Test
    void speakerLinkedIn() {
        String response = chatClient.prompt("What is Dan Vega's LinkedIn?")
                .call()
                .content();

        assertThat(response).contains("linkedin.com/in/danvega");
    }

    @Test
    void speakerCompany() {
        String response = chatClient.prompt("What company does Mark Pollack work for?")
                .call()
                .content();

        assertThat(response).contains("Broadcom");
    }

    @Test
    void speakerTagline() {
        String response = chatClient.prompt("What is Venkat Subramaniam's tagline?")
                .call()
                .content();

        assertThat(response).contains("Agile Developer, Inc.");
    }
}
