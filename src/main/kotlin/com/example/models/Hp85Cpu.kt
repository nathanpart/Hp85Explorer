package com.example.models

enum class OperandFormat {DR, DRR, None, DrAr, ImmByte, ImmMulti, Jmp, Literal, Index,
    Sub, SubIndex, StackUp, StackDown, DrpArp}


data class Instruction(val address: Int, val size: Int, val instrAddress: Int, val arpDrp: ArpDrp,
                       val name: String, val operandType: OperandFormat, val lengthUnknown: Boolean = false)

operator fun Instruction.contains(address: Int) = address in this.address until this.address + size

object HpCpu {
    private val arpDrp = ArpDrp()

    private var arp: Int?
        get() = arpDrp.arp
        set(value) {
            arpDrp.arp = value
        }

    private var drp: Int?
        get() = arpDrp.drp
        set(value) {
            arpDrp.drp = value
        }


    data class InstrMap(val opCode: Int, val name: String, val adrMode: OperandFormat)
    private val instrMap = mapOf(
        0x80 to InstrMap(200, "ELB ", OperandFormat.DR),
        0x81 to InstrMap(201, "ELM ", OperandFormat.DR),
        0x82 to InstrMap(202, "ERB ", OperandFormat.DRR),
        0x83 to InstrMap(203, "ERM ", OperandFormat.DRR),
        0x84 to InstrMap(204, "LLB ", OperandFormat.DR),
        0x85 to InstrMap(205, "LLM ", OperandFormat.DR),
        0x86 to InstrMap(206, "LRB ", OperandFormat.DR),
        0x87 to InstrMap(207, "LRM ", OperandFormat.DR),
        0x88 to InstrMap(210, "ICB ", OperandFormat.DR),
        0x89 to InstrMap(211, "ICM ", OperandFormat.DR),
        0x8A to InstrMap(212, "DCB ", OperandFormat.DR),
        0x8B to InstrMap(213, "DCM ", OperandFormat.DR),
        0x8C to InstrMap(214, "TCB ", OperandFormat.DR),
        0x8D to InstrMap(215, "TCM ", OperandFormat.DR),
        0x8E to InstrMap(216, "NCB ", OperandFormat.DR),
        0x8F to InstrMap(217, "NCM ", OperandFormat.DR),
        0x90 to InstrMap(220, "TSB ", OperandFormat.DR),
        0x91 to InstrMap(221, "TSM ", OperandFormat.DR),
        0x92 to InstrMap(222, "CLB ", OperandFormat.DR),
        0x93 to InstrMap(223, "CLM ", OperandFormat.DR),
        0x94 to InstrMap(224, "ORB ", OperandFormat.DR),
        0x95 to InstrMap(225, "ORM ", OperandFormat.DR),
        0x96 to InstrMap(226, "XRB ", OperandFormat.DR),
        0x97 to InstrMap(227, "XRM ", OperandFormat.DR),

        0x98 to InstrMap(230, "BIN ", OperandFormat.None),
        0x99 to InstrMap(231, "BCD ", OperandFormat.None),
        0x9A to InstrMap(232, "SAD ", OperandFormat.None),
        0x9B to InstrMap(233, "DCE ", OperandFormat.None),
        0x9C to InstrMap(234, "ICE ", OperandFormat.None),
        0x9D to InstrMap(235, "CLE ", OperandFormat.None),
        0x9E to InstrMap(236, "RTN ", OperandFormat.None),
        0x9F to InstrMap(237, "PAD ", OperandFormat.None),

        0xA0 to InstrMap(240, "LDB ", OperandFormat.DrAr),
        0xA1 to InstrMap(241, "LDM ", OperandFormat.DrAr),
        0xA2 to InstrMap(242, "STB ", OperandFormat.DrAr),
        0xA3 to InstrMap(243, "STM ", OperandFormat.DrAr),
        0xA4 to InstrMap(244, "LDBD", OperandFormat.DrAr),
        0xA5 to InstrMap(245, "LDMD", OperandFormat.DrAr),
        0xA6 to InstrMap(246, "STBD", OperandFormat.DrAr),
        0xA7 to InstrMap(247, "STMD", OperandFormat.DrAr),
        0xA8 to InstrMap(250, "LDB ", OperandFormat.ImmByte),
        0xA9 to InstrMap(251, "LDM ", OperandFormat.ImmMulti),
        0xAA to InstrMap(252, "STB ", OperandFormat.ImmByte),
        0xAB to InstrMap(253, "STM ", OperandFormat.ImmMulti),
        0xAC to InstrMap(254, "LDBI", OperandFormat.DrAr),
        0xAD to InstrMap(255, "LDMI", OperandFormat.DrAr),
        0xAE to InstrMap(256, "STBI", OperandFormat.DrAr),
        0xAF to InstrMap(257, "STMI", OperandFormat.DrAr),
        0xB0 to InstrMap(260, "LDBD", OperandFormat.Literal),
        0xB1 to InstrMap(261, "LDMD", OperandFormat.Literal),
        0xB2 to InstrMap(262, "STBD", OperandFormat.Literal),
        0xB3 to InstrMap(263, "STMD", OperandFormat.Literal),
        0xB4 to InstrMap(264, "LDBD", OperandFormat.Index),
        0xB5 to InstrMap(265, "LDMD", OperandFormat.Index),
        0xB6 to InstrMap(266, "STBD", OperandFormat.Index),
        0xB7 to InstrMap(267, "STMD", OperandFormat.Index),
        0xB8 to InstrMap(270, "LDBI", OperandFormat.Literal),
        0xB9 to InstrMap(271, "LDMI", OperandFormat.Literal),
        0xBA to InstrMap(272, "STBI", OperandFormat.Literal),
        0xBB to InstrMap(273, "STMI", OperandFormat.Literal),
        0xBC to InstrMap(274, "LDBI", OperandFormat.Index),
        0xBD to InstrMap(275, "LDMI", OperandFormat.Index),
        0xBE to InstrMap(276, "STBI", OperandFormat.Index),
        0xBF to InstrMap(277, "STMI", OperandFormat.Index),

        0xC0 to InstrMap(300, "CMB ", OperandFormat.DrAr),
        0xC1 to InstrMap(301, "CMM ", OperandFormat.DrAr),
        0xC2 to InstrMap(302, "ADB ", OperandFormat.DrAr),
        0xC3 to InstrMap(303, "ADM ", OperandFormat.DrAr),
        0xC4 to InstrMap(304, "SBB ", OperandFormat.DrAr),
        0xC5 to InstrMap(305, "SBM ", OperandFormat.DrAr),
        0xC6 to InstrMap(306, "JSB ", OperandFormat.SubIndex),
        0xC7 to InstrMap(307, "ANM ", OperandFormat.DrAr),
        0xC8 to InstrMap(310, "CMB ", OperandFormat.Literal),
        0xC9 to InstrMap(311, "CMM ", OperandFormat.Literal),
        0xCA to InstrMap(312, "ADB ", OperandFormat.Literal),
        0xCB to InstrMap(313, "ADM ", OperandFormat.Literal),
        0xCC to InstrMap(314, "SBB ", OperandFormat.Literal),
        0xCD to InstrMap(315, "SBM ", OperandFormat.Literal),
        0xCE to InstrMap(316, "JSB ", OperandFormat.Sub),
        0xCF to InstrMap(317, "ANM ", OperandFormat.Literal),
        0xD0 to InstrMap(320, "CMBD", OperandFormat.ImmByte),
        0xD1 to InstrMap(321, "CMMD", OperandFormat.ImmMulti),
        0xD2 to InstrMap(322, "ADBD", OperandFormat.ImmByte),
        0xD3 to InstrMap(323, "ADMD", OperandFormat.ImmMulti),
        0xD4 to InstrMap(324, "SBBD", OperandFormat.ImmByte),
        0xD5 to InstrMap(325, "SBMD", OperandFormat.ImmMulti),
        0xD7 to InstrMap(327, "ANMD", OperandFormat.ImmMulti),
        0xD8 to InstrMap(330, "CMBD", OperandFormat.DrAr),
        0xD9 to InstrMap(331, "CMMD", OperandFormat.DrAr),
        0xDA to InstrMap(332, "ADBD", OperandFormat.DrAr),
        0xDB to InstrMap(333, "ADMD", OperandFormat.DrAr),
        0xDC to InstrMap(334, "SBBD", OperandFormat.DrAr),
        0xDD to InstrMap(335, "SBMD", OperandFormat.DrAr),
        0xDF to InstrMap(337, "ANMD", OperandFormat.DrAr),

        0xE0 to InstrMap(340, "POBD", OperandFormat.StackUp),
        0xE1 to InstrMap(341, "POMD", OperandFormat.StackUp),
        0xE2 to InstrMap(342, "POBD", OperandFormat.StackDown),
        0xE3 to InstrMap(343, "POBD", OperandFormat.StackDown),
        0xE4 to InstrMap(344, "PUBD", OperandFormat.StackUp),
        0xE5 to InstrMap(345, "PUMD", OperandFormat.StackUp),
        0xE6 to InstrMap(346, "PUBD", OperandFormat.StackDown),
        0xE7 to InstrMap(347, "PUMD", OperandFormat.StackDown),
        0xE8 to InstrMap(350, "POBI", OperandFormat.StackUp),
        0xE9 to InstrMap(351, "POMI", OperandFormat.StackUp),
        0xEA to InstrMap(352, "POBI", OperandFormat.StackDown),
        0xEB to InstrMap(353, "POMI", OperandFormat.StackDown),
        0xEC to InstrMap(354, "PUBI", OperandFormat.StackUp),
        0xED to InstrMap(355, "PUMI", OperandFormat.StackUp),
        0xEE to InstrMap(356, "PUBI", OperandFormat.StackDown),
        0xEF to InstrMap(357, "PUMI", OperandFormat.StackDown),

        0xF0 to InstrMap(360, "JMP ", OperandFormat.Jmp),
        0xF1 to InstrMap(361, "JNO ", OperandFormat.Jmp),
        0xF2 to InstrMap(362, "JOD ", OperandFormat.Jmp),
        0xF3 to InstrMap(363, "JEV ", OperandFormat.Jmp),
        0xF4 to InstrMap(364, "JNG ", OperandFormat.Jmp),
        0xF5 to InstrMap(365, "JPS ", OperandFormat.Jmp),
        0xF6 to InstrMap(366, "JNZ ", OperandFormat.Jmp),
        0xF7 to InstrMap(367, "JZR ", OperandFormat.Jmp),
        0xF8 to InstrMap(370, "JEN ", OperandFormat.Jmp),
        0xF9 to InstrMap(371, "JEZ ", OperandFormat.Jmp),
        0xFA to InstrMap(372, "JNC ", OperandFormat.Jmp),
        0xFB to InstrMap(373, "JCY ", OperandFormat.Jmp),
        0xFC to InstrMap(374, "JLZ ", OperandFormat.Jmp),
        0xFD to InstrMap(375, "JLN ", OperandFormat.Jmp),
        0xFE to InstrMap(376, "JRZ ", OperandFormat.Jmp),
        0xFF to InstrMap(377, "JRN ", OperandFormat.Jmp),
    )

