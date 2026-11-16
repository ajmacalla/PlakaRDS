// gawa ni allie

import java.util.Arrays;

public class FinalsActivity1 {

	public static void main(String[] args) {
		
		int[] numbers = {45, 12, 89, 33, 67, 21, 54};
		
		// Arrays.toString
		System.out.println("Original: " + Arrays.toString(numbers));
		
		// Arrays.copyOf
		int[] num = Arrays.copyOf(numbers, numbers.length);
		
		// ascending
		Arrays.sort(numbers);
		System.out.println("Ascending: " + Arrays.toString(numbers));
		
		// descending

		// lowest & highest 
		
		// binary search
		int target = 67;
		int index = Arrays.binarySearch(numbers, target);
						
		if (index >= 0) {
			System.out.println(target + " found at index " + index);
		} else {
			System.out.println(target + " was not found.");
		}
		
		// arrays equals
		
//		Create a Java program named GroupName using:
//			int[] numbers = {45, 12, 89, 33, 67, 21, 90, 54};
//			Your program must:
//			1.	Display the original array using Arrays.toString(). ✅
//			2.	Create an independent copy using Arrays.copyOf(). ✅
//			3.	Sort the copy in ascending order. ✅
//			4.	Display the sorted copy. ✅
//			5.	Display its values in descending order using a loop.
//			6.	Display the lowest and highest values.
//			7.	Search for 67 using Arrays.binarySearch(). ✅
//			8.	Print whether the original and sorted arrays are equal.
//			Expected output:
//			Original: [45, 12, 89, 33, 67, 21, 90, 54]
//			Ascending: [12, 21, 33, 45, 54, 67, 89, 90]
//			Descending: 90 89 67 54 45 33 21 12
//			Lowest: 12
//			Highest: 90
//			67 found at sorted index 5
//			Arrays are equal: false

	}

}
