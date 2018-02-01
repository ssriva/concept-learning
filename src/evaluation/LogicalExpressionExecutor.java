package evaluation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.lang.reflect.Method;

import utils.TSVEmailReader;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.jayantkrish.jklol.ccg.lambda.ExpressionParser;
import com.jayantkrish.jklol.ccg.lambda2.Expression2;

public class LogicalExpressionExecutor {

	Class noparams[] = {};

	public static ExpressionParser<Expression2> expressionParser = ExpressionParser.expression2();

	public static Expression2 getExpressionFromString(String logicalForm){
		return expressionParser.parse(logicalForm);
	}

	public static void evaluate(Expression2 expression){
		List<Expression2> sexprs = expression.getSubexpressions();

		if(sexprs==null){
			//System.out.println(expression.toString()+" is an atom. ");
		}else{
			for(int i=0; i<sexprs.size(); i++){
				evaluate(sexprs.get(i));
			}
			System.out.println("Evaluated "+expression);
		}
	}

	public static QueryReturnValue evaluateOnEmail(Expression2 expression, EmailObject email){
		List<Expression2> sexprs = expression.getSubexpressions();

		if(sexprs==null){
			return new QueryReturnValue(email, expression.toString());
		}else{
			ArrayList<QueryReturnValue> argvals = new ArrayList<QueryReturnValue>();
			for(int i=1; i<sexprs.size(); i++){
				argvals.add(evaluateOnEmail(sexprs.get(i), email));
			}
			System.out.println("Applying "+sexprs.get(0).toString() + " with arguments: "+String.join(" ", argvals.stream().map(e -> e.type).collect(Collectors.toList())));
			return applyMethodOnEmail(email, sexprs.get(0).toString(),argvals);
		}
	}

	private static QueryReturnValue applyMethodOnEmail(EmailObject email, String methodName, ArrayList<QueryReturnValue> argvals) {

		Class[] params = new Class[argvals.size()+1];
		Object[] obj = new Object[argvals.size()+1];
		params[0] = EmailObject.class;
		obj[0] = email;
		for(int i=0 ;i<argvals.size(); i++){
			params[i+1] = QueryReturnValue.class;
			obj[i+1] = argvals.get(i);
		}

		Class cls;
		QueryReturnValue qval = null;
		try {
			cls = Class.forName("evaluation.ExecutionMethods");
			//Method method = cls.getDeclaredMethod(methodName, noparams);
			//method.invoke(null, null);
			Method method = cls.getDeclaredMethod(methodName, params);
			qval = (QueryReturnValue) method.invoke(null, obj);

		} catch (Exception e) {
			e.printStackTrace();
		}

		Preconditions.checkArgument(qval!=null,"The return value from applying method "+methodName+" on email is null!");
		return qval;
	}

	public static void main(String []args){
		

		//Expression2 expr = LogicalExpressionExecutor.getExpressionFromString("( ( a b ) c ( (d e) (f (g h) ) ) ) ");
		//Expression2 expr = LogicalExpressionExecutor.getExpressionFromString("(a ( b c ))");
		
		//Expression2 expr = LogicalExpressionExecutor.getExpressionFromString("( and ( stringEquals sender:tt, recipient:tt) ( getCatInstance email:tt DATE:ne) ( getCatInstance email:tt TIME:ne)  )");
		//LogicalExpressionExecutor.evaluateOnEmail(expr, null);
		//LogicalExpressionExecutor.applyMethodOnEmail(null, "testMethod", new ArrayList<QueryReturnValue>());
		
		//Expression2 expr1 = LogicalExpressionExecutor.getExpressionFromString(" (getCatInstance email:tt DATE:ne) ");
		Expression2 expr1 = LogicalExpressionExecutor.getExpressionFromString("( and ( stringEquals sender:tt recipient:tt) ( getCatInstance subject:tt EMAIL:ne ) (stringMatch email:tt (merge policy:s today:s nona:s ) ) ) ");
		
		//expr1 = LogicalExpressionExecutor.getExpressionFromString(" (stringMatch subject:tt NULL:s) ");
		//expr1 = LogicalExpressionExecutor.getExpressionFromString(" ( stringMatch attachment:tt (stringVal STAR ) ) ");
		expr1 = LogicalExpressionExecutor.getExpressionFromString(" (getCatInstance (merge body:tt recipient:tt ) (merge \"police\" (merge ZY:pos TIME:ne ) ) ) ");
		EmailObject email = TSVEmailReader.parseLineToEmail("AGRKG3YT3KMD8	POLICY	Barack Obama's barack@gmail.com office policy	So, yeah, there will be a new policy at 3pm in the office today. Everyone needs to make sure that TPS Cover letters are stocked at all times. That way, when we need them, they're there.	mike@initech-corp.com	NONE	mike@initech-corp.com");
		LogicalExpressionExecutor.evaluateOnEmail(expr1, email);
		
	}
}
