package view;

import constructs.MLConstruct;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class View {
    @Setter @NonNull
    private String name;
    @NonNull
    private UUID id;
    @Singular
    private Map<UUID, MLConstruct> constructViews;
}
