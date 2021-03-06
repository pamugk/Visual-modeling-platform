package ru.psu.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import ru.psu.model.Model
import ru.psu.repository.entries.ModelEntry
import ru.psu.repository.entries.ViewEntry
import ru.psu.view.View
import java.io.File
import java.io.IOException
import java.sql.Connection
import java.util.*

class ModelRepository(pathToRepository:String, val transferSystem: ModelTransferSystem) {
    //Настройка репозитория - создание соответствующей директории
    //и инициализация SQLite-БД репозитория
    init {
        val repositoryDirectory = File(pathToRepository)
        if (!repositoryDirectory.exists() && !repositoryDirectory.mkdirs())
            throw IOException("Repository directory can not be created")
        Database.connect("jdbc:sqlite:$pathToRepository/repository.db", "org.sqlite.JDBC")
        TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
        transaction {
            if (!Models.exists())
                SchemaUtils.create(Models)
            if (!Views.exists())
                SchemaUtils.create(Views)
        }
    }

    //Метод определяющий наличие модели в репозитории вне зависимости от её уровня (мета- или прикладной уровень)
    fun containsModel(model:Model):Boolean =
            transaction { Models.slice(Models.id).select { Models.id eq model.id }.firstOrNull() != null }

    //Метод, определяющий наличие графического представления в репозитории
    fun containsView(view:View):Boolean =
            transaction { Models.slice(Models.id).select { Views.id eq view.id }.firstOrNull() != null }

    //Метод для удаления из репозитория
    //Если модель является прототипом других моделей, то они также удаляются
    //При удалении модели удаляются и все её графические представления
    fun deleteModel(model:Model) =
            transaction { Models.deleteWhere { Models.id eq model.id } }

    //Метод для удаления графического представления модели из репозитория
    fun deleteView(view:View) =
            transaction { Views.deleteWhere { Views.id eq view.id } }

    //Метод для получения метамоделей из репозитория
    fun listMetamodels(): List<ModelEntry> = transaction {
            Models.slice(Models.id, Models.prototypeId, Models.name, Models.description)
                    .select { Models.prototypeId.isNull() }
                    .map { ModelEntry(it[Models.prototypeId], it[Models.id], it[Models.name], it[Models.description]) }
    }

    //Метод для получения моделей из репозитория
    fun listModels(): List<ModelEntry> {
        return transaction {
            Models.slice(Models.id, Models.prototypeId, Models.name, Models.description)
                    .select { Models.prototypeId.isNotNull() }
                    . map { ModelEntry(it[Models.prototypeId], it[Models.id], it[Models.name], it[Models.description]) }
        } .toList()
    }

    //Метод для получения списка графических представлений модели
    fun listViewsOfModel(model:Model):List<ViewEntry> = transaction {
            Views.slice(Views.id, Views.name, Views.description).select { Views.modelId eq model.id }
                .map{ ViewEntry(it[Views.id], it[Views.name], it[Views.description]) }
    }

    //Метод для получения модели/метамодели из репозитория
    fun loadModel(id: UUID):Model?{
        val result:ResultRow? = transaction {
            Models.slice(Models.data)
                    .select { (Models.id eq id) and (Models.dataType eq transferSystem.importer.format) }
                    .firstOrNull()
        }
        return if (result == null) null else transferSystem.importer.deserializeFromText(result[Models.data])
    }

    //Метод для получения графических представлений модели
    fun loadModelViews(id: UUID):List<View> = transaction {
        Views.slice(Views.data)
                .select { (Views.modelId eq id) and (Views.dataType eq transferSystem.importer.format) }
                .mapNotNull { row -> transferSystem.importer.deserializeViewFromText(row[Views.data]) }
    }

    //Метод для получения графического представления модели
    fun loadView(id: UUID):View?{
        val result:ResultRow? = transaction {
            Views.slice(Models.data)
                    .select { (Views.id eq id) and (Views.dataType eq transferSystem.importer.format) }
                    .firstOrNull()
        }
        return if (result == null) null else transferSystem.importer.deserializeViewFromText(result[Views.data])
    }

    //Метод для сохранения модели в репозитории (добавления/сохранения изменений)
    fun saveModel(model:Model):Boolean {
        val result = transferSystem.exporter.serializeToText(model) ?: return false
        transaction {
            //Если модели ещё нет в репозитории, добавляем, иначе обновляем
            if (Models.slice(Models.id).select { Models.id eq model.id }.firstOrNull() == null)
                Models.insert {
                    it[id] = model.id; it[prototypeId] = model.prototypeId
                    it[name] = model.name; it[description] = model.description
                    it[dataType] = transferSystem.exporter.format; it[data] = result
                }
            else
                Models.update({Models.id eq model.id}) {
                    it[name] = model.name; it[description] = model.description
                    it[dataType] = transferSystem.exporter.format; it[data] = result
                }
        }
        return true
    }

    //Метод для сохранения графического представления в репозитории (добавления/сохранения изменений)
    private fun saveView(view:View): Boolean {
        val result = transferSystem.exporter.serializeToText(view) ?: return false
        transaction {
            //Если модели ещё нет в репозитории, добавляем, иначе обновляем
            if (Views.slice(Views.id).select { Views.id eq view.id }.firstOrNull() == null)
                Views.insert {
                    it[id] = view.id; it[prototypeId] = view.prototypeId; it[modelId] = view.modelId
                    it[name] = view.name; it[description] = view.description
                    it[dataType] = transferSystem.exporter.format; it[data] = result
                }
            else
                Views.update({Views.id eq view.id}) {
                    it[name] = view.name; it[description] = view.description
                    it[dataType] = transferSystem.exporter.format; it[data] = result
                }
        }
        return true
    }

    //Метод для сохранения всех переданных графических представлений в одной транзакции
    fun saveViews(views:List<View>):Boolean =
         transaction {
            for (view in views)
                if (!saveView(view))
                    return@transaction false
            return@transaction true
        }
}