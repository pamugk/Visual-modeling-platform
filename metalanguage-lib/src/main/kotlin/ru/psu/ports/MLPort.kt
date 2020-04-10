package ru.psu.ports

import ru.psu.constructs.MLConstruct
import java.util.*

class MLPort(
        id: UUID, name: String, prototypeId: UUID?, innerStructure: UUID?, var kinds: MLPortKinds
): MLConstruct(id, name, prototypeId, innerStructure)