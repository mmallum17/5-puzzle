/**
 * The Node class represents a state in the 5-puzzle problem.
 * It holds the state, parent node, action, and depth.
 *
 * File: Node.java
 * Author: Marcus Mallum
 * Date: 9/11/2018
 */
public class Node {
    String state;
    Node parentNode;
    int action;
    int depth;

    public Node(String state, Node parentNode, int action, int depth) {
        this.state = state;
        this.parentNode = parentNode;
        this.action = action;
        this.depth = depth;
    }
}
