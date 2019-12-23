package ports;

import constructs.MLConstruct;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class MLPort extends MLConstruct {
    @NonNull
    private MLPortKinds kind;

    public MLPort(@NonNull String name, @NonNull UUID id, @NonNull UUID prototypeId, @NonNull MLPortKinds kind) {
        super(name, id, prototypeId);
        this.kind = kind;
    }
}
