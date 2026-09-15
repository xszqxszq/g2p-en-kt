package xyz.xszq.g2p

import xyz.xszq.g2p.internal.Lexicon
import xyz.xszq.g2p.internal.PhonemeTree

/**
 * English grapheme to phoneme converter.
 *
 * Words present in the packaged CMUdict derived lexicon are looked up directly,
 * everything else goes through a decision tree trained on the same lexicon. The
 * symbols follow the SAMPA style inventory used for en_US.
 *
 * Instances are thread safe, the packaged data is read on first use.
 */
class EnglishPhonemizer : Phonemizer {
    private val lexicon = Lexicon()
    private val tree = PhonemeTree()

    override fun phonemize(word: String): List<String> {
        val key = word.lowercase()
        if (key.none { it in 'a'..'z' }) {
            return emptyList()
        }
        lexicon.pronunciations(key).firstOrNull()?.let { pronunciation ->
            return pronunciation.split(' ')
        }
        return tree.phonemize(key)
    }
}
