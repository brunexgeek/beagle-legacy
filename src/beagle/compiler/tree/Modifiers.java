package beagle.compiler.tree;

public class Modifiers extends TreeElement implements IModifiers
{

	private int modifiers;

	public Modifiers( int flags )
	{
		this.modifiers = flags;
	}

	@Override
	public int getModifiers()
	{
		return modifiers;
	}

	@Override
	public boolean hasModifier(int flags)
	{
		return (this.modifiers & flags) == flags;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		visitor.visit(this);
		visitor.finish(this);
	}

//	@Override
//	public void print(Printer out, int level)
//	{
//		out.printTag(getClass().getSimpleName(), level);
//
//		if ((modifiers & PUBLIC) != 0)
//			out.print("public  ");
//		if ((modifiers & PROTECTED) != 0)
//			out.print("protected  ");
//		if ((modifiers & PRIVATE) != 0)
//			out.print("private  ");
//		if ((modifiers & INTERNAL) != 0)
//			out.print("const  ");
//		if ((modifiers & PACKAGE) != 0)
//			out.print("package  ");
//		if ((modifiers & STATIC) != 0)
//			out.print("static  ");
//
//		out.println();
//	}

}
