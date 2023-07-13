package cc.cryptopunks.astral.agent

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.filter

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAstralService()
        setContent {
            val status by astralStatus.collectAsState()
            var goja by remember { mutableStateOf("unknown") }
            AstralTheme {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column {
                        Text(
                            text = "astral status: " + status.name
                        )
                        Text(
                            text = "goja status: $goja"
                        )
                    }
                }
            }
            LaunchedEffect(Unit) {
                astralStatus.filter { it == AstralStatus.Started }.collect {
                    goja = "copying"
                    try {
                        copyGojaBackend()
                        goja = "copied"
                        AppHostAdapter.run {
                            val id = resolve("localnode")
                            println("start service with identity: $id")
//                            serviceExec(id, dataDir.resolve(GojaBackendFilepath).absolutePath)
                            val dir = dataDir.resolve("bin")
                            val file = dir.resolve(GojaBackendFilename)
                            if (file.exists().not()) throw Exception(file.absolutePath + " not exist")
//                            ProcessRunner.run(dir.absolutePath, "$GojaBackendFilename app.js")
                            ProcessRunner.run(dir.absolutePath, "ls -lah")
                        }
                        startActivity(Intent(this@MainActivity, AstralWebView::class.java))
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        goja = e.localizedMessage ?: "error"
                    }
                }
            }
        }
    }
}


@Composable
fun AstralTheme(
    content: @Composable () -> Unit,
) = MaterialTheme(
    colors = lightColors(
        primary = Color(0xDE000000),
        primaryVariant = Color(0xFF000000),
    ),
    content = content
)
