package beagle.compiler;

public interface CompilationListener
{

	void onStart();
	
	boolean onError( SourceLocation location, String message );
	
	boolean onWarning( SourceLocation location, String message );
	
	void onFinish();
	
}
