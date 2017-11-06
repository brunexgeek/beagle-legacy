package beagle.compiler.tree;

public class Comment implements IComment
{

	String text;

	boolean isDoc;

	public Comment( String text, boolean isDoc )
	{
		this.text = text;
		this.isDoc = isDoc;
	}

	@Override
	public String getText()
	{
		return text;
	}

	@Override
	public boolean isDocumentation()
	{
		return isDoc;
	}

}
