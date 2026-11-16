import java.util.Arrays;

public class BinarySearch {

	public static void main(String[] args) {
		// Arrays.binarySearch();
		int[] num = {40, 10, 50, 20, 30};
		
		Arrays.sort(num);
		
		int target = 30;
		int index = Arrays.binarySearch(num, target);
		
		System.out.println("Sorted: " + Arrays.toString(num));
		
		if (index >= 0) {
			System.out.println(target + " found at index " + index);
		} else {
			System.out.println(target + " was not found.");
		}
	}

}
