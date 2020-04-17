package ru.psu.repository.transferImplementations.json

import com.fasterxml.jackson.core.JsonGenerationException
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ru.psu.model.Model
import ru.psu.repository.transferInterfaces.ModelExporter
import ru.psu.view.View
import java.io.File
import java.io.IOException

//Реализация экспортёра моделей в json
class JsonModelExporter:ModelExporter {
    override val fileExtension: String = "json"
    private val mapper = jacksonObjectMapper() //Преобразователь объектов в json

    private fun doExport(filePath: String, obj: Any): Boolean{
        try{
            mapper.writeValue(File("$filePath.$fileExtension"), obj)
            return true
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        catch(e: JsonGenerationException){
            e.printStackTrace()
        }
        catch(e: JsonMappingException){
            e.printStackTrace()
        }
        return false

    }

    private fun doTextSerialization(obj: Any):String? {
        var textForm:String? = null
        try{
            textForm = mapper.writeValueAsString(obj)
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        catch(e: JsonGenerationException){
            e.printStackTrace()
        }
        catch(e: JsonMappingException){
            e.printStackTrace()
        }
        return textForm
    }

    override fun export(filePath: String, model: Model) = doExport(filePath, model)
    override fun serializeToText(model: Model) = doTextSerialization(model)
    override fun exportView(filePath:String, view: View) = doExport(filePath, view)
    override fun serializeViewToText(view: View) = doTextSerialization(view)
}