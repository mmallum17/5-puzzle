/**
 * The FivePuzzleSolver implements the iterative deepening search algorithm to solve a 5-puzzle.
 *
 * File: FivePuzzleSolver.java
 * Author: Marcus Mallum
 * Date: 9/11/2018
 */

import java.util.LinkedList;
import java.util.Scanner;

public class FivePuzzleSolver {
    private static Node goalNode = null;
    private static final String GOAL_STATE = "123450";

    // Possible results from iterative deepening search
    private static final int SOLUTION = 0;
    private static final int FAILURE = 1;
    private static final int CUTOFF = 2;

    // Possible actions for a tile
    private static final int DOWN = 0;
    private static final int RIGHT = 1;
    private static final int LEFT = 2;
    private static final int UP = 3;

    // States that have already been explored
    private static LinkedList<String> exploredStates = new LinkedList<String>();

   /**
    * Method: main
    * Purpose: Controls execution of the program
    * Parameters: String[] args
    * Return value: void
    */
    public static void main(String[] args) {
        // Get the initial state and validate it
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

   /**
    * Method: getInitialState
    * Purpose: Prompts the user for the initial state and returns it as a string
    * Parameters: void
    * Return value: String, the initial state entered by the user
    */
    private static String getInitialState() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter in the initial state (Ex: '013254'): ");
        String input = scanner.nextLine();

        return input;
    }

   /**
    * Method: validateInitialState
    * Purpose: Determines whether the initial state is valid
    * Parameters: String, the initial state of the puzzle
    * Return value: boolean, true if the initial state is valid, otherwise false
    */
    private static boolean validateInitialState(String initialState) {
        boolean valid = true;

        // If incorrect length, initial state is invalid
        if(initialState.length() != 6) {
            valid = false;
        }
        else {
            for(int i = 0; i <= 5; i++) {
                // If a tile is missing, the initial state is invalid
                if(!initialState.contains(Integer.toString(i))) {
                    valid = false;
                }
            }
        }
        return valid;
    }

   /**
    * Method: iterativeDeepeningSearch
    * Purpose: Implementation of iterative deepening search
    * Parameters: String, the initial state of the puzzle
    * Return value: int, If the puzzle has been solved it returns SOLUTION, otherwise FAILURE
    */
    private static int iterativeDeepeningSearch(String initialState) {
        int result = CUTOFF;
        for(int depthLimit = 0; result == CUTOFF; depthLimit++) {
            exploredStates.clear();
            result = depthLimitedSearch(initialState, depthLimit);
        }
        return result;
    }

   /**
    * Method: depthLimitedSearch
    * Purpose: Implementation of depth limited search
    * Parameters: String, the initial state of the puzzle
    *             int, the depth limit for the search
    * Return value: int, If the puzzle has been solved it returns SOLUTION. Else if the puzzle is not solvable
    *                    it returns FAILURE. Else if the depth limit has been reach, CUTOFF is returned
    */
    private static int depthLimitedSearch(String initialState, int depthLimit) {
        return recursiveDLS(new Node(initialState, null, 0, 0), depthLimit);
    }

   /**
    * Method: recursiveDLS
    * Purpose: Implementation of recursive depth limited search
    * Parameters: Node, a state of the puzzle
    *             int, the depth limit for the search
    * Return value: int, If the puzzle has been solved it returns SOLUTION. Else if the puzzle is not solvable
    *                    it returns FAILURE. Else if the depth limit has been reach, CUTOFF is returned.
    */
    private static int recursiveDLS(Node node, int depthLimit) {
        boolean cutoffOccurred = false;
        exploredStates.add(node.state);

        // If goal state is found, the puzzle has a solution
        if(node.state.equals(GOAL_STATE)) {
            goalNode = node;
            return SOLUTION;
        }
        else if(node.depth == depthLimit) {
            // If node has reached the depth limit, return CUTOFF value
            return CUTOFF;
        }
        else {
            // Get successors, and keep searching
            LinkedList<Node> successors = getSuccessors(node);
            for(int i = 0; i < successors.size(); i++) {
                int result = recursiveDLS(successors.get(i), depthLimit);
                if(result == CUTOFF) {
                    cutoffOccurred = true;
                }
                else if(result != FAILURE) {
                    // Solution has been found
                    return result;
                }
            }
        }
        if(cutoffOccurred) {
            // search has reach its depth limit
            return CUTOFF;
        }
        else {
            // puzzle is not solvable
            return FAILURE;
        }
    }

   /**
    * Method: getSuccessors
    * Purpose: Finds the successors of a node and returns them
    * Parameters: Node, a state of the puzzle
    * Return value: LinkedList, the successors of the given node
    */
    private static LinkedList<Node> getSuccessors(Node node) {
        String state = node.state;
        LinkedList<Node> successors = new LinkedList<Node>();

        // Get the position of the blank space
        int blankSpace = state.indexOf('0');

        // Check possible actions. If action is possible, add a successor.
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

   /**
    * Method: swap
    * Purpose: Swap two characters in a string
    * Parameters: String, string to modify
    *             int, index of first character to swap
    *             int, index of second character to swap
    * Return value: String, string with the 2 characters swapped
    */
    private static String swap(String str, int i, int j)
    {
        StringBuilder sb = new StringBuilder(str);
        sb.setCharAt(i, str.charAt(j));
        sb.setCharAt(j, str.charAt(i));
        return sb.toString();
    }

   /**
    * Method: displayPath
    * Purpose: Display the final path of the puzzle
    * Parameters: Node, the goal node from the puzzle
    * Return value: void
    */
    private static void displayPath(Node goalNode) {
        StringBuilder sb = new StringBuilder();
        Node node = goalNode;

        // Iterate from the goal node up to the root, tracking the action from child to parent
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

        // Since iteration was done from goal node to root, the path needs to be reversed
        sb = sb.reverse();

        // Print out the path
        for(int i = 0; i < sb.length(); i++) {
            System.out.print(sb.charAt(i));
            if(i < sb.length() - 1) {
                System.out.print('-');
            }
        }
        System.out.println();
    }
}
