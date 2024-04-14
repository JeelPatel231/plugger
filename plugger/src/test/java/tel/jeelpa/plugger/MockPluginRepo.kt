package tel.jeelpa.plugger

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MockPluginRepo<T, U>(
    private val mockResultList: List<Result<Pair<T,U>>>
) : PluginRepo<T, U> {
    override fun getAllPlugins(): StateFlow<List<Result<Pair<T, U>>>> {
        return MutableStateFlow(mockResultList).asStateFlow()
    }
}