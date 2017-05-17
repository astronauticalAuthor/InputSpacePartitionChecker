
public class TestTestTest {
	
	public static void main(String[] args) {
		TestTestTest test = new TestTestTest();
		test.isEven(-1);
		test.isEven(0);
		test.isEven(1);
		test.isEven(50000);
		
		boolean covered = ObserveTests.allTestCasesCovered("<TestTestTest: boolean isEven(int)>", 0);
		System.out.println(covered);
	}
	
	public boolean isEven(int num) {
		return num % 2 == 0;
	}
	
	public int max(int a, int b) {
		if (a > b) {
			return a;
		}
		else {
			return b;
		}
	}
	
//	public void mystery(boolean a, int b, String c) {
//		if (a) {
//			b++;
//		}
//		else {
//			c = "";
//		}
//	}

}
