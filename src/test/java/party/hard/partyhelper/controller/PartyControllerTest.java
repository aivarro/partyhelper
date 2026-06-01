package party.hard.partyhelper.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import party.hard.partyhelper.model.PartyDecision;
import party.hard.partyhelper.model.UserInput;
import party.hard.partyhelper.service.PartyService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartyControllerTest {

    @Mock
    private PartyService partyService;

    @InjectMocks
    private PartyController partyController;

    @Test
    void givenInput_whenDecideParty_thenDelegatesToPartyService() {
        // Antud sisend
        UserInput input = new UserInput(8, "rõõmus", "muusika");
        PartyDecision expectedDecision = new PartyDecision(true, "Mõttekäik", "Metsareiv", "Mine reivile!");

        // Mockime ainult teenuse (Service) vastust
        when(partyService.getPartyRecommendation(input)).thenReturn(expectedDecision);

        // Käivitame kontrolleri meetodi
        PartyDecision result = partyController.decideParty(input);

        // Kontrollime, et kontroller andis teenuse vastuse puutumata edasi
        assertNotNull(result);
        assertEquals(expectedDecision, result);
    }
}