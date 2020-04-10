package ru.psu

import ru.psu.views.MainView
import javafx.application.Application
import tornadofx.App

class ShellApp: App(MainView::class)

fun main(args: Array<String>) {
    Application.launch(ShellApp::class.java, *args)
}
