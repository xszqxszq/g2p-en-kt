# g2p-en-kt

g2p-en-kt is a pure Kotlin English grapheme-to-phoneme converter. It turns English words into phoneme sequences; the phoneme inventory follows the en_US set of [MaryTTS](https://marytts.github.io/). The pronunciation table and the decision tree ship inside the jar and there are no runtime dependencies.

## Features

- **Lexicon first**: 126,052 CMUdict entries, including forms such as `o'clock` and `mother-in-law`
- **Tree fallback**: a letter-context decision tree covers names and words outside the dictionary
- **SAMPA inventory**: the phone set used for en_US, as plain space separated symbols
- **No dependencies**: no third-party runtime libraries, about 1.1 MB of packaged data

## Install

```kotlin
// build.gradle.kts
dependencies {
    implementation("xyz.xszq:g2p-en-kt:1.0.0")
}
```

```groovy
// build.gradle
dependencies {
    implementation 'xyz.xszq:g2p-en-kt:1.0.0'
}
```

## Usage

```kotlin
import xyz.xszq.g2p.EnglishPhonemizer

val phonemizer = EnglishPhonemizer()

phonemizer.phonemize("hello")             // [h, @, l, @U]
phonemizer.phonemizeToString("hello")     // "h @ l @U"
phonemizer.phonemizeToString("Kotlin")    // "k A t l I n"
```

Input is case insensitive. Words without ASCII letters return an empty sequence.

## Data and license

- Code is released under the MIT license, see [LICENSE](LICENSE).
- The pronunciation table is derived from [CMUdict](https://github.com/cmusphinx/cmudict) under its BSD-2 license, reproduced in [licenses/CMUdict-BSD-2.txt](licenses/CMUdict-BSD-2.txt).
- The decision tree was trained on the same dictionary and contains no third-party code or model.
