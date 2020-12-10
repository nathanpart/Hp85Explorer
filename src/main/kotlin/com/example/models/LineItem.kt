package com.example.models

import javafx.beans.property.*
import javafx.collections.FXCollections.observableArrayList
import tornadofx.*
import tornadofx.getValue
import tornadofx.setValue

class HeadFootLine(id: Int, lineItem:Int, order: Int, comment: String) {
    val idProperty = SimpleIntegerProperty(id)
    var id by idProperty

    val lineItemProperty = SimpleIntegerProperty(lineItem)
    var lineItem by lineItemProperty

    val orderProperty = SimpleIntegerProperty(order)
    var order by orderProperty

    val commentProperty = SimpleStringProperty(comment)
    val comment by commentProperty
}

class LineItem(id: Int, address: Int, instrAddress: Int, size: Int, name: String, operand: OperandFormat,
               arp: Byte?, drp: Byte?, sizeUnknown: Boolean, comment: String,
               headingLines: List<HeadFootLine>, footerLines: List<HeadFootLine>) {
    val idProperty = SimpleIntegerProperty(id)
    var id by idProperty

    val addressProperty = SimpleIntegerProperty(address)
    var address by addressProperty

    val instrAddressProperty = SimpleIntegerProperty(instrAddress)
    var instrAddress by instrAddressProperty

    val sizeProperty = SimpleIntegerProperty(size)
    var size by sizeProperty

    val nameProperty = SimpleStringProperty(name)
    var name by nameProperty

    var operand: OperandFormat by property<OperandFormat>(operand)
    fun operandProperty() = getProperty(LineItem::operand)

    val arpProperty = SimpleObjectProperty<Byte?>(arp)
    var arp: Byte? by arpProperty

    val drpProperty = SimpleObjectProperty<Byte?>(drp)
    var drp: Byte? by drpProperty

    val sizeUnknownProperty = SimpleBooleanProperty(sizeUnknown)
    var sizeUnknown by sizeUnknownProperty

    val commentProperty = SimpleStringProperty(comment)
    var comment by commentProperty

    val headings = observableArrayList(headingLines)
    val footings = observableArrayList(footerLines)


}

class ListItemModel(property: ObjectProperty<LineItem>): ItemViewModel<LineItem>(itemProperty = property) {
    val address = bind { item?.addressProperty }
    val operand = bind { item?.operandProperty() }
    val size = bind { item?.sizeProperty }
    val name = bind { item?.nameProperty }
    val arp = bind(LineItem::arp)
    val drp = bind(LineItem::drp)
    val comment = bind { item?.commentProperty }
    val headings = bind(LineItem::headings)
    val footings = bind(LineItem::footings)
}

