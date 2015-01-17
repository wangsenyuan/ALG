package fp.monad

import scala.language.higherKinds

trait Traverse[F[_]] extends Applicative[F] {
  def traverse[M[_]: Applicative, A, B](fa: F[A])(f: A => M[B]): M[F[B]] =
    sequence(map(fa)(f))
  def sequence[M[_]: Applicative, A](fma: F[M[A]]): M[F[A]] =
    traverse(fma)(ma => ma)
}

case class Tree[+A](head: A, tail: List[Tree[A]])

object Traverse {
//  val listTraverse = new Traverse[List] {
//    override def traverse[M[_], A, B](as: List[A])(f: A => M[B])(implicit M: Applicative[M]): M[List[B]] =
//      as.foldRight(M.unit(List[B]()))((a, fbs) => M.map2(f(a), fbs)(_ :: _))
//  }
//
//  val optionTraverse = new Traverse[Option] {
//    override def traverse[M[_], A, B](oa: Option[A])(f: A => M[B])(implicit M: Applicative[M]): M[Option[B]] =
//      oa match {
//        case Some(a) => M.map(f(a))(Some(_))
//        case None => M.unit(None)
//      }
//  }
//
//  val treeTraverse = new Traverse[Tree] {
//    override def traverse[M[_], A, B](ta: Tree[A])(f: A => M[B])(implicit M: Applicative[M]): M[Tree[B]] =
//      M.map2(f(ta.head), listTraverse.traverse(ta.tail)(a => traverse(a)(f)))(Tree(_, _))
//  }
}