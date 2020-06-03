package ru.psu.fragments

import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Spinner
import javafx.scene.control.Tab
import javafx.scene.layout.Pane
import javafx.scene.layout.VBox
import javafx.util.StringConverter
import ru.psu.utils.transform
import ru.psu.view.auxiliaries.shapes.*
import tornadofx.*
import java.util.*

class ShapeDialog:Fragment() {
    enum class Shapes { Circle, Ellipse, Rectangle }

    private class ShapeStringConverter(private val strings:ResourceBundle):StringConverter<Shapes>() {
        override fun toString(`object`: Shapes?): String = if (`object` == null) "" else strings[`object`.name]

        override fun fromString(string: String?): Shapes {
            if (string != null)
                for (key in strings.keys)
                    if (strings[key] == string)
                        return Shapes.valueOf(key)
            return Shapes.Circle
        }
    }

    override val root: VBox by fxml()
    val shapesBox: ComboBox<Shapes> by fxid()
    val shapePane: Pane by fxid()

    val circleTab: Tab by fxid()
    val radiusSpinner:Spinner<Double> by fxid()

    val ellipseTab: Tab by fxid()
    val radiusXSpinner:Spinner<Double> by fxid()
    val radiusYSpinner:Spinner<Double> by fxid()

    val rectangleTab:Tab by fxid()
    val widthSpinner:Spinner<Double> by fxid()
    val heightSpinner:Spinner<Double> by fxid()
    val arcWidthSpinner:Spinner<Double> by fxid()
    val arcHeightSpinner:Spinner<Double> by fxid()

    val okBtn:Button by fxid()
    val cancelBtn:Button by fxid()

    var save:Boolean = false
        private set
    var shape: ShapeDto? = null
        private set(value) {
            shapePane.clear()
            if (value != null)
                shapePane.add(value.transform())
            field = value
        }

    init {
        title = messages["title"]
        shapesBox.items.addAll(Shapes.values())
        shapesBox.converter = ShapeStringConverter(messages)
        shapesBox.selectionModel.selectedItemProperty().addListener { _, _, newShape -> updateShape(newShape) }
        radiusSpinner.valueProperty().addListener { _, _, _ -> updateShape(Shapes.Circle) }
        radiusXSpinner.valueProperty().addListener { _, _, _ -> updateShape(Shapes.Ellipse) }
        radiusYSpinner.valueProperty().addListener { _, _, _ -> updateShape(Shapes.Ellipse) }
        widthSpinner.valueProperty().addListener { _, _, _ -> updateShape(Shapes.Rectangle) }
        heightSpinner.valueProperty().addListener { _, _, _ -> updateShape(Shapes.Rectangle) }
        arcWidthSpinner.valueProperty().addListener { _, _, _ -> updateShape(Shapes.Rectangle) }
        arcHeightSpinner.valueProperty().addListener { _, _, _ -> updateShape(Shapes.Rectangle) }
    }

    fun cancel() {
        this.currentStage?.close()
    }

    fun ok() {
        save = true
        this.currentStage?.close()
    }

    private fun updateShape(newShape:Shapes) {
        val updOutcome = when (newShape) {
            Shapes.Circle ->
                Pair(CircleDto(PointDto(radiusSpinner.value, radiusSpinner.value), radiusSpinner.value), circleTab)
            Shapes.Ellipse ->
                Pair(EllipseDto(PointDto(radiusXSpinner.value, radiusYSpinner.value),
                        PointDto(radiusXSpinner.value, radiusYSpinner.value)), ellipseTab)
            Shapes.Rectangle ->
                Pair(RectangleDto(PointDto(), widthSpinner.value, heightSpinner.value,
                        arcHeightSpinner.value, arcWidthSpinner.value), rectangleTab)
        }
        shape = updOutcome.first; updOutcome.second.select()
    }

    fun updateShapeProperties(shape:ShapeDto) {
        when (shape){
            is CircleDto -> {
                radiusSpinner.valueFactory.value = shape.radius
                shapesBox.value = Shapes.Circle
            }
            is EllipseDto -> {
                radiusXSpinner.valueFactory.value = shape.radius.x
                radiusYSpinner.valueFactory.value = shape.radius.y
                shapesBox.value = Shapes.Ellipse
            }
            is RectangleDto -> {
                widthSpinner.valueFactory.value = shape.width
                heightSpinner.valueFactory.value = shape.width
                arcWidthSpinner.valueFactory.value = shape.arcWidth
                arcHeightSpinner.valueFactory.value = shape.arcHeight
                shapesBox.value = Shapes.Rectangle
            }
        }
    }
}