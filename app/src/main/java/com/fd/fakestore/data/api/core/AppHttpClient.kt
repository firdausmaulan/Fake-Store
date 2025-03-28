package com.fd.fakestore.data.api.core

import com.fd.fakestore.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import java.io.IOException

object AppHttpClient {

    const val BASE_URL = BuildConfig.BASE_URL

    val httpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    suspend inline fun <reified T> get(endpoint: String, params: Map<String, Any> = emptyMap()): Result<T> {
        return try {
            val response: HttpResponse = httpClient.get("$BASE_URL$endpoint") {
                params.forEach { (key, value) ->
                    parameter(key, value)
                }
            }

            if (response.contentType().toString().contains("application/json")) {
                val result: T = response.body()
                Result.success(result)
            } else {
                val errorString: String = response.body()
                Result.failure(IOException(errorString))
            }

        } catch (e: RedirectResponseException) {
            Result.failure(IOException("Redirect error"))
        } catch (e: ClientRequestException) {
            Result.failure(IOException("Client error"))
        } catch (e: ServerResponseException) {
            Result.failure(IOException("Server error"))
        } catch (e: IOException) {
            Result.failure(IOException("Network error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend inline fun <reified T, reified R> post(endpoint: String, requestBody: R): Result<T> {
        return try {
            val response: HttpResponse = httpClient.post("$BASE_URL$endpoint") {
                contentType(ContentType.Application.Json)
                setBody(requestBody)
            }

            if (response.contentType().toString().contains("application/json")) {
                val result: T = response.body()
                Result.success(result)
            } else {
                val errorString: String = response.body()
                Result.failure(IOException(errorString))
            }

        } catch (e: RedirectResponseException) {
            Result.failure(IOException("Redirect error"))
        } catch (e: ClientRequestException) {
            Result.failure(IOException("Client error"))
        } catch (e: ServerResponseException) {
            Result.failure(IOException("Server error"))
        } catch (e: IOException) {
            Result.failure(IOException("Network error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}