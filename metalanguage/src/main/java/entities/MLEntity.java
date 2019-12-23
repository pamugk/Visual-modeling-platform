package entities;

import attributes.MLAttribute;
import constraints.MLConstraint;
import constructs.MLConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class MLEntity extends MLConstruct {
    @Setter
    private long maxCount;
    private List<MLAttribute> attributes;
    private List<MLConstraint> constraints;
    @Setter
    private boolean unique;

    public MLEntity(@NonNull String name, @NonNull UUID id, @NonNull UUID prototypeId) {
        super(name, id, prototypeId);
        attributes = new ArrayList<MLAttribute>();
        constraints = new ArrayList<MLConstraint>();
    }
}
