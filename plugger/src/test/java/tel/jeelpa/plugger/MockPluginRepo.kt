package tel.jeelpa.plugger

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MockPluginRepo<T, U>(
    private val mockResultList: List<Result<Pair<T,U>>>
) : PluginRepo<T, U> {
    override fun getAllPlugins(): Flow<List<Result<Pair<T,U>>>> {
        return flowOf(mockResultList)
    }
}