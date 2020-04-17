package ru.psu.repository.transferInterfaces

import ru.psu.model.Model
import ru.psu.view.View

//Интерфейс импортёра моделей
interface ModelImporter {
    val fileExtension:String //Используемое расширение файлов
    fun import(filePath:String): Model? //Заголовок функции импорта модели
    fun deserializeFromText(text:String): Model? //Заголовок функции десериализации текста в модель
    fun importView(filePath:String): View? //Заголовок функции импорта представления модели
    fun deserializeViewFromText(text:String): View? //Заголовок функции десериализации текста в представление модели
}