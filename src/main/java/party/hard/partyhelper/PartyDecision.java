package party.hard.partyhelper;

public record PartyDecision(boolean shouldGoOut,
                            String chainOfThought,
                            String recommendedParty,
                            String markdownMessage) {
}
