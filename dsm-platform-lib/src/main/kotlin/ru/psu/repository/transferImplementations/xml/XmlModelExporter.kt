package ru.psu.repository.transferImplementations.xml

import com.thoughtworks.xstream.XStream
import com.thoughtworks.xstream.io.xml.CompactWriter
import ru.psu.model.Model
import ru.psu.repository.transferInterfaces.ModelExporter
import ru.psu.view.View
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.StringWriter

//Реализация экспортёра моделей в XML
class XmlModelExporter:ModelExporter {
    override val fileExtension:String = "xml"
    private val mapper = XStream() //Преобразователь объектов в XML

    private fun doExport(filePath: String, obj: Any): Boolean{
        try{
            val destination = File("$filePath.$fileExtension")
            if (!destination.exists())
                destination.createNewFile()
            FileWriter(filePath).use {  mapper.marshal(obj, CompactWriter(it))}
            return true
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    private fun doTextSerialization(obj: Any):String? {
        try{
            val writer = StringWriter()
            writer.use { mapper.marshal(obj, CompactWriter(it)) }
            return writer.toString()
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun export(filePath: String, model: Model) = doExport(filePath, model)
    override fun serializeToText(model: Model) = doTextSerialization(model)
    override fun exportView(filePath:String, view: View) = doExport(filePath, view)
    override fun serializeViewToText(view: View) = doTextSerialization(view)
}