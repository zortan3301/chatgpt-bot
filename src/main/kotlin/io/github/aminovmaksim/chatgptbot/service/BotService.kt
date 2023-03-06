package io.github.aminovmaksim.chatgptbot.service

import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Message
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.SendMessage
import io.github.aminovmaksim.chatgpt4j.ChatGPTClient
import io.github.aminovmaksim.chatgpt4j.model.ChatMessage
import io.github.aminovmaksim.chatgpt4j.model.ChatRequest
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class BotService(
    val bot: TelegramBot,
    val chatGPTClient: ChatGPTClient,
) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    fun handleUpdate(update: Update) {
        // New message
        if (update.message() != null) {
            handleMessage(update.message())
        }
    }

    fun handleMessage(message: Message) {
        val userId = message.from().id()
        val text = message.text()
        val messageId = message.messageId()

        logger.info("Message $messageId received from user $userId")

        if (text == null) {
            logger.info("Empty message $messageId skipped from user $userId")
            return
        }

        if ("/start" == text) {
            bot.execute(
                SendMessage(userId, "Hi! Just text me something...")
            )
            return
        }

        val responseMessage = sendChatGPTRequest(text, userId)
        bot.execute(
            SendMessage(userId, responseMessage)
                .replyToMessageId(messageId)
        )
    }

    fun sendChatGPTRequest(message: String, userId: Long): String {
        val request = ChatRequest(ChatMessage(message))
        request.user = userId.toString()

        return try {
            val response = chatGPTClient.sendChat(request)
            response.choices[0].message.content
        } catch (e: Exception) {
            logger.error("ChatGPTRequestError for userId: $userId", e)
            "Developer is dumb and something went wrong. If it is not burden you, send him this message: ${Instant.now().epochSecond}"
        }
    }
}