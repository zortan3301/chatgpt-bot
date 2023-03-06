package io.github.aminovmaksim.chatgptbot.config

import io.github.aminovmaksim.chatgpt4j.ChatGPTClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ChatGPTConfig {

    @Bean
    fun chatGPTClient(
        @Value("\${openai.token}") token: String
    ): ChatGPTClient {
        return ChatGPTClient.builder()
            .apiKey(token)
            .build()
    }
}