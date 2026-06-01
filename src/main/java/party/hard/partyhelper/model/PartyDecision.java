package party.hard.partyhelper.model;

public record PartyDecision(boolean shouldGoOut,
                            String chainOfThought,
                            String recommendedParty,
                            String markdownMessage) {
}
