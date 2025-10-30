package com.chililabs.giphysearch.data.mapper

import com.chililabs.giphysearch.data.remote.dto.GifDto
import com.chililabs.giphysearch.data.remote.dto.ImageData
import com.chililabs.giphysearch.data.remote.dto.Images
import org.junit.Test
import kotlin.test.assertEquals

class GifMapperTest {

    @Test
    fun `toDomain maps GifDto correctly`() {
        val dto = GifDto(
            id = "123",
            title = "Funny Cat",
            username = "catuser",
            rating = "g",
            images = Images(
                fixedWidth = ImageData("preview.gif", "200", "150"),
                original = ImageData("original.gif", "800", "600")
            ),
            importDatetime = "2024-01-01 12:00:00"
        )

        val domain = dto.toDomain()

        assertEquals("123", domain.id)
        assertEquals("Funny Cat", domain.title)
        assertEquals("catuser", domain.username)
        assertEquals("G", domain.rating)
        assertEquals("preview.gif", domain.previewUrl)
        assertEquals("original.gif", domain.originalUrl)
        assertEquals(800, domain.width)
        assertEquals(600, domain.height)
        assertEquals("2024-01-01 12:00:00", domain.importDatetime)
    }

    @Test
    fun `toDomain handles empty title`() {
        val dto = GifDto(
            id = "123",
            title = "",
            username = "user",
            rating = "g",
            images = Images(
                fixedWidth = ImageData("url", "100", "100"),
                original = ImageData("url", "200", "200")
            ),
            importDatetime = "2024-01-01"
        )

        val domain = dto.toDomain()

        assertEquals("Untitled", domain.title)
    }

    @Test
    fun `toDomain handles empty username`() {
        val dto = GifDto(
            id = "123",
            title = "Title",
            username = "",
            rating = "g",
            images = Images(
                fixedWidth = ImageData("url", "100", "100"),
                original = ImageData("url", "200", "200")
            ),
            importDatetime = "2024-01-01"
        )

        val domain = dto.toDomain()

        assertEquals("Unknown", domain.username)
    }

    @Test
    fun `toDomain converts rating to uppercase`() {
        val dto = GifDto(
            id = "123",
            title = "Title",
            username = "user",
            rating = "pg-13",
            images = Images(
                fixedWidth = ImageData("url", "100", "100"),
                original = ImageData("url", "200", "200")
            ),
            importDatetime = "2024-01-01"
        )

        val domain = dto.toDomain()

        assertEquals("PG-13", domain.rating)
    }

    @Test
    fun `toDomain handles invalid dimensions`() {
        val dto = GifDto(
            id = "123",
            title = "Title",
            username = "user",
            rating = "g",
            images = Images(
                fixedWidth = ImageData("url", "invalid", "invalid"),
                original = ImageData("url", "not a number", "also not a number")
            ),
            importDatetime = "2024-01-01"
        )

        val domain = dto.toDomain()

        assertEquals(0, domain.width)
        assertEquals(0, domain.height)
    }
}