package examples;// user input, using multiple classes

import java.util.Scanner;

public class Example4 {
    static void myMethod(){
        System.out.println("I just got executed!");
    }

    static void myMethod2(String name){
        System.out.println("My name is " + name);
    }

    static int myMethod3(int x){
        return x * 10;
    }

}

class Main{
    public static void main(String[] args){
        Example4 classObj = new Example4();
        classObj.myMethod();

        Scanner scan = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String myName = scan.nextLine();
        classObj.myMethod2(myName);

        System.out.println("Enter a number: ");
        int myInt = scan.nextInt();
        System.out.println(classObj.myMethod3(myInt));
    }
}
