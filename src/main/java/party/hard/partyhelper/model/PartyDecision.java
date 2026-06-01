package party.hard.partyhelper.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

public record PartyDecision(boolean shouldGoOut,
                            @JsonProperty(access = WRITE_ONLY)
                            String chainOfThought,
                            String recommendedParty,
                            String markdownMessage) {
}
