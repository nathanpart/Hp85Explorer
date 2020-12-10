package com.example.views

import tornadofx.*
import javafx.scene.control.MenuBar

class Menu : View("My View") {
    override val root = menubar {
        menu("File") {
            item("Save")
            item("Exit")
        }
        menu("View") {
            item("hexdump")
            item("something")
        }
    }

}