    fun decodeInstruction(address: Int, image: HpMemory): Instruction? {
        var size = 1
        var instrAddress = address
        var opcode = image[instrAddress] ?: return null
        while (opcode < 0x80) {
            instrAddress++

            when {
                opcode < 0x40 -> arp = opcode.toInt()
                else -> drp = opcode.toInt() and 0x3F
            }
            val nextOpcode = image[instrAddress] ?: return if (opcode < 0x80 && size == 1)
                Instruction(address, 1, address, ArpDrp(arp, drp), "ARP", OperandFormat.DrpArp)
            else
                Instruction(address, 1, address, ArpDrp(arp, drp), "DRP", OperandFormat.DrpArp)

            if (size == 1) {
                when {
                    (opcode in 0x00..0x3F) && (nextOpcode in 0x00..0x3F) ->
                        return Instruction(address, 1, address, ArpDrp(arp, drp), "ARP", OperandFormat.DrpArp)
                    (opcode in 0x40..0x7F) && (nextOpcode in 0x40..0x7F) ->
                        return Instruction(address, 1, address, ArpDrp(arp, drp), "DRP", OperandFormat.DrpArp)
                }
            }

            if (nextOpcode < 0x80 && size > 1)
                return when (opcode) {
                    in 0x00..0x3F -> Instruction(address, 1, address, ArpDrp(arp, drp), "DRP",
                        OperandFormat.DrpArp)
                    else -> Instruction(address, 1, address, ArpDrp(arp, drp), "ARP", OperandFormat.DrpArp)
                }
            size += 1
            opcode = nextOpcode
        }

        assert(opcode >= 0x80) { "Expecting an instruction Opcode but got ARP or DRP $opcode" }
        val instruction = instrMap[opcode.toInt()] ?: return null
        var lengthUnknown = false

        size = when (instruction.adrMode) {
            OperandFormat.Literal, OperandFormat.Sub, OperandFormat.SubIndex, OperandFormat.Index -> size + 2
            OperandFormat.ImmByte -> size + 1
            OperandFormat.ImmMulti -> when (drp) {
                null, 1 -> {
                    lengthUnknown = true
                    size
                }
                in 0x00..0x1F -> size + 2 - drp!!.toInt() and 0x01
                else -> size + 8 - drp!!.toInt() and 0x07
            }
            else -> size
        }
        val regPtrs = ArpDrp(arp, drp)

        val iSet = setOf("CMB", "CMBD", "CMM", "CMMD", "TSB", "TSM")
        val mSet = setOf(OperandFormat.Sub, OperandFormat.SubIndex, OperandFormat.DrAr, OperandFormat.Jmp,
            OperandFormat.None, OperandFormat.StackDown, OperandFormat.StackUp)
        with (instruction) {
            if (name == "JMP" || (drp == 4 && name !in iSet && adrMode !in mSet))  {
                arp = null
                drp = null
            }
        }
        return Instruction(address, size, instrAddress, regPtrs, instruction.name, instruction.adrMode, lengthUnknown)
    }
}

