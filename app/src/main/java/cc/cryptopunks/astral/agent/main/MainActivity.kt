package cc.cryptopunks.astral.agent.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.remember
import cc.cryptopunks.astral.agent.compose.ErrorViewModel
import cc.cryptopunks.astral.agent.js.JsAppsManager
import cc.cryptopunks.astral.agent.util.use
import java.lang.Exception

class MainActivity : ComponentActivity() {

    private val errors: ErrorViewModel by viewModels()

    private val askPermissions = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        tryStartAstralService()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            PackageManager.PERMISSION_GRANTED != checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
        ) {
            askPermissions.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        tryStartAstralService()
        setContent {
            MainScreen(
                errors = errors,
                jsAppsManager = remember { use<JsAppsManager.Provider>().jsAppsManager },
            )
        }
    }

    private fun tryStartAstralService() = try {
        startAstralService()
    } catch (e: Exception) {
        e.printStackTrace()
        errors.state += e
    }
}
