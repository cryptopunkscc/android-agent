package cc.cryptopunks.astral.agent

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startAstralService()
        setContent {
            val status by astralStatus.collectAsState()
            AstralTheme {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    Column {
                        Text(
                            text = "astral status: " + status.name
                        )
                        Button(onClick = {
                            startActivity(jsAppIntent("node_info.html"))
                        }) {
                            Text(text = "node info")
                        }
                        Button(onClick = {
                            startActivity(jsAppIntent("hello.html"))
                        }) {
                            Text(text = "basic example")
                        }
                        Button(onClick = {
                            startActivity(jsAppIntent("hello.rpc.html"))
                        }) {
                            Text(text = "rpc example")
                        }
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
