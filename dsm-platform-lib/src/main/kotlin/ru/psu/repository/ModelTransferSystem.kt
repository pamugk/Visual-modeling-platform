package ru.psu.repository

import ru.psu.repository.transferInterfaces.ModelExporter
import ru.psu.repository.transferInterfaces.ModelImporter

//Класс системы экспорта-импорта моделей
class ModelTransferSystem(
        var exporter: ModelExporter, //Экспортёр моделей
        var importer: ModelImporter //Импортёр моделей
)