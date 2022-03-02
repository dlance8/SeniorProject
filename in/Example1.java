public class Example1 {
    public static void main(String[] args){
        System.out.println("Hello World");
    }
}


/**
 *
 * class MyClass:
 * 		def main():
 * 			print("Hello, World")
 *
 *
 * ClassDeclaration.getKeyword().getIdentifier()
 * ClassBody.ClassBodyDeclaration.MainMethodDeclaration.getIdentifier().getArguments().getMethodBody()
 *
 * makeClass(){
 *      print(keyword + identifier + ":" + "\n" + "\t+)  // class MyClass: newline + tab
 *
 * makeMainMethod(){
 *      print("def" + identifier + "(" + arguments ")" + ":" + "\n" + "\t"
 *      + methodBody
 *
 * }
 *
 * makeMethod(){
 *
 * }
 * }
 */