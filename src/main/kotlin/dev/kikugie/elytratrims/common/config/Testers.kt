package dev.kikugie.elytratrims.common.config

import dev.kikugie.elytratrims.common.ETCommon

interface Tester {
    fun test(): Boolean

    companion object {
        @JvmStatic
        fun runTest(klass: Class<*>): Boolean = (klass.kotlin.objectInstance as? Tester)?.test()
            ?: throw IllegalArgumentException("Tester class ${klass.simpleName} should be implemented")
    }
}


object TrimTester : Tester {
    override fun test() = ETCommon.config.addTrims
}

object PatternTester : Tester {
    override fun test() = ETCommon.config.addPatterns
}

object GlowTester : Tester {
    override fun test() = ETCommon.config.addGlow
}

object RequireClientTester : Tester {
    override fun test() = !ETCommon.config.requireClientSide
}