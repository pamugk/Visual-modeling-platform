package repository;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import model.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class ModelRepository {
    @NonNull
    private ModelTransferSystem transferSystem;

    public List<Model> getModelList(){
        return new ArrayList<Model>();
    }

    public boolean saveModel(Model model){
        return false;
    }

    public Model loadModel(UUID id){
        return new Model("", id);
    }
}
