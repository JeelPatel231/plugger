package tel.jeelpa.plugger


interface ManifestParser<TInputData, TMetadata> {
    fun parseManifest(data: TInputData): TMetadata
}

