import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap; // import the HashMap class
import java.util.Iterator;
import java.util.List;
import java.lang.Character;

public class RegexEngine {
    static HashMap<Character, Character> operators = new HashMap<Character, Character>();

    static void parseLine(String line) {
        for(int i = 0 ; i<line.length(); i++){

        }
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

        Graph epsilonNFA = new Graph();
        epsilonNFA.printGraph(epsilonNFA);
        epsilonNFA.addNode(1);
        epsilonNFA.addEdge(0, 1, "x");
        epsilonNFA.addEdge(1, 2, "e");

        epsilonNFA.printGraph(epsilonNFA);

        epsilonNFA.deleteEdge(0, 1, "e");

        epsilonNFA.printGraph(epsilonNFA);

        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter username");

        parseLine("testing");
        

        String userName = myObj.nextLine();
        System.out.println("Username is: " + userName);
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
 
    // Graph Constructor
    public Graph()
    {
        // add start and end node
        adj_list.add(new ArrayList<>());
        adj_list.add(new ArrayList<>());
 
        // draw epsilon edge
        adj_list.get(0).add(new Node(1, "e"));
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
                System.out.print("Vertex:" + src_vertex + " ==> " + edge.dest + 
                                " (" + edge.transition + ")\t");
            }
 
            System.out.println();
            src_vertex++;
        }
    }
}