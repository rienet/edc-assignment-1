import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap; // import the HashMap class
import java.util.Iterator;
import java.util.List;
import java.lang.Character;

public class RegexEngine {
    static HashMap<Character, Character> operators = new HashMap<Character, Character>();
    static boolean first = true;

    // parses given regex and creates a e-nfa out of it
    static Graph parseLine(String line) {
        Graph epsilonNFA = new Graph();
        for(int i = 0 ; i<line.length(); i++){
            Character currentChar = line.charAt(i);

            // handling operators
            if(operators.containsValue(currentChar)){

            // handling non-operators
            } else if(Character.isLetter(currentChar) || Character.isDigit(currentChar) || 
                      Character.isWhitespace(currentChar)){
                        
                boolean skip = false;
                boolean last = false;
                // check if next char is an operator, if so skip to process operator
                try{
                    if(operators.containsValue(line.charAt(i+1))){
                        skip = true;
                    }
                // must be last character if exception occured
                } catch(Exception e){
                    epsilonNFA.addNode(epsilonNFA.adj_list.size()-2);
                    int size = epsilonNFA.adj_list.size();
                    epsilonNFA.addEdge(size-3, size-2, Character.toString(currentChar));
                    epsilonNFA.addEdge(size-2, size-1, "e");
                    last = true;
                }

                if(!skip && !last){
                    // handle first character in a block
                    if (first){
                        // append onto latest node on block
                        epsilonNFA.addNode(epsilonNFA.adj_list.size()-2);
                        epsilonNFA.addNode(epsilonNFA.adj_list.size()-2);
                        // change for alternators later
                        int size = epsilonNFA.adj_list.size();
                        epsilonNFA.addEdge(size-4, size-3, "e");
                        epsilonNFA.addEdge(size-3, size-2, Character.toString(currentChar));

                        first = false;
                    } else {
                        // append onto latest node on block
                        epsilonNFA.addNode(epsilonNFA.adj_list.size()-2);
                        int size = epsilonNFA.adj_list.size();
                        epsilonNFA.addEdge(size-3, size-2, Character.toString(currentChar));
                    }
                }
            }
        }
        epsilonNFA.printGraph(epsilonNFA);
        return epsilonNFA;
    }

    // evaluates given input on a line against e nfa
    static void evaluateInput(Graph epsilonNFA, String line) {
        ArrayList<Character> baseState = epsilonNFA.initialiseBaseState(epsilonNFA);
        
        System.out.print("Base state: ");
        System.out.println(baseState);

        epsilonNFA.helperState(epsilonNFA, line);
        epsilonNFA.flushState();

    }

    public static void main(String[] args) {
        operators.put('(', '(');
        operators.put(')', ')');
        operators.put('|', '|');
        operators.put('+', '+');
        operators.put('*', '*');

        
        if(args.length > 0){
            System.out.println("flag on");
        }

        Scanner myObj = new Scanner(System.in);
        String regex = myObj.nextLine();
        Graph stateDiagram = parseLine(regex);
        System.out.println(regex);
        System.out.println("ready");
        
        while(true){
            String input = myObj.nextLine();
            evaluateInput(stateDiagram, input);
        }
    }
}

// class that defines transition of states
class Edge {
    int src;
    int dest;
    String transition;
    Edge(int src, int dest, String transition) {
        this.src = src;
        this.dest = dest;
        this.transition = transition;
    }
}
// Graph class
class Graph {
    // node of adjacency list 
    static class Node {
        int dest;
        String transition;
        Node(int dest, String transition)  {
            this.dest = dest;
            this.transition = transition;
        }
    };
 
    // define adjacency list
    List<List<Node>> adj_list = new ArrayList<>();

    // keeps track of state of nfa
    ArrayList<Character> state = new ArrayList<Character>();
    ArrayList<Character> bufferState = new ArrayList<Character>();
 
