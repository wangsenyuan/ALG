package p047;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangsenyuan on 7/14/16.
 */
public class Solution {

    public static void main(String[] args) {
        Solution solution = new Solution();
        int[] nums = {2, 2, 1, 1};
        List<List<Integer>> ps = solution.permuteUnique(nums);
        for (List<Integer> p : ps) {
            for (int x : p) {
                System.out.printf("%d,", x);
            }
            System.out.println();
        }
    }

    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);

        List<List<Integer>> ps = new ArrayList<>();
        ps.add(toList(nums));
        boolean last = false;
        while (!last) {
            last = true;
            for (int k = nums.length - 1; k > 0; k--) {
                if (nums[k - 1] >= nums[k]) {
                    continue;
                }
                last = false;
                int l = nums.length - 1;
                while (nums[l] <= nums[k - 1]) {
                    l--;
                }

                swap(nums, l, k - 1);

                for (int a = k, b = nums.length - 1; a < b; a++, b--) {
                    swap(nums, a, b);
                }
                ps.add(toList(nums));
                break;
            }
        }

        return ps;
    }

    private List<Integer> toList(int[] nums) {
        List<Integer> p = new ArrayList<>(nums.length);
        for (int x : nums) {
            p.add(x);
        }
        return p;
    }

    private void swap(int[] nums, int i, int j) {
        int tmp = nums[i];
        nums[i] = nums[j];
        nums[j] = tmp;
    }
}
