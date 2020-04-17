package ru.psu.repository.transferImplementations.json

import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.psu.model.Model
import ru.psu.repository.transferInterfaces.ModelImporter
import ru.psu.view.View
import java.io.File
import java.io.IOException

//Реализация импортёра моделей из формата json
class JsonModelImporter: ModelImporter {
    override val fileExtension: String = "json"
    private val mapper = jacksonObjectMapper() //Преобразователь объектов в json

    private inline fun <reified T> doImport(filePath: String): T? {
        try {
            return mapper.readValue(File("$filePath.$fileExtension"), T::class.java)
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        catch(e: JsonParseException){
            e.printStackTrace()
        }
        catch(e: JsonMappingException){
            e.printStackTrace()
        }
        return null
    }

    private inline fun <reified T> doTextDeserialization(text: String): T? {
        try {
            return mapper.readValue(text, T::class.java)
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        catch(e: JsonParseException){
            e.printStackTrace()
        }
        catch(e: JsonMappingException){
            e.printStackTrace()
        }
        return null
    }

    override fun import(filePath: String) = doImport<Model>(filePath)
    override fun deserializeFromText(text: String) = doTextDeserialization<Model>(text)
    override fun importView(filePath: String) = doImport<View>(filePath)
    override fun deserializeViewFromText(text: String) = doTextDeserialization<View>(text)
}