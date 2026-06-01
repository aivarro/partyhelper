package party.hard.partyhelper;

public record SecurityResult(
        boolean isMalicious // true = ohtlik (JAH), false = turvaline (EI)
) {}
