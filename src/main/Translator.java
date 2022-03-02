package main;
import constants.Nonterminal;
import tree.NonterminalNode;

public class Translator extends MyProcess {
	public static void main(String[] args)  {
		new Translator().translate(new Parser().parse(new Lexer().lexFromFile("in/MyClass.java")));

	}



	private final StringBuilder indent = new StringBuilder();
	private final StringBuilder out = new StringBuilder();

	public String translate(NonterminalNode root) {
		//compilationUnit(root.getNonTerminalChild(0));

		return out.toString();
	}

	private void print(String text) {
		out.append(text);
	}
	private void println(String text) {
		out.append(text).append('\n').append(indent);
	}

	private void increaseIndent() {
		indent.append('\t');
	}
	private void decreaseIndent() {
		if (indent.length() < 1) return;
		indent.setLength(indent.length() - 1);
	}


//
//	private void typeDeclaration(NonterminalNode parent) {
////TypeDeclaration = ClassDeclaration
////                | InterfaceDeclaration
////                | ";" ;
//
//		classDeclaration(parent.getNonTerminalChild(0));
//	}
//	private void classDeclaration(NonterminalNode parent) {
//		// ClassDeclaration = NormalClassDeclaration
//		//                  | EnumDeclaration ;
//		normalClassDeclaration(parent.getNonTerminalChild(0));
//	}
//	private void normalClassDeclaration(NonterminalNode parent) {
//		// NormalClassDeclaration = { ClassModifier } , "class" , Identifier , [ TypeParameters ] , [ Superclass ] , [ Superinterfaces ] , ClassBody ;
//
//
//		int index = 0;
//
//		for (; index < parent.getChildren().size(); ++index) {
//			if (parent.getChildren().get(index) instanceof TerminalNode) break;
//			// else skip. we won't translate class modifiers
//		}
//
//
//		print("class ");
//		index++;
//
//		String name = parent.getNonTerminalChild(index).getTerminalChild(0).getToken().getText();
//		print(name);
//
//		increaseIndent();
//
//		println(":");
//
//		while (parent.getNonTerminalChild(index).getValue() != Nonterminal.CLASS_BODY)
//			++index;
//
//		classBody(parent.getNonTerminalChild(index));
//	}
//	private void classBody(NonterminalNode parent) {
//		// ClassBody = "{" , { ClassBodyDeclaration } , "}" ;
//
//		// Skip the first and last children, as they are braces, which Python does not require
//		for (int i = 1; i < parent.getChildren().size() - 1; ++i) {
//			classBodyDeclaration(parent.getNonTerminalChild(i));
//		}
//	}
//	private void classBodyDeclaration(NonterminalNode parent) {
//		// ClassBodyDeclaration = ClassMemberDeclaration
//		//                      | InstanceInitializer
//		//                      | StaticInitializer
//		//                      | ConstructorDeclaration ;
//
//		NonterminalNode child = parent.getNonTerminalChild(0);
//
//		switch (child.getValue()) {
//			case CLASS_MEMBER_DECLARATION:
//				classMemberDeclaration(child);
//				break;
//			case INSTANCE_INITIALIZER:
//			case STATIC_INITIALIZER:
//				// skip, don't support these
//				break;
//			case CONSTRUCTOR_DECLARATION:
//				constructorDeclaration(child);
//				break;
//		}
//	}
//	private void classMemberDeclaration(NonterminalNode parent) {
//		// ClassMemberDeclaration = FieldDeclaration
//		//                        | MethodDeclaration
//		//                        | ClassDeclaration
//		//                        | InterfaceDeclaration
//		//                        | ";" ;
//
//		if (parent.getChildren().get(0) instanceof TerminalNode)
//			return; // SKIP
//
//		NonterminalNode child = parent.getNonTerminalChild(0);
//		switch (child.getValue()) {
//			case FIELD_DECLARATION:
//				fieldDeclaration(child);
//				break;
//		}
//	}
//	private void constructorDeclaration(NonterminalNode parent) {
//
//	}

	private void type(NonterminalNode parent) {
		// Type = PrimitiveType
		//      | ReferenceType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void primitiveType(NonterminalNode parent) {
		// PrimitiveType = { Annotation } , NumericType
		//               | { Annotation } , "boolean" ;

		// DO NOTHING
	}
	private void numericType(NonterminalNode parent) {
		// NumericType = IntegralType
		//             | FloatingPointType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void integralType(NonterminalNode parent) {
		// IntegralType = "byte" | "short" | "int" | "long" | "char" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void floatingPointType(NonterminalNode parent) {
		// FloatingPointType = "float" | "double" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void referenceType(NonterminalNode parent) {
		// ReferenceType = ArrayType
		//               | ClassOrInterfaceType
		//               | TypeVariable ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classOrInterfaceType(NonterminalNode parent) {
		// ClassOrInterfaceType = ClassType
		//                      | InterfaceType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classType(NonterminalNode parent) {
		// ClassType = { Annotation } , Identifier , [ TypeArguments ]
		//           | ClassOrInterfaceType , "." , { Annotation } , Identifier , [ TypeArguments ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void interfaceType(NonterminalNode parent) {
		// InterfaceType = ClassType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeVariable(NonterminalNode parent) {
		// TypeVariable = { Annotation } , Identifier ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void arrayType(NonterminalNode parent) {
		// ArrayType = PrimitiveType , Dims
		//           | ClassType , Dims
		//           | TypeVariable , Dims ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void dims(NonterminalNode parent) {
		// Dims = { Annotation } , "[" , "]" , { { Annotation } , "[" , "]" } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeParameter(NonterminalNode parent) {
		// TypeParameter = { TypeParameterModifier } , Identifier , [ TypeBound ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeParameterModifier(NonterminalNode parent) {
		// TypeParameterModifier = Annotation ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeBound(NonterminalNode parent) {
		// TypeBound = "extends" , TypeVariable
		//           | "extends" , ClassOrInterfaceType , { AdditionalBound } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void additionalBound(NonterminalNode parent) {
		// AdditionalBound = "&" , InterfaceType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeArguments(NonterminalNode parent) {
		// TypeArguments = "<" , TypeArgumentList , ">" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeArgumentList(NonterminalNode parent) {
		// TypeArgumentList = TypeArgument , { "," , TypeArgument } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeArgument(NonterminalNode parent) {
		// TypeArgument = ReferenceType
		//              | Wildcard;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void wildcard(NonterminalNode parent) {
		// Wildcard = { Annotation } , "?" , [ WildcardBounds ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void wildcardBounds(NonterminalNode parent) {
		// WildcardBounds = "extends" , ReferenceType
		//                | "super" , ReferenceType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void packageName(NonterminalNode parent) {
		// PackageName = Identifier , { "." , Identifier } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeName(NonterminalNode parent) {
		// TypeName = Identifier , { "." , Identifier } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void packageOrTypeName(NonterminalNode parent) {
		// PackageOrTypeName = Identifier , { "." , Identifier } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void expressionName(NonterminalNode parent) {
		// ExpressionName = Identifier , { "." , Identifier } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void methodName(NonterminalNode parent) {
		// MethodName = Identifier ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void ambiguousName(NonterminalNode parent) {
		// AmbiguousName = Identifier , { "." , Identifier } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void compilationUnit(NonterminalNode parent) {
		// CompilationUnit = [ PackageDeclaration ] , { ImportDeclaration } , { TypeDeclaration } ;

		if (parent.getChildren().size() == 0)
			return;

		NonterminalNode child;
		int index = 0;
		child = parent.getNonTerminalChild(index);
		if (child.getValue() == Nonterminal.PACKAGE_DECLARATION) {
			packageDeclaration(child);
			++index;
		}

		while (index < parent.size()) {
			child = parent.getNonTerminalChild(index);
			if (child.getValue() == Nonterminal.IMPORT_DECLARATION) {
				importDeclaration(child);
				++index;
			} else {
				break;
			}
		}

		while (true) {
			child = parent.getNonTerminalChild(index);
			if (child.getValue() == Nonterminal.IMPORT_DECLARATION) {
				importDeclaration(child);
				++index;
			} else {
				break;
			}
		}


		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
//	private void compilationUnit(NonterminalNode parent) {
//		// CompilationUnit = { TypeDeclaration } ;
//
//		for (int i = 0; i < parent.getChildren().size(); ++i) {
//			typeDeclaration(parent.getNonTerminalChild(i));
//		}
//	}
	private void packageDeclaration(NonterminalNode parent) {
		// PackageDeclaration = { PackageModifier } , "package" , Identifier , { "." , Identifier }, ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void packageModifier(NonterminalNode parent) {
		// PackageModifier = Annotation ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void importDeclaration(NonterminalNode parent) {
		// ImportDeclaration = SingleTypeImportDeclaration
		//                   | TypeImportOnDemandDeclaration
		//                   | SingleStaticImportDeclaration
		//                   | StaticImportOnDemandDeclaration ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void singleTypeImportDeclaration(NonterminalNode parent) {
		// SingleTypeImportDeclaration = "import" , TypeName , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeImportOnDemandDeclaration(NonterminalNode parent) {
		// TypeImportOnDemandDeclaration = "import" , PackageOrTypeName , "." , "*" , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void singleStaticImportDeclaration(NonterminalNode parent) {
		// SingleStaticImportDeclaration = "import" , "static" , TypeName , "." , Identifier , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void staticImportOnDemandDeclaration(NonterminalNode parent) {
		// StaticImportOnDemandDeclaration = "import" , "static" , TypeName , "." , "*" , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeDeclaration(NonterminalNode parent) {
		// TypeDeclaration = ClassDeclaration
		//                 | InterfaceDeclaration
		//                 | ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classDeclaration(NonterminalNode parent) {
		// ClassDeclaration = NormalClassDeclaration
		//                  | EnumDeclaration ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void normalClassDeclaration(NonterminalNode parent) {
		// NormalClassDeclaration = { ClassModifier } , "class" , Identifier , [ TypeParameters ] , [ Superclass ] , [ Superinterfaces ] , ClassBody ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classModifier(NonterminalNode parent) {
		// ClassModifier = Annotation | "public" | "protected" | "private" | "abstract" | "static" | "final" | "strictfp" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeParameters(NonterminalNode parent) {
		// TypeParameters = "<" , TypeParameterList , ">" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeParameterList(NonterminalNode parent) {
		// TypeParameterList = TypeParameter , { "," , TypeParameter } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void superclass(NonterminalNode parent) {
		// Superclass = "extends" , ClassType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void superinterfaces(NonterminalNode parent) {
		// Superinterfaces = "implements" , InterfaceTypeList ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void interfaceTypeList(NonterminalNode parent) {
		// InterfaceTypeList = InterfaceType , { "," , InterfaceType } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classBody(NonterminalNode parent) {
		// ClassBody = "{" , { ClassBodyDeclaration } , "}" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classBodyDeclaration(NonterminalNode parent) {
		// ClassBodyDeclaration = ClassMemberDeclaration
		//                      | InstanceInitializer
		//                      | StaticInitializer
		//                      | ConstructorDeclaration ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classMemberDeclaration(NonterminalNode parent) {
		// ClassMemberDeclaration = FieldDeclaration
		//                        | MethodDeclaration
		//                        | ClassDeclaration
		//                        | InterfaceDeclaration
		//                        | ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void fieldDeclaration(NonterminalNode parent) {
		// FieldDeclaration = { FieldModifier } , UnannType , VariableDeclaratorList , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void fieldModifier(NonterminalNode parent) {
		// FieldModifier = Annotation | "public" | "protected" | "private" | "static" | "final" | "transient" | "volatile" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void variableDeclaratorList(NonterminalNode parent) {
		// VariableDeclaratorList = VariableDeclarator , { "," , VariableDeclarator } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void variableDeclarator(NonterminalNode parent) {
		// VariableDeclarator = VariableDeclaratorId , [ "=" , VariableInitializer ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void variableDeclaratorId(NonterminalNode parent) {
		// VariableDeclaratorId = Identifier , [ Dims ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void variableInitializer(NonterminalNode parent) {
		// VariableInitializer = Expression
		//                     | ArrayInitializer ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unannType(NonterminalNode parent) {
		// UnannType = UnannReferenceType
		//           | UnannPrimitiveType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unannPrimitiveType(NonterminalNode parent) {
		// UnannPrimitiveType = NumericType
		//                    | "boolean" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unannReferenceType(NonterminalNode parent) {
		// UnannReferenceType = UnannArrayType
		//                    | UnannClassOrInterfaceType
		//                    | UnannTypeVariable ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unannClassOrInterfaceType(NonterminalNode parent) {
		// UnannClassOrInterfaceType = UnannClassType
		//                           | UnannInterfaceType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unannClassType(NonterminalNode parent) {
		// UnannClassType = Identifier , [ TypeArguments ]
		//                | UnannClassOrInterfaceType , "." , { Annotation } , Identifier , [ TypeArguments ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unannInterfaceType(NonterminalNode parent) {
		// UnannInterfaceType = UnannClassType ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unannTypeVariable(NonterminalNode parent) {
		// UnannTypeVariable = Identifier ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unannArrayType(NonterminalNode parent) {
		// UnannArrayType = UnannPrimitiveType , Dims
		//                | UnannClassOrInterfaceType , Dims
		//                | UnannTypeVariable , Dims ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void methodDeclaration(NonterminalNode parent) {
		// MethodDeclaration = { MethodModifier } , MethodHeader , MethodBody ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void methodModifier(NonterminalNode parent) {
		// MethodModifier = Annotation | "public" | "protected" | "private" | "abstract" | "static" | "final" | "synchronized" | "native" | "strictfp" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void methodHeader(NonterminalNode parent) {
		// MethodHeader = Result , MethodDeclarator , [ Throws ]
		//              | TypeParameters , { Annotation } , Result , MethodDeclarator , [ Throws ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void result(NonterminalNode parent) {
		// Result = UnannType
		//        | "void" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void methodDeclarator(NonterminalNode parent) {
		// MethodDeclarator = Identifier , "(" , [ FormalParameterList ] , ")", [ Dims ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void formalParameterList(NonterminalNode parent) {
		// FormalParameterList = FormalParameters , [ "," , LastFormalParameter ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void formalParameters(NonterminalNode parent) {
		// FormalParameters = FormalParameter , { "," , FormalParameter }
		//                  | ReceiverParameter , { "," , FormalParameter } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void formalParameter(NonterminalNode parent) {
		// FormalParameter = { VariableModifier } , UnannType , VariableDeclaratorId ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void variableModifier(NonterminalNode parent) {
		// VariableModifier = Annotation | "final" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void lastFormalParameter(NonterminalNode parent) {
		// LastFormalParameter = { VariableModifier } , UnannType , { Annotation } , "..." , VariableDeclaratorId
		//                     | FormalParameter ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void receiverParameter(NonterminalNode parent) {
		// ReceiverParameter = { Annotation } , UnannType , [ Identifier , "." ] , "this" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void throws_(NonterminalNode parent) {
		// Throws = "throws" , ExceptionTypeList ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void exceptionTypeList(NonterminalNode parent) {
		// ExceptionTypeList = ExceptionType , { "," , ExceptionType } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void exceptionType(NonterminalNode parent) {
		// ExceptionType = ClassType
		//               | TypeVariable ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void methodBody(NonterminalNode parent) {
		// MethodBody = Block
		//            | ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void instanceInitializer(NonterminalNode parent) {
		// InstanceInitializer = Block ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void staticInitializer(NonterminalNode parent) {
		// StaticInitializer = "static" , Block ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void constructorDeclaration(NonterminalNode parent) {
		// ConstructorDeclaration = { ConstructorModifier } , ConstructorDeclarator , [ Throws ] , ConstructorBody ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void constructorModifier(NonterminalNode parent) {
		// ConstructorModifier = Annotation | "public" | "protected" | "private" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void constructorDeclarator(NonterminalNode parent) {
		// ConstructorDeclarator = [ TypeParameters ] , SimpleTypeName , "(" , [ FormalParameterList ] , ")" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void simpleTypeName(NonterminalNode parent) {
		// SimpleTypeName = Identifier ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void constructorBody(NonterminalNode parent) {
		// ConstructorBody = "{" , [ ExplicitConstructorInvocation ] , [ BlockStatements ] , "}" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void explicitConstructorInvocation(NonterminalNode parent) {
		// ExplicitConstructorInvocation = [ TypeArguments ] , "this" , "(" , [ ArgumentList ] , ")" , ";"
		//                               | [ TypeArguments ] , "super" , "(" , [ ArgumentList ] , ")" , ";"
		//                               | ExpressionName , "." , [ TypeArguments ] , "super" , "(" , [ ArgumentList ] , ")" , ";"
		//                               | Primary , "." , [ TypeArguments ] , "super" , "(" , [ ArgumentList ] , ")" , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void enumDeclaration(NonterminalNode parent) {
		// EnumDeclaration = { ClassModifier } , "enum" , Identifier , [ Superinterfaces ] , EnumBody ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void enumBody(NonterminalNode parent) {
		// EnumBody = "{" , [ EnumConstantList ] , [ "," ] , [ EnumBodyDeclarations ] , "}" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void enumConstantList(NonterminalNode parent) {
		// EnumConstantList = EnumConstant , { "," , EnumConstant } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void enumConstant(NonterminalNode parent) {
		// EnumConstant = { EnumConstantModifier } , Identifier , [ "(" , ArgumentList , ")" ] , [ ClassBody ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void enumConstantModifier(NonterminalNode parent) {
		// EnumConstantModifier = Annotation ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void enumBodyDeclarations(NonterminalNode parent) {
		// EnumBodyDeclarations = ";" , { ClassBodyDeclaration } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void interfaceDeclaration(NonterminalNode parent) {
		// InterfaceDeclaration = NormalInterfaceDeclaration
		//                      | AnnotationTypeDeclaration ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void normalInterfaceDeclaration(NonterminalNode parent) {
		// NormalInterfaceDeclaration = { InterfaceModifier } , "interface" , Identifier , [ TypeParameters ] , [ ExtendsInterfaces ] , InterfaceBody ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void interfaceModifier(NonterminalNode parent) {
		// InterfaceModifier = Annotation | "public" | "protected" | "private" | "abstract" | "static" | "strictfp" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void extendsInterfaces(NonterminalNode parent) {
		// ExtendsInterfaces = "extends" , InterfaceTypeList ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void interfaceBody(NonterminalNode parent) {
		// InterfaceBody = "{" , { InterfaceMemberDeclaration } , "}" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void interfaceMemberDeclaration(NonterminalNode parent) {
		// InterfaceMemberDeclaration = ConstantDeclaration
		//                            | InterfaceMethodDeclaration
		//                            | ClassDeclaration
		//                            | InterfaceDeclaration
		//                            | ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void constantDeclaration(NonterminalNode parent) {
		// ConstantDeclaration = { ConstantModifier } , UnannType , VariableDeclaratorList , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void constantModifier(NonterminalNode parent) {
		// ConstantModifier = Annotation | "public" | "static" | "final" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void interfaceMethodDeclaration(NonterminalNode parent) {
		// InterfaceMethodDeclaration = { InterfaceMethodModifier } , MethodHeader , MethodBody ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void interfaceMethodModifier(NonterminalNode parent) {
		// InterfaceMethodModifier = Annotation | "public" | "abstract" | "default" | "static" | "strictfp" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void annotationTypeDeclaration(NonterminalNode parent) {
		// AnnotationTypeDeclaration = { InterfaceModifier } , "@" , "interface" , Identifier , AnnotationTypeBody ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void annotationTypeBody(NonterminalNode parent) {
		// AnnotationTypeBody = "{" , { AnnotationTypeMemberDeclaration } , "}" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void annotationTypeMemberDeclaration(NonterminalNode parent) {
		// AnnotationTypeMemberDeclaration = AnnotationTypeElementDeclaration
		//                                 | ConstantDeclaration
		//                                 | ClassDeclaration
		//                                 | InterfaceDeclaration
		//                                 | ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void annotationTypeElementDeclaration(NonterminalNode parent) {
		// AnnotationTypeElementDeclaration = { AnnotationTypeElementModifier } , UnannType , Identifier , "(" , ")" , [ Dims ] , [ DefaultValue ] , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void annotationTypeElementModifier(NonterminalNode parent) {
		// AnnotationTypeElementModifier = Annotation | "public" | "abstract" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void defaultValue(NonterminalNode parent) {
		// DefaultValue = "default" , ElementValue ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void annotation(NonterminalNode parent) {
		// Annotation = NormalAnnotation
		//            | MarkerAnnotation
		//            | SingleElementAnnotation ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void normalAnnotation(NonterminalNode parent) {
		// NormalAnnotation = "@" , TypeName , "(" , [ ElementValuePairList ] , ")" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void elementValuePairList(NonterminalNode parent) {
		// ElementValuePairList = ElementValuePair , { "," , ElementValuePair } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void elementValuePair(NonterminalNode parent) {
		// ElementValuePair = Identifier , "=" , ElementValue ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void elementValue(NonterminalNode parent) {
		// ElementValue = ConditionalExpression
		//              | ElementValueArrayInitializer
		//              | Annotation ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void elementValueArrayInitializer(NonterminalNode parent) {
		// ElementValueArrayInitializer = "{" , [ ElementValueList ] , [ "," ] , "}" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void elementValueList(NonterminalNode parent) {
		// ElementValueList = ElementValue , { "," , ElementValue } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void markerAnnotation(NonterminalNode parent) {
		// MarkerAnnotation = "@" , TypeName ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void singleElementAnnotation(NonterminalNode parent) {
		// SingleElementAnnotation = "@" , TypeName , "(" , ElementValue , ")" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void arrayInitializer(NonterminalNode parent) {
		// ArrayInitializer = "{" , [ VariableInitializerList ] , [ "," ] , "}" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void variableInitializerList(NonterminalNode parent) {
		// VariableInitializerList = VariableInitializer , { "," , VariableInitializer } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void block(NonterminalNode parent) {
		// Block = "{" , [ BlockStatements ] , "}" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void blockStatements(NonterminalNode parent) {
		// BlockStatements = BlockStatement , { BlockStatement } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void blockStatement(NonterminalNode parent) {
		// BlockStatement = LocalVariableDeclarationStatement
		//                | ClassDeclaration
		//                | Statement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void localVariableDeclarationStatement(NonterminalNode parent) {
		// LocalVariableDeclarationStatement = LocalVariableDeclaration , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void localVariableDeclaration(NonterminalNode parent) {
		// LocalVariableDeclaration = { VariableModifier } , UnannType , VariableDeclaratorList ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void statement(NonterminalNode parent) {
		// Statement = StatementWithoutTrailingSubstatement
		//           | LabeledStatement
		//           | IfThenStatement
		//           | IfThenElseStatement
		//           | WhileStatement
		//           | ForStatement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void statementNoShortIf(NonterminalNode parent) {
		// StatementNoShortIf = StatementWithoutTrailingSubstatement
		//                    | LabeledStatementNoShortIf
		//                    | IfThenElseStatementNoShortIf
		//                    | WhileStatementNoShortIf
		//                    | ForStatementNoShortIf ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void statementWithoutTrailingSubstatement(NonterminalNode parent) {
		// StatementWithoutTrailingSubstatement = Block
		//                                      | EmptyStatement
		//                                      | ExpressionStatement
		//                                      | AssertStatement
		//                                      | SwitchStatement
		//                                      | DoStatement
		//                                      | BreakStatement
		//                                      | ContinueStatement
		//                                      | ReturnStatement
		//                                      | SynchronizedStatement
		//                                      | ThrowStatement
		//                                      | TryStatement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void emptyStatement(NonterminalNode parent) {
		// EmptyStatement = ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void labeledStatement(NonterminalNode parent) {
		// LabeledStatement = Identifier , ":" , Statement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void labeledStatementNoShortIf(NonterminalNode parent) {
		// LabeledStatementNoShortIf = Identifier , ":" , StatementNoShortIf ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void expressionStatement(NonterminalNode parent) {
		// ExpressionStatement = StatementExpression , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void statementExpression(NonterminalNode parent) {
		// StatementExpression = Assignment
		//                     | PreIncrementExpression
		//                     | PreDecrementExpression
		//                     | PostIncrementExpression
		//                     | PostDecrementExpression
		//                     | MethodInvocation
		//                     | ClassInstanceCreationExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void ifThenStatement(NonterminalNode parent) {
		// IfThenStatement = "if" , "(" , Expression , ")" , Statement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void ifThenElseStatement(NonterminalNode parent) {
		// IfThenElseStatement = "if" , "(" , Expression , ")" , StatementNoShortIf , "else" , Statement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void ifThenElseStatementNoShortIf(NonterminalNode parent) {
		// IfThenElseStatementNoShortIf = "if" , "(" , Expression , ")" , StatementNoShortIf , "else" , StatementNoShortIf ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void assertStatement(NonterminalNode parent) {
		// AssertStatement = "assert" , Expression , ";"
		//                 | "assert" , Expression , ":" , Expression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void switchStatement(NonterminalNode parent) {
		// SwitchStatement = "switch" , "(" , Expression , ")" , SwitchBlock ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void switchBlock(NonterminalNode parent) {
		// SwitchBlock = "{" , { SwitchBlockStatementGroup } , { SwitchLabel } , "}" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void switchBlockStatementGroup(NonterminalNode parent) {
		// SwitchBlockStatementGroup = SwitchLabels , BlockStatements ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void switchLabels(NonterminalNode parent) {
		// SwitchLabels = SwitchLabel , { SwitchLabel } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void switchLabel(NonterminalNode parent) {
		// SwitchLabel = "case" , ConstantExpression , ":"
		//             | "case" , EnumConstantName , ":"
		//             | "default" , ":" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void enumConstantName(NonterminalNode parent) {
		// EnumConstantName = Identifier ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void whileStatement(NonterminalNode parent) {
		// WhileStatement = "while" , "(" , Expression , ")" , Statement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void whileStatementNoShortIf(NonterminalNode parent) {
		// WhileStatementNoShortIf = "while" , "(" , Expression , ")" , StatementNoShortIf ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void doStatement(NonterminalNode parent) {
		// DoStatement = "do" , Statement , "while" , "(" , Expression , ")" , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void forStatement(NonterminalNode parent) {
		// ForStatement = BasicForStatement
		//              | EnhancedForStatement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void forStatementNoShortIf(NonterminalNode parent) {
		// ForStatementNoShortIf = BasicForStatementNoShortIf
		//                       | EnhancedForStatementNoShortIf ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void basicForStatement(NonterminalNode parent) {
		// BasicForStatement = "for" , "(" , [ ForInit ] , ";" , [ Expression ] , ";" , [ ForUpdate ] , ")" , Statement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void basicForStatementNoShortIf(NonterminalNode parent) {
		// BasicForStatementNoShortIf = "for" , "(" , [ ForInit ] , ";" , [ Expression ] , ";" , [ ForUpdate ] , ")" , StatementNoShortIf ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void forInit(NonterminalNode parent) {
		// ForInit = StatementExpressionList
		//         | LocalVariableDeclaration ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void forUpdate(NonterminalNode parent) {
		// ForUpdate = StatementExpressionList ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void statementExpressionList(NonterminalNode parent) {
		// StatementExpressionList = StatementExpression , { "," , StatementExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void enhancedForStatement(NonterminalNode parent) {
		// EnhancedForStatement = "for" , "(" , { VariableModifier } , UnannType , VariableDeclaratorId , ":" , Expression , ")" , Statement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void enhancedForStatementNoShortIf(NonterminalNode parent) {
		// EnhancedForStatementNoShortIf = "for" , "(" , { VariableModifier } , UnannType , VariableDeclaratorId , ":" , Expression , ")" , StatementNoShortIf ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void breakStatement(NonterminalNode parent) {
		// BreakStatement = "break" , [ Identifier ] , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void continueStatement(NonterminalNode parent) {
		// ContinueStatement = "continue" , [ Identifier ] , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void returnStatement(NonterminalNode parent) {
		// ReturnStatement = "return" , [ Expression ] , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void throwStatement(NonterminalNode parent) {
		// ThrowStatement = "throw" , Expression , ";" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void synchronizedStatement(NonterminalNode parent) {
		// SynchronizedStatement = "synchronized" , "(" , Expression , ")" , Block ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void tryStatement(NonterminalNode parent) {
		// TryStatement = "try" , Block , [ Catches ] , Finally
		//              | "try" , Block , Catches
		//              | TryWithResourcesStatement ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void catches(NonterminalNode parent) {
		// Catches = CatchClause , { CatchClause } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void catchClause(NonterminalNode parent) {
		// CatchClause = "catch" , "(" , CatchFormalParameter , ")" , Block ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void catchFormalParameter(NonterminalNode parent) {
		// CatchFormalParameter = { VariableModifier } , CatchType , VariableDeclaratorId ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void catchType(NonterminalNode parent) {
		// CatchType = "UnannClassType" , { "|" , ClassType } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void finally_(NonterminalNode parent) {
		// Finally = "finally" , Block ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void tryWithResourcesStatement(NonterminalNode parent) {
		// TryWithResourcesStatement = "try" , ResourceSpecification , Block , [ Catches ] , [ Finally ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void resourceSpecification(NonterminalNode parent) {
		// ResourceSpecification = "(" , ResourceList , [ ";" ] , ")" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void resourceList(NonterminalNode parent) {
		// ResourceList = Resource , { ";" , Resource } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void resource(NonterminalNode parent) {
		// Resource = { VariableModifier } , UnannType , VariableDeclaratorId , "=" , Expression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void primary(NonterminalNode parent) {
		// Primary = PrimaryNoNewArray
		//         | ArrayCreationExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void primaryNoNewArray(NonterminalNode parent) {
		// PrimaryNoNewArray = Literal
		//                   | ClassLiteral
		//                   | "this"
		//                   | TypeName , "." , "this"
		//                   | "(" , Expression , ")"
		//                   | ClassInstanceCreationExpression
		//                   | FieldAccess
		//                   | ArrayAccess
		//                   | MethodInvocation
		//                   | MethodReference ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classLiteral(NonterminalNode parent) {
		// ClassLiteral = TypeName , { "[" , "]" } , "." , "class"
		//              | NumericType , { "[" , "]" } , "." , "class"
		//              | "boolean" , { "[" , "]" } , "." , "class"
		//              | "void" , "." , "class" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classInstanceCreationExpression(NonterminalNode parent) {
		// ClassInstanceCreationExpression = UnqualifiedClassInstanceCreationExpression
		//                                 | ExpressionName , "." , UnqualifiedClassInstanceCreationExpression
		//                                 | Primary , "." , UnqualifiedClassInstanceCreationExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unqualifiedClassInstanceCreationExpression(NonterminalNode parent) {
		// UnqualifiedClassInstanceCreationExpression = "new" , [ TypeArguments ] , ClassOrInterfaceTypeToInstantiate , "(" , [ ArgumentList ] , ")" , [ ClassBody ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void classOrInterfaceTypeToInstantiate(NonterminalNode parent) {
		// ClassOrInterfaceTypeToInstantiate = { Annotation } , Identifier , { "." , { Annotation } , Identifier } , [ TypeArgumentsOrDiamond ] ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void typeArgumentsOrDiamond(NonterminalNode parent) {
		// TypeArgumentsOrDiamond = TypeArguments
		//                        | "<" , ">" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void fieldAccess(NonterminalNode parent) {
		// FieldAccess = Primary , "." , Identifier
		//             | "super" , "." , Identifier
		//             | TypeName , "." , "super" , "." , Identifier ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void arrayAccess(NonterminalNode parent) {
		// ArrayAccess = ExpressionName , "[" , Expression , "]"
		//             | PrimaryNoNewArray , "[" , Expression , "]" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void methodInvocation(NonterminalNode parent) {
		// MethodInvocation = MethodName , "(" , [ArgumentList ] , ")"
		//                  | TypeName , "." , [ TypeArguments ] , Identifier , "(" , [ ArgumentList ] , ")"
		//                  | ExpressionName , "." , [ TypeArguments ] , Identifier , "(" , [ArgumentList ] , ")"
		//                  | Primary , "." , [ TypeArguments ] , Identifier , "(" , [ArgumentList ] , ")"
		//                  | "super" , "." , [ TypeArguments ] , Identifier , "(" , [ArgumentList ] , ")"
		//                  | TypeName , "." , "super" , "." , [ TypeArguments ] , Identifier , "(" , [ArgumentList ] , ")" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void argumentList(NonterminalNode parent) {
		// ArgumentList = Expression , { "," , Expression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void methodReference(NonterminalNode parent) {
		// MethodReference = ExpressionName , "::" , [ TypeArguments ] , Identifier
		//                 | ReferenceType , "::" , [ TypeArguments ] , Identifier
		//                 | Primary , "::" , [ TypeArguments ] , Identifier
		//                 | "super" , "::" , [ TypeArguments ] , Identifier
		//                 | TypeName , "." , "super" , "::" , [ TypeArguments ] , Identifier
		//                 | ClassType , "::" , [ TypeArguments ] , "new"
		//                 | ArrayType , "::" , "new" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void arrayCreationExpression(NonterminalNode parent) {
		// ArrayCreationExpression = "new" , PrimitiveType , DimExprs , [ Dims ]
		//                         | "new" , ClassOrInterfaceType , DimExprs , [ Dims ]
		//                         | "new" , PrimitiveType , Dims , ArrayInitializer
		//                         | "new" , ClassOrInterfaceType , Dims , ArrayInitializer ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void dimExprs(NonterminalNode parent) {
		// DimExprs = DimExpr , { DimExpr } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void dimExpr(NonterminalNode parent) {
		// DimExpr = { Annotation } , "[" , Expression , "]" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void expression(NonterminalNode parent) {
		// Expression = LambdaExpression
		//            | AssignmentExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void lambdaExpression(NonterminalNode parent) {
		// LambdaExpression = LambdaParameters , "->" , LambdaBody ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void lambdaParameters(NonterminalNode parent) {
		// LambdaParameters = Identifier
		//                  | "(" , [ FormalParameterList ] , ")"
		//                  | "(" , InferredFormalParameterList , ")" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void inferredFormalParameterList(NonterminalNode parent) {
		// InferredFormalParameterList = Identifier , { "," , Identifier } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void lambdaBody(NonterminalNode parent) {
		// LambdaBody = Expression
		//            | Block ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void assignmentExpression(NonterminalNode parent) {
		// AssignmentExpression = ConditionalOrExpression
		//                      | Assignment ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void assignment(NonterminalNode parent) {
		// Assignment = LeftHandSide , AssignmentOperator , Expression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void leftHandSide(NonterminalNode parent) {
		// LeftHandSide = ExpressionName
		//              | FieldAccess
		//              | ArrayAccess ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void assignmentOperator(NonterminalNode parent) {
		// AssignmentOperator = "=" | "*=" | "/=" | "%=" | "+=" | "-=" | "<<=" | ">>=" | ">>>=" | "&=" | "^=" | "|=" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void conditionalExpression(NonterminalNode parent) {
		// ConditionalExpression = ConditionalOrExpression , "?" , Expression , ":" , ConditionalExpression
		//                       | ConditionalOrExpression , "?" , Expression , ":" , LambdaExpression
		//                       | ConditionalOrExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void conditionalOrExpression(NonterminalNode parent) {
		// ConditionalOrExpression = ConditionalAndExpression , { "||" , ConditionalAndExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void conditionalAndExpression(NonterminalNode parent) {
		// ConditionalAndExpression = InclusiveOrExpression , { "&&" , InclusiveOrExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void inclusiveOrExpression(NonterminalNode parent) {
		// InclusiveOrExpression = ExclusiveOrExpression , { "|" , ExclusiveOrExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void exclusiveOrExpression(NonterminalNode parent) {
		// ExclusiveOrExpression = AndExpression , { "^" , AndExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void andExpression(NonterminalNode parent) {
		// AndExpression = EqualityExpression , { "&" , EqualityExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void equalityExpression(NonterminalNode parent) {
		// EqualityExpression = RelationalExpression , { ( "==" | "!=" ) , RelationalExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void relationalExpression(NonterminalNode parent) {
		// RelationalExpression = ShiftExpression , "instanceof" , ReferenceType
		//                      | ShiftExpression , { ( "<" | ">" | "<=" | ">=" ) , ShiftExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void shiftExpression(NonterminalNode parent) {
		// ShiftExpression = AdditiveExpression , { ( "<<" | ">>" | ">>>" ) , AdditiveExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void additiveExpression(NonterminalNode parent) {
		// AdditiveExpression = MultiplicativeExpression , { ( "+" | "-" ) , MultiplicativeExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void multiplicativeExpression(NonterminalNode parent) {
		// MultiplicativeExpression = UnaryExpression , { ( "*" | "/" | "%" ) , UnaryExpression } ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unaryExpression(NonterminalNode parent) {
		// UnaryExpression = PreIncrementExpression
		//                 | PreDecrementExpression
		//                 | "+" , UnaryExpression
		//                 | "-" , UnaryExpression
		//                 | UnaryExpressionNotPlusMinus ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void preIncrementExpression(NonterminalNode parent) {
		// PreIncrementExpression = "++" , UnaryExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void preDecrementExpression(NonterminalNode parent) {
		// PreDecrementExpression = "--" , UnaryExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void unaryExpressionNotPlusMinus(NonterminalNode parent) {
		// UnaryExpressionNotPlusMinus = PostfixExpression
		//                             | "~" , UnaryExpression
		//                             | "!" , UnaryExpression
		//                             | CastExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void postfixExpression(NonterminalNode parent) {
		// PostfixExpression = Primary
		//                   | ExpressionName
		//                   | PostIncrementExpression
		//                   | PostDecrementExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void postIncrementExpression(NonterminalNode parent) {
		// PostIncrementExpression = PostfixExpression , "++" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void postDecrementExpression(NonterminalNode parent) {
		// PostDecrementExpression = PostfixExpression , "--" ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void castExpression(NonterminalNode parent) {
		// CastExpression = "(" , PrimitiveType , ")" , UnaryExpression
		//                | "(" , ReferenceType , { AdditionalBound } , ")" , UnaryExpressionNotPlusMinus
		//                | "(" , ReferenceType , { AdditionalBound } , ")" , LambdaExpression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
	private void constantExpression(NonterminalNode parent) {
		// ConstantExpression = Expression ;
		error("Nonterminal " + parent.getValue() + " is not supported.");
	}
}