package beagle.compiler.tree;

public class Comment
{

	String text;

	boolean isDoc;

	public Comment( String text, boolean isDoc )
	{
		this.text = text;
		this.isDoc = isDoc;
	}

	public String text()
	{
		return text;
	}

	public boolean isDocumentation()
	{
		return isDoc;
	}

}
