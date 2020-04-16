package ru.psu.ports

import ru.psu.constructs.MLConstruct
import java.util.*

//Класс порта в MetaLanguage 1.1
class MLPort(
        parentId:UUID?, //Идентификатор конструкции-родителя
        id: UUID, //Id порта
        name: String, //Имя порта
        prototypeId: UUID?, //Id прототипа
        var kind: MLPortKinds //Вид порта
): MLConstruct(parentId, id, name, prototypeId, null)