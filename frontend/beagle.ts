
/// <reference path="Scanner.ts" />



//let body = document.getElementsByTagName("body")[0];
let ss = new beagle.compiler.ScanString("bla", "function abobrinha { }");
let sc = new beagle.compiler.Scanner(null, ss);
while (sc.readToken)
{
	let tok = sc.readToken();
	if (tok.type == beagle.compiler.TOK_EOF) break;
	console.log("'" + tok.value + "'");
	/*let tmp = document.createElement("span");
	tmp.innerHTML = tok.value;
	body.appendChild(tmp);*/
}