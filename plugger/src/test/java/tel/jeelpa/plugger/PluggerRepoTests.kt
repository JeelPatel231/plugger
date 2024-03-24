package tel.jeelpa.plugger

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.assertEquals

class PluggerRepoTests {

    @Test
    fun repoComposerShouldComposeMultipleRepositories(): Unit = runBlocking {
        val mockError1 = Error("Failed to load plugin 2")
        val repo1 = MockPluginRepo(
            listOf(
                Result.success(1),
                Result.failure(mockError1),
                Result.success(3),
            )
        )

        val mockError2 = Error("Failed to load plugin 5")
        val repo2 = MockPluginRepo(
            listOf(
                Result.success(2),
                Result.success(4),
                Result.failure(mockError2),
            )
        )

        val composed = RepoComposer(repo1, repo2)
        val allPlugins = composed.getAllPlugins().first()

        assertEquals(
            setOf(1, 2, 3, 4),
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
        val repo = MockPluginRepo(
            listOf(
                Result.success(1),
                Result.failure(mockError),
                Result.success(3),
            )
        )
        val allPlugins = repo.getAllPlugins().first()

        assertEquals(
            setOf(1, 3),
            allPlugins.filter { it.isSuccess }.map { it.getOrThrow() }.toSet()
        )
        assertEquals(
            setOf(mockError),
            allPlugins.filter { it.isFailure }.mapNotNull { it.exceptionOrNull() }.toSet()
        )
    }

}