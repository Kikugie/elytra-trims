package dev.kikugie.elytratrims.common.util

typealias ARGB = Int
typealias Channel = Int

val ARGB.red: Channel
    @JvmName("red") get() = (this and 0x00FF0000 ushr 16)
val ARGB.green: Channel
    @JvmName("green") get() = (this and 0x0000FF00 ushr 8)
val ARGB.blue: Channel
    @JvmName("blue") get() = (this and 0x000000FF)
val ARGB.alpha: Channel
    @JvmName("alpha") get() = (this and -0x1000000 ushr 24)
val Channel.scaled: Float
    @JvmName("scaled") get() = this / 255F
val ARGB.floatChannels
    @JvmName("floatChannels") get() = floatArrayOf(red.scaled, green.scaled, blue.scaled, alpha.scaled)
fun toARGB(red: Float, green: Float, blue: Float, alpha: Float) = floatArrayOf(red, green, blue, alpha).toARGB()
fun FloatArray.toARGB(): ARGB {
    val r = (this[0] * 255).toInt() and 0xFF
    val g = (this[1] * 255).toInt() and 0xFF
    val b = (this[2] * 255).toInt() and 0xFF
    val a = if (this.size == 4) {
        (this[3] * 255).toInt() and 0xFF
    } else {
        0xFF
    }
    return (a shl 24) or (r shl 16) or (g shl 8) or b
}

fun ARGB.withoutAlpha() = this and 0xFFFFFF
fun ARGB.withAlpha(alpha: Int) = withoutAlpha() or (alpha shl 24)