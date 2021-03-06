package set0000.set200.set270.p274;

import java.util.Arrays;

/**
 * Created by senyuanwang on 15/9/4.
 */
public class Solution1 {
    public int hIndex(int[] citations) {
        Arrays.sort(citations);

        int n = citations.length;
        int i = 0, j = n - 1;

        while (i <= j) {
            int k = (i + j) / 2;
            int v = citations[k];
            int h = n - k;
            if (v >= h) {
                j = k - 1;
            } else {
                i = k + 1;
            }
        }

        return n - j - 1;
    }

    public static void main(String[] args) {
        int[] citations = {0};
        Solution1 solution1 = new Solution1();
        System.out.println(solution1.hIndex(citations));
    }
}
