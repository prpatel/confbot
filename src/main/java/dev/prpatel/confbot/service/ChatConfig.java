package dev.prpatel.confbot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;

@Configuration
public class ChatConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .build();
    }

    @Bean(name = "openAiChatClient")
    public ChatClient openAiChatClient(@Qualifier("openAiChatModel") ChatModel openAiChatModel,
                                       VectorStore vectorStore,
                                       ConferenceTools conferenceTools,
                                       ChatMemory chatMemory) {
        QuestionAnswerAdvisor questionAnswerAdvisor =  QuestionAnswerAdvisor.builder(vectorStore).build();
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        return ChatClient.builder(openAiChatModel)
                .defaultSystem("/nothink \n" +
                        " You are a helpful assistant who knows the information for a conference, including detailed knowledge for all the speakers, their sessions, the schedule for the conference.")
                .defaultTools(conferenceTools)
                .defaultAdvisors(
//                        questionAnswerAdvisor,
//                        messageChatMemoryAdvisor
                )
                .build();
    }

    @Bean(name = "ollamaChatClient")
    public ChatClient ollamaChatClient(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel,
                                       VectorStore vectorStore,
                                       ConferenceTools conferenceTools,
                                       ChatMemory chatMemory) {
        QuestionAnswerAdvisor questionAnswerAdvisor =  QuestionAnswerAdvisor.builder(vectorStore).build();
        MessageChatMemoryAdvisor messageChatMemoryAdvisor = MessageChatMemoryAdvisor.builder(chatMemory).build();
        SimpleLoggerAdvisor  simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("/nothink \n" +
                        "You are a helpful assistant who knows the information for a conference, including detailed knowledge for all the speakers, their sessions, the schedule for the conference.")
                .defaultTools(conferenceTools)
                .defaultAdvisors(
                        questionAnswerAdvisor,
//                        messageChatMemoryAdvisor,
                        simpleLoggerAdvisor
                )
                .build();
    }

    @Bean(name = "ollamaChatClientNoRAG")
    public ChatClient ollamaChatClientNoRAG(@Qualifier("ollamaChatModel") ChatModel ollamaChatModel,
                                       VectorStore vectorStore,
                                       ConferenceTools conferenceTools,
                                       ChatMemory chatMemory) {
        SimpleLoggerAdvisor  simpleLoggerAdvisor = new SimpleLoggerAdvisor();
        return ChatClient.builder(ollamaChatModel)
                .defaultSystem("You are a helpful assistant who knows the information for a conference, including detailed knowledge for all the speakers, their sessions, the schedule for the conference.")
                .defaultTools(conferenceTools)
                .defaultAdvisors(
                        simpleLoggerAdvisor
                )
                .build();
    }
}
