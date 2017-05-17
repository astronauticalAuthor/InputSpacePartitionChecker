import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputSpaceOptions {
	
	private Map<String, Object[]> inputOptions;
	
	public InputSpaceOptions() {
		inputOptions = new HashMap<String, Object[]>();
		
		Object[] booleans = new Object[] {true, false};
		inputOptions.put("java.lang.Boolean", booleans);
		
		Object[] ints = new Object[] {-1, 0, 1, 50000};
		inputOptions.put("java.lang.Integer", ints);
		
		String[] strings = new String[] {"", "a", "abc", "asdfasdf", null};
		inputOptions.put("java.lang.String", strings);
		
		Object[] doubles = {-30000, -10.5, -1, 0, 1, 5.5, 30000};
		inputOptions.put("java.lang.Float", doubles);
		inputOptions.put("java.lang.Double", doubles);

		List<Object> list = new ArrayList<Object>();
		List<Object> list2 = new ArrayList<Object>();
		list2.add(null);
		List<Object> list3 = new ArrayList<Object>();
		list3.add(new Object());
		list3.add(new Object());
		list3.add(new Object());
		Object[] lists = new Object[] {null, list, list2, list3};
		inputOptions.put("List", lists);
	}
	
	public void addInputOption(String name, Object[] options) {
		inputOptions.put(name, options);
	}
	
	public Object[] getOptions(String key) {
		return inputOptions.getOrDefault(key, new Object[] {});
	}
}
