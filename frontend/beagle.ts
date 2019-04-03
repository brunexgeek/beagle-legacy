
/// <reference path="Scanner.ts" />

declare var require: any;
let fs = require("fs");
let content = fs.readFileSync("input.txt");

//let body = document.getElementsByTagName("body")[0];
let ss = new beagle.compiler.ScanString("bla", content.toString() /*"function abobrinha { }"*/);
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