package problems.graph

abstract class GraphBase[T, U] {
  case class Edge(n1: Node, n2: Node, value: U) {
    def toTuple = (n1.value, n2.value, value)
    override def toString = value match {
      case () => n1.value + edgeSep + n2.value
      case v => n1.value + edgeSep + n2.value + labelSep + v
    }
  }
  case class Node(value: T) {
    var adj: List[Edge] = Nil
    // neighbors are all nodes adjacent to this node.
    def neighbors: List[Node] = adj.map(edgeTarget(_, this).get)
    def degree: Int = edges.length
  }

  var nodes: Map[T, Node] = Map()
  var edges: List[Edge] = Nil

  val edgeSep: String
  val labelSep: String = "/"

  // If the edge E connects N to another node, returns the other node,
  // otherwise returns None.
  def edgeTarget(e: Edge, n: Node): Option[Node]

  override def equals(o: Any) = o match {
    case g: GraphBase[_, _] => (nodes.keys.toList.diff(g.nodes.keys.toList) == Nil &&
      edges.map(_.toTuple).diff(g.edges.map(_.toTuple)) == Nil)
    case _ => false
  }
  def addNode(value: T) = {
    val n = new Node(value)
    nodes = Map(value -> n) ++ nodes
    n
  }

  override def toString = {
    val (edgeStrs, unlinkedNodes) =
      edges.foldLeft((Nil: List[String], nodes.values.toList))((r, e) => (e.toString :: r._1, r._2.filter((n) => n != e.n1 && n != e.n2)))
    "[" + (unlinkedNodes.map(_.value.toString) ::: edgeStrs).mkString(", ") + "]"
  }
  def toTermForm: (List[T], List[(T, T, U)]) =
    (nodes.keys.toList, edges.map((e) => (e.n1.value, e.n2.value, e.value)))
  def toAdjacentForm: List[(T, List[(T, U)])] =
    nodes.values.toList.map((n) => (n.value, n.adj.map((e) =>
      (edgeTarget(e, n).get.value, e.value))))

  def findPaths(source: T, dest: T): List[List[T]] = {

    def findPathsR(curNode: Node, curPath: List[T]): List[List[T]] = {
      if (curNode.value == dest) List(curPath)
      else curNode.adj.map(edgeTarget(_, curNode).get).filter(n => !curPath.contains(n.value)).flatMap(n => findPathsR(n, n.value :: curPath))
    }
    findPathsR(nodes(source), List(source)).map(_.reverse)
  }

  def findCycles(source: T): List[List[T]] = {
    val n = nodes(source)
    n.adj.map(edgeTarget(_, n).get.value).flatMap(findPaths(_, source)).map(source :: _).filter(_.lengthCompare(3) > 0)
  }

  def isIsomorphicTo[R, S](o: GraphBase[R, S]): Boolean = {
    // Build a lazy list so we only have to evaluate as much as necessary.
    def listMappings(tNodes: List[Node], oNodes: List[o.Node]) =
      tNodes.view.flatMap(tn => oNodes.view.map((tn, _)))
    // Used on partially-filled isomorphisms to weed out some early.
    def isValidMapping(iso: Map[Node, o.Node]): Boolean =
      nodes.values forall { tn =>
        (!iso.contains(tn) ||
          tn.neighbors.filter(iso.contains).forall(tnn => iso(tn).neighbors.contains(iso(tnn))))
      }
    def isValidCompleteMapping(iso: Map[Node, o.Node]): Boolean =
      nodes.values forall { tn =>
        Set(tn.neighbors.map(iso.apply(_)): _*) == Set(iso(tn).neighbors: _*)
      }
    def isIsomorphicToR(tNodes: List[Node], oNodes: List[o.Node], iso: Map[Node, o.Node]): Boolean =
      if (tNodes == Nil) isValidCompleteMapping(iso)
      else listMappings(tNodes, oNodes).filter(p => isValidMapping(iso + p)) exists { p =>
        isIsomorphicToR(tNodes.filterNot(_ == p._1), oNodes.filterNot(_ == p._2), iso + p)
      }
    isIsomorphicToR(nodes.values.toList, o.nodes.values.toList, Map())
  }

