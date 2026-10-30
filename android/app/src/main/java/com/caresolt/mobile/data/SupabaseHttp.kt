package com.caresolt.mobile.data

import com.caresolt.mobile.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class ApiException(val code: Int, message: String) : Exception(message)

/** Minimal Supabase REST client: publishable key + user JWT, one automatic refresh on 401. */
class SupabaseHttp(private val store: SessionStore) {
    val json = Json { ignoreUnknownKeys = true; encodeDefaults = true }
    private val client = OkHttpClient.Builder().callTimeout(30, TimeUnit.SECONDS).build()
    private val mt = "application/json".toMediaType()

    suspend fun request(
        method: String, path: String, query: Map<String, String> = emptyMap(),
        body: String? = null, anon: Boolean = false, retry: Boolean = true
    ): String = withContext(Dispatchers.IO) {
        val url = (BuildConfig.SUPABASE_URL + path).toHttpUrl().newBuilder()
            .apply { query.forEach { (k, v) -> addQueryParameter(k, v) } }.build()
        val token = if (anon) BuildConfig.SUPABASE_PUBLISHABLE_KEY else (store.accessToken ?: BuildConfig.SUPABASE_PUBLISHABLE_KEY)
        val req = Request.Builder().url(url)
            .header("apikey", BuildConfig.SUPABASE_PUBLISHABLE_KEY)
            .header("Authorization", "Bearer $token")
            .header("Content-Type", "application/json")
            .method(method, if (method == "GET") null else (body ?: "{}").toRequestBody(mt))
            .build()
        client.newCall(req).execute().use { r ->
            val text = r.body?.string().orEmpty()
            if (r.code == 401 && retry && !anon && store.refreshToken != null && refresh()) {
                return@withContext request(method, path, query, body, anon, retry = false)
            }
            if (!r.isSuccessful) throw ApiException(r.code, errorOf(text, r.code))
            text
        }
    }

    private fun errorOf(text: String, code: Int): String = runCatching {
        val o = json.parseToJsonElement(text).jsonObject
        listOf("msg", "message", "error_description", "error").firstNotNullOfOrNull { o[it]?.jsonPrimitive?.contentOrNull }
    }.getOrNull() ?: "Request failed ($code)"

    private suspend fun refresh(): Boolean = runCatching {
        val res = request("POST", "/auth/v1/token", mapOf("grant_type" to "refresh_token"),
            buildJsonObject { put("refresh_token", store.refreshToken) }.toString(), anon = true, retry = false)
        val s = json.decodeFromString<AuthSession>(res)
        store.accessToken = s.accessToken; store.refreshToken = s.refreshToken; true
    }.getOrElse { store.clearSession(); false }

    suspend inline fun <reified T> get(path: String, query: Map<String, String> = emptyMap()): T =
        json.decodeFromString(request("GET", path, query))

    suspend inline fun <reified T> rpc(fn: String, args: JsonObject): T =
        json.decodeFromString(request("POST", "/rest/v1/rpc/$fn", body = args.toString()))
}
