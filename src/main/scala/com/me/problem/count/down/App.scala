package com.me.problem.count.down

/**
 * Created by senyuanwang on 15/5/25.
 */
object App extends App {

  abstract trait Op {
    def value: Int
    def expression: String
    def isValid: Boolean
  }

  object Op {
    def list(x: Op, y: Op) = List(Add(x, y), Sub(x, y), Mul(x, y), Div(x, y))
  }

  case class Add(left: Op, right: Op) extends Op {
    override def value: Int = left.value + right.value

    override def expression: String = "(" + left.expression + " + " + right.expression + ")"

    override def isValid: Boolean = true
  }

  case class Sub(left: Op, right: Op) extends Op {
    override def value: Int = left.value - right.value

    override def expression: String = "(" + left.expression + " - " + right.expression + ")"

    override def isValid: Boolean = left.value > right.value
  }

  case class Mul(left: Op, right: Op) extends Op {
    override def value: Int = left.value * right.value

    override def expression: String = left.expression + " * " + right.expression

    override def isValid: Boolean = true
  }

  case class Div(left: Op, right: Op) extends Op {
    override def value: Int = left.value / right.value

    override def expression: String = left.expression + " / " + right.expression

    override def isValid: Boolean = right.value != 0 && left.value % right.value == 0
  }

  case class Val(override val value: Int) extends Op {
    override def expression: String = s"$value"

    override def isValid: Boolean = true
  }

  def splitArray(array: List[Int]): List[(List[Int], List[Int])] =
    array match {
      case Nil => throw new RuntimeException("no empty array allowed here.")
      case x :: y :: Nil => List((List(x), List(y)))
      case h :: tail =>
        (List(h), tail) :: (for {
          (x, y) <- splitArray(tail)
        } yield (h :: x, y))
    }

  def express(nums: List[Int]): List[Op] =
    nums match {
      case Nil => throw new RuntimeException("empty list")
      case x :: Nil => List(Val(x))
      case _ =>
        for {
          (left, right) <- splitArray(nums)
          x <- express(left)
          y <- express(right)
          op <- Op.list(x, y)
          if op.isValid
        } yield op
    }

  def choices(nums: List[Int]): List[List[Int]] =
    nums match {
      case Nil => List(Nil)
      case h :: tail =>
        (for {
          subChoice <- choices(tail)
        } yield List(subChoice, (h :: subChoice), subChoice ++ List(h))).flatten
    }

  def countDown(nums: Array[Int], res: Int): List[Op] = {
    for {
      list <- choices(nums.toList)
      if (!list.isEmpty)
      op <- express(list)
      if (op.value == res)
    } yield op
  }

  //  choices(Array(1, 2).toList).foreach(println)

  //  splitArray(Array(1, 3, 7, 10, 25, 50).toList).foreach(println)

  val opList = countDown(Array(1, 3, 7, 10, 25, 50), 765)
  opList.foreach(op => println(op.expression))
}