  def nodesByDegree: List[Node] = nodes.values.toList.sortWith(_.degree > _.degree)
  def colorNodes: List[(Node, Int)] = {
    import collection.immutable.Set
    def applyColor(color: Int, uncolored: List[Node], colored: List[(Node, Int)], adjacentNodes: Set[Node]): List[(Node, Int)] =
      uncolored match {
        case Nil => colored
        case n :: tail => {
          val newAdjacent = adjacentNodes ++ n.neighbors
          applyColor(color, tail.dropWhile(newAdjacent.apply), (n, color) :: colored, newAdjacent)
        }
      }
    def colorNodesR(color: Int, uncolored: List[Node], colored: List[(Node, Int)]): List[(Node, Int)] =
      if (uncolored == Nil) colored
      else {
        val newColored = applyColor(color, uncolored, colored, Set())
        colorNodesR(color + 1, uncolored.diff(newColored.map(_._1)), newColored)
      }
    colorNodesR(1, nodesByDegree, Nil)
  }
}

class Graph[T, U] extends GraphBase[T, U] {
  override def equals(o: Any) = o match {
    case g: Graph[_, _] => super.equals(g)
    case _ => false
  }

  def edgeTarget(e: Edge, n: Node): Option[Node] =
    if (e.n1 == n) Some(e.n2)
    else if (e.n2 == n) Some(e.n1)
    else None

  def addEdge(n1: T, n2: T, value: U) = {
    val e = new Edge(nodes(n1), nodes(n2), value)
    edges = e :: edges
    nodes(n1).adj = e :: nodes(n1).adj
    nodes(n2).adj = e :: nodes(n2).adj
  }

  val edgeSep: String = "-"

  // edgeConnectsToGraph is needed for P84, so it's not an internal function.
  def edgeConnectsToGraph[T, U](e: Edge, nodes: List[Node]): Boolean =
    !(nodes.contains(e.n1) == nodes.contains(e.n2)) // xor
  def spanningTrees = {
    def spanningTreesR(graphEdges: List[Edge], graphNodes: List[Node], treeEdges: List[Edge]): List[Graph[T, U]] = {
      if (graphNodes == Nil) List(Graph.termLabel(nodes.keys.toList, treeEdges.map(_.toTuple)))
      else if (graphEdges == Nil) Nil
      else graphEdges.filter(edgeConnectsToGraph(_, graphNodes)) flatMap { ge =>
        spanningTreesR(graphEdges.filterNot(_ == ge),
          graphNodes.filter(edgeTarget(ge, _) == None),
          ge :: treeEdges)
      }
    }
    spanningTreesR(edges, nodes.values.toList.tail, Nil).distinct
  }

  def minimalSpanningTree(implicit f: (U) => Ordered[U]): Graph[T, U] = {
    def minimalSpanningTreeR(graphEdges: List[Edge], graphNodes: List[Node], treeEdges: List[Edge]): Graph[T, U] =
      if (graphNodes == Nil) Graph.termLabel(nodes.keys.toList, treeEdges.map(_.toTuple))
      else {
        val nextEdge = graphEdges.filter(edgeConnectsToGraph(_, graphNodes)).reduceLeft((r, e) => if (r.value < e.value) r else e)
        minimalSpanningTreeR(graphEdges.filterNot(_ == nextEdge),
          graphNodes.filter(edgeTarget(nextEdge, _) == None),
          nextEdge :: treeEdges)
      }
    minimalSpanningTreeR(edges, nodes.values.toList.tail, Nil)
  }

  def isTree: Boolean = spanningTrees.lengthCompare(1) == 0
  def isConnected: Boolean = spanningTrees.lengthCompare(0) > 0
}

class Digraph[T, U] extends GraphBase[T, U] {
  override def equals(o: Any) = o match {
    case g: Digraph[_, _] => super.equals(g)
    case _ => false
  }

  def edgeTarget(e: Edge, n: Node): Option[Node] =
    if (e.n1 == n) Some(e.n2)
    else None

  def addArc(source: T, dest: T, value: U) = {
    val e = new Edge(nodes(source), nodes(dest), value)
    edges = e :: edges
    nodes(source).adj = e :: nodes(source).adj
  }

  val edgeSep: String = ">"
}

abstract class GraphObjBase {
  type GraphClass[T, U]
  def addLabel[T](edges: List[(T, T)]) =
    edges.map(v => (v._1, v._2, ()))
  def term[T](nodes: List[T], edges: List[(T, T)]) =
    termLabel(nodes, addLabel(edges))
  def termLabel[T, U](nodes: List[T], edges: List[(T, T, U)]): GraphClass[T, U]
  def addAdjacentLabel[T](nodes: List[(T, List[T])]) =
    nodes.map(a => (a._1, a._2.map((_, ()))))
  def adjacent[T](nodes: List[(T, List[T])]) =
    adjacentLabel(addAdjacentLabel(nodes))
  def adjacentLabel[T, U](nodes: List[(T, List[(T, U)])]): GraphClass[T, U]

  val edgeSep: String
  val labelSep: String = "/"

  def fromStringBase[U, V](s: String)(mkGraph: (List[String], List[V]) => GraphClass[String, U])(parseEdge: String => V): GraphClass[String, U] = {
    assert(s(0) == '[')
    assert(s(s.length - 1) == ']')
    val tokens = s.substring(1, s.length - 1).split(", *").toList
    val nodes = tokens.flatMap(_.replaceAll(labelSep + ".*", "").split(edgeSep)).distinct
    val edges = tokens.filter(_.matches(".*" + edgeSep + ".*")).map(parseEdge)
    mkGraph(nodes, edges)
  }
  def fromString(s: String): GraphClass[String, Unit] =
    fromStringBase(s)(term[String]) { t =>
      val split = t.split(edgeSep)
      (split(0), split(1))
    }
  def fromStringLabel(s: String): GraphClass[String, Int] =
    fromStringBase(s)(termLabel[String, Int]) { t =>
      val split = t.split(edgeSep)
      val split2 = split(1).split(labelSep)
      (split(0), split2(0), split2(1).toInt)
    }
}

object Graph extends GraphObjBase {
  type GraphClass[T, U] = Graph[T, U]

  def termLabel[T, U](nodes: List[T], edges: List[(T, T, U)]) = {
    val g = new Graph[T, U]
    nodes.map(g.addNode)
    edges.map(v => g.addEdge(v._1, v._2, v._3))
    g
  }
  def adjacentLabel[T, U](nodes: List[(T, List[(T, U)])]) = {
    val g = new Graph[T, U]
    for ((v, a) <- nodes) g.addNode(v)
    for ((n1, a) <- nodes; (n2, l) <- a) {
      if (!g.nodes(n1).neighbors.contains(g.nodes(n2)))
        g.addEdge(n1, n2, l)
    }
    g
  }

  val edgeSep: String = "-"
}

object Digraph extends GraphObjBase {
  type GraphClass[T, U] = Digraph[T, U]

  def termLabel[T, U](nodes: List[T], edges: List[(T, T, U)]) = {
    val g = new Digraph[T, U]
    nodes.map(g.addNode)
    edges.map(v => g.addArc(v._1, v._2, v._3))
    g
  }
  def adjacentLabel[T, U](nodes: List[(T, List[(T, U)])]) = {
    val g = new Digraph[T, U]
    for ((n, a) <- nodes) g.addNode(n)
    for ((s, a) <- nodes; (d, l) <- a) g.addArc(s, d, l)
    g
  }

  val edgeSep: String = ">"
}