package party.hard.partyhelper;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserInput(@Min(1) @Max(10) int energyLevel,
                        @NotBlank @Size(max = 50) String mood,
                        @NotBlank @Size(max = 50) String preferences) {
}
