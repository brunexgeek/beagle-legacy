package beagle.compiler;

import java.util.HashMap;

import beagle.compiler.tree.IName;
import beagle.compiler.tree.IPackage;
import beagle.compiler.tree.ITypeDeclaration;
import beagle.compiler.tree.Package;

public class CompilationContext
{

	/**
	 * Map containing every known type.
	 *
	 * Types can be obtained from loading modules or from compilation units
	 * (both complete and incomplete types).
	 */
	public HashMap<String, ITypeDeclaration> types;

	/**
	 * Map containing every known package.
	 *
	 * Packages can be obtained from loading modules or from compilation units.
	 */
	public HashMap<String, IPackage> packages;


	public CompilationListener listener;


	public CompilationContext( CompilationListener listener )
	{
		this.types = new HashMap<>();
		this.packages = new HashMap<>();
		this.listener = listener;
	}


	public IPackage createPackage(IName packageName)
	{
		IPackage result = packages.get(packageName);
		if (result == null)
		{
			result = new Package(packageName);
			packages.put(packageName.toString(), result);
		}
		return result;
	}


	/*public ITypeDeclaration createType(IName name, IPackage pack)
	{
		if (name.isQualified())
			throw new IllegalArgumentException("Invalid simple name");

		ITypeDeclaration result = types.get(name);
		if (result == null)
		{
			result = new TypeDeclaration(pack, name);
			types.put(name.toString(), result);
		}
		return result;
	}*/

	public CompilationListener getListener()
	{
		return listener;
	}

	public void throwExpected( Token found, TokenType... types )
	{
		boolean first = true;
		String message = "Syntax error, expected ";
		for (TokenType type : types)
		{
			if (!first) message += " or ";
			message += "'" + type.getName() + "'";
			first = false;

		}
		message += " but found '" + found.type.getName() + "'";
		listener.onError(found.location, message);
	}

}
