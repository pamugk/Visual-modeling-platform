package view;

import lombok.*;

import java.awt.*;
import java.util.UUID;

@Builder
@Getter
@RequiredArgsConstructor
@Setter
public class ConstructView {
    @NonNull
    private UUID associatedConstructId;
    private Color backColor;
    private String content;
    @NonNull
    private UUID id;
    private Font font;
    private Shape shape;
    private Stroke stroke;
    private Color strokeColor;
}
