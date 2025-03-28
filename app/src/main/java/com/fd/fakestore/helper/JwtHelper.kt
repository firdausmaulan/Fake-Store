package com.fd.fakestore.helper

import android.util.Base64
import org.json.JSONObject

object JwtHelper {

    fun decodeJwt(token: String): JSONObject? {
        try {
            val parts = token.split(".")
            if (parts.size != 3) {
                return null // Invalid token format
            }

            val payload = parts[1]
            val decodedPayload = decodeBase64(payload)

            return JSONObject(decodedPayload)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun decodeBase64(encoded: String): String {
        val decodedBytes = Base64.decode(encoded, Base64.URL_SAFE)
        return String(decodedBytes)
    }

    fun getClaim(token: String, claimName: String): Any? {
        val decoded = decodeJwt(token)
        return decoded?.opt(claimName)
    }

    fun getSubject(token: String): Int? {
        val subject = getClaim(token, "sub")
        return subject as? Int
    }

    fun getUsername(token: String): String? {
        val username = getClaim(token, "user")
        return username as? String
    }

    fun getIssuedAt(token: String): Long? {
        val issuedAt = getClaim(token, "iat")
        return issuedAt as? Long
    }
}