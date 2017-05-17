import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import soot.*;
import soot.grimp.internal.ExprBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IdentityStmt;
import soot.jimple.IntConstant;
import soot.jimple.InvokeStmt;
import soot.jimple.JasminClass;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
import soot.jimple.ParameterRef;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.validation.InvokeArgumentValidator;
import soot.jimple.validation.InvokeValidator;
import soot.options.Options;
import soot.util.JasminOutputStream;
import soot.validation.LocalsValidator;
import soot.validation.UsesValidator;
import soot.validation.ValidationException;
import soot.validation.ValueBoxesValidator;

import java.util.Scanner;

public class CodeInserter {

	public static void main(String[] args) throws Exception {
		// CHANGE THIS
		Options.v().set_soot_classpath(Scene.v().defaultClassPath() + ";C:\\Users\\kerrickm\\workspace\\InputSpacePartitionChecker\\bin");
		
//		Scanner scanner = new Scanner(System.in);
//		System.out.println("Enter the name of a class to test or enter 0 for TestTestTest.");
//		String classname = scanner.next();
//		
//		if (classname.equals("0")) {
//			classname = "TestTestTest";
//		}
		
		String classname = "TestTestTest";
		
		Scene.v().loadClassAndSupport(classname);
		Scene.v().loadClassAndSupport("ObserveTests");
		Scene.v().loadClassAndSupport("java.lang.Integer");
		Scene.v().loadNecessaryClasses();
		SootClass s = Scene.v().getSootClass(classname);
		SootClass integer = Scene.v().getSootClass("java.lang.Integer");
		
		SootMethod integerValueOf = integer.getMethod("java.lang.Integer valueOf(int)");
		
		List<SootMethod> methods = s.getMethods();
		for (SootMethod m : methods) {
			m.retrieveActiveBody();
			
			if (m.isStatic() || m.getSignature().contains("<init>")) {
				continue;
			}
			
			Iterator it = m.retrieveActiveBody().getUnits().snapshotIterator();
			PatchingChain<Unit> units = m.retrieveActiveBody().getUnits();
			Body methodBody = m.getActiveBody();

			SootClass x = Scene.v().getSootClass("ObserveTests");
			SootMethod toCall = x.getMethodByName("testCaseFound");
			
			int parameterCount = m.getParameterCount();
			
			Stmt insertAt = (Stmt) it.next();
			insertAt = (Stmt) it.next();
			for (int count = 0; count < parameterCount; count++) {
				insertAt = (Stmt) it.next();
			}
			
			NewArrayExpr arr = Jimple.v().newNewArrayExpr(RefType.v("java.lang.Object"), IntConstant.v(parameterCount));
			
			Local arrReference = Jimple.v().newLocal("l0", ArrayType.v(RefType.v("java.lang.Object"), 1));
			m.getActiveBody().getLocals().add(arrReference);
			AssignStmt arrayAssignment = Jimple.v().newAssignStmt(arrReference, arr);
			units.insertBefore(arrayAssignment, insertAt);
			
			for (int i = 0; i < parameterCount; i++) {
				ArrayRef arrRef = Jimple.v().newArrayRef(arrReference, IntConstant.v(i));
				
				Value v = methodBody.getParameterLocal(i);
				if (v.getType() instanceof IntType) {
					StaticInvokeExpr valueOfCall = Jimple.v().newStaticInvokeExpr(integerValueOf.makeRef(), v);
					Local integerFromInt = Jimple.v().newLocal("l" + i + 1, RefType.v("java.lang.Integer"));
					m.getActiveBody().getLocals().add(integerFromInt);
					AssignStmt integerAssignment = Jimple.v().newAssignStmt(integerFromInt, valueOfCall);
					units.insertBefore(integerAssignment, insertAt);
					
					AssignStmt assignment = Jimple.v().newAssignStmt(arrRef, integerFromInt);
					units.insertBefore(assignment, insertAt);
				}
				else {
					AssignStmt assignment = Jimple.v().newAssignStmt(arrRef, methodBody.getParameterLocal(i));
					units.insertBefore(assignment, insertAt);
				}
				
			}
			
			units.insertBefore(Jimple.v().newInvokeStmt(Jimple.v().newStaticInvokeExpr(toCall.makeRef(), StringConstant.v(m.toString()), arrReference)), insertAt);
			System.out.println(m.getName() + ": " + units.toString());
			
			List<ValidationException> exceptions = new ArrayList<ValidationException>();
			InvokeValidator.v().validate(m.getActiveBody(), exceptions);
			InvokeArgumentValidator.v().validate(m.getActiveBody(), exceptions);
			LocalsValidator.v().validate(m.getActiveBody(), exceptions);
			UsesValidator.v().validate(m.getActiveBody(), exceptions);
			ValueBoxesValidator.v().validate(m.getActiveBody(), exceptions);
			
			for (int a = 0; a < exceptions.size(); a++) {
				System.err.println(exceptions.get(a));
			}
		}
		
		// Automatically write to the automatically chosen class file. Ripped directly from Soot/Creating a class from scratch
		String fileName = SourceLocator.v().getFileNameFor(s, Options.output_format_class);
		System.out.println(fileName);
		OutputStream streamOut = new JasminOutputStream(new FileOutputStream(fileName));
		PrintWriter writerOut = new PrintWriter(new OutputStreamWriter(streamOut));
		
		JasminClass jasminClass = new soot.jimple.JasminClass(s);
		jasminClass.print(writerOut);
		writerOut.flush();
		streamOut.close();
	
		
	}

}