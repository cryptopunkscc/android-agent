package cc.cryptopunks.astral.agent.compose

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.ViewModel
import java.lang.Exception

class ErrorViewModel : ViewModel() {
    val state = mutableStateListOf<Throwable>()
}

infix fun MutableList<Throwable>.catch(block: () -> Unit) {
    try {
        block()
    } catch (e: Exception) {
        e.printStackTrace()
        this += e
    }
}

@Preview
@Composable
private fun ErrorsPreview() = AstralTheme {
    val message = "Test error preview, with some long description, that will overflow in dialog, so the UI can be adjusted."
    Errors(errors = mutableListOf(Throwable(message)))
}

@Composable
fun Errors(
    errors: MutableList<Throwable>,
) {
    errors.forEachIndexed { index, err ->
        err.printStackTrace()
        val drop: () -> Unit = {
            errors.removeAt(index)
        }
        var stacktrace by remember(err) {
            mutableStateOf(false)
        }
        val clipboard = LocalClipboardManager.current
        Dialog(drop) {
            Surface(
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        text = "Unexpected Error",
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
                        style = MaterialTheme.typography.h5,
                    )
                    err.message?.let { message ->
                        Text(
                            text = message,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(16.dp),
                            style = MaterialTheme.typography.subtitle1,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(onClick = {
                            stacktrace = !stacktrace
                        }) {
                            Text(text = "trace")
                        }
                        Button(onClick = {
                            val string = err.stackTraceToString()
                            clipboard.setText(AnnotatedString(string))
                        }) {
                            Text(text = "copy")
                        }
                        Button(onClick = drop) {
                            Text(text = "close")
                        }
                    }
                }
            }
        }
        if (stacktrace) StackTrace(err = err) {
            stacktrace = false
        }
    }
}

@Preview
@Composable
private fun StackTracePreview() = AstralTheme {
    val message = "Test error preview, with some long description, that will overflow in dialog, so the UI can be adjusted."
    StackTrace(err = Throwable(message))
}

@Composable
private fun StackTrace(
    err: Throwable,
    dismiss: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = dismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = dismiss) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "cancel")
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    modifier = Modifier
                        .padding(bottom = 96.dp)
                        .horizontalScroll(rememberScrollState()),
                    text = err.stackTraceToString(),
                    fontSize = 10.sp,
                )
            }
        }
    }
}
