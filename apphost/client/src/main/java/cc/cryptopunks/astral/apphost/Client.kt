package cc.cryptopunks.astral.apphost

interface AppHostClient {
    fun query(nodeId: String, query: String): Conn
    fun register(name: String): ApphostListener
    fun resolve(name: String): String
}

interface ApphostListener {
    operator fun next(): QueryData
}

interface QueryData {
    fun accept(): Conn
    fun caller(): String
    fun query(): String
    fun reject()
}

interface Conn {
    fun close()
    fun read(buff: ByteArray): Long
    fun readN(n: Long): ByteArray
    fun write(buff: ByteArray): Long
}
