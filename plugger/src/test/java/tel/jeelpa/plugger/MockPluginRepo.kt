package tel.jeelpa.plugger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockPluginRepo<T>(
    private val mockResultList: List<Result<T>>
) : PluginRepo<T> {
    override fun getAllPlugins(): Flow<List<Result<T>>> {
        return flowOf(mockResultList)
    }
}