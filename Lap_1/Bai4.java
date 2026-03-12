import java.util.*;

public class Bai4 {
    public static List<Integer> maxSubarray(int[] arr) {
        int bestSum = Integer.MIN_VALUE;
        int currentSum = 0;
        int start = 0, bestStart = 0, bestEnd = -1;
        for (int i = 0; i < arr.length; i++) {
            if (currentSum <= 0) {
                currentSum = arr[i];
                start = i;
            } else {
                currentSum += arr[i];
            }
            if (currentSum > bestSum) {
                bestSum = currentSum;
                bestStart = start;
                bestEnd = i;
            }
        }
        List<Integer> result = new ArrayList<>();
        if (bestEnd >= bestStart) {
            for (int i = bestStart; i <= bestEnd; i++) {
                result.add(arr[i]);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Nhập dãy số nguyên, cách nhau bằng dấu cách (nhấn Enter khi xong):");
        String line = sc.nextLine().trim();
        String[] parts = line.split("\\s+");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i]);
        }
        List<Integer> sub = maxSubarray(arr);
        System.out.println("Dãy con có tổng lớn nhất:");
        for (int v : sub) {
            System.out.print(v + " ");
        }
        System.out.println();
        sc.close();
    }
}