    // Graph Constructor
    public Graph()
    {
        // add start and end node
        adj_list.add(new ArrayList<>());
        adj_list.add(new ArrayList<>());
 
    }

    // add a transition to a source node to destination node
    public void addEdge(int src, int dest, String transition) {
        adj_list.get(src).add(new Node(dest, transition));
    }

    // delete a transition to a source node to destination node
    public void deleteEdge(int src, int dest, String transition) {
        Iterator<Node> it = adj_list.get(src).iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.dest == dest && node.transition.equals(transition)) {
                it.remove();
            }
        }
    }

    // add a new state to the system (e.g q1, q2, q3...)
    public void addNode(int newState) {
        adj_list.add(newState, new ArrayList<>());
    }

    // print adjacency list for the graph
    public void printGraph(Graph graph)  {
        int src_vertex = 0;
        int list_size = graph.adj_list.size();
 
        System.out.println("The contents of the graph:");
        while (src_vertex < list_size) {
            //traverse through the adjacency list and print the edges
            for (Node edge : graph.adj_list.get(src_vertex)) {
                System.out.print("Vertex:" + src_vertex + " ==> q" + edge.dest + 
                                " (" + edge.transition + ")\t");
            }
 
            System.out.println();
            src_vertex++;
        }
    }

    // initialise base states of string transition function and return the base states
    // in a list format
    public ArrayList<Character> initialiseBaseState(Graph graph)  {
        ArrayList<Character> visited = new ArrayList<Character>();

        for(int i = 0; i<graph.adj_list.size(); i++){
            // initialise all states to be inactive
            state.add('n');
            visited.add('n');
            bufferState.add('n');
        }

        // start at the base node
        int SRC_VERTEX = 0;
        traverseBaseState(graph, SRC_VERTEX, visited);

        return state;
    }

    // traverse the graph recursively - base version where we only
    // set the epsilon states
    public void traverseBaseState(Graph graph, int currentState, List<Character> visited) {
        // log current state as visited and count it as a base state
        state.set(currentState, 'a');
        visited.set(currentState, 'v');

        // go to all nodes traversable by an epsilon if we havent been there before
        for (Node edge : graph.adj_list.get(currentState)) {
            if(edge.transition.equals("e") &&  visited.get(edge.dest) != 'v'){
                traverseBaseState(graph, edge.dest, visited);
            }
        }
    }

    // helper function to evaluate regex after base state is initialised
    public void helperState(Graph graph, String input){
        for(int i = 0; i<input.length(); i++){
            bufferState.replaceAll(e -> 'n');

            System.out.println("the loop is on: " + Character.toString(input.charAt(i)));
            // perform transition in active states
            for(int currentState = 0; currentState<state.size(); currentState++){
                if(state.get(currentState).equals('a')){
                    for (Node edge : graph.adj_list.get(currentState)) {
                        // transition on regex matches on edges
                        System.out.println("state is this: " + currentState);
                        System.out.println(edge.transition + " into " + edge.dest);
                        if(edge.transition.equals("e") || edge.transition.equals(Character.toString(input.charAt(i)))){
                            System.out.println("transition: " + Character.toString(input.charAt(i)));
                            bufferState.set(edge.dest, 'a');
                        }
                    }
                }
            }
            state.clear();
            state.addAll(bufferState);
        }

        bufferState.replaceAll(e -> 'n');

        // finish off by resolving e transitions
        for(int currentState = 0; currentState<state.size(); currentState++){
            if(state.get(currentState).equals('a')){
                for (Node edge : graph.adj_list.get(currentState)) {
                    // transition on regex matches on edges
                    System.out.println("state is this: " + currentState);
                    System.out.println(edge.transition + " into " + edge.dest);
                    if(edge.transition.equals("e")){
                        bufferState.set(edge.dest, 'a');
                    }
                }
            }
        }

        state.clear();
        state.addAll(bufferState);

        System.out.println(state);
    }

    public void flushState() {
        state.clear();
        bufferState.clear();
    }
}