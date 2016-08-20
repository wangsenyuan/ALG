/**
  * Our Chef is catering for a big corporate office party and is busy preparing different mouth watering dishes. The host has insisted that he serves his delicious cupcakes for dessert.
  * *
  * On the day of the party, the Chef was over-seeing all the food arrangements as well, ensuring that every item was in its designated position. The host was satisfied with everything except the cupcakes. He noticed they were arranged neatly in the shape of a rectangle. He asks the Chef to make it as square-like as possible.
  * *
  * The Chef is in no mood to waste his cupcakes by transforming it into a perfect square arrangement. Instead, to fool the host, he asks you to arrange the N cupcakes as a rectangle so that the difference between the length and the width is minimized.
  * *
  * Input
  * *
  * The first line of the input file contains an integer T, the number of test cases. Each of the following T lines contains a single integer N denoting the number of cupcakes.
  * *
  * Output
  * *
  * Output T lines, each indicating the minimum possible difference between the length and the width in a rectangular arrangement of the cupcakes.
  * *
  * Constraints
  * *
  * 1 ≤ T ≤ 100
  * *
  * 1 ≤ N ≤ 108
  * *
  * Example
  * *
  * Input:
  * 4
  * 20
  * 13
  * 8
  * 4
  * *
  * Output:
  * 1
  * 12
  * 2
  * 0
  * Explanation
  * *
  * Case 1: 20 cupcakes can be arranged in 6 possible ways - 1 x 20, 2 x 10, 4 x 5, 5 x 4, 10 x 2 and 20 x 1. The corresponding differences between the length and the width are 19, 8, 1, 1, 8 and 19 respectively. Hence, 1 is the answer.
  * *
  * Case 4: 4 cupcakes can be arranged as a 2 x 2 square. Difference between the length and the width is 0. You can't do anything better than 0.
  *
  * Created by wangsenyuan on 8/19/16.
  */

package codechef.resq


import scala.io.StdIn

object Main {
  def main(args: Array[String]): Unit = {
    var t = StdIn.readInt()
    while (t > 0) {
      val n = StdIn.readInt()
      val r = arrange(n);
      println(r)
      t -= 1
    }
  }

  def arrange(n: Int): Int = {
    def go(root: Int): (Int, Int) = {
      if (n % root == 0) {
        (root, n / root)
      } else {
        go(root - 1)
      }
    }
    val (a, b) = go(math.sqrt(n).toInt)
    (a - b).abs
  }
}
