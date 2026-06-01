package party.hard.partyhelper.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import party.hard.partyhelper.model.PartyDecision;
import party.hard.partyhelper.model.UserInput;
import party.hard.partyhelper.service.PartyService;

@RestController
@RequestMapping("/api/party")
public class PartyController {

    private final PartyService partyService;

    // Süstime teenuse kontrollerisse
    public PartyController(PartyService partyService) {
        this.partyService = partyService;
    }

    @PostMapping("/decide")
    public PartyDecision decideParty(@Valid @RequestBody UserInput input) {
        // Kontroller teeb ainult ühe asja: küsib teenuselt vastust!
        return partyService.getPartyRecommendation(input);
    }
}