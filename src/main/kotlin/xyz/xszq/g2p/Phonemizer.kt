package xyz.xszq.g2p

/**
 * Grapheme to phoneme converter.
 */
interface Phonemizer {
    /**
     * Convert a single word into its phoneme sequence.
     *
     * @param word word to convert
     * @return phoneme symbols, empty when the word holds nothing pronounceable
     */
    fun phonemize(word: String): List<String>

    /**
     * Convert a single word into a phoneme string.
     *
     * @param word word to convert
     * @param separator separator placed between phoneme symbols
     * @return phoneme symbols joined by the separator
     */
    fun phonemizeToString(word: String, separator: String = " "): String =
        phonemize(word).joinToString(separator)
}
