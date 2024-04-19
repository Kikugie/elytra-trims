package dev.kikugie.elytratrims.client

import dev.kikugie.elytratrims.client.config.ETClientConfig

object ETClient {
    lateinit var config: ETClientConfig
        private set

    fun init() {
        config = ETClientConfig.load()
    }
}