import java.util.Arrays;

public class ArraysFill {

	public static void main(String[] args) {
		// Arrays.fill(variable, value);
		
		int[] i = new int[5];
		
		Arrays.fill(i, 100);
		System.out.println(Arrays.toString(i));
	}

}
