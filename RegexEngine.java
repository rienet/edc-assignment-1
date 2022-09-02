import java.util.Scanner;
import java.util.HashMap; // import the HashMap class
import java.util.ArrayList;

public class RegexEngine {
    public void dummyMethod() {
        System.out.println("Enter username");
    }

    public static void main(String[] args) {
        if(args.length > 0){
            System.out.println("flag on");
        }

        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter username");

        String userName = myObj.nextLine();
        System.out.println("Username is: " + userName);
    }
}