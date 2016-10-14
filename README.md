MinHash Library
=======================

## Overview

This library provides tools for b-bit MinHash algorism.

### Issues/Questions

Please file an [issue](https://github.com/codelibs/minhash/issues "issue").
(Japanese forum is [here](https://github.com/codelibs/codelibs-ja-forum "here").)

## Installation

### Maven

Put the following dependency into pom.xml:

```xml
<dependency>
  <groupId>org.codelibs</groupId>
  <artifactId>minhash</artifactId>
  <version>0.2.0</version>
</dependency>
```

## References

### Calculate MinHash

MinHash class provides tools to calculate MinHash.

```java
// Lucene's tokenizer parses a text.
Tokenizer tokenizer = ...;
// The number of bits for each hash value.
int hashBit = 1;
// A base seed for hash functions.
int seed = 0;
// The number of hash functions.
int num = 128;
// Analyzer for 1-bit 128 hash with default Tokenizer (WhitespaceTokenizer).
Analyzer analyzer = MinHash.createAnalyzer(hashBit, seed, num);
// Analyzer for 1-bit 128 hash with custom Tokenizer.
Analyzer analyzer2 = MinHash.createAnalyzer(tokenizer, hashBit, seed, num);

String text = "Fess is very powerful and easily deployable Enterprise Search Server.";

// Calculate a minhash value. The size is hashBit*num.
byte[] minhash = MinHash.calculate(analyzer, text);
```

### Compare Texts

compare method returns a similarity between texts.
The value is from 0 to 1.
But a value below 0.5 means different texts.

```java
String text1 = "Fess is very powerful and easily deployable Search Server.";
byte[] minhash1 = MinHash.calculate(analyzer, text1);
assertEquals(0.953125f, MinHash.compare(minhash, minhash1));

// Compare a different text.
String text2 = "Solr is the popular, blazing fast open source enterprise search platform";
byte[] minhash2 = MinHash.calculate(analyzer, text2);
assertEquals(0.453125f, MinHash.compare(minhash, minhash2));
```
