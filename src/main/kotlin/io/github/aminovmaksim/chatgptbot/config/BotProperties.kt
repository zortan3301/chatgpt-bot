package io.github.aminovmaksim.chatgptbot.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class BotProperties(

    @param:Value("\${telegram.bot.token}")
    val token: String
)