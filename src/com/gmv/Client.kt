package com.gmv

import java.io.*
import java.net.Socket
import java.text.MessageFormat

const val SERVER_PORT = 9998
const val STX: Byte = 0x02
const val RS: Byte = 0x1E
const val ETX: Byte = 0x03

class Client {

    fun connect() {

        val socket = Socket("10.0.1.10", SERVER_PORT)

        val dataInputStream = DataInputStream(BufferedInputStream(socket.getInputStream()))
        val dataOutputStream = DataOutputStream(socket.getOutputStream())

        dataOutputStream.write(toBytes("X38.753218Y-9.167966"))

        val byteArrayOutputStream = ByteArrayOutputStream()
        while (!socket.isClosed) {
            try {
                when (val byteRead = dataInputStream.readByte()) {
                    STX -> byteArrayOutputStream.write(byteRead.toInt())
                    ETX -> {
                        byteArrayOutputStream.write(byteRead.toInt())
                        println(MessageFormat.format("Received new message: {0}", byteArrayOutputStream))
                        byteArrayOutputStream.reset()
                    }
                    else -> byteArrayOutputStream.write(byteRead.toInt())
                }
            } catch (e: IOException) {
                socket.close()
                println("${socket?.inetAddress?.hostAddress} closed the connection")
            }
        }
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