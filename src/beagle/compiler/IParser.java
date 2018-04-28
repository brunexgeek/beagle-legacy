package beagle.compiler;

import beagle.compiler.tree.CompilationUnit;

public interface IParser
{

	/**
	 * Parse the following grammar:
	 *
	 *   CompilationUnit := PackageDeclaration ImportDeclaration* TypeDeclaration+
	 *
	 * @return
	 */
	CompilationUnit parse();

}