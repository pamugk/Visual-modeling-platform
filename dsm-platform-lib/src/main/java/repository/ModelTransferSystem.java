package repository;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@Setter
public class ModelTransferSystem {
    @NonNull
    private ModelExporter exporter;
    @NonNull
    private ModelImporter importer;
}
