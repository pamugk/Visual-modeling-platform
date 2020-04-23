package ru.psu.repository.transferImplementations.xml

import com.thoughtworks.xstream.XStream
import ru.psu.model.Model
import ru.psu.repository.transferInterfaces.ModelExporter
import ru.psu.view.View
import java.io.File
import java.io.IOException

//Реализация экспортёра моделей в XML
class XmlModelExporter:ModelExporter {
    override val fileExtension:String = "xml"
    private val mapper = XStream() //Преобразователь объектов в XML

    private fun doExport(filePath: String, obj: Any): Boolean{
        try{
            val xml:String = doTextSerialization(obj)!!
            val destination = File("$filePath.$fileExtension")
            if (!destination.exists())
                destination.createNewFile()
            destination.writeText(xml, Charsets.UTF_8)
            return true
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun doTextSerialization(obj: Any):String? {
        var textForm:String? = null
        try{
            textForm = mapper.toXML(obj)
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        return textForm
    }

    override fun export(filePath: String, model: Model) = doExport(filePath, model)
    override fun serializeToText(model: Model) = doTextSerialization(model)
    override fun exportView(filePath:String, view: View) = doExport(filePath, view)
    override fun serializeViewToText(view: View) = doTextSerialization(view)
}