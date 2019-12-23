package constructs;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class MLConstruct {
    @NonNull
    @Setter
    private String name;
    @NonNull
    private UUID id;
    @NonNull
    private UUID prototypeId;
    @Setter
    private UUID innerStructure;
}
