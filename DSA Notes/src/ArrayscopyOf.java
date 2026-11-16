import java.util.Arrays;

public class ArrayscopyOf {

	public static void main(String[] args) {
		// Arrays.copyOf();
		
		int[] x = {20, 31, 19, 11} ;
		
		System.out.println(Arrays.toString(x));
		int[] y = Arrays.copyOf(x, x.length);
		System.out.println(Arrays.toString(y));

	}

}
