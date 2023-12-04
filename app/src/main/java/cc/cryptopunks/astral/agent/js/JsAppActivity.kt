package cc.cryptopunks.astral.agent.js

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.core.net.toFile
import cc.cryptopunks.astral.bind.astral.JsAppHostClient
import org.koin.android.ext.android.inject
import java.io.File

internal fun Context.startJsAppActivity(app: JsApp) {
    val dir = appsDir.resolve(app.dir)
    val file = dir.resolve("index.html")
    val uri = Uri.fromFile(file)
    val intent = Intent()
    val activity = "$packageName.js.JsAppActivity\$Id${app.activity}"
    intent.component = ComponentName(packageName, activity)
    intent.data = uri
    intent.putExtra("title", app.name)
    startActivity(intent)
}

sealed class JsAppActivity : ComponentActivity() {

    private val webView: WebView by lazy { WebView(this) }
    private val appHostClient: JsAppHostClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(webView)

        val uri = intent.data ?: return
        val dir = uri.toFile().parentFile ?: File("")
        title = intent.getStringExtra("title") ?: title

        webView.apply {
            webViewClient = FileResolvingWebViewClient(dir)
            webChromeClient = WebChromeClient()
            settings.apply {
                @SuppressLint("SetJavaScriptEnabled")
                javaScriptEnabled = true
                domStorageEnabled = true
                useWideViewPort = true
                allowFileAccess = true
                allowContentAccess = true
                domStorageEnabled = true
                databaseEnabled = true
                javaScriptCanOpenWindowsAutomatically = true
            }

            addJavascriptInterface(JsAppHostAdapter(webView, appHostClient), "_app_host")
            loadUrl(uri.toString())
        }

        onBackPressedDispatcher.addCallback(this) {
            if (webView.canGoBack()) {
                webView.goBack()
            } else {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
                isEnabled = true
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        appHostClient.close()
    }

    class Id0 : JsAppActivity()
    class Id1 : JsAppActivity()
    class Id2 : JsAppActivity()
    class Id3 : JsAppActivity()
    class Id4 : JsAppActivity()
    class Id5 : JsAppActivity()
    class Id6 : JsAppActivity()
    class Id7 : JsAppActivity()
    class Id8 : JsAppActivity()
    class Id9 : JsAppActivity()
    class Id10 : JsAppActivity()
    class Id11 : JsAppActivity()
    class Id12 : JsAppActivity()
    class Id13 : JsAppActivity()
    class Id14 : JsAppActivity()
    class Id15 : JsAppActivity()
    class Id16 : JsAppActivity()
    class Id17 : JsAppActivity()
    class Id18 : JsAppActivity()
    class Id19 : JsAppActivity()


    companion object {
        const val Limit = 20
    }
}

