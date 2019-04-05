
/// <reference path="Scanner.ts" />
/// <reference path="Parser.ts" />
/// <reference path="context.ts" />


class MyListener implements beagle.compiler.CompilationListener
{
	onStart() {

	}
	onError(location: beagle.compiler.SourceLocation, message: string): boolean {
		throw new Error(message);
	}
	onWarning(location: beagle.compiler.SourceLocation, message: string): boolean {
		throw new Error(message);
	}
	onFinish() {

	}

}


declare var require: any;
let fs = require("fs");
let util = require("util");
let content = fs.readFileSync("input.txt");

let ctx = new beagle.compiler.CompilationContext();
ctx.listener = new MyListener();

//let body = document.getElementsByTagName("body")[0];
let ss = new beagle.compiler.ScanString(ctx, "bla", content.toString() /*"function abobrinha { }"*/);
let sc = new beagle.compiler.Scanner(ctx, ss);
/*
let tarr = new beagle.compiler.TokenArray(sc);
let tok = null;
while ((tok = tarr.read()) != null)
{
	if (tok.type == beagle.compiler.TokenType.TOK_EOF) break;
	let text = '[' + ((tok.type === null) ? "???" : tok.type.token) + '] ';
	if (tok.value != null) text += "'" + tok.value + "'";
	console.log(text);
	///let tmp = document.createElement("span");
	//tmp.innerHTML = tok.value;
	//body.appendChild(tmp);
}*/

let pa = new beagle.compiler.Parser(ctx, sc);
let unit = pa.parse();

console.log(util.inspect(unit, {showHidden: false, depth: null}))