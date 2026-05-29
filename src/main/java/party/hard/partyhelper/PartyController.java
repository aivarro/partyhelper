package party.hard.partyhelper;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.converter.BeanOutputConverter;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/party")
@Validated
public class PartyController {

    private final ChatClient chatClient;

    public PartyController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @PostMapping("/decide")
    public PartyDecision decideParty(@Valid @RequestBody UserInput input) {

        var outputConverter = new BeanOutputConverter<>(PartyDecision.class);

        String systemPrompt = """
            Oled ekspert peonõustaja. Sinu ülesanne on otsustada, kas kasutaja peaks peole minema ja leidma talle sobiva variandi.
            
            MÕTLEMISPROTSESS (Chain of Thought):
            1. Analüüsi energia taset. Kui energia on alla 4 ja meeleolu on väsinud, soovita kindlasti koju jääda.
            2. Kui energia on piisav, otsi veebist kasutaja huvidele (preferences) kõige paremini vastav pidu.
            
            NÄITED (Few-Shot):
            Kasutaja: Energia 2, Meeleolu: kurnatud, Huvid: magamine
            Vastus: "shouldGoOut": false, "chainOfThought": "Energia on liiga madal, vajab puhkust.", "recommendedParty": "Kodu", "markdownMessage": "**Puhka!** Täna pole pidutsemise päev."
            
            Kasutaja: Energia 9, Meeleolu: pöörane, Huvid: tantsimine
            Vastus: "shouldGoOut": true, "chainOfThought": "Kõrge energia ja tantsuhimu sobib reiviga.", "recommendedParty": "Metsareiv 3000", "markdownMessage": "🔥 **Pane vaim valmis!** Sinu jaoks on parim valik Metsareiv 3000."
            
            Vormista väljund vastavalt süsteemi antud JSON struktuurile.
            {format}
            """;

        // 3. User Prompt: Edastame parameetrid
        String userPrompt = """
            Minu andmed:
            Energia: {energyLevel}
            Meeleolu: {mood}
            Huvid: {preferences}
            """;

        // 4. Kutsume AI välja
        return chatClient.prompt()
                .system(sys -> sys.text(systemPrompt)
                        .param("format", outputConverter.getFormat())) // Lisab JSON schema
                .user(u -> u.text(userPrompt)
                        .param("energyLevel", input.energyLevel())
                        .param("mood", input.mood())
                        .param("preferences", input.preferences()))
                .call()
                .entity(outputConverter);
    }
}
