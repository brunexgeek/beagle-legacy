package beagle.tool;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import beagle.compiler.CompilationContext;
import beagle.compiler.CompilationListener;
import beagle.compiler.HtmlVisitor;
import beagle.compiler.IParser;
import beagle.compiler.IScanner;
import beagle.compiler.Parser;
import beagle.compiler.ScanString;
import beagle.compiler.Scanner;
import beagle.compiler.Semantic;
import beagle.compiler.SourceLocation;
import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.ITreeVisitor;

public class PrintTree
{


	public static void main(String[] args) throws IOException
	{
		CompilationContext context = new CompilationContext(new Listener());

		if (args.length < 2)
		{
			System.err.println("Usage: PrintTree <source1> ... <source-n> <html file>");
			return;
		}

		String outputFileName = args[args.length-1];
		if (!outputFileName.endsWith(".html"))
		{
			System.err.println("Output file must be an '.html' file");
			return;
		}

		OutputStream os = new FileOutputStream(outputFileName);

		for (int i = 0; i < args.length-1; i++)
		{
			String fileName = args[i];
			String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);

			System.err.println("Compiling '" + fileName + "'");
			IScanner scanner = new Scanner(context, new ScanString(fileName, content));

			IParser parser = new Parser(context, scanner);
			CompilationUnit unit = parser.parse();
			if (unit == null) return;
			HtmlVisitor visitor = new HtmlVisitor(new PrintStream(os));
			visitor.print(unit);
		}

		os.close();
	}

	public static class Listener implements CompilationListener
	{

		@Override
		public void onStart()
		{
		}

		@Override
		public boolean onError(SourceLocation location, String message)
		{
			if (location != null)
			{
				System.err.print(location.getFileName());
				System.err.print(":");
				System.err.print(location.getLine());
				System.err.print(":");
				System.err.print(location.getColumn());
			}
			else
				System.err.print("unknow:0:0");
			System.err.print(": [E] ");
			System.err.println(message);
			return true;
		}

		@Override
		public boolean onWarning(SourceLocation location, String message)
		{
			return false;
		}

		@Override
		public void onFinish()
		{
		}

	}


}
