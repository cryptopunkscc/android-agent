package cc.cryptopunks.astral.agent.api

import kotlinx.coroutines.flow.Flow

fun interface ServiceStatus : (String) -> Flow<Boolean>
