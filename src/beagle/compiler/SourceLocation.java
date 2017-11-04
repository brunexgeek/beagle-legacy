package beagle.compiler;

public class SourceLocation implements Cloneable
{

	protected int line;

	protected int column;

	protected String fileName;

	public SourceLocation(String fileName)
	{
		this.fileName = fileName;
		line = column = 1;
	}

	public SourceLocation(String fileName, int line, int column)
	{
		this.fileName = fileName;
		this.line = line;
		this.column = column;
	}

	public int getLine()
	{
		return line;
	}

	public int getColumn()
	{
		return column;
	}

	public String getFileName()
	{
		return fileName;
	}

	public char update(char value)
	{
		if (value == '\n')
		{
			++line;
			column = 0;
		} else
		{
			++column;
		}
		return value;
	}

	@Override
	public SourceLocation clone()
	{
		return new SourceLocation(fileName, line, column);		
	}
}
