package validator;

import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Getter
public class ValidationResult {
    @Singular
    private List<ValidationMessage> validationMessages;
}
