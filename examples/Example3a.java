// Direct Translation of a Java class with multiple methods

public class Example3a {
    static void myMethod() {
        System.out.println("myMethod() just got executed!");
    }

    public static void main(String[] args){
        System.out.println("Main() is now executing!");
        myMethod();
    }
}


/**
 *
 * class Example3a:
 *     def myMethod(self):
 *         print("myMethod() just got executed!")
 *
 *     def main(self):
 *         print("Main() is now executing!")
 *         self.myMethod()
 *
 *
 * if __name__=="__main__":
 *     obj = Example3a()
 *     obj.main()
 *
 */

