package ru.psu.validator

import ru.psu.model.Model

class ModelValidator {
    fun validateModel(dsl:Model, model: Model):ValidationResult {
        return ValidationResult(ArrayList())
    }
}