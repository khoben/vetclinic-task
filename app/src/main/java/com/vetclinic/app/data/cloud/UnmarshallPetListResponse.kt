package com.vetclinic.app.data.cloud

import com.vetclinic.app.common.network.UnmarshallResponse
import com.vetclinic.app.data.cloud.exceptions.InvalidPetListDataException
import okhttp3.Response
import org.json.JSONObject

class UnmarshallPetListResponse : UnmarshallResponse<List<PetCloud>> {
    override fun unmarshall(response: Response): List<PetCloud> {
        try {
            val rootObject = JSONObject(response.body!!.string())
            val petList = rootObject.getJSONArray("pets")
            val result = ArrayList<PetCloud>(petList.length())
            repeat(petList.length()) {
                val pet = petList.getJSONObject(it)
                result.add(
                    PetCloud(
                        pet.getString("image_url"),
                        pet.getString("title"),
                        pet.getString("content_url"),
                        pet.getString("date_added")
                    )
                )
            }
            return result
        } catch (e: Exception) {
            throw InvalidPetListDataException(e)
        }
    }
}