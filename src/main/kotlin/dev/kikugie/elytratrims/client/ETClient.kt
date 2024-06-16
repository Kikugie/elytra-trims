package dev.kikugie.elytratrims.client

import dev.kikugie.elytratrims.client.config.ETClientConfig

object ETClient {
    var config: ETClientConfig = ETClientConfig()
        private set

    fun init() {
        config = ETClientConfig.load()
    }
}