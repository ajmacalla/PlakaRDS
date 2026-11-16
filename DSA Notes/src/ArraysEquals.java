import java.util.Arrays;

public class ArraysEquals {

	public static void main(String[] args) {
		// Arrays.equals(value1, value2);
				
		int[] num1 = {1, 2, 3, 4};
		int[] num2 = {1, 2, 3, 4};
		int[] num3 = {2, 3, 4, 1};
				
		System.out.println(Arrays.equals(num1, num3));
		System.out.println(Arrays.equals(num2, num1));
	}

}
