package com.example.models

fun Int.toOctalString() = "%o".format(this)

class ArpDrp(a: Int?=null, d: Int?=null) {
    var arp: Int? = null
        set(value) = when (value) {
            null -> field = null
            in 0x00..0x3F -> field = value
            else -> throw IllegalArgumentException("HP85 only has 64 registers. Trying to set AR to $value")
        }

    var drp: Int? = null
        set(value) = when (value) {
            null -> field = null
            in 0x00..0x3F -> field = value
            else -> throw IllegalArgumentException("HP85 only has 64 registers. Trying to set AR to $value")
        }

    init {
        arp = a
        drp = d
    }

    val arpString: String get() = if (arp == null) "?" else if (arp == 1) "*" else arp!!.toOctalString()
    val drpString: String get() = if (drp == null) "?" else if (drp == 1) "*" else drp!!.toOctalString()

    override fun equals(other: Any?): Boolean {
        if (other === this) return true
        if (other !is ArpDrp) return false
        return other.arp == arp && other.drp == drp
    }

    override fun hashCode(): Int = ((arp ?: 0) shl 6) + (drp ?: 0)

    override fun toString(): String = "Arp: R$arpString  Drp: R$drpString"
}

