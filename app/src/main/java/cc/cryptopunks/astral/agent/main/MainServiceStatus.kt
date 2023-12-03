package cc.cryptopunks.astral.agent.main

import cc.cryptopunks.astral.agent.api.ServiceStatus
import cc.cryptopunks.astral.agent.node.AstralStatus
import cc.cryptopunks.astral.agent.node.astralStatus
import cc.cryptopunks.astral.agent.warpdrive.WarpdriveStatus
import cc.cryptopunks.astral.agent.warpdrive.warpdriveStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MainServiceStatus : ServiceStatus {

    private val map = mapOf(
        "astral" to astralStatus.map { it == AstralStatus.Started },
        "warpdrive" to warpdriveStatus.map { it == WarpdriveStatus.Started },
    )

    override fun invoke(service: String): Flow<Boolean> = map.getValue(service)
}
