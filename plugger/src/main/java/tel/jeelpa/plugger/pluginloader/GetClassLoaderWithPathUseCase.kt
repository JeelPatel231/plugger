package tel.jeelpa.plugger.pluginloader

import dalvik.system.DexClassLoader

class GetClassLoaderWithPathUseCase(
    private val parentClassLoader: ClassLoader,
    private val optimizedDirectory: String? = null
) {
    fun getWithPath(path: String): DexClassLoader {
        return DexClassLoader(
            path,
            optimizedDirectory,
            null,
            parentClassLoader
        )
    }
}