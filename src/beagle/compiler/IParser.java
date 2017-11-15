package beagle.compiler;

import beagle.compiler.tree.ICompilationUnit;

public interface IParser
{

	/**
	 * Parse the following grammar:
	 *
	 *   CompilationUnit := PackageDeclaration ImportDeclaration* TypeDeclaration+
	 *
	 * @return
	 */
	ICompilationUnit parse();

}