package validator;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class ValidationMessage {
    private String messageText;
    private Severity severity;
}
