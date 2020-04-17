package ru.psu.repository

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import java.util.*

//Таблица, хранящая информацию о сохранённых моделях
internal object Models: Table() {
    //Столбец id модели
    val id: Column<UUID> = uuid("id")
    //Столбец id прототипа модели, ссылающийся на столбец id
    val prototypeId: Column<UUID?> = reference("prototype_id", id, onDelete = ReferenceOption.CASCADE).nullable()
    //Столбец названия модели
    var name: Column<String> = varchar("name", 100)
    //Столбец описания модели
    var description: Column<String> = text("description")
    //Столбец с указанием типа сохранённых данных
    val dataType: Column<String> = varchar("data_type", 10)
    //Столбец с сериализованным графическим представлением
    val data: Column<String> = text("data")

    //Указание первичного ключа для таблицы
    override val primaryKey = PrimaryKey(id)
}

//Таблица, хранящая информацию о сохранённых графических представлениях
internal object Views: Table() {
    //Столбец Id графического представления
    val id: Column<UUID> = uuid("id")
    //Столбец id прототипа графического представления, ссылающийся на столбец id
    val prototypeId: Column<UUID?> = reference("prototype_id", id, onDelete = ReferenceOption.CASCADE).nullable()
    //Столбец id прототипа графического представления, ссылающийся на столбец id в таблице моделей
    val modelId: Column<UUID> = reference("model_id", Models.id, onDelete = ReferenceOption.CASCADE)
    //Столбец названия графического представления
    val name: Column<String> = varchar("name", 100)
    //Столбец описания графического представления
    val description: Column<String> = text("description")
    //Столбец с указанием типа сохранённых данных
    val dataType: Column<String> = varchar("data_type", 10)
    //Столбец с сериализованным графическим представлением
    val data: Column<String> = text("data")

    //Указание первичного ключа для таблицы
    override val primaryKey = PrimaryKey(Models.id)
}