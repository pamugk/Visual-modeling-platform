package repository;

import model.Model;

import java.util.UUID;

public class ModelImporter {
    public Model importModel(String fileName){
        return new Model("", UUID.randomUUID());
    }
}
