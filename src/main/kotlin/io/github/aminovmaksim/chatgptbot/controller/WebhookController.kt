package io.github.aminovmaksim.chatgptbot.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.pengrad.telegrambot.TelegramBot
import com.pengrad.telegrambot.model.Update
import com.pengrad.telegrambot.request.GetWebhookInfo
import com.pengrad.telegrambot.request.SetWebhook
import io.github.aminovmaksim.chatgptbot.config.BotProperties
import io.github.aminovmaksim.chatgptbot.service.BotService
import okhttp3.HttpUrl
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import javax.annotation.PostConstruct
import kotlin.system.exitProcess

@RestController
@ConditionalOnProperty(name = ["telegram.bot.webhook.enabled"], havingValue = "true")
class WebhookController(
    val bot: TelegramBot,
    val botService: BotService,
    val botProperties: BotProperties,
    val context: ApplicationContext,
    val objectMapper: ObjectMapper
) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostConstruct
    fun registerWebhook() {
        val webhookUrl = HttpUrl.Builder()
            .scheme("https")
            .host(botProperties.webhookHost!!)
            .port(443)
            .addPathSegment(botProperties.token)
            .build().toUrl().toString()
        logger.info("Setting up webhook for https://${botProperties.webhookHost}:443")
        val response = bot.execute(
            SetWebhook()
                .url(webhookUrl)
                .maxConnections(botProperties.webhookMaxConnections!!)
        )
        if (!response.isOk) {
            logger.error("Cannot register webhook: ${response.errorCode()} - ${response.description()}")
            SpringApplication.exit(context)
            exitProcess(1)
        }
        logger.info("Webhook successfully registered")
    }

    @PostMapping(path = ["/#{@botProperties.getToken()}"], consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun handleUpdate(@RequestBody update: Update): ResponseEntity<String?>? {
        return try {
            botService.handleUpdate(update)
            ResponseEntity.ok<String?>("ACCEPTED")
        } catch (e: Exception) {
            logger.error("Update Listener exception", e)
            ResponseEntity.internalServerError().build<String?>()
        }
    }

    @GetMapping(path = ["/#{@botProperties.getToken()}/webhookInfo"], produces = [MediaType.APPLICATION_JSON_VALUE])
    fun getWebhookInfo(): ResponseEntity<String?>? {
        val response = bot.execute(GetWebhookInfo())
        return if (response.isOk) {
            ResponseEntity.ok<String?>(objectMapper.writeValueAsString(response.webhookInfo()))
        } else ResponseEntity.internalServerError().build<String?>()
    }
}