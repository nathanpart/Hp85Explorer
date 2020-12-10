package com.example.views

import com.example.Styles
import com.example.models.HeadFootLine
import com.example.models.LineItem
import com.example.models.ListItemModel
import javafx.collections.ObservableList
import javafx.scene.layout.Priority
import tornadofx.*

class LineItemFragment : ListCellFragment<LineItem>()  {

    private val lineItem = ListItemModel(itemProperty)

    override val root = vbox {
        fun commentLines(lines: ObservableList<HeadFootLine>) {
            lines.forEach {
                hbox {
                    label(it.commentProperty) {
                        hgrow = Priority.ALWAYS
                        useMaxSize = true
                        removeWhen { editingProperty }
                    }
                    textfield(it.commentProperty) {
                        hgrow = Priority.ALWAYS
                        removeWhen { editingProperty.not() }
                        whenVisible { requestFocus() }
                        action { commitEdit(item) }
                    }
                }
            }
        }

        commentLines(lineItem.headings.value)
        hbox {
            label(lineItem.address) {
                addClass(Styles.address)
            }

        }
        commentLines(lineItem.footings.value)

    }
}