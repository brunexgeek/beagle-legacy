# Beagle Compiler  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

An ahead-of-time compiler for [Beagle programming language](http://github.com/brunexgeek/beagle-docs). The language is designed to be general purpose, easier enough to prototype complex algorithms (e.g. machine learning and signal processing) and still provide a good execution performance.

## FAQ

**Q: Why another language? Don't we have enough?**

Beagle is a hobby project and I don't intend to create the next *&lt;insert a popular programming language here&gt;*. 

**Q: Why write the compiler in Java? Why not use language *X*?**

The current implementation is done in Java to make it easier to "translate" the code to Beagle in the future. This is only an intermediate implementation (bootstrap compiler) until Beagle is able to compile itself.

**Q: What's the compiler target?**

For now, this compiler generates ANSI C 99 code. You can generate native binaries using any C/C++ compiler. I also intend to target [LLVM assembly](https://llvm.org/docs/LangRef.html) and [WebAssembly](http://webassembly.org/) (directly or via LLVM) in the future.

**Q: Why you've used the class/feature *X* instead of *Y* to perform *Z*?**

I want to keep the compiler as simple as possible to make it easier to "translate" to Beagle later. That means I'll sacrifice performance by not using specialized Java features/classes.