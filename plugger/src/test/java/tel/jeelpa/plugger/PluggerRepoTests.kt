package tel.jeelpa.plugger

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class PluggerRepoTests {

    @Test
    fun repoComposerShouldComposeMultipleRepositories(): Unit = runBlocking {
        val mockError1 = Error("Failed to load plugin 2")
        val repo1Set = listOf(
            Result.success(Pair(1, 1)),
            Result.failure(mockError1),
            Result.success(Pair(3, 3)),
        )
        val repo1 = MockPluginRepo(repo1Set)

        val mockError2 = Error("Failed to load plugin 5")

        val repo2set = listOf(
            Result.success(Pair(2, 2)),
            Result.success(Pair(4, 4)),
            Result.failure(mockError2),
        )
        val repo2 = MockPluginRepo(repo2set)

        val composed = RepoComposer(repo1, repo2)
        val allPlugins = composed.getAllPlugins().first()

        assertEquals(
            (repo1Set + repo2set).mapNotNull { it.getOrNull() }.toSet(),
            allPlugins.filter { it.isSuccess }.map { it.getOrThrow() }.toSet()
        )
        assertEquals(
            setOf(mockError1, mockError2),
            allPlugins.filter { it.isFailure }.mapNotNull { it.exceptionOrNull() }.toSet()
        )
    }

    @Test
    fun shouldReturnErrorsAsValues(): Unit = runBlocking {
        val mockError = Error("Failed to load plugin 2")
        val repoSet = listOf(
            Result.success(Pair(1, 1)),
            Result.failure(mockError),
            Result.success(Pair(3, 3)),
        )
        val repo = MockPluginRepo(repoSet)
        val allPlugins = repo.getAllPlugins().first()

        assertEquals(
            repoSet.mapNotNull { it.getOrNull() }.toSet(),
            allPlugins.filter { it.isSuccess }.map { it.getOrThrow() }.toSet()
        )
        assertEquals(
            setOf(mockError),
            allPlugins.filter { it.isFailure }.mapNotNull { it.exceptionOrNull() }.toSet()
        )
    }

}