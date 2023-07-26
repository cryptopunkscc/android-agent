package cc.cryptopunks.astral.agent

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import astral.Astral
import java.io.File

fun Activity.startJsAppActivity(name: String) {
    val intent = Intent(this, AstralWebView::class.java)
    intent.putExtra("name", name)
    startActivity(intent)
}

class AstralWebView : Activity() {

    private val webView: WebView by lazy { WebView(this) }
    private val appHostClient by lazy { Astral.newAppHostClient() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(webView)

        val name = intent.getStringExtra("name") ?: return
        val dir = appsDir.resolve(name)
        val source = try {
            val index = dir.resolve("index.html")
            index.readText()
        } catch (e: Throwable) {
            e.printStackTrace()
            return
        }

        webView.apply {
            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(
                    view: WebView,
                    request: WebResourceRequest,
                ): WebResourceResponse? {
                    return when (request.url.scheme) {
                        "file" -> {
                            val path = request.url.path
                            println("requesting path: $path")
                            path ?: return null
                            val relative = if (path.startsWith('/')) path.drop(1) else path
                            val file = dir.resolve(relative)
                            println("resolved file: ${file.absolutePath}, exist: ${file.exists()}")
                            file.exists() || return null

                            val mimeType = file.mimeType
                            val encoding = "UTF-8"
                            val data = file.inputStream()

                            return WebResourceResponse(mimeType, encoding, data)
                        }

                        else -> super.shouldInterceptRequest(view, request)
                    }
                }
            }

            webChromeClient = WebChromeClient()
            settings.apply {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
            }

            addJavascriptInterface(AppHostAdapter(webView, appHostClient), "_app_host")
            loadUrl("file:///android_asset/apphost_android.js")
            loadDataWithBaseURL(
                "file:///android_asset/apphost.js",
                source,
                "html/text",
                "UTF-8",
                null
            )
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appHostClient.close()
    }
}

private val File.mimeType get() = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
