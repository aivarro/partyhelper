package party.hard.partyhelper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public record PartyDecision(boolean shouldGoOut,
                            @JsonIgnore
                            String chainOfThought,
                            String recommendedParty,
                            String markdownMessage) {
}
