package set0000.set300.set330.p330;

/**
 * Created by wangsenyuan on 1/28/16.
 */
public class Solution1 {

    public int minPatches(int[] nums, int n) {
        long miss = 1;
        int count = 0, i = 0;

        while (miss <= n) {
            if (i < nums.length && nums[i] <= miss) {
                miss += nums[i++];
            } else {
                miss += miss;
                count += 1;
            }
        }

        return count;
    }
}
