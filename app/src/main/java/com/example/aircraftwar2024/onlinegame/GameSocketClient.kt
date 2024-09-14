import kotlinx.coroutines.*
import java.io.*
import java.net.Socket

class GameSocketClient(private val serverAddress: String, private val serverPort: Int) {
    private var socket: Socket? = null
    private var outputStream: PrintWriter? = null
    private var inputStream: BufferedReader? = null

    suspend fun connect() = withContext(Dispatchers.IO) {
        try {
            socket = Socket(serverAddress, serverPort)
            outputStream = PrintWriter(socket!!.getOutputStream(), true)
            inputStream = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            println("Connected to server.")
        } catch (e: Exception) {
            println("Error connecting to server: ${e.message}")
            throw IOException("Failed to connect to server.", e)
        }
    }

    suspend fun sendScore(score: Int) = withContext(Dispatchers.IO) {
        try {
            outputStream?.println(score.toString()) // 发送分数为字符串
        } catch (e: Exception) {
            println("Error sending score: ${e.message}")
        }
    }

    suspend fun receiveScore(): Int? = withContext(Dispatchers.IO) {
        try {
            inputStream?.readLine()?.toIntOrNull() ?: return@withContext null // 读取并转换为Int
        } catch (e: Exception) {
            println("Error receiving score: ${e.message}")
            null
        }
    }

    suspend fun disconnect() = withContext(Dispatchers.IO) {
        inputStream?.close()
        outputStream?.close()
        socket?.close()
    }

    suspend fun waitForMatchStart()  = withContext(Dispatchers.IO) {
        while (true) {
            val response = inputStream?.readLine()
            response?.let {
                when (it) {
                    "match_success" -> {
                        println("Match started.")
                        return@withContext true // 匹配成功，返回true
                    }
                    // 可以添加更多处理其他消息的逻辑
                    else -> {}
                }
            }
            delay(500L) // 每隔500毫秒检查一次消息
        }
    }
    suspend fun getGameType() = withContext(Dispatchers.IO){
        val response = inputStream?.readLine()
        response?.let {
          return@withContext it.toInt()
        }
    }
    suspend fun sendReady() = withContext(Dispatchers.IO)
    {
        outputStream?.println("match_ready")
    }

}