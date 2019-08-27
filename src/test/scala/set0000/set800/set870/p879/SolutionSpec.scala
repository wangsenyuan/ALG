package set0000.set800.set870.p879

import org.scalatest.{FlatSpec, Matchers}

class SolutionSpec extends FlatSpec with Matchers {
  "example one" should "work" in {
    val res = Solution.profitableSchemes(5, 3, Array(2, 2), Array(2, 3))
    res should be(2)
  }

  "example two" should "work" in {
    val res = Solution.profitableSchemes(10, 5, Array(2, 3, 5), Array(6, 7, 8))
    res should be(7)
  }

  "example three" should "work" in {
    val res = Solution.profitableSchemes(100, 100,
      Array(6, 37, 1, 24, 35, 31, 1, 2, 15, 4, 7, 3, 1, 1, 1, 2, 7, 13, 6, 2, 21, 31, 4, 7, 7, 8, 7, 4, 15, 2, 1, 24, 27, 8, 30, 10, 8, 6, 4, 9, 2, 6, 12, 3, 4, 11, 8, 29, 3, 1, 8, 4, 1, 6, 1, 9, 3, 11, 1, 5, 1, 1, 3, 8, 7, 4, 15, 2, 3, 15, 9, 2, 12, 12, 11, 3, 10, 5, 12, 13, 7, 33, 10, 42, 11, 2, 9, 32, 1, 1, 3, 44, 7, 15, 4, 1, 9, 19, 6, 15),
      Array(2, 7, 1, 29, 1, 5, 0, 0, 1, 0, 14, 4, 7, 0, 22, 2, 0, 13, 4, 6, 13, 9, 3, 0, 9, 7, 21, 21, 23, 1, 13, 10, 5, 13, 2, 15, 3, 5, 6, 20, 11, 1, 14, 36, 0, 20, 12, 14, 0, 5, 8, 4, 24, 18, 12, 23, 7, 6, 7, 4, 12, 24, 5, 6, 25, 9, 11, 21, 13, 10, 49, 2, 0, 1, 6, 1, 18, 35, 1, 19, 0, 17, 2, 39, 21, 3, 34, 8, 13, 13, 0, 0, 4, 20, 35, 2, 31, 8, 3, 70))
    res should be(332911349)
  }
}
