package io.github.aminovmaksim.chatgptbot.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class BotService(
    val bot: TelegramBot,
    val objectMapper: ObjectMapper
) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun handleUpdate(update: Update) {
        logger.info(objectMapper.writeValueAsString(update))
    }
}