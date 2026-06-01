package party.hard.partyhelper;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/party")
@Validated
public class PartyController {

    private static final Logger log = LoggerFactory.getLogger(PartyController.class);

    private static final Pattern INJECTION_PATTERN = Pattern.compile(
            "(?i)(ignoreeri|unusta|ignore|bypass|system prompt|override|käsk|kood|drop table|instruction)"
    );

    private final ChatClient chatClient;

    @Value("classpath:prompts/system-prompt.md")
    private Resource systemPrompt;
    @Value("classpath:prompts/user-prompt.md")
    private Resource userPrompt;
    @Value("classpath:prompts/judge-system-prompt.md")
    private Resource judgeSystemPrompt;
    @Value("classpath:prompts/judge-user-prompt.md")
    private Resource judgeUserPrompt;

    public PartyController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/decide")
    public PartyDecision decideParty(@Valid @RequestBody UserInput input) {

        String combinedInput = input.mood() + " " + input.preferences();

        // 1. STAATILINE KONTROLL
        if (INJECTION_PATTERN.matcher(combinedInput).find()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ebasobiv sisend!");
        }

        // 2. LLM KOHTUNIK
        SecurityResult judgeDecision = chatClient.prompt()
                .system(sys -> sys.text(judgeSystemPrompt))
                .user(u -> u.text(judgeUserPrompt)
                        .param("userInput", combinedInput))
                .options(ChatOptions.builder()
                        .temperature(0.0))
                .call()
                .entity(SecurityResult.class);

        log.info("LLM Kohtuniku vastus: {}", judgeDecision);

        if (judgeDecision != null && judgeDecision.isMalicious()) {
            log.warn("LLM Judge blokeeris sisendi: {}", combinedInput);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Turvarisk tuvastatud LLM Kohtuniku poolt.");
        }

        // 3. PEONÕUSTAJA
        PartyDecision decision = chatClient.prompt()
                .system(sys -> sys.text(systemPrompt))
                .user(u -> u.text(userPrompt)
                        .param("energyLevel", input.energyLevel())
                        .param("mood", input.mood())
                        .param("preferences", input.preferences()))
                .options(ChatOptions.builder()
                        .temperature(0.7))
                .call()
                .entity(PartyDecision.class);

        log.info("--- AI MÕTTEKÄIK (CoT) ---");
        log.info("Kasutaja andmed: Energia {}, Meeleolu '{}', Huvid '{}'",
                input.energyLevel(), input.mood(), input.preferences());

        if (decision != null) {
            log.info("AI Loogika: {}", decision.chainOfThought());
        }
        log.info("---------------------------");

        return decision;
    }
}