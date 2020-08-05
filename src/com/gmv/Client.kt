package com.gmv

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.Socket
import java.text.MessageFormat

const val SERVER_PORT = 9998
const val STX: Byte = 0x02
const val RS: Byte = 0x1E
const val ETX: Byte = 0x03

class Client {

    fun connect() {

        val client = Socket("localhost", SERVER_PORT)
        client.outputStream.write(toBytes("X38.753218Y-9.167966"))
        client.close()
    }

    private fun toBytes(message: String): ByteArray? {

        try {
            ByteArrayOutputStream().use { byteArrayOutputStream ->
                byteArrayOutputStream.write(STX.toInt())
                byteArrayOutputStream.write(message.toByteArray())
                byteArrayOutputStream.write(ETX.toInt())
                return byteArrayOutputStream.toByteArray()
            }
        } catch (e: IOException) {
            println(MessageFormat.format("Error while processing message: {0}", e.message))
            return ByteArray(0)
        }
    }
}