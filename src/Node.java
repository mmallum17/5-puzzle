/**
 * Created by Marcus on 9/6/2018.
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
