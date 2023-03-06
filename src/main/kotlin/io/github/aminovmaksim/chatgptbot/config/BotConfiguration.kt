package io.github.aminovmaksim.chatgptbot.config

import com.pengrad.telegrambot.TelegramBot
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfiguration {

    @Bean
    fun telegramBot(properties: BotProperties): TelegramBot {
        println(properties.token)
        return TelegramBot(properties.token)
    }
}