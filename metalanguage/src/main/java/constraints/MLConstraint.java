package constraints;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class MLConstraint {
    private UUID attributeId;
    private MLComparsions comparsion;
    private String value;
}
