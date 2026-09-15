package xyz.xszq.g2p

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Words outside the packaged lexicon, checked against the expected phonemes.
 */
class PhonemeTreeTest {
    private val phonemizer = EnglishPhonemizer()

    @Test
    fun shouldCoverUnknownWords() {
        var checked = 0
        var failed = 0
        val examples = StringBuilder()
        EnglishPhonemizer::class.java.getResourceAsStream("/tree-cases.tsv")!!
            .bufferedReader().forEachLine { line ->
                val tab = line.indexOf('\t')
                if (tab <= 0) return@forEachLine
                val word = line.substring(0, tab)
                val expected = line.substring(tab + 1)
                val actual = phonemizer.phonemizeToString(word)
                checked++
                if (actual != expected) {
                    failed++
                    if (failed <= 5) examples.append("\n  $word: $actual != $expected")
                }
            }
        assertEquals(2000, checked)
        assertEquals(0, failed, "mismatches:$examples")
    }
}
