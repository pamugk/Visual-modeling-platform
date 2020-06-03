package ru.psu.repository.transferImplementations.xml

import com.thoughtworks.xstream.XStream
import ru.psu.model.Model
import ru.psu.repository.transferInterfaces.ModelImporter
import ru.psu.view.View
import java.io.File
import java.io.IOException

//Реализация импортёра моделей из формата json
class XmlModelImporter: ModelImporter {
    override val format:String = "xml"
    private val mapper = XStream() //Преобразователь XML в объекты

    private fun doImport(filePath: String): Any? { //Реализация фунции импорта
        try {
            return mapper.fromXML(File("$filePath.$format"))
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    private fun doTextDeserialization(text: String): Any? {
        try {
            return mapper.fromXML(text)
        }
        catch(e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    override fun import(filePath: String) = doImport(filePath) as Model?
    override fun deserializeFromText(text: String) = doTextDeserialization(text) as Model?
    override fun importView(filePath: String) = doImport(filePath) as View
    override fun deserializeViewFromText(text: String) = doTextDeserialization(text) as View?
}