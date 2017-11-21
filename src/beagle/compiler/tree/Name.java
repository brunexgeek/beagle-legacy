package beagle.compiler.tree;

import java.util.ArrayList;

public class Name extends TreeElement implements IName
{

	private ArrayList<String> names;

	private String qualifiedName;

	public Name( String value )
	{
		if (value == null)
			value = "";
		names = new ArrayList<>();
		names.add(value);
		qualifiedName = value;
	}

	public Name append( String value )
	{
		if (value == null || value.isEmpty())
			return this;

		names.add(value);
		qualifiedName += '.' + value;

		return this;
	}

	@Override
	public String qualifiedName()
	{
		return qualifiedName;
	}

	@Override
	public String name(int index)
	{
		if (index >= names.size())
			return null;
		return names.get(index);
	}

	@Override
	public int count()
	{
		return names.size();
	}

	@Override
	public boolean equals( Object name)
	{
		if (!(name instanceof IName))
			return false;
		IName object = (Name) name;
		return compareTo(object) == 0;
	}

	@Override
	public int compareTo(IName name)
	{
		return qualifiedName.compareTo(name.qualifiedName());
	}

	@Override
	public int hashCode()
	{
		return qualifiedName.hashCode();
	}

	@Override
	public String toString()
	{
		return qualifiedName;
	}

	@Override
	public IName slice(int start)
	{
		return slice(start, count() - start);
	}

	@Override
	public IName slice(int start, int length)
	{
		if (start < 0 || start >= names.size() || length <= 0)
			return null;

		Name output = null;
		int end = start + length;
		if (end > names.size())
			end = names.size();

		for (int i = start; i < end; ++i)
		{
			if (output == null)
				output = new Name( names.get(i) );
			else
				output.append( names.get(i) );
		}

		/*int n = names.size() - index;
		for (int i = 0; i < n; ++i)
			names.remove( names.size() - 1);

		qualifiedName = names.get(0);
		for (int i = 1; i < names.size(); ++i)
		{
			qualifiedName += '.';
			qualifiedName += names.get(i);
		}*/

		return output;
	}

	@Override
	public boolean isQualified()
	{
		return names.size() > 1;
	}

	@Override
	public void accept(ITreeVisitor visitor)
	{
		visitor.visit(this);
		visitor.finish(this);
	}

}
