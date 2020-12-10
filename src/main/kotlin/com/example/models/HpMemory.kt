package com.example.models

class HpMemory {
    val roms = Roms()
    val ram = Ram()

    operator fun get(address: Int): Byte? = when (address) {
        in 0x0000..0x7FFF -> roms[address]
        in 0x8000..0xFEFF -> ram[address]
        else -> null
    }
}