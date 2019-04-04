namespace beagle.compiler {

export interface CompilationListener
{

	onStart();

	onError( location : SourceLocation, message : string ) : boolean;

	onWarning( location : SourceLocation, message : string ) : boolean;

	onFinish();

}

export class CompilationContext
{
    listener : CompilationListener = null;

    public throwExpected( found : Token, ...types : TokenType[] )
    {
		let first = true;
		let message = "Syntax error, expected ";
		for (let type of types)
		{
			if (!first) message += " or ";
			message += "'" + type.token + "'";
			first = false;

		}
		message += " but found '" + found.type.name + "'";
		this.listener.onError(found.location, message);
	}
}

}