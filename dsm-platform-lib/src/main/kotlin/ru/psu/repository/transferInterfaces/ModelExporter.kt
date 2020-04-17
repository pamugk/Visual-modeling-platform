package ru.psu.repository.transferInterfaces

import ru.psu.model.Model
import ru.psu.view.View

//Интерфейс экспортёра моделей
interface ModelExporter {
    val fileExtension:String //Используемое расширение файлов
    fun export(filePath:String, model: Model):Boolean //Заголовок функции экспорта модели
    fun serializeToText(model: Model):String? //Заголовок функции сериализации модели в текст
    fun exportView(filePath:String, view: View):Boolean //Заголовок функции экспорта представления модели
    fun serializeViewToText(view: View):String? //Заголовок функции сериализации представления модели в текст
}