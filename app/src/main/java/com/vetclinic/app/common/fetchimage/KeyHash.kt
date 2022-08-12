package com.vetclinic.app.common.fetchimage

import java.math.BigInteger
import java.security.MessageDigest

interface KeyHash {
    fun hash(key: String): String

    class MD5 : KeyHash {
        private val instance = MessageDigest.getInstance("MD5")

        override fun hash(key: String): String {
            return BigInteger(
                1,
                instance.digest(key.toByteArray())
            ).toString(16).padStart(32, '0')
        }
    }

    class Empty : KeyHash {
        override fun hash(key: String): String = key
    }
}