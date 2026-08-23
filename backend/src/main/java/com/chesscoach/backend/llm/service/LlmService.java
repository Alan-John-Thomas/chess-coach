package com.chesscoach.backend.llm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LlmService {

    private final ChatModel chatModel;

    // since pom.xml has both spring-ai-starter-model-ollama and spring-ai-starter-model-openai
    // therefore Chatmodel has two beans so we need to use Qualifier to decide which
    // can't use @RequiredArgsConstructor here because Lombok doesn't support @Qualifier on generated constructors
    public LlmService(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String generateResponse(String promptText) {
        log.info("Sending prompt to LLM (length: {} chars)", promptText.length());

        try {
            String response = chatModel.call(promptText);
            log.info("Received response from LLM (length: {} chars)", response.length());
            return response;
        } catch (Exception e) {
            log.error("Error communicating with LLM service", e);
            throw new RuntimeException("Failed to generate response from LLM engine", e);
        }
    }
}