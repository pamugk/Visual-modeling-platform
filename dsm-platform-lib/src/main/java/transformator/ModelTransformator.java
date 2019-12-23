package transformator;

import model.Model;

import java.util.UUID;

public class ModelTransformator {
    public Model createModelByDsl(Model dsl){
        return new Model("", UUID.randomUUID());
    }

    public Model transformModel(Model model, Model dslFrom, Model dslTo){
        return new Model("", UUID.randomUUID());
    }
}
