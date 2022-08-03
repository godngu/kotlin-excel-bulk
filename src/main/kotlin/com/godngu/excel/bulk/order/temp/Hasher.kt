package com.godngu.excel.bulk.order.temp

import java.security.MessageDigest

class Hasher {
    companion object {
        const val HEXADECIMAL = "%02x"

        private fun toSHA256(target: String): String = MessageDigest.getInstance("SHA-256").digest(target.toByteArray())
            .fold("") { str, it -> str + HEXADECIMAL.format(it) }

        private fun toSHA512(target: String): String = MessageDigest.getInstance("SHA-512").digest(target.toByteArray())
            .fold("") { str, it -> str + HEXADECIMAL.format(it) }

        fun convert(input: String, algorithm: ALGORITHM): String =
            when (algorithm) {
                ALGORITHM.SHA256 -> toSHA256(input)
                ALGORITHM.SHA512 -> toSHA512(input)
            }
    }

    enum class ALGORITHM {
        SHA256,
        SHA512,
    }
}
