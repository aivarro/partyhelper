package party.hard.partyhelper.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import party.hard.partyhelper.model.UserInput;
import party.hard.partyhelper.service.PartyService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PartyController.class)
class PartyControllerValidationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PartyService partyService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    // --- ENERGIA TESTID (@Min(1) ja @Max(10)) ---

    @Test
    void givenEnergyTooLow_whenDecideParty_thenReturnsBadRequest() throws Exception {
        // Energia on 0 (lubatud on 1-10)
        UserInput invalidInput = new UserInput(0, "rõõmus", "muusika");

        mockMvc.perform(post("/api/party/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenEnergyTooHigh_whenDecideParty_thenReturnsBadRequest() throws Exception {
        // Energia on 11 (lubatud on 1-10)
        UserInput invalidInput = new UserInput(11, "rõõmus", "muusika");

        mockMvc.perform(post("/api/party/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    // --- MEELEOLU TESTID (@NotBlank ja @Size(max = 50)) ---

    @Test
    void givenMoodIsBlank_whenDecideParty_thenReturnsBadRequest() throws Exception {
        // Meeleolu on tühi või koosneb ainult tühikutest
        UserInput invalidInput = new UserInput(5, "   ", "muusika");

        mockMvc.perform(post("/api/party/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenMoodIsTooLong_whenDecideParty_thenReturnsBadRequest() throws Exception {
        // Loome stringi, mis on täpselt 51 tähemärki pikk
        String tooLongMood = "a".repeat(51);
        UserInput invalidInput = new UserInput(5, tooLongMood, "muusika");

        mockMvc.perform(post("/api/party/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    // --- EELISTUSTE TESTID (@NotBlank ja @Size(max = 50)) ---

    @Test
    void givenPreferencesAreBlank_whenDecideParty_thenReturnsBadRequest() throws Exception {
        // Eelistused on tühjad
        UserInput invalidInput = new UserInput(5, "rõõmus", "");

        mockMvc.perform(post("/api/party/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenPreferencesAreTooLong_whenDecideParty_thenReturnsBadRequest() throws Exception {
        // Loome stringi, mis on täpselt 51 tähemärki pikk
        String tooLongPreferences = "b".repeat(51);
        UserInput invalidInput = new UserInput(5, "rõõmus", tooLongPreferences);

        mockMvc.perform(post("/api/party/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidInput)))
                .andExpect(status().isBadRequest());
    }

    // --- ÕNNELIK RADA (Kõik andmed on korrektsed) ---

    @Test
    void givenValidInput_whenDecideParty_thenReturnsOk() throws Exception {
        // Kõik andmed mahuvad piiridesse
        UserInput validInput = new UserInput(8, "rõõmus", "muusika");

        mockMvc.perform(post("/api/party/decide")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validInput)))
                .andExpect(status().isOk());
    }
}