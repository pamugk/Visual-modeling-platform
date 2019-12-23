package model;

import constructs.MLConstruct;
import lombok.*;
import view.View;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Model {
    @Setter @NonNull
    private String name;
    @NonNull
    private UUID id;
    @Singular
    private List<View> views;
    @Singular
    private Map<UUID, MLConstruct> constructs;
}
