namespace beagle.compiler.tree {

export class Comment 
{
	text : string;
	isDoc : boolean;

	constructor( text : string, isDoc : boolean = false )
	{
		this.text = text;
		this.isDoc = isDoc;
	}
}

}