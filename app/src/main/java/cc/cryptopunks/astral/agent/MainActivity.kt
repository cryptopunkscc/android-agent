package cc.cryptopunks.astral.agent

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import cc.cryptopunks.astral.agent.compose.Main
import cc.cryptopunks.astral.agent.compose.MainModel

class MainActivity : ComponentActivity() {

    private val askPermissions = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        try {
            startAstralService()
        } catch (e: Throwable) {
            e.printStackTrace()
            model.errors += e
        }
    }

    private val model: MainModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            PackageManager.PERMISSION_GRANTED != checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS)
        ) {
            askPermissions.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }

        try {
            startAstralService()
        } catch (e: Throwable) {
            e.printStackTrace()
            model.errors += e
        }
        setContent {
            Main(
                jsAppsManager = jsAppsManager,
                model = model,
            )
        }
    }
}
