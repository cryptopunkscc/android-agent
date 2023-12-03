package cc.cryptopunks.astral.agent.main

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import cc.cryptopunks.astral.agent.api.inject
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val errors: MutableList<Throwable> by inject()

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
            MainScreen()
            inject.Errors()
        }
    }

    private fun tryStartAstralService() = try {
        startAstralService()
    } catch (e: Exception) {
        e.printStackTrace()
        errors += e
    }
}
