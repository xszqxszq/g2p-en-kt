package xyz.xszq.g2p

import kotlin.test.Test
import kotlin.test.assertEquals

class EnglishPhonemizerTest {
    private val phonemizer = EnglishPhonemizer()

    @Test
    fun shouldLookUpPackagedLexicon() {
        assertEquals("h @ l @U", phonemizer.phonemizeToString("hello"))
        assertEquals("w V n", phonemizer.phonemizeToString("one"))
        assertEquals("tS r= tS", phonemizer.phonemizeToString("church"))
        assertEquals("@ k l A k", phonemizer.phonemizeToString("o'clock"))
        assertEquals("m V D r= I n l O", phonemizer.phonemizeToString("mother-in-law"))
    }

    @Test
    fun shouldIgnoreCase() {
        assertEquals(
            phonemizer.phonemizeToString("record"),
            phonemizer.phonemizeToString("RECORD")
        )
    }

    @Test
    fun shouldFallBackToTree() {
        assertEquals("k A t l I n", phonemizer.phonemizeToString("Kotlin"))
    }

    @Test
    fun shouldReturnNothingForUnpronounceableInput() {
        assertEquals(emptyList(), phonemizer.phonemize(""))
        assertEquals(emptyList(), phonemizer.phonemize("123"))
        assertEquals(emptyList(), phonemizer.phonemize("---"))
    }
}
