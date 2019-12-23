package relations;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MLMultiplicity {
    private int fromMult;
    private int toMult;
}
