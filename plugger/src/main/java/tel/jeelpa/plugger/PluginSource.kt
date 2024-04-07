package tel.jeelpa.plugger

import kotlinx.coroutines.flow.Flow

interface PluginSource<TSourceInput> {
    fun getSourceFiles() : Flow<List<TSourceInput>>
}