package party.hard.partyhelper.model;

public record SecurityResult(
        boolean isMalicious // true = ohtlik (JAH), false = turvaline (EI)
) {}
