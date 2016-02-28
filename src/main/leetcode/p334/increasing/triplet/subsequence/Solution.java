package p334.increasing.triplet.subsequence;

/**
 * Created by senyuanwang on 16/2/16.
 */
public class Solution {

    public boolean increasingTriplet(int[] nums) {
        if (nums == null || nums.length < 3) {
            return false;
        }

        int a = Integer.MAX_VALUE, b = Integer.MAX_VALUE;

        for (int num : nums) {
            if (num < a) {
                a = num;
            } else if (num < b) {
                b = num;
            } else {
                return true;
            }
        }

        return false;
    }

    public static void main(String[] args) {
        int[] nums = {3, 1, 4, 5};
        Solution solution = new Solution();
        System.out.println(solution.increasingTriplet(nums));
    }
}
