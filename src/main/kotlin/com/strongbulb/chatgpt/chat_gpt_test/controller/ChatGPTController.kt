package com.strongbulb.chatgpt.chat_gpt_test.controller

import lombok.Data
import org.springframework.boot.configurationprocessor.json.JSONObject
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient


@RestController
class ChatGPTController {

    //GET
    @GetMapping(path = ["/chat-gpt/{prompt}"])
    fun chatGpt(@PathVariable("prompt") prompt: String): String {
        return callChatGPTAPI(prompt)
    }

    fun callChatGPTAPI(prompt: String): String {
        val webClient: WebClient = WebClient.builder()
            .baseUrl("https://api.openai.com/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer your key")
            .build()

        val body = JSONObject()
        body.put("model", "text-davinci-003")
        body.put("prompt", "Hello, how can I help you today?")
        body.put("temperature", 0.7)
        body.put("max_tokens", 50)
        body.put("n", 1)
        body.put("stop", "A:")

        val response = webClient.post()
            .uri("v1/completions")
            .body(BodyInserters.fromValue(body.toString()))
            .retrieve()
            .bodyToMono(ChatGPTApiResponse::class.java).block()

        return response?.choices?.get(0)?.text ?: ""
    }

    @Data
     class ChatGPTApiResponse{
        val choices: List<Completion>? = null
        val created: Long = 0
     }

    @Data
    class Completion {
        val text: String? = null
        val index: Int? = null
    }

}