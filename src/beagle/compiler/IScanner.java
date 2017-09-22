package beagle.compiler;

public interface IScanner
{

	/**
	 * Returns the next token read from the source code.
	 * 
	 * @return
	 */
	Token readToken();
	
	String getFileName(); 
	
	CompilationContext getContext();

}