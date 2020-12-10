package com.example.models

import tornadofx.singleAssign

data class RomItem(val id: Byte, val name: String, val contents: List<Byte>)

enum class SystemType {
    ModelA, ModelB
}

class Roms {
    var systemType = SystemType.ModelA
    var rom0A: List<Byte> by singleAssign()
    var rom1A: List<Byte> by singleAssign()
    var rom2A: List<Byte> by singleAssign()
    var rom0B: List<Byte> by singleAssign()
    var rom1B: List<Byte> by singleAssign()
    var rom2B: List<Byte> by singleAssign()

    val applicationRomsA = arrayListOf<RomItem>()
    val applicationRomsB = arrayListOf<RomItem>()

    init {
        // Load system main roms
        rom0A = javaClass.getResourceAsStream("/roms/romsys1").use { it.readBytes().toList() }
        rom1A = javaClass.getResourceAsStream("/roms/romsys2").use { it.readBytes().toList() }
        rom2A = javaClass.getResourceAsStream("/roms/romsys3").use { it.readBytes().toList() }
        rom0B = javaClass.getResourceAsStream("/roms/romsys1B").use { it.readBytes().toList() }
        rom1B = javaClass.getResourceAsStream("/roms/romsys2B").use { it.readBytes().toList() }
        rom2B = javaClass.getResourceAsStream("/roms/romsys3B").use { it.readBytes().toList() }

        // Load the system application rom. i.e. application Rom 0
        applicationRomsA.add(RomItem(0, "System", javaClass.getResourceAsStream("/roms/rom000").use {
            it.readBytes().toList()
        }))
        applicationRomsB.add(RomItem(0, "System", javaClass.getResourceAsStream("/roms/rom000B").use {
            it.readBytes().toList()
        }))
    }

    private val applicationRoms get() = if (systemType == SystemType.ModelA) applicationRomsA else applicationRomsB
    private val rom0 get() = if (systemType == SystemType.ModelA) rom0A else rom0B
    private val rom1 get() = if (systemType == SystemType.ModelA) rom1A else rom1B
    private val rom2 get() = if (systemType == SystemType.ModelA) rom2A else rom2B

    private var appRom = listOf<Byte>()
    var currentAppRom: Byte = 0
        set(value) {
            applicationRoms.forEach {
                if (it.id == value) {
                    field = value
                    appRom = it.contents
                    return
                }
            }
            field = 0
        }

    infix fun byteAt(location: Int): Byte? {
        return when (location) {
            in 0x0000..0x1FFF -> rom0[location]
            in 0x2000..0x3FFF -> rom1[location - 0x2000]
            in 0x4000..0x5FFF -> rom2[location - 0x4000]
            in 0x6000..0x7FFF -> appRom[location - 0x6000]
            else -> null
        }
    }

    infix fun wordAt(location: Int): Int? {
        if (location !in 0x0000..0x7FFE) return null
        return ((this byteAt (location + 1))!!.toInt() shl 8) or (this byteAt location)!!.toInt()
    }

    fun slice(indices: IntRange): List<Byte> {
        with(indices) {
            if (first !in 0x0000..0x7FFF || first < 0) return listOf()

            val endPoint = last + 1
            val start0 = first in 0x0000..0x1FFF
            val start1 = first in 0x2000..0x3FFF
            val start2 = first in 0x4000..0x5FFF
            val start3 = first in 0x6000..0x7FFF
            val end0 = endPoint in 0x0000..0x2000
            val end1 = endPoint in 0x2001..0x4000
            val end2 = endPoint in 0x4001..0x6000
            val end3 = endPoint in 0x6001..0x8000
            val end4 = endPoint > 0x8000

            val sliceStart = when (first) {
                in 0x0000..0x1FFF -> first
                in 0x2000..0x3FFF -> first - 0x2000
                in 0x4000..0x5FFF -> first - 0x4000
                else -> first - 0x6000
            }
            val sliceEnd = when (endPoint) {
                in 0x0000..0x2000 -> endPoint
                in 0x2001..0x4000 -> endPoint - 0x2000
                in 0x4001..0x6000 -> endPoint - 0x4000
                else -> endPoint - 0x6000
            }

            return when {
                start0 && end0 -> rom0.slice(sliceStart until sliceEnd)
                start0 && end1 -> rom0.slice(sliceStart until 0x2000) + rom1.slice(0 until sliceEnd)
                start0 && end2 -> rom0.slice(sliceStart until 0x2000) + rom1 + rom2.slice(0 until sliceEnd)
                start0 && end3 -> rom0.slice(sliceStart until 0x2000) + rom1 + rom2 + appRom.slice(0 until sliceEnd)
                start0 && end4 -> rom0.slice(sliceStart until 0x2000) + rom1 + rom2 + appRom

                start1 && end1 -> rom1.slice(sliceStart until sliceEnd)
                start1 && end2 -> rom1.slice(sliceStart until 0x4000) + rom2.slice(0 until sliceEnd)
                start1 && end3 -> rom1.slice(sliceStart until 0x4000) + rom2 + appRom.slice(0 until sliceEnd)
                start1 && end4 -> rom1.slice(sliceStart until 0x4000) + rom2 + appRom

                start2 && end2 -> rom2.slice(sliceStart until sliceEnd)
                start2 && end3 -> rom2.slice(sliceStart until 0x6000) + appRom.slice(0 until sliceEnd)
                start2 && end4 -> rom2.slice(sliceStart until 0x6000) + appRom

                start3 && end3 -> appRom.slice(sliceStart until sliceEnd)
                start3 && end4 -> appRom.slice(sliceStart until 0x8000)
                else -> listOf()
            }
        }
    }
}

operator fun Roms.get(index: Int): Byte? = this byteAt index


/*
Plug in app roms with names
        "rom000",   // System Rom
        "rom010",   // Program Development
        "rom050",   // Assembler ROM
        "rom250",   //
        "rom260",   // Matrix
        "rom300",   // I/O
        "rom317",   // EMS
        "rom320",   // Mas Storage
        "rom340",   // Service ROM
        "rom350",   // Advanced Programming
        "rom360"    // Printer/Plotter

 */