package dev.kikugie.elytratrims.client.resource

typealias RGBA = Int
typealias Channel = Int

val RGBA.red: Channel
    @JvmName("red") get() = (this and 0x00FF0000 ushr 16)
val RGBA.green: Channel
    @JvmName("green") get() = (this and 0x0000FF00 ushr 8)
val RGBA.blue: Channel
    @JvmName("blue") get() = (this and 0x000000FF)
val RGBA.alpha: Channel
    @JvmName("alpha") get() = (this and -0x1000000 ushr 24)
val Channel.scaled: Float
    @JvmName("scaled") get() = this / 255F
val RGBA.floatChannels
    @JvmName("floatChannels") get() = floatArrayOf(red.scaled, green.scaled, blue.scaled, alpha.scaled)