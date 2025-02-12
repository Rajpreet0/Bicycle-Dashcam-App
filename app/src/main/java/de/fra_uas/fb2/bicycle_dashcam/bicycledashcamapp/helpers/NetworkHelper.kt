package de.fra_uas.fb2.bicycle_dashcam.bicycledashcamapp.helpers

import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okio.IOException

class NetworkHelper {

    private val client = OkHttpClient()


    //TODO: Datatype Birthday is not correctly formated
    fun register(lastname: String,
                 firstname: String,
                 gender: String,
                 birthday: String,
                 birthplace: String,
                 birthcountry: String,
                 address: String,
                 telephone_number: String,
                 email: String,
                 password: String): JsonObject {

        val json = JsonObject().apply {
            addProperty("lastname", lastname);
            addProperty("firstname", firstname);
            addProperty("gender", gender);
            addProperty("birthday", birthday);
            addProperty("birthplace", birthplace);
            addProperty("birthcountry", birthcountry);
            addProperty("address", address);
            addProperty("telephone_number", telephone_number);
            addProperty("email", email);
            addProperty("password", password);
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_ADDRESS_REGISTER)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body!!.string()
            return Gson().fromJson(responseData, JsonObject::class.java)
        }

    }

    fun login(email: String, password: String): JsonObject {

        val json = JsonObject().apply {
            addProperty("email", email)
            addProperty("password", password)
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_ADDRESS_LOGIN)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body!!.string()
            return Gson().fromJson(responseData, JsonObject::class.java)
        }
    }

    fun deleteAccount(id: String): JsonObject{
        val json = JsonObject().apply {
            addProperty("id", id)
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_ADDRESS_DELETE_ACCOUNT)
            .delete(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body!!.string()
            return Gson().fromJson(responseData, JsonObject::class.java)
        }
    }

    fun updateUserData(
        id: String,
        lastname: String,
        firstname: String,
        gender: String,
        birthday: String,
        birthplace: String,
        birthcountry: String,
        address: String,
        telephoneNumber: String,
        email: String
    ): JsonObject{
        val json = JsonObject().apply {
            addProperty("id", id)
            addProperty("lastname", lastname)
            addProperty("firstname", firstname)
            addProperty("gender", gender)
            addProperty("birthday", birthday)
            addProperty("birthplace", birthplace)
            addProperty("birthcountry", birthcountry)
            addProperty("address", address)
            addProperty("telephone_number", telephoneNumber)
            addProperty("email", email)
        }.toString()

        val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
        val requestBody = json.toRequestBody(mediaType)

        val request = Request.Builder()
            .url(SERVER_ADDRESS_UPDATE_USER)
            .put(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            val responseData = response.body!!.string()
            return Gson().fromJson(responseData, JsonObject::class.java)
        }
    }

    companion object {
        //const val BASE_URL="http://10.0.2.2:5000"  // development - for Emulator
        const val BASE_URL="https://bicycle-dashcam-server.vercel.app"
        const val SERVER_ADDRESS_LOGIN="$BASE_URL/login"
        const val SERVER_ADDRESS_REGISTER="$BASE_URL/register"
        const val SERVER_ADDRESS_DELETE_ACCOUNT="$BASE_URL/delete_user"
        const val SERVER_ADDRESS_UPDATE_USER="$BASE_URL/update_user"
    }
}