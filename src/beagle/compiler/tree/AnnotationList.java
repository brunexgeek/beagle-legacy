package beagle.compiler.tree;

public class AnnotationList extends TreeElementList<Annotation>
{

	private static final long serialVersionUID = 1394405999887189989L;

	@Override
	public void accept(ITreeVisitor visitor)
	{
		if (visitor.visit(this))
			for (Annotation item : this)
				item.accept(visitor);
		visitor.finish(this);
	}

}
