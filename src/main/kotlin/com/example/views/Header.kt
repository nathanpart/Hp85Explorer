package com.example.views

import tornadofx.*

class Header : View("My View") {
    val menu: Menu by inject()

    override val root = vbox {
        add(menu.root)
    }
}
