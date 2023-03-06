package io.github.aminovmaksim.chatgptbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChatGptBotApplication

fun main(args: Array<String>) {
	runApplication<ChatGptBotApplication>(*args)
}
