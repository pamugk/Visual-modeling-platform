package relations;

import attributes.MLAttribute;
import constraints.MLConstraint;
import constructs.MLConstruct;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
public class MLRelation extends MLConstruct {
    @Setter
    private MLRelTypes type;
    @Setter
    private boolean unique;
    @Singular
    private List<MLAttribute> attributes;
    @Singular
    private List<MLConstraint> constraints;
    @Setter
    private MLMultiplicity multiplicity;

    public MLRelation(@NonNull String name, @NonNull UUID id, @NonNull UUID prototypeId) {
        super(name, id, prototypeId);
        attributes = new ArrayList<MLAttribute>();
        constraints = new ArrayList<MLConstraint>();
    }
}
