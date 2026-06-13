import java.util.*;

public class Bai4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Nhập số lượng phần tử và số k
        System.out.print("Nhap so luong phan tu: ");
        int n = sc.nextInt();
        
        System.out.print("Nhap tong muc tieu (k): ");
        int k = sc.nextInt();

        // 2. Nhập các phần tử của mảng
        int[] a = new int[n];
        System.out.println("Nhap " + n + " phan tu cua mang:");
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        // Gọi hàm xử lý
        findLongestSubsequence(a, k);
        
        sc.close();
    }

    public static void findLongestSubsequence(int[] a, int k) {
        int n = a.length;
        
        // dp[j] lưu độ dài lớn nhất của dãy con có tổng bằng j
        int[] dp = new int[k + 1];
        Arrays.fill(dp, -1);
        dp[0] = 0; 

        // chosen[i][j] đánh dấu phần tử thứ i có được chọn để đạt tổng j hay không
        boolean[][] chosen = new boolean[n][k + 1];

        for (int i = 0; i < n; i++) {
            // Duyệt ngược từ k về a[i] để mỗi phần tử chỉ dùng 1 lần
            for (int j = k; j >= a[i]; j--) {
                if (dp[j - a[i]] != -1) {
                    // Nếu chọn a[i] tạo ra dãy dài hơn hiện tại
                    if (dp[j - a[i]] + 1 > dp[j]) {
                        dp[j] = dp[j - a[i]] + 1;
                        chosen[i][j] = true;
                    }
                }
            }
        }

        System.out.println("---------------------------");
        if (dp[k] == -1) {
            System.out.println("Khon tim thay day con nao co tong bang " + k);
        } else {
            // Truy vết ngược lại các phần tử đã chọn
            List<Integer> result = new ArrayList<>();
            int currentSum = k;
            for (int i = n - 1; i >= 0; i--) {
                if (chosen[i][currentSum]) {
                    result.add(a[i]);
                    currentSum -= a[i];
                }
            }
            
            // Đảo ngược danh sách để đúng thứ tự xuất hiện ban đầu
            Collections.reverse(result);
            
            System.out.println("Ket qua day con dai nhat co tong bang: + k");
            for (int num : result) {
                System.out.print(num + " ");
            }
            System.out.println("\nDo dái: " + dp[k]);
        }
    }
}