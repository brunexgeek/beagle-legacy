package beagle.compiler;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

import beagle.compiler.tree.CompilationUnit;
import beagle.compiler.tree.TreeVisitor;

public class Main
{

	@SuppressWarnings("unused")
	public static void main(String[] args) throws IOException
	{
		CompilationContext context = new CompilationContext(new Listener());

		for (String fileName : args)
		{
			String content = new String(Files.readAllBytes(Paths.get(fileName)), StandardCharsets.UTF_8);

			//System.out.println("Compiling '" + fileName + "'");
			IScanner scanner = new Scanner(context, new ScanString(fileName, content));
			if (false)
			{
				Token tok;
				while ((tok = scanner.readToken()).type != TokenType.TOK_EOF)
				{
					System.out.println(tok);
				}
			}
			else
			{
				IParser parser = new Parser(context, scanner);
				CompilationUnit unit = parser.parse();
				if (unit == null) return;
				HtmlVisitor visitor = new HtmlVisitor(new PrintStream(System.out));
				visitor.print(unit);
				//unit.print(new Printer(System.out), 0);
				/*IModule module = new Module( new Name("beagle"));
				module.addCompilationUnit(unit);
				CodeGenerator codegen = new CodeGenerator(System.out);
				codegen.visitModule(module, null);*/
			}
		}
	}

	public static class Listener implements CompilationListener
	{

		@Override
		public void onStart()
		{
			// TODO Auto-generated method stub

		}

		@Override
		public boolean onError(SourceLocation location, String message)
		{
			System.out.print("--- ");
			if (location != null)
			{
				System.out.print(location.fileName);
				System.out.print(":");
				System.out.print(location.line);
				System.out.print(":");
				System.out.print(location.column);
			}
			else
				System.out.print("unknow:0:0");
			System.out.print(": [E] ");
			System.out.println(message);
			return true;
		}

		@Override
		public boolean onWarning(SourceLocation location, String message)
		{
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void onFinish()
		{
			// TODO Auto-generated method stub

		}

	}

}
