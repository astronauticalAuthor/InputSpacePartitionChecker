import java.util.ArrayList;
import java.util.HashMap;

public class ObserveTests {
	
	private static HashMap<String, ArrayList<ArrayList<Object>>> methods = new HashMap<String, ArrayList<ArrayList<Object>>>();
	
	public static ArrayList<ArrayList<Object>> getTestedCases(String methodName) {
		return methods.get(methodName);
	}
	
	public static void testCaseFound(String methodName, Object[] tests) {
		ArrayList<ArrayList<Object>> params = methods.get(methodName);
		if (params == null) {
			params = new ArrayList<ArrayList<Object>>();
			methods.put(methodName, params);
		}
		while (params.size() < tests.length) {
			params.add(new ArrayList<Object>());
		}
		
		for (int x = 0; x < tests.length; x++) {
			params.get(x).add(tests[x]);
		}
	}
	
	// Have this read automatically from Soot?
	public static boolean allTestCasesCovered(String methodName, int index) {
		ArrayList<Object> tests = methods.get(methodName).get(index);

		if (tests.size() == 0) {
			return false;
		}
		
		String type = tests.get(0).getClass().getCanonicalName();
		System.out.println(type);
		InputSpaceOptions i = new InputSpaceOptions();
		Object[] allCases = i.getOptions(type);
		
		for (int x = 0; x < tests.size(); x++) {
			boolean found = false;
			for (int y = 0; y < allCases.length; y++) {
				if (tests.get(x).equals(allCases[y])) {
					found = true;
				}
			}
			
			if (found == false) {
				return false;
			}
		}
		
		return true;
	}

}
