package com.example.views

import com.example.Styles
import tornadofx.*

class MainView : View("Hello TornadoFX") {
    val header: Header by inject()
    override val root = borderpane {
        top = header.root
    }
}


