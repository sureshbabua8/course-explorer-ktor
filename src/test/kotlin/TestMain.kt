package hello

import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.setBody
import io.ktor.server.testing.withTestApplication

class TestMain : StringSpec({
    "should retrieve root path properly" {
        withTestApplication(Application::viewCourse) {
            handleRequest(HttpMethod.Get, "/").apply {
                response.status() shouldBe HttpStatusCode.OK
                response.content shouldBe "Hello, world!"
            }
        }
    }
    "should accept post calculate request" {
        withTestApplication(Application::viewCourse) {
            handleRequest(HttpMethod.Post, "/calculate") {
                addHeader("content-type", "application/json")
                setBody("""
{
  "operation": "add",
  "first": 5,
  "second": 5
}
                """.trim())
            }.apply {
                response.status() shouldBe HttpStatusCode.OK
            }
        }
    }
    "should count routes properly" {
        withTestApplication(Application::viewCourse) {
            handleRequest(HttpMethod.Get, "/count/first").apply {
                response.status() shouldBe HttpStatusCode.OK
                response.content shouldBe "1"
            }
            handleRequest(HttpMethod.Get, "/count/second").apply {
                response.status() shouldBe HttpStatusCode.OK
                response.content shouldBe "1"
            }
            handleRequest(HttpMethod.Get, "/count/first").apply {
                response.status() shouldBe HttpStatusCode.OK
                response.content shouldBe "2"
            }
        }
    }
})
