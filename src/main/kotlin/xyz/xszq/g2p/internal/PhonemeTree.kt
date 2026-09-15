package xyz.xszq.g2p.internal

import java.util.zip.GZIPInputStream

/**
 * Decision tree predicting phoneme chunks from a letter context.
 *
 * The feature vector follows the training script: seven letters around the
 * current position, the position itself, the distance to the word end, the
 * capped word length and the phoneme predicted for the previous letter.
 *
 * @property resource classpath location of the exported tree
 */
internal class PhonemeTree(
    private val resource: String = "/tree.tsv.gz"
) {
    private val model: Model by lazy { load() }

    /**
     * Convert a word with the tree.
     *
     * @param word lowercase word
     * @return phoneme symbols
     */
    fun phonemize(word: String): List<String> {
        val model = model
        val phones = ArrayList<String>(word.length + 2)
        var previous = model.startId
        for (index in word.indices) {
            val chunk = model.predict(model.features(word, index, previous))
            if (chunk == SILENT) {
                continue
            }
            val symbols = chunk.split(' ')
            phones += symbols
            previous = model.phoneIds[symbols.last()] ?: previous
        }
        return phones
    }

    private fun load(): Model {
        val stream = PhonemeTree::class.java.getResourceAsStream(resource)
            ?: error("Missing resource $resource")
        var alphabet: List<String> = emptyList()
        var phoneTable: List<String> = emptyList()
        var classes: List<String> = emptyList()
        val feature = ArrayList<Int>()
        val threshold = ArrayList<Double>()
        val left = ArrayList<Int>()
        val right = ArrayList<Int>()
        val leaf = ArrayList<Int>()
        GZIPInputStream(stream).bufferedReader().useLines { lines ->
            lines.forEach { line ->
                when {
                    line.startsWith("#alphabet\t") ->
                        alphabet = line.substring(10).split('\t')
                    line.startsWith("#phones\t") ->
                        phoneTable = line.substring(8).split('\t')
                    line.startsWith("#classes\t") ->
                        classes = line.substring(9).split('\t')
                    line.startsWith("#nodes\t") -> Unit
                    line.isEmpty() -> Unit
                    else -> {
                        val parts = line.split('\t')
                        feature += parts[1].toInt()
                        threshold += parts[2].toDouble()
                        left += parts[3].toInt()
                        right += parts[4].toInt()
                        leaf += parts[5].toInt()
                    }
                }
            }
        }
        return Model(alphabet, phoneTable, classes, feature, threshold, left, right, leaf)
    }

    private class Model(
        alphabet: List<String>,
        phones: List<String>,
        private val classes: List<String>,
        private val feature: List<Int>,
        private val threshold: List<Double>,
        private val left: List<Int>,
        private val right: List<Int>,
        private val leaf: List<Int>
    ) {
        private val letterIds = alphabet.withIndex().associate { (index, symbol) -> symbol.single() to index }
        val phoneIds = phones.withIndex().associate { (index, symbol) -> symbol to index }
        private val padding = letterIds['_'] ?: 0
        val startId = phoneIds[START] ?: 0

        /**
         * Walk the tree for one letter.
         *
         * @param values feature vector
         * @return predicted chunk, [SILENT] when the letter maps to no phoneme
         */
        fun predict(values: IntArray): String {
            var node = 0
            while (feature[node] >= 0) {
                node = if (values[feature[node]] <= threshold[node]) left[node] else right[node]
            }
            return classes[leaf[node]]
        }

        /**
         * Build the feature vector of one letter.
         *
         * @param word lowercase word
         * @param index letter index
         * @param previous phoneme id of the previous letter
         * @return feature vector
         */
        fun features(word: String, index: Int, previous: Int): IntArray {
            val values = IntArray(FEATURE_COUNT)
            for (offset in -CONTEXT..CONTEXT) {
                val position = index + offset
                val symbol = if (position in word.indices) word[position] else '_'
                values[offset + CONTEXT] = letterIds[symbol] ?: padding
            }
            values[7] = minOf(index, 9)
            values[8] = minOf(word.length - 1 - index, 9)
            values[9] = minOf(word.length, 18)
            values[10] = previous
            return values
        }
    }

    private companion object {
        const val CONTEXT = 3
        const val FEATURE_COUNT = CONTEXT * 2 + 5
        const val SILENT = "null"
        const val START = "<s>"
    }
}
