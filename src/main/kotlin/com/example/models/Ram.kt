package com.example.models

data class RamImage(val range: IntRange, val name: String, val image: List<Byte>)
class Ram {
    private val images = arrayListOf<RamImage>()

    operator fun get(index: Int): Byte? {
        for (image in images) {
            if (index in image.range) {
                return image.image[index - image.range.first]
            }
        }
        return null
    }
}

