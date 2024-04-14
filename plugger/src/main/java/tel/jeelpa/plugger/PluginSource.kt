package tel.jeelpa.plugger

import kotlinx.coroutines.flow.StateFlow

interface PluginSource<TSourceInput> {
    fun getSourceFiles() : StateFlow<List<TSourceInput>>
}