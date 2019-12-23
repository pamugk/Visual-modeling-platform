package attributes;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class MLAttribute {
    private MLTypes type;
    private String value;
    private String defaultValue;
    private String description;
}
