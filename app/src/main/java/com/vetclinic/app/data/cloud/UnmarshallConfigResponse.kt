package com.vetclinic.app.data.cloud

import com.vetclinic.app.common.network.UnmarshallResponse
import com.vetclinic.app.data.cloud.exceptions.InvalidConfigDataException
import okhttp3.Response
import org.json.JSONObject

class UnmarshallConfigResponse : UnmarshallResponse<ConfigCloud> {
    override fun unmarshall(response: Response): ConfigCloud {
        try {
            val rootObject = JSONObject(response.body!!.string())
            val config = rootObject.getJSONObject("settings")
            return ConfigCloud(
                config.getBoolean("isChatEnabled"),
                config.getBoolean("isCallEnabled"),
                config.getString("workHours")
            )
        } catch (e: Exception) {
            throw InvalidConfigDataException(e)
        }
    }
}