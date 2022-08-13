package com.vetclinic.app.ui.mock

import android.os.Handler
import android.os.Message

object MockHandler : Handler() {
    override fun sendMessageAtTime(msg: Message, uptimeMillis: Long): Boolean {
        msg.callback?.run()
        return true
    }
}