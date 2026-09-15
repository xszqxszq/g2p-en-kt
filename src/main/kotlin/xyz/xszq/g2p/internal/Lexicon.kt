package xyz.xszq.g2p.internal

import java.util.zip.GZIPInputStream

/**
 * Pronunciation table derived from CMUdict.
 *
 * @property resource classpath location of the tab separated table
 */
internal class Lexicon(
    private val resource: String = "/lexicon.tsv.gz"
) {
    private val entries: Map<String, String> by lazy { load() }

    /**
     * Look up every pronunciation variant of a word.
     *
     * @param word lowercase word
     * @return pronunciation variants in dictionary order
     */
    fun pronunciations(word: String): List<String> =
        entries[word]?.split('\t') ?: emptyList()

    private fun load(): Map<String, String> {
        val stream = Lexicon::class.java.getResourceAsStream(resource)
            ?: error("Missing resource $resource")
        val map = HashMap<String, String>(1 shl 18)
        GZIPInputStream(stream).bufferedReader().useLines { lines ->
            lines.forEach { line ->
                val tab = line.indexOf('\t')
                if (tab > 0) {
                    map[line.substring(0, tab)] = line.substring(tab + 1)
                }
            }
        }
        return map
    }
}
