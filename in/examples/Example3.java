package examples;// conditional statements and loops

import java.util.Scanner;

public class Example3 {
    public static void main(String[] args) {

        int myNum = 0;
        double number = 12.3;

        if(number < 0.0){
            System.out.println(number + " is a negative number");
        }
        else if(number > 0.0){
            System.out.println(number + " is a positive number");
        }
        else{
            System.out.println(number + " is 0.");
        }

        for(myNum = 0; myNum<5; myNum++){
            System.out.println(myNum);
        }

    }
}