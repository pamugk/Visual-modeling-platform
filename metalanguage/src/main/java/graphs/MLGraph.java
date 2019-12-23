package graphs;

import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
public class MLGraph {
    private Set<UUID> entities;
    private Set<UUID> ports;
    private Set<UUID> relations;
    @Setter
    private UUID parentId;

    MLGraph(){
        entities = new HashSet<UUID>();
        ports = new HashSet<UUID>();
        relations = new HashSet<UUID>();
    }
}
