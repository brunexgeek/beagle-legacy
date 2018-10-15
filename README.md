# Beagle Compiler  [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

An ahead-of-time compiler for [Beagle programming language](http://github.com/brunexgeek/beagle-docs). The language is designed to be general purpose, easier enough to prototype complex algorithms (e.g. machine learning and signal processing) and still provide a good execution performance.

The code is being implemented in a incremental fashion. I want to get a minimal viable product just to be able to compile Beagle itself. Then the Java code will be *translated* to Beagle and the compiler will be refined until a production ready version is achieved.

For now, this compiler generates ANSI C99 code from Beagle sources. You can generate native binaries using any C/C++ compiler. I also intend to target [LLVM assembly](https://llvm.org/docs/LangRef.html) and [WebAssembly](http://webassembly.org/) in the future.

Status of the main components:
* **Syntactic analysis**: almost complete, including AST generation
* **Semantic analysis**: basic type inference/checking for variables, parameters and functions
* **Code generation**: basic C99 code generation (missing function bodies)
* **Error handling**: basic error messages including line information (need to improve)
* **Garbage collection**: not implemented yet (the first version will probably use reference counting)

Features I'm planning do implement:
* Binary meta-information, to be used by the reflection mechanism and to enable importing (like using shared libraries, but without the need for header files)
* Introduce object orientation
* Real world garbage collection


## FAQ

**Q: Why another language? Don't we have enough?**

Beagle is a hobby project and I don't intend to create the next *&lt;insert a popular programming language here&gt;*. The main goal here is to practice compiler development and to experiment some concepts.

**Q: Why write the compiler in Java? Why not use language *X*?**

The current implementation is done in Java to make it easier to "translate" the code to Beagle in the future. This is only an intermediate implementation (bootstrap compiler) until Beagle is able to compile itself.

**Q: Why you've used the class/feature *X* instead of *Y* to perform *Z*?**

I want to keep the compiler as simple as possible to make it easier to "translate" to Beagle later. That means I'll sacrifice performance by not using specialized Java features/classes.