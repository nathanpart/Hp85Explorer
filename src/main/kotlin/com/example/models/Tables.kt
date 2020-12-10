package com.example.models

import org.jetbrains.exposed.dao.id.IntIdTable


object AppRomTbl: IntIdTable("roms") {
    val romId = byte("rom_id").uniqueIndex()
    val romName = varchar("name", length = 50)
    val fileName = varchar("file_name", length = 255)
}

object LineItemTbl: IntIdTable("line_item") {
    val address = integer("address")
    val instrAddress = integer("instr_address")
    val size = integer("size")
    val name = varchar("name", 10)
    val operand = enumerationByName("operand", 10, OperandFormat::class)
    val arp = byte("arp").nullable()
    val drp = byte("drp").nullable()
    val sizeUnknown = bool("size_unknown")
    val comment = varchar("comment", length = 255).default("")
}

object HeadingLineTbl: IntIdTable("heading_line") {
    val line = reference("line", LineItemTbl.id)
    val order = integer("order")
    val comment = varchar("comment", length = 255)
}

object FootingLineTbl: IntIdTable("footing_line") {
    val line = reference("line", LineItemTbl.id)
    val order = integer("order")
    val comment = varchar("comment", length = 255)
}

object EntryPointsTbl: IntIdTable("entry_points") {
    val location = reference("location", LabelTbl.id)
    val disassembled = bool("disassembled")
}

object CallerTbl: IntIdTable("caller") {
    val address = integer("address")
    val target = reference("target", EntryPointsTbl.id)
    val callType = varchar("call_type", 10)
}


object LabelTbl: IntIdTable("label") {
    val address = integer("address")
    val name = varchar("name", 32)
}

object VarReferenceTbl: IntIdTable("var_reference") {
    val address = integer("address")
    val varReferenced = reference("var_referenced", LabelTbl.id)
}

