package cc.cryptopunks.astral.agent

import android.webkit.JavascriptInterface
import astral.Astral

object AppHostAdapter {
    private val client = Astral.newAppHostClient()

    @JavascriptInterface
    fun log(var1: String) = client.log(var1)

    @JavascriptInterface
    fun sleep(var1: Long) = client.sleep(var1)

    @JavascriptInterface
    fun connAccept(var1: String): String = client.connAccept(var1)

    @JavascriptInterface
    fun connClose(var1: String) = client.connClose(var1)

    @JavascriptInterface
    fun connRead(var1: String): String = client.connRead(var1)

    @JavascriptInterface
    fun connWrite(var1: String, var2: String) = client.connWrite(var1, var2)

    @JavascriptInterface
    fun query(var1: String, var2: String): String = client.query(var1, var2)

    @JavascriptInterface
    fun queryName(var1: String, var2: String): String = client.queryName(var1, var2)

    @JavascriptInterface
    fun resolve(var1: String): String = client.resolve(var1)

    @JavascriptInterface
    fun serviceExec(var1: String, var2: String) = client.serviceExec(var1, var2)

    @JavascriptInterface
    fun serviceClose(var1: String) = client.serviceClose(var1)

    @JavascriptInterface
    fun serviceRegister(var1: String) = client.serviceRegister(var1)

    @JavascriptInterface
    fun nodeInfo(var1: String): String = client.getNodeInfo(var1)?.run {
        """{"name":"$name","identity":"$identity"}"""
    } ?: "{}"
}
