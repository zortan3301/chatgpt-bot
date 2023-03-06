package io.github.aminovmaksim.chatgptbot.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
data class BotProperties(

    @param:Value("\${telegram.bot.token}")
    val token: String,

    @param:Value("\${telegram.bot.webhook.enabled}")
    val webhookEnabled: Boolean,

    @param:Value("\${telegram.bot.webhook.host}")
    val webhookHost: String?,

    @param:Value("\${telegram.bot.webhook.maxConnections}")
    val webhookMaxConnections: Int?
)