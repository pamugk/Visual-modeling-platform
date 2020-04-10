package ru.psu

import ru.psu.generator.DslDefGenerator
import ru.psu.repository.ModelRepository
import ru.psu.transformator.ModelTransformator
import ru.psu.validator.ModelValidator

class DsmPlatform(
        var defGenerator:DslDefGenerator,
        var validator:ModelValidator,
        var transforator:ModelTransformator,
        var repository:ModelRepository
)