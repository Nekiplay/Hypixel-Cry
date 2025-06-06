package com.nekiplay.hypixelcry.utils

import java.awt.Color

object SpecialColor {
    private const val MIN_CHROMA_SECS = 1
    private const val MAX_CHROMA_SECS = 60
    private val startTime = SimpleTimeMark.now()

    @JvmStatic
    fun String.toSpecialColor() = Color(toSpecialColorInt(), true)

    @JvmStatic
    fun String.toSpecialColorInt(): Int {
        val (chroma, alpha, red, green, blue) = decompose(this)
        val (hue, sat, bri) = Color.RGBtoHSB(red, green, blue, null)

        val adjustedHue = if (chroma > 0) (hue + (startTime.passedSince().inWholeMilliseconds / 1000f / chromaSpeed(chroma) % 1)).let {
            if (it < 0) it + 1f else it
        } else hue

        return (alpha and 0xFF) shl 24 or (Color.HSBtoRGB(adjustedHue, sat, bri) and 0x00FFFFFF)
    }

    @JvmStatic
    fun String.toSpecialColorIntNoAlpha(): Int {
        val components = decompose(this)
        val (chroma, red, green, blue) = when (components.size) {
            4 -> components // chroma:r:g:b
            5 -> components.take(4) // chroma:alpha:r:g:b -> игнорируем alpha
            else -> intArrayOf(0, 0, 0, 0) // default
        }

        val (hue, sat, bri) = Color.RGBtoHSB(red, green, blue, null)

        val adjustedHue = if (chroma > 0) (hue + (startTime.passedSince().inWholeMilliseconds / 1000f / chromaSpeed(chroma) % 1)).let {
            if (it < 0) it + 1f else it
        } else hue

        return Color.HSBtoRGB(adjustedHue, sat, bri)
    }

    @JvmStatic
    fun String.toSpecialColorFloatArray(): FloatArray {
        if (this == "255:0:0:255") {
            return floatArrayOf(1.0f, 0.0f, 0.0f, 1.0f)
        }

        val colorInt = toSpecialColor()
        return floatArrayOf(
            colorInt.red / 255f,
            colorInt.green / 255f,
            colorInt.blue / 255f,
            colorInt.alpha / 255f
        )
    }

    private fun decompose(csv: String) = csv.split(":").mapNotNull { it.toIntOrNull() }.toIntArray()
    private fun chromaSpeed(speed: Int) = (255 - speed) / 254f * (MAX_CHROMA_SECS - MIN_CHROMA_SECS) + MIN_CHROMA_SECS
}