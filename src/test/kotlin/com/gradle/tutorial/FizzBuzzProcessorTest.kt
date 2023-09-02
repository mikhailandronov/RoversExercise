package com.gradle.tutorial

import com.gradle.tutorial.FizzBuzzProcessor as fb
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class FizzBuzzProcessorTest {
    @Test
    fun `fizz buzz normal numbers`() {
        assertEquals("1", fb.convert(1))
        assertEquals("2", fb.convert(2))
    }

    @Test
    fun `fizz buzz three numbers`() {
        assertEquals("Fizz", fb.convert(3))
    }

    @Test
    fun `fizz buzz five numbers`() {
        assertEquals("Buzz", fb.convert(5))
    }

    @Test
    fun `fizz buzz three and five numbers`() {
        assertEquals("FizzBuzz", fb.convert(15))
    }
}