package set0000.set200.set290.p295;

/**
 * Created by wangsenyuan on 11/10/2016.
 */
public class MedianFinder1 {
    private Node root;
    private Node medianLeft;
    private Node medianRight;
    private int size;

    public MedianFinder1() {
    }

    // Adds a number into the data structure.
    public void addNum(int num) {
        if (root == null) {
            root = new Node(num);
            medianLeft = root;
            medianRight = root;
        } else {
            root.addNode(num);
            if (size % 2 == 0) {
                if (num < medianLeft.data) {
                    medianRight = medianLeft;
                } else if (medianLeft.data <= num && num < medianRight.data) {
                    medianLeft = medianLeft.successor();
                    medianRight = medianRight.predecessor();
                } else if (medianRight.data <= num) {
                    medianLeft = medianRight;
                }
            } else {
                if (num < medianLeft.data) {
                    medianLeft = medianLeft.predecessor();
                } else {
                    medianRight = medianRight.successor();
                }
            }
        }
        size++;
    }

    // Returns the median of current data stream
    public double findMedian() {
        return (medianLeft.data + medianRight.data) / 2.0;
    }

    class Node {
        private Node parent;
        private Node left;
        private Node right;
        private int data;

        public Node(int data) {
            this.data = data;
        }

        public void addNode(int data) {
            if (data >= this.data) {
                if (right == null) {
                    right = new Node(data);
                    right.parent = this;
                } else
                    right.addNode(data);
            } else {
                if (left == null) {
                    left = new Node(data);
                    left.parent = this;
                } else
                    left.addNode(data);
            }
        }

        public Node predecessor() {
            if (left != null)
                return left.rightMost();

            Node predecessor = parent;
            Node child = this;

            while (predecessor != null && child != predecessor.right) {
                child = predecessor;
                predecessor = predecessor.parent;
            }

            return predecessor;
        }

        public Node successor() {
            if (right != null)
                return right.leftMost();

            Node successor = parent;
            Node child = this;

            while (successor != null && child != successor.left) {
                child = successor;
                successor = successor.parent;
            }

            return successor;
        }

        public Node leftMost() {
            if (left == null)
                return this;
            return left.leftMost();
        }

        private Node rightMost() {
            if (right == null)
                return this;
            return right.rightMost();
        }

    }

    public static void main(String[] args) {
        MedianFinder1 medianFinder1 = new MedianFinder1();
        medianFinder1.addNum(1);
        medianFinder1.addNum(2);
        System.out.println(medianFinder1.findMedian());
        medianFinder1.addNum(3);
        System.out.println(medianFinder1.findMedian());
        medianFinder1.addNum(4);
        System.out.println(medianFinder1.findMedian());
    }
}
