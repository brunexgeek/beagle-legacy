package beagle.compiler.tree;

import java.util.LinkedList;

public class AnnotationList extends LinkedList<IAnnotation> implements IAnnotationList
{

	private static final long serialVersionUID = 1394405999887189989L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (IAnnotation item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

	@Override
	public void accept(ITreeVisitor visitor, ITreeElement child)
	{
		if (child != null) child.accept(visitor);
	}

}
