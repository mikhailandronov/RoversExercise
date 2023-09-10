package com.ma.rovers.tests

import com.ma.rovers.domain.*
import com.ma.rovers.domain.Field
import com.ma.rovers.repositories.IFieldsRepository
import com.ma.rovers.repositories.InMemoryFieldsRepository

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class InMemoryRepositoryTests {
    @Test
    fun `Save, restore and delete field`() { // Сохранить плато и все, что на нем
        // Given
        val length = 10
        val width = 20
        val field: IField = Field(
            IntSize(length),
            IntSize(width)
        )

        val repository: IFieldsRepository = InMemoryFieldsRepository()

        // When
        assertThrows<NoSuchElementException>("Restore before save: exception should be thrown")
        {repository.restore()}

        repository.save(field)

        // Then
        assertEquals(
            length, field.length.size,
            "Object is changed when saved to repository"
        )

        // When
        val restoredField = repository.restore()

        // Then
        assertAll("field properties",
            {assertEquals(field.length.size, restoredField.length.size, "Wrong length")},
            {assertEquals(field.width.size, restoredField.width.size, "Wrong width")},
            {
                for (x in 0..<length)
                    for (y in 0..<width) {
                        val initialCell = field.cell(Point(x, y))
                        val restoredCell = restoredField.cell(Point(x, y))
                        assertEquals(initialCell.locatedObject?.location,
                            restoredCell.locatedObject?.location,
                            "Wrong located object on cell [$x, $y]")
                    }
            }
        )

        // When
        repository.delete()

        // Then
        assertThrows<NoSuchElementException>("Restore after delete: exception should be thrown")
        {repository.restore()}

    }
}