import generator.DslDefGenerator;
import lombok.Getter;
import lombok.Setter;
import repository.ModelRepository;
import transformator.ModelTransformator;
import validator.ModelValidator;

@Getter
@Setter
public class DsmPlatform {
    private DslDefGenerator defGenerator;
    private ModelValidator validator;
    private ModelTransformator transformator;
    private ModelRepository repository;
}
