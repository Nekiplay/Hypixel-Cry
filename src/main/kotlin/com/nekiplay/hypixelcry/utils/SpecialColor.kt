package com.nekiplay.hypixelcry.utils

import java.awt.Color

object SpecialColor {
    private const val MIN_CHROMA_SECS = 1
    private const val MAX_CHROMA_SECS = 60
    private val startTime = System.currentTimeMillis()

    @JvmStatic
    fun String.toSpecialColor() = Color(toSpecialColorInt(), true)

    @JvmStatic
    fun String.toSpecialColorInt(): Int {
        val components = decompose(this)
        val chroma = components.getOrElse(0) { 0 }
        val alpha = components.getOrElse(1) { 255 }
        val red = components.getOrElse(2) { 0 }
        val green = components.getOrElse(3) { 0 }
        val blue = components.getOrElse(4) { 0 }

        val (hue, sat, bri) = Color.RGBtoHSB(red, green, blue, null)

        val adjustedHue = if (chroma > 0) (hue + ((System.currentTimeMillis() - startTime) / 1000f / chromaSpeed(chroma) % 1)).let {
            if (it < 0) it + 1f else it
        } else hue

        return (alpha and 0xFF) shl 24 or (Color.HSBtoRGB(adjustedHue, sat, bri) and 0x00FFFFFF)
    }

    @JvmStatic
    fun String.toSpecialColorIntNoAlpha(): Int {
        val components = decompose(this)
        val chroma = components.getOrElse(0) { 0 }
        val red = components.getOrElse(if (components.size == 4) 1 else 2) { 0 }
        val green = components.getOrElse(if (components.size == 4) 2 else 3) { 0 }
        val blue = components.getOrElse(if (components.size == 4) 3 else 4) { 0 }

        val (hue, sat, bri) = Color.RGBtoHSB(red, green, blue, null)

        val adjustedHue = if (chroma > 0) (hue + ((System.currentTimeMillis() - startTime) / 1000f / chromaSpeed(chroma) % 1)).let {
            if (it < 0) it + 1f else it
        } else hue

        return Color.HSBtoRGB(adjustedHue, sat, bri)
    }

    @JvmStatic
    fun String.toSpecialColorFloatArray(): FloatArray {
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