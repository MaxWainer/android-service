package tservice.plugins;

import tservice.module

import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.server.testing.*
import kotlin.test.Test

class RoutingKtTest {

    @Test
    fun testGetUserByidId() = testApplication {
        application {
            module()
        }
        client.get("/user/byId/{id}").apply {

        }
    }
}