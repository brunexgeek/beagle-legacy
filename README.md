# Beagle Compiler  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

An ahead-of-time compiler for [Beagle programming language](http://github.com/brunexgeek/beagle-docs).

## FAQ

**Q:** Why another language? Don't we have enough?
Beagle is a hobby project and I don't intend to solve one of the world's problems with it. That said, the language is designed to be general purpose and easier enough to prototype complex algorithms (e.g. machine learning and signal processing) and yet offer a good running performance.

**Q:** Why write the compiler in Java?
The current implementation is made in Java to became easier to "translate" the code for Beagle in the future. This is just an intermediary implementation until Beagle is able to compile itself.

**Q:** Whats the compiler target?
For now, this compiler generates [LLVM assembly](https://llvm.org/docs/LangRef.html) in text mode. You can generate native binaries using LLVM `llc` tool. I also intend to target [WebAssembly](http://webassembly.org/) (directly or via LLVM).

**Q**: Why you've used the class *X* instead of *Y* to perform *Z*?
I want to keep the compiler as simple as possible to make it easier to "translate" to Beagle later. That means I'll sacrifice performance by not using specialized Java features/classes.