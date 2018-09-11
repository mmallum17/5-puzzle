import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by Marcus on 9/6/2018.
 */
public class FivePuzzleSolver {

    private static Node goalNode = null;
    private static final String GOAL_STATE = "123450";
    private static final int SOLUTION = 0;
    private static final int FAILURE = 1;
    private static final int CUTOFF = 2;
    private static final int DOWN = 0;
    private static final int RIGHT = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;
    private static LinkedList<String> exploredStates = new LinkedList<String>();

    public static void main(String[] args) {
        String initialState = getInitialState();
        boolean validInitialState = validateInitialState(initialState);

        // If initial state is valid, solve the puzzle. Else, end program
        if(validInitialState) {
            System.out.println("Goal State is " + GOAL_STATE);

            int result = iterativeDeepeningSearch(initialState);

            // If result is solvable, print out the path. Else, display "No solution"
            if(result == SOLUTION) {
                System.out.print("Solution: ");
                displayPath(goalNode);
            }
            else {
                System.out.println("No Solution");
            }
        }
        else {
            System.out.println("Input for the initial state is invalid");
        }
    }

    private static String getInitialState() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter in the initial state (Ex: '013254'): ");
        String input = scanner.nextLine();

        return input;
    }

    private static boolean validateInitialState(String initialState) {
        boolean valid = true;
        if(initialState.length() != 6) {
            valid = false;
        }
        else {
            for(int i = 0; i <= 5; i++) {
                if(!initialState.contains(Integer.toString(i))) {
                    valid = false;
                }
            }
        }
        return valid;
    }

    private static int iterativeDeepeningSearch(String initialState) {
        int result = CUTOFF;
        for(int depthLimit = 0; result == CUTOFF; depthLimit++) {
            exploredStates.clear();
            result = depthLimitedSearch(initialState, depthLimit);
        }
        return result;
    }

    private static int depthLimitedSearch(String initialState, int depthLimit) {
        return recursiveDLS(new Node(initialState, null, 0, 0), depthLimit);
    }

    private static int recursiveDLS(Node node, int depthLimit) {
        boolean cutoffOccurred = false;
        exploredStates.add(node.state);
        //System.out.println(node.state);

        if(node.state.equals(GOAL_STATE)) {
            goalNode = node;
            return SOLUTION;
        }
        else if(node.depth == depthLimit) {
            return CUTOFF;
        }
        else {
            LinkedList<Node> successors = getSuccessors(node);
            for(int i = 0; i < successors.size(); i++) {
                int result = recursiveDLS(successors.get(i), depthLimit);
                if(result == CUTOFF) {
                    cutoffOccurred = true;
                }
                else if(result != FAILURE) {
                    return result;
                }
            }
        }
        if(cutoffOccurred) {
            return CUTOFF;
        }
        else {
            return FAILURE;
        }
    }

    private static LinkedList<Node> getSuccessors(Node node) {
        String state = node.state;
        LinkedList<Node> successors = new LinkedList<Node>();

        int blankSpace = state.indexOf('0');
        // Check down
        if(blankSpace >= 3 && blankSpace <= 5) {
            String newState = swap(state, blankSpace, blankSpace - 3);
            if(!exploredStates.contains(newState)) {
                successors.add(new Node(newState, node, DOWN, node.depth + 1));
            }
        }

        // Check right
        if(blankSpace == 1 || blankSpace == 2 || blankSpace == 4 || blankSpace == 5) {
            String newState = swap(state, blankSpace, blankSpace - 1);
            if(!exploredStates.contains(newState)) {
                successors.add(new Node(newState, node, RIGHT, node.depth + 1));
            }
        }

        // Check left
        if(blankSpace == 0 || blankSpace == 1 || blankSpace == 3 || blankSpace == 4) {
            String newState = swap(state, blankSpace, blankSpace + 1);
            if(!exploredStates.contains(newState)) {
                successors.add(new Node(newState, node, LEFT, node.depth + 1));
            }
        }

        // Check up
        if(blankSpace >= 0 && blankSpace <= 2) {
            String newState = swap(state, blankSpace, blankSpace + 3);
            if(!exploredStates.contains(newState)) {
                successors.add(new Node(newState, node, UP, node.depth + 1));
            }
        }
        return successors;
    }

    private static String swap(String str, int i, int j)
    {
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(i, str.charAt(j));
        sb.setCharAt(j, str.charAt(i));
        return sb.toString();
    }

    private static void displayPath(Node goalNode) {
        StringBuilder sb = new StringBuilder();
        Node node = goalNode;

        while(node.parentNode != null) {
            switch(node.action){
                case DOWN:
                    sb.append('D');
                    break;
                case RIGHT:
                    sb.append('R');
                    break;
                case LEFT:
                    sb.append('L');
                    break;
                case UP:
                    sb.append('U');
                    break;
            }
            node = node.parentNode;
        }

        sb = sb.reverse();
        for(int i = 0; i < sb.length(); i++) {
            System.out.print(sb.charAt(i));
            if(i < sb.length() - 1) {
                System.out.print('-');
            }
        }
    }
}
