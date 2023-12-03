package cc.cryptopunks.wrapdrive

import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class OffersRepositoryTest {

    private val peers: PeersRepository = mockk()
    private val client: WarpdriveClient = mockk()

    private val repository = OffersRepository(
        client = client,
        peers = peers,
        scope = CoroutineScope(Job()),
        warpdriveStatus = WarpdriveStatus { flowOf(true) },
    )

    @Before
    fun setUp() {
    }

    @Test
    fun getIncoming() = runBlocking {
        coEvery { client.listenOffers(any()) } returns emptyFlow()
        coEvery { client.listOffers(any()) } returns (0..10).map {
            Offer(peer = "$it", id = "$it")
        }
        coEvery { client.listenStatus(any()) } returns flow {
            while(isActive) {
                delay(100)
                emit(Status(id = "0", update = System.currentTimeMillis()))
            }
            println("listenStatus finish")
        }
        coEvery { peers.get(any()) } returns null

        repository.incoming.test {
            repeat(11) {
                println(awaitItem().size)
            }
            cancelAndConsumeRemainingEvents()
        }
        println("========================================================")
        delay(4000)
        println("========================================================")
        repository.incoming.test {
            repeat(11) {
                println("2: " + awaitItem().size)
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun getOutgoing() {
    }

    @Test
    fun download() {
    }
}
