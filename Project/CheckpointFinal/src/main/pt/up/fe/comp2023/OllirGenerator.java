package pt.up.fe.comp2023;
import org.antlr.v4.runtime.misc.Pair;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;


import javax.sound.midi.SysexMessage;
import java.util.*;
import java.util.stream.Collectors;

public class OllirGenerator extends AJmmVisitor<String, String> {
    static int varCounter;
    static int ifCounter;
    static int whileCounter;
    private final StringBuilder code;
    private final SymbolTable symbolTable;

    public OllirGenerator(SymbolTable symbolTable) {
        this.code = new StringBuilder();
        this.symbolTable = symbolTable;
        this.buildVisitor();
    }

    public String getCode() {
        return code.toString();
    }

    @Override
    protected void buildVisitor() {
        addVisit("Program", this::programVisit);
        addVisit("ImportStmt", this::importDeclVisit);
        addVisit("ClassStmt", this::classDeclVisit);
        addVisit("VarDeclaration", this::varDeclVisit);
        addVisit("MethodDeclaration", this::methodDeclVisit);
        addVisit("Integer", this::numberVisit);
        addVisit("Identifier", this::identifierVisit);
        addVisit("Assignment", this::assignmentVisit);
        addVisit("ReturnStmt", this::returnVisit);
        addVisit("ArrayAccess", this::arrayAccessVisit);
        addVisit("BinaryOp", this::operationVisit);
        addVisit("Negation", this::negationVisit);
        addVisit("Length", this::lengthVisit);
        addVisit("ArrayDeclaration", this::newVisit);
        addVisit("NewClass", this::newVisit);
        addVisit("FunctionCall", this::functionCallVisit);
        addVisit("IfStmt", this::ifVisit);
        addVisit("IfExpr", this::ifExprVisit);
        addVisit("Brackets", this::bracketsVisit);
        addVisit("ElseExpr", this::elseExprVisit);
        addVisit("WhileStmt", this::whileVisit);
        // addVisit("Object", this::objectVisit);
        //TODO
        //addVisit("FunctionCall", this::memberCallVisit);
        setDefaultVisit(this::defaultVisit);
    }

    private String whileVisit(JmmNode jmmNode, String s) {
        StringBuilder whileV = new StringBuilder();
        whileV.append("if (");
        if (jmmNode.getJmmChild(0).getKind().equals("Identifier")) {
            whileV.append(visit(jmmNode.getJmmChild(0))).append(getVarType(jmmNode.getJmmChild(0).get("value"), jmmNode.getJmmChild(0)));
        }
        else {
            JmmNode conditionExpr = jmmNode.getJmmChild(0);
            while (!conditionExpr.getKind().equals("BinaryOp")) {
                conditionExpr = conditionExpr.getJmmChild(0);
            }
            String operador = conditionExpr.get("op");
            JmmNode tester = conditionExpr.getJmmChild(0);
            while (tester.getKind().equals("Parentesis")) {
                tester = tester.getJmmChild(0);
            }
            if (tester.getKind().equals("BinaryOp")) {
                JmmNode value = tester;
                while (!tester.getKind().equals("BinaryOp")) {
                    tester = tester.getJmmChild(0);
                }
                while (!value.hasAttribute("value")) {
                    value = value.getJmmChild(0);
                }
                String tipo = getVarType(value.get("value"), value);
                String declareVariable = "temp" + ++varCounter + tipo;
                String auxiliary = declareVariable + " :=" + tipo + " " + visit(tester.getJmmChild(0)) + tipo + ";\n";
                whileV.insert(0, auxiliary);
                whileV.append(declareVariable);
                //ifExprStr.insert(0,)
            }
            else if (tester.getKind().equals("Length")) {
                String tipo = ".i32";
                String declareVariable = "temp" + ++varCounter + tipo;
                String auxiliary = declareVariable + " :=" + tipo + " " + visit(tester.getJmmChild(0)) + tipo + ";\n";
                whileV.insert(0, auxiliary);
                whileV.append(declareVariable);
            }
            else {
                String tipo = getVarType(tester.get("value"), tester);
                whileV.append(visit(tester) + tipo);
            }

            whileV.append(" " + operador + ".bool ");

            JmmNode newTester = jmmNode.getJmmChild(0).getJmmChild(1);
            while (newTester.getKind().equals("Parentesis")) {
                newTester = newTester.getJmmChild(0);
            }
            if (newTester.getKind().equals("BinaryOp")) {
                JmmNode value = newTester;
                while (!newTester.getKind().equals("BinaryOp")) {
                    newTester = newTester.getJmmChild(0);
                }
                while (!value.hasAttribute("value")) {
                    value = value.getJmmChild(0);
                }
                String tipo = getVarType(value.get("value"), value);
                String declareVariable = "temp" + ++varCounter + tipo;
                String auxiliary = declareVariable + " :=" + tipo + " " + visit(newTester.getJmmChild(0)) + tipo +  ";\n";
                whileV.insert(0, auxiliary);
                whileV.append(declareVariable);
                //ifExprStr.insert(0,)
            }
            else if (newTester.getKind().equals("Length")) {
                String tipo = ".i32";
                String declareVariable = "temp" + ++varCounter + tipo;
                String auxiliary = declareVariable + " :=" + tipo + " " + visit(newTester.getJmmChild(0)) + tipo + ";\n";
                whileV.insert(0, auxiliary);
                whileV.append(declareVariable);
            }
            else {
                String tipo = getVarType(newTester.get("value"), newTester);
                whileV.append(visit(newTester) + tipo);
            }

        }


        whileV.append(") goto whilebody_"+whileCounter+";\ngoto endwhile_"+whileCounter + ";");
        whileV.append("\nwhilebody_"+whileCounter+":\n");
        JmmNode afterWhile = jmmNode.getJmmChild(1);
        for (JmmNode child : afterWhile.getChildren()) {
            whileV.append(visit(child));
        }

        String currentWhile = whileV.toString();

        int startIndex = currentWhile.indexOf('('); // Find the index of the opening parenthesis
        int endIndex = currentWhile.indexOf(')'); // Find the index of the closing parenthesis

        String contentBetweenParentheses = "";
        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            contentBetweenParentheses = currentWhile.substring(startIndex + 1, endIndex);
        }

        whileV.append("if (" + contentBetweenParentheses + ") goto whilebody_"+whileCounter+";\n");
        whileV.append("endwhile_"+whileCounter++ + ":\n");
        return whileV.toString();
    }

    private String elseExprVisit(JmmNode elseExpr, String s) {
        StringBuilder elseV = new StringBuilder();
        for (JmmNode child: elseExpr.getChildren()) {
            elseV.append(visit(child));
        }
        return elseV.toString();
    }

    private String bracketsVisit(JmmNode jmmNode, String s) {
        StringBuilder bracketsV = new StringBuilder();
        for (JmmNode child : jmmNode.getChildren()) {
            bracketsV.append(visit(child));
        }
        return bracketsV.toString();
    }

    private String ifExprVisit(JmmNode ifExpr, String s) {
        StringBuilder ifExprStr = new StringBuilder();
        ifExprStr.append("if (");
        if (ifExpr.getJmmChild(0).getKind().equals("Identifier")) {
            ifExprStr.append(visit(ifExpr.getJmmChild(0))).append(getVarType(ifExpr.getJmmChild(0).get("value"), ifExpr.getJmmChild(0)));
        }
        else {
            JmmNode conditionExpr = ifExpr.getJmmChild(0);
            while (!conditionExpr.getKind().equals("BinaryOp")) {
                conditionExpr = conditionExpr.getJmmChild(0);
            }
            String operador = conditionExpr.get("op");
            JmmNode tester = conditionExpr.getJmmChild(0);
            while (tester.getKind().equals("Parentesis")) {
                tester = tester.getJmmChild(0);
            }
            if (tester.getKind().equals("BinaryOp")) {
                JmmNode value = tester;
                while (!tester.getKind().equals("BinaryOp")) {
                    tester = tester.getJmmChild(0);
                }
                while (!value.hasAttribute("value")) {
                    value = value.getJmmChild(0);
                }
                String tipo = getVarType(value.get("value"), value);
                String declareVariable = "temp" + ++varCounter + tipo;
                String auxiliary = declareVariable + " :=" + tipo + " " + visit(tester.getJmmChild(0)) + tipo + ";\n";
                ifExprStr.insert(0, auxiliary);
                ifExprStr.append(declareVariable);
                //ifExprStr.insert(0,)
            }
            else if (tester.getKind().equals("Length")) {
                String tipo = ".i32";
                String declareVariable = "temp" + ++varCounter + tipo;
                String auxiliary = declareVariable + " :=" + tipo + " " + visit(tester.getJmmChild(0)) + tipo + ";\n";
                ifExprStr.insert(0, auxiliary);
                ifExprStr.append(declareVariable);
            }
            else {
                String tipo = getVarType(tester.get("value"), tester);
                ifExprStr.append(visit(tester) + tipo);
            }

            ifExprStr.append(" " + operador + ".bool ");

            JmmNode newTester = ifExpr.getJmmChild(0).getJmmChild(1);
            while (newTester.getKind().equals("Parentesis")) {
                newTester = newTester.getJmmChild(0);
            }
            if (newTester.getKind().equals("BinaryOp")) {
                JmmNode value = newTester;
                while (!newTester.getKind().equals("BinaryOp")) {
                    newTester = newTester.getJmmChild(0);
                }
                while (!value.hasAttribute("value")) {
                    value = value.getJmmChild(0);
                }
                String tipo = getVarType(value.get("value"), value);
                String declareVariable = "temp" + ++varCounter + tipo;
                String auxiliary = declareVariable + " :=" + tipo + " " + visit(tester.getJmmChild(0)) + tipo +  ";\n";
                ifExprStr.insert(0, auxiliary);
                ifExprStr.append(declareVariable);
                //ifExprStr.insert(0,)
            }
            else if (newTester.getKind().equals("Length")) {
                String tipo = ".i32";
                String declareVariable = "temp" + ++varCounter + tipo;
                String auxiliary = declareVariable + " :=" + tipo + " " + visit(newTester.getJmmChild(0)) + tipo + ";\n";
                ifExprStr.insert(0, auxiliary);
                ifExprStr.append(declareVariable);
            }
            else {
                String tipo = getVarType(newTester.get("value"), newTester);
                ifExprStr.append(visit(newTester) + tipo);
            }

        }
        ifExprStr.append(") ");
        return ifExprStr.toString();
    }
    private String ifVisit(JmmNode ifStmt, String s) {
        StringBuilder ifStr = new StringBuilder();
        ifStr.append(visit(ifStmt.getJmmChild(0)));
        ifStr.append("goto ifbody_" + ifCounter + ";\n");
        if (ifStmt.getNumChildren()>1) { // has else
            ifStr.append(visit(ifStmt.getJmmChild(1)));
            ifStr.append("goto endif_" + ifCounter + ";\n");
        }
        ifStr.append("ifbody_"+ifCounter + ":\n");
        ifStr.append(visit(ifStmt.getJmmChild(0).getJmmChild(1)));
        ifStr.append("endif_"+ifCounter++ + ":\n");
        return ifStr.toString();
    }

    private String getBinOp(String op) {
        if (op.equals("*") || op.equals("+") || op.equals("/") || op.equals("-")) {
            return ".i32";
        }
        else {
            return ".bool";
        }
    }


    private String functionCallVisit(JmmNode memberCall, String dummy){
        StringBuilder methodString = new StringBuilder();
        String first = "";
        String kind = "";
        String second = memberCall.get("name");
        String finalString = "";

        if (!memberCall.getJmmChild(0).getKind().equals("Parameter")) {
            if (memberCall.getJmmChild(0).hasAttribute("value")) {
                first = memberCall.getJmmChild(0).get("value"); // io
            }
            else if (memberCall.getJmmChild(0).hasAttribute("name")) {
                first = memberCall.getJmmChild(0).get("name"); // io
            }
            else if (memberCall.getJmmChild(0).hasAttribute("classname")) {
                first = memberCall.getJmmChild(0).get("classname"); // io

            }
            kind = memberCall.getKind();
        }
        else {
            first = "this";
        }
        if (!first.equals("this")) {
            if (checkIfImport(first)) {
                methodString.append("invokestatic(").append(first).append(", \"" + second + "\"");
                finalString = ")" + getVarType(first, memberCall) + ";\n";
            } else {
                methodString.append("invokevirtual(").append(first);
                if (!getVarType(first, memberCall.getJmmChild(0)).equals(".V")) {
                    methodString.append(getVarType(first, memberCall.getJmmChild(0)));
                    finalString = ")" + getVarType(memberCall.get("name"), memberCall) + ";\n";
                }
                methodString.append(", \"" + second + "\"");
            }
            if (memberCall.getNumChildren() > 1) {

                var parameters = memberCall.getJmmChild(0).getChildren();
                if (!memberCall.getJmmChild(0).getKind().equals("Parameter") && !memberCall.getJmmChild(0).getKind().equals("Object")) {
                    parameters = memberCall.getJmmChild(1).getChildren();
                }
                for (var parameter : parameters) {
                    methodString.append(", ");
                    if (!parameter.getKind().equals("BinaryOp") && !parameter.getKind().equals("FunctionCall") && !parameter.getKind().equals("Negation")) {
                        String parametroVisitado = visit(parameter);
                        if (parametroVisitado.contains("getfield")) {
                            String tempvar = parametroVisitado.split("\n")[0] + "\n";
                            String thistemp = parametroVisitado.split("\n")[1];
                            methodString.insert(0, tempvar);
                            methodString.append(thistemp);
                        }
                        else if (!parameter.getKind().equals("ArrayAccess") && !parameter.getKind().equals("Length")){
                            methodString.append(parametroVisitado).append(getVarType(parameter.get("value"), parameter));
                        }
                        else if (parameter.getKind().equals("ArrayAccess")) {
                            String auxVar = parametroVisitado.split(";\n")[0];
                            String variable = parametroVisitado.split(";\n")[1];
                            String secondVariable = "temp" + varCounter++ + ".i32 ";
                            String secondDeclaration = secondVariable + ":=.i32 " + variable + ";\n";
                            methodString.insert(0, secondDeclaration);
                            methodString.insert(0, auxVar + ";\n");
                            methodString.append(secondVariable);
                        }
                        else {
                            String length = visit(parameter);
                            String variable = length.split(" :=")[0];
                            methodString.insert(0, length);
                            methodString.append(variable);
                        }
                    }
                    else if (!parameter.hasAttribute("op") && !parameter.getKind().equals("Negation")){ // function call
                        String thistemp = "temp" + varCounter + getVarType(parameter.get("name"), parameter);
                        String tempVar = "temp" + varCounter++ + getVarType(parameter.get("name"), parameter)
                                + " :=" + getVarType(parameter.get("name"), parameter) + " " + visit(parameter);
                        methodString.insert(0, tempVar);
                        methodString.append(thistemp);
                    }
                    else if (parameter.getKind().equals("BinaryOp")){ // BINARY OP
                        String visitParameter = visit(parameter);
                        String[] splits = visitParameter.split("\n");
                        String finalVisitParameter = "";
                        for (String split : splits) {
                            if (!split.contains(":=")) {
                                split = "temp" + ++varCounter + getBinOp(parameter.get("op")) + " :=" + getBinOp(parameter.get("op")) + " " + split;
                            }
                            finalVisitParameter += split + "\n";
                        }
                        String tempVar = "temp" + varCounter +  getBinOp(parameter.get("op"))
                                + " :=" +  getBinOp(parameter.get("op")) + " " + finalVisitParameter.substring(0, finalVisitParameter.lastIndexOf("\n")) + ";\n";
                        String thistemp = "temp" + varCounter + getBinOp(parameter.get("op"));
                        methodString.insert(0, tempVar);


                        methodString.append(thistemp);
                    }
                    else { // NEGATION
                        String tempVar = "temp" + ++varCounter +  ".bool"
                                + " :=.bool "  + visit(parameter) + ";\n";
                        String thistemp = "temp" + varCounter + ".bool";
                        methodString.insert(0, tempVar);
                        methodString.append(thistemp);
                    }
                }
            }
            methodString.append(finalString);
        } else {
            methodString.append("invokevirtual(").append(first).append(", \"" + second + "\"");
            if (memberCall.getNumChildren() >= 1) {
                var parameters = memberCall.getJmmChild(0).getChildren();
                if (!memberCall.getJmmChild(0).getKind().equals("Parameter") && !memberCall.getJmmChild(0).getKind().equals("Object")) {
                    parameters = memberCall.getJmmChild(1).getChildren();
                }
                for (var parameter : parameters) {
                    methodString.append(", ");
                    if (!parameter.getKind().equals("BinaryOp") && !parameter.getKind().equals("FunctionCall")) {
                        methodString.append(visit(parameter)).append(getVarType(parameter.get("value"), parameter));
                    }
                    else if (!parameter.hasAttribute("op")){ // function call
                        String thistemp = "temp" + varCounter + getVarType(parameter.get("name"), parameter);
                        String tempVar = "temp" + varCounter++ + getVarType(parameter.get("name"), parameter)
                                + " :=" + getVarType(parameter.get("name"), parameter) + " " + visit(parameter);
                        methodString.insert(0, tempVar);
                        methodString.append(thistemp);
                    }
                    else {
                        String thistemp = "temp" + varCounter + getBinOp(parameter.get("op"));
                        String tempVar = "temp" + varCounter++ +  getBinOp(parameter.get("op"))
                                + " :=" +  getBinOp(parameter.get("op")) + " " + visit(parameter) + ";\n";
                        methodString.insert(0, tempVar);
                        methodString.append(thistemp);
                    }
                }
            }

            methodString.append(")");
            if (memberCall.hasAttribute("name")) {
                methodString.append(OllirUtils.getTypeOfOllir(symbolTable.getReturnType(memberCall.get("name")))).append(";\n");
            }
            else {
                methodString.append(OllirUtils.getTypeOfOllir(symbolTable.getReturnType(getMethodName(memberCall)))).append(";\n");
            }
        }

        List<String>everyVariable = splitStringByDelimiter(methodString.toString());
        int counter = 0;
        int counter2 = 0;
        for (String variable : everyVariable) {
            if (countInString(variable, ':') > 1) {
                String tempVar = getSubstringFromIndexUntilSecondSpace(variable, 0);
                everyVariable.set(counter, variable.replace(tempVar, ""));
                for (String variableTwo : everyVariable) {
                    if ((variableTwo.contains("+.") || variableTwo.contains("*.") || variableTwo.contains("/.") || variableTwo.contains("-.")) && !variableTwo.contains(":=")) {
                        if (variableTwo.contains("\n")) {
                            everyVariable.set(counter2, "\n" + tempVar + variableTwo.substring(1));
                        }
                        else {
                            everyVariable.set(counter2, "\n" + tempVar + variableTwo);
                        }
                    }
                    counter2++;
                }
            }
            counter++;
        }
        String metodoFinal = String.join("" , everyVariable);


        return metodoFinal;


    }



    public static List<String> splitStringByDelimiter(String str) {
        List<String> list = new ArrayList<>();

        // Loop through the string and find the indices of the delimiter ";"
        int start = 0;
        int end = str.indexOf(';');

        while (end >= 0) {
            // Add the substring from the start index to the delimiter index
            list.add(str.substring(start, end+1));

            // Update the start index to the character after the delimiter and find the next delimiter index
            start = end+1;
            end = str.indexOf(';', start);
        }

        // Add the remaining substring after the last delimiter (if any)
        if (start < str.length()) {
            list.add(str.substring(start));
        }

        return list;
    }


    private String newVisit(JmmNode newNode, String s){

        StringBuilder newString = new StringBuilder();

        if (newNode.getNumChildren() > 0) {
            JmmNode child = newNode.getJmmChild(0);

            if (!child.getKind().equals("Identifier")) {
                if (child.getKind().equals("Integer")) {
                    newString.append("{{temp"+varCounter+".i32 :=.i32 " + visit(child)+getVarType(child.get("value"), child)+"}}");
                }
                newString.append("new(array, ");
                newString.append(visit(child));
                newString.append(".i32).array.i32");
            } else {
                String newId = newNode.getJmmParent().getJmmChild(0).getKind().equals("Identifier") ? newNode.getJmmParent().getJmmChild(0).get("value") : newNode.getJmmParent().getJmmChild(0).getJmmChild(0).get("value");
                newString.append("invokespecial(").append(newId).append(".").append(child.get("value")).append(",\"<init>\").V;\n");
                newString.append("new(").append(child.get("value")).append(").").append(child.get("value")); //typecheck
            }
        }
        else {
            newString.append("new(" + newNode.get("classname") + ")." + newNode.get("classname") + ";\n");
        }
        return newString.toString();
    }

    private String lengthVisit(JmmNode lengthDeclaration, String s){
        StringBuilder methodString = new StringBuilder();
        JmmNode id = lengthDeclaration.getJmmChild(0);

        JmmNode func = lengthDeclaration;

        JmmNode args;
        String argPrefix = "";
        String argString = "";
        String argSavePostfix = "";
        Boolean flag = false;


        if (func.getKind().equals("Length")) {
            String idLenStr = visit(id);

            if (idLenStr.contains("\n")) {
                methodString.append(idLenStr.substring(0, idLenStr.lastIndexOf("\n") + 1));
                idLenStr = idLenStr.substring(idLenStr.lastIndexOf("\n") + 1);
            }

            methodString.append("temp").append(++varCounter).append(".i32 :=.i32 arraylength(").append(idLenStr).append(getVarType(id.get("value"), id)).append(").i32.i32;\n"); //todo check type
        }
        return methodString.toString();
    }

    private String negationVisit(JmmNode negationDeclaration, String s) {
        StringBuilder negationCode = new StringBuilder();


        JmmNode expressionToNegate = negationDeclaration.getChildren().get(0);
        if (expressionToNegate.getKind().equals("Parentesis")) { //TODO
            if (expressionToNegate.getJmmChild(0).getKind().equals("BinaryOp")) {
                String output = visit(expressionToNegate.getJmmChild(0));
                String[] lista = output.split("\n");
                for (int i = 0; i < lista.length-1; i++) {

                    negationCode.append(lista[i] + "\n");
                }
                negationCode.append("!.bool " + lista[lista.length-1]+ ";\n");
            }
            else {
                String output = visit(expressionToNegate.getJmmChild(0));
                String[] lista = output.split("\n");
                negationCode.append(lista[0] + "\n").append("!.bool " + lista[1] + ";" + "\n");
                // negationCode.append(expressionToNegate.getJmmChild(0).get("value")).append("!.bool\n");
            }
        }
        else if (expressionToNegate.getKind().equals("Identifier")) {
            negationCode.append("!.bool ").append(expressionToNegate.get("value")).append(getVarType(expressionToNegate.get("value"), expressionToNegate));
        }
        else {

        }
        return negationCode.toString();
    } // Not sure whether or not binaryOp should have aux var

    private String operationVisit(JmmNode operation, String s) {

        StringBuilder operationStr = new StringBuilder();
        String leftNewVar = "";
        String rightNewVar = "";
        String retType = OllirUtils.getOperatorOfOllir(operation);
        String opType = "";
        if(operation.get("op").equals("+") || operation.get("op").equals("-") || operation.get("op").equals("*") || operation.get("op").equals("/") || operation.get("op").equals("<")) {
            opType = ".i32";
        }
        else opType = ".bool";

        JmmNode left = operation.getJmmChild(0);
        JmmNode right = operation.getChildren().get(1);
        JmmNode indexLeft = operation.getJmmChild(0);
        JmmNode indexRight = operation.getJmmChild(1);
        while (indexLeft.getKind().equals("Parentesis")) {
            indexLeft.getJmmChild(0);
        }
        while (indexRight.getKind().equals("Parentesis")) {
            indexRight.getJmmChild(0);
        }
        String leftStr = visit(left);
        String rightStr = visit(right);

        if (leftStr.contains("\n")){
            String leftPrefix = leftStr.substring(0, leftStr.lastIndexOf("\n") + 1);
            operationStr.append(leftPrefix);
            leftNewVar = leftStr.substring(leftStr.lastIndexOf("\n") + 1);
        }


        if (rightStr.contains("\n")){
            String rightPrefix = rightStr.substring(0, rightStr.lastIndexOf("\n") + 1);
            operationStr.append(rightPrefix);
            rightNewVar = rightStr.substring(rightStr.lastIndexOf("\n") + 1);
        }


        if (OllirUtils.isOperation(operation.getJmmParent()))
            operationStr.append("temp").append(++varCounter).append(retType).append(" :=").append(retType).append(" ");
        System.out.println("LEFT STRING: " + leftStr);
        System.out.println("RIGHT STRING: " + rightStr);
        if (leftStr.contains("arraylength")) {
            leftStr += "temp"+varCounter;
        }
        if (leftNewVar.equals("")) operationStr.append(leftStr).append(opType);
        else operationStr.append(leftNewVar);


        operationStr.append(getOperator(operation));

        if (rightStr.contains("arraylength")) {
            rightStr += "temp"+varCounter;
        }
        if (rightNewVar.equals("")) operationStr.append(rightStr).append(opType);
        else operationStr.append(rightNewVar);

        if (OllirUtils.isOperation(operation.getJmmParent()))
            operationStr.append(";\ntemp").append(varCounter).append(retType);

        return operationStr.toString();
    } // Working for || and &&

    private int countInString(String someString, Character someChar) {
        int count = 0;
        for (int i = 0; i < someString.length(); i++) {
            if (someString.charAt(i) == someChar) {
                count++;
            }
        }
        return count;
    }

    private String getOperator(JmmNode jmmNode) {
        if (jmmNode.get("op").equals("+"))
            return " +.i32 ";
        if (jmmNode.get("op").equals("-"))
            return " -.i32 ";
        if (jmmNode.get("op").equals("*"))
            return " *.i32 ";
        if (jmmNode.get("op").equals("/"))
            return " /.i32 ";
        if (jmmNode.get("op").equals("<"))
            return " <.bool ";
        if (jmmNode.get("op").equals("&&"))
            return " &&.bool ";
        if (jmmNode.get("op").equals("||"))
            return " ||.bool ";
        return " .V";
    } // DONE

    public static int getSecondSpaceIndex(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        if (firstSpaceIndex != -1) {
            int secondSpaceIndex = input.indexOf(' ', firstSpaceIndex + 1);
            if (secondSpaceIndex != -1) {
                return secondSpaceIndex;
            }
        }
        return -1; // Return -1 if the second space is not found
    }
    public String arrayAccessVisit(JmmNode arrayAccessDeclaration, String s){
        boolean funcCallInIndex = false;
        StringBuilder retStr = new StringBuilder();
        StringBuilder auxiliary = new StringBuilder();
        JmmNode index = arrayAccessDeclaration.getJmmChild(1);
        String tipo = "";
        String visited = "";

        while (index.getKind().equals("Parentesis")) {
            index = index.getJmmChild(0);
        }

        if (index.getKind().equals("FunctionCall")) { // STILL GOTTA FIX THIS
            visited = visit(arrayAccessDeclaration.getJmmChild(1));
            System.out.println("FunctionCallVisit: " + visited);
            tipo = OllirUtils.getTypeOfOllir(symbolTable.getReturnType(index.get("name")));
            funcCallInIndex = true;
        }

        else if (index.getKind().equals("BinaryOp")) {
            if (index.get("op").equals("+") || index.get("op").equals("-") || index.get("op").equals("*") || index.get("op").equals("/") || index.get("op").equals("<") || index.get("op").equals(">")) {
                tipo = ".i32";
            }
            else {
                tipo = ".bool";
            }

            visited = visit(arrayAccessDeclaration.getJmmChild(1));
            if (visited.contains("arraylength")) {
                String[] splits = visited.split("\n");
                List<String>list = Arrays.asList(splits);
                LinkedHashSet<String> setSplit = new LinkedHashSet<>(list);
                splits = setSplit.toArray(new String[setSplit.size()]);
                StringBuilder newvisited = new StringBuilder();
                for (String split : splits) {
                    if (!split.contains(":=")) {
                        split = "temp" + ++varCounter + tipo + " :=" + tipo + " " + split;
                    }
                    if (!split.contains(";")) {
                        split += ";";
                    }
                    newvisited.append(split);
                }
                visited = newvisited.toString();

            }

            System.out.println("Visited: " + visited);
        }
        else if (index.getKind().equals("ArrayAccess")){
            tipo = ".i32";
            visited = visit(arrayAccessDeclaration.getJmmChild(1));
            String[] strings = visited.split("\n");
            String finalString = strings[strings.length-1];
            visited = "";
            for (String split : strings) {
                if (finalString.equals(split)) {
                    if (!split.contains(":=")) {
                        split = "temp" + ++varCounter + tipo + " :="+tipo + " " + split;
                    }
                }
                visited += split;
            }
        }
        else {
            tipo = getVarType(arrayAccessDeclaration.getJmmChild(1).get("value"), arrayAccessDeclaration.getJmmChild(1));
            visited = visit(arrayAccessDeclaration.getJmmChild(1));
        }
        auxiliary.append("temp" + varCounter++ +tipo);
        StringBuilder previousVarDecl = new StringBuilder();
        if (visited.contains(".")) {
            if (visited.substring(visited.lastIndexOf(".")).contains(tipo)) {
                previousVarDecl.append(auxiliary + " :=" + tipo + " " + visited);
            } else {
                previousVarDecl.append(auxiliary + " :=" + tipo + " " + visited + tipo);
            }
        }
        else {
            previousVarDecl.append(auxiliary + " :=" + tipo + " " + visited + tipo);
        }
        System.out.println("prevVarDecl: " + previousVarDecl);
        StringBuilder mainDecl = new StringBuilder();
        mainDecl.append(arrayAccessDeclaration.getJmmChild(0).get("value") + "[" + auxiliary + "]" + tipo);
        System.out.println("previous var declaration: " +previousVarDecl.substring(previousVarDecl.length()-1));
        if (!previousVarDecl.substring(previousVarDecl.length()-1).contains(";") && !previousVarDecl.substring(previousVarDecl.length()-1).trim().equals("")) {
            previousVarDecl.append(";");
        }
        retStr.append(previousVarDecl+"\n"+mainDecl);
        System.out.println("PreviousVarDecl: " + previousVarDecl);
        System.out.println("RETSTRING: " + retStr);
        return retStr.toString();
    }

    public static StringBuilder removeWhitespaceAfterLastSemicolon(String input) {
        if (input.contains(";")) {
            int lastIndex = input.lastIndexOf(';');
            if (lastIndex != -1) {
                StringBuilder sb = new StringBuilder(input);

                // Remove whitespace characters after the last semicolon
                for (int i = lastIndex + 1; i < sb.length(); i++) {
                    if (Character.isWhitespace(sb.charAt(i))) {
                        sb.deleteCharAt(i);
                        i--; // Decrement the index to adjust for removed character
                    }
                }
            }
        }

        // No semicolon found, return the original string
        return new StringBuilder(input.toString().replaceAll("\\s", ""));
    }

    private String returnVisit(JmmNode returnDeclaration, String s) {
        String temp = "temp" + varCounter;
        Boolean isTemp = false;

        StringBuilder returnString = new StringBuilder();
        JmmNode exp = returnDeclaration.getChildren().get(0);
        String expression = visit(exp);


        String tipo = OllirUtils.getTypeOfOllir(symbolTable.getReturnType(getMethodName(returnDeclaration)));

        if(expression.contains(temp))
            isTemp = true;

        if (expression.contains("\n")) {
            returnString.append(expression.substring(0, expression.lastIndexOf("\n")+1));
            expression = expression.substring(expression.lastIndexOf("\n")+1);
        }
        if (OllirUtils.isOperation(exp)) {
            returnString.append("temp").append(++varCounter).append(tipo).append(" :=").append(tipo).append(" ").append(expression).append(";\n");
            expression = ("temp" + varCounter);
        }
        else if (exp.getKind().equals("FunctionCall")) {
            String functionCall = visit(exp);
            List<String>everyVariable = splitStringByDelimiter(functionCall.toString());
            int counter = 0;
            for (String variable : everyVariable) {
                if (variable.contains("invoke") && !variable.contains(":=")) {
                    if (variable.contains("\n")) {
                        everyVariable.set(counter, "\nt" + ++varCounter + tipo + " :=" + tipo + " " + variable.substring(1));
                    }
                    else {
                        everyVariable.set(counter, "\nt" + ++varCounter + tipo + " :=" + tipo + " " + variable);
                    }
                }
                counter++;
            }
            returnString = new StringBuilder();
            for (var v : everyVariable) {
                returnString.append(v);
            }
            expression = ("temp" + varCounter);
        }
        if (expression.contains(".") && !expression.contains("$")) {
            expression = expression.substring(0, expression.lastIndexOf("."));
        }
        returnString.append("ret").append(tipo).append(" ");

        if(isTemp)
            returnString.append("temp" + varCounter).append(tipo).append(";\n");
        else
            returnString.append(expression).append(tipo).append(";\n");

        return returnString.toString();
    }



    private String assignmentVisit(JmmNode assignmentDeclaration, String s) {

        StringBuilder metodo = new StringBuilder();
        JmmNode id = assignmentDeclaration.getJmmChild(0);
        JmmNode assignmentNode = assignmentDeclaration.getJmmChild(1);
        String tipo = "";
        if (assignmentDeclaration.getNumChildren() > 0) {
            if (id.getKind().equals("ArrayAccess")) {
                tipo = getVarType(assignmentDeclaration.getJmmChild(0).getJmmChild(0).get("value"), assignmentDeclaration.getJmmChild(0).getJmmChild(0));
                tipo = tipo.replace(".array", "");
            } else {
                tipo = getVarType(assignmentDeclaration.getJmmChild(0).get("value"), id);
            }
        }

        String identifierSt = visit(id);
        String assignmentSt = visit(assignmentNode);

        JmmNode child;
        if (id.getNumChildren() == 0) {
            child = id;
        } else {
            child = id.getJmmChild(0);
        }

        boolean isCampo = checkField(child.get("value"), child);
        if (assignmentNode.getKind().equals("NewClass")) {
            assignmentSt = visit(assignmentNode);
            assignmentSt = assignmentSt.replaceAll(";\n", "");

        }

        if (identifierSt.contains("\n")) {
            if (!id.getKind().equals("NewClass")) {
                String pref = identifierSt.substring(0, identifierSt.lastIndexOf("\n") + 1);
                identifierSt = identifierSt.substring(identifierSt.lastIndexOf("\n") + 1);
                if (identifierSt.contains(".") && !identifierSt.contains("[")) {
                    identifierSt = identifierSt.substring(0, identifierSt.lastIndexOf("."));
                }
                metodo.append(pref);

            }
        }


        if (!isCampo) {
            if (assignmentSt.contains(";\n") && (!assignmentNode.getKind().equals("NewClass")) && (!assignmentNode.getKind().equals("FunctionCall"))) {
                String prefix = assignmentSt.substring(0, assignmentSt.lastIndexOf("\n") + 1);
                assignmentSt = assignmentSt.substring(assignmentSt.lastIndexOf("\n") + 1);
                metodo.append(prefix);
            }


            if (assignmentNode.getKind().equals("Integer"))
                assignmentSt += ".i32";
            if (assignmentNode.getKind().equals("Identifier") && !assignmentSt.contains(tipo))
                assignmentSt += getVarType(assignmentNode.get("value"), assignmentNode);

            metodo.append(identifierSt);
            if (!id.getKind().equals("ArrayAccess")) {
                metodo.append(tipo);
            }
            System.out.println("AssignmentString: " + assignmentSt);
            System.out.println("tipo: " + tipo);
            if (!assignmentSt.contains(tipo)) {
                if (!assignmentSt.substring(assignmentSt.lastIndexOf("."), assignmentSt.lastIndexOf(";")).equals(tipo)) {
                    assignmentSt = assignmentSt.substring(0, assignmentSt.lastIndexOf(".")) + tipo + ";\n";
                }
                else {
                    assignmentSt += tipo;
                }
            }
            else if (assignmentSt.lastIndexOf(tipo) != assignmentSt.length()-tipo.length() && !assignmentSt.contains("<.") && !assignmentSt.contains(">.")) { // Se não for operador booleano
                assignmentSt = assignmentSt.replace(assignmentSt.substring(assignmentSt.lastIndexOf("."), assignmentSt.lastIndexOf(";")), tipo);
            }

            if (assignmentNode.getKind().equals("FunctionCall")) {
                System.out.println(metodo.toString());
                System.out.println(assignmentSt);
                metodo.append(" :=").append(tipo).append(" ").append(assignmentSt); // typecheck
            }
            else {
                metodo.append(" :=").append(tipo).append(" ").append(assignmentSt).append(";\n"); // typecheck
            }


        } else {
            if (assignmentSt.contains("\n")) {
                String prefix = assignmentSt.substring(0, assignmentSt.lastIndexOf("\n") + 1);
                assignmentSt = assignmentSt.substring(assignmentSt.lastIndexOf("\n") + 1);
                metodo.append(prefix);
            } else if (!assignmentNode.getKind().equals("Integer") && !assignmentNode.getKind().equals("Identifier")) {
                metodo.append("temp").append(++varCounter).append(tipo).append(" :=").append(tipo).append(" ");
                metodo.append(assignmentSt).append(";\n");
                assignmentSt = "temp" + varCounter + tipo;
            }


            if (!assignmentSt.contains(".")) {
                assignmentSt = assignmentSt + ".i32";
            }

            String campoId = "";

                campoId = id.get("value") + tipo;

                if (OllirUtils.isOperation(assignmentNode)) {
                    metodo.append("temp").append(++varCounter).append(tipo).append(" :=").append(tipo).append(" ")
                            .append(assignmentSt).append(";\n");
                    assignmentSt = ("temp" + varCounter + tipo);
                }
            metodo.append("putfield(this, ").append(campoId).append(", ").append(assignmentSt).append(").V;\n");
        }

        if (assignmentNode.getKind().equals("NewClass")) {
            metodo.append("invokespecial(" + identifierSt + tipo + ", \"<init>\").V;\n");
        }

        if (assignmentSt.contains("invoke") && assignmentSt.contains(":=")) {
            String toReplace = getSubstringFromIndexUntilSecondSpace(metodo.toString(), metodo.indexOf(identifierSt));
            metodo.replace(metodo.indexOf(identifierSt), toReplace.length(), "");
            metodo.insert(metodo.indexOf("\ninvoke"), "\n"+toReplace);
            metodo.replace(metodo.indexOf("\ninvoke"), metodo.indexOf("\ninvoke")+1, "");
        }

        if (assignmentSt.contains("{{")) {
            String prevAss = assignmentSt;
            int start = assignmentSt.indexOf("{{") + 2;
            int end = assignmentSt.indexOf("}}");
            String extracted = assignmentSt.substring(start, end);
            String variableName = extracted.split(" :=")[0];
            String constant = extracted.substring(extracted.lastIndexOf(" "));
            assignmentSt = assignmentSt.replace("{{" + extracted + "}}", "");
            assignmentSt = assignmentSt.replace(constant, variableName);
            metodo.insert(0, extracted+";\n");
            System.out.println("prevAss: " + prevAss);
            metodo.replace(metodo.indexOf(prevAss),metodo.indexOf(prevAss)+prevAss.length(), assignmentSt);
        }


        return metodo.toString();
    } // DONE for integer and int[]

    public static String getSubstringFromIndexUntilSecondSpace(String str, int startIndex) {
        int count = 0;
        int index = 0;

        // Loop through the characters in the string starting from the given index
        for (int i = startIndex; i < str.length(); i++) {
            char c = str.charAt(i);

            // If the character is a space, increment the count
            if (c == ' ') {
                count++;

                // If this is the second space, store its index and break out of the loop
                if (count == 2) {
                    index = i;
                    break;
                }
            }
        }

        // Return the substring from the given index to the index of the second space
        return str.substring(startIndex, index)+" ";
    }

    private String programVisit(JmmNode program, String s) {
        for (var child : program.getChildren()) {
            code.append(visit(child));
        }
        return code.toString();
    } // DONE

    private String importDeclVisit(JmmNode importDeclaration, String s) {

        StringBuilder importStmt = new StringBuilder();
        importStmt.append("import ");


        for (var importID : symbolTable.getImports()) {
            if (importID.contains(importDeclaration.get("ID"))) {
                importStmt.append(importID); // to handle multiple imports (import a.b.c)
            }
        }


        return importStmt + ";\n";
    } // DONE


    private boolean checkIfImport(String name) {
        for (var importID : symbolTable.getImports()) {
            if (importID.equals(name)) {
                return true;
            }
        }
        return false;
    }

    private String classDeclVisit(JmmNode classDeclaration, String s) {
        //
        StringBuilder classStmt = new StringBuilder();
        String className = symbolTable.getClassName();
        classStmt.append(className);
        var superClass = symbolTable.getSuper();
        boolean existssuperclass = false;
        if (!superClass.equals("")) { // in case there is a super class
            existssuperclass = true;
            classStmt.append(" extends ").append(superClass);
        }
        classStmt.append(" {\n");

        for (int i = 0; i<classDeclaration.getChildren().size(); i++) {
            if (classDeclaration.getChildren().get(i).getKind().equals("VarDeclaration")) {
                classStmt.append(visit(classDeclaration.getChildren().get(i)));
            }
            else break;
        }
        classStmt.append(".construct ").append(symbolTable.getClassName()).append("().V{\ninvokespecial(this, \"<init>\").V;\n}\n\n");

        for (var child: classDeclaration.getChildren()) {
            if (!child.getKind().equals("Identifier") && !child.getKind().equals("VarDeclaration")) classStmt.append(visit(child));
        }




        classStmt.append("}\n");
        //

        String classString = classStmt.toString();
        classString = classString.replaceAll(";(?!\n)", ";\n");
        String[] splits = classString.split("\n");
        String finalClassCode = "";
        for (String split : splits) {
            if (countInString(split, ':') > 1) {
                split = split.substring(getSecondSpaceIndex(split));
            }
            split += "\n";
            finalClassCode += split;
        }
        return finalClassCode;
    } // DONE

    private String methodDeclVisit(JmmNode methodDeclaration, String s) {
        StringBuilder method = new StringBuilder();
        method.append(".method ");
        method.append("public ");
        List<Symbol> parameters;
        if (!methodDeclaration.hasAttribute("methodName")){
            method.append("static ");
            method.append("main(");
            parameters = symbolTable.getParameters("main");
            String codeParam = parameters.stream().map(symbol -> OllirUtils.getCode(symbol)).collect(Collectors.joining(", "));
            method.append(codeParam);
            method.append(").V");
            method.append(" {\n");
        }
        else {
            method.append(methodDeclaration.get("methodName")).append("(");
            parameters = symbolTable.getParameters(getMethodName(methodDeclaration));
            String codeParam = parameters.stream().map(symbol -> OllirUtils.getCode(symbol)).collect(Collectors.joining(", "));
            method.append(codeParam);
            method.append(")");
            method.append(OllirUtils.getTypeOfOllir(symbolTable.getReturnType(getMethodName(methodDeclaration))));
            method.append(" {\n");

        }



        for (JmmNode child : methodDeclaration.getChildren()) {
            if (!child.getKind().equals("VarDeclaration")) {
                method.append(visit(child, s));
            }
        }

        String metodoString = method.toString();
        int secondLastNewLineIndex = metodoString.lastIndexOf("\n", metodoString.lastIndexOf("\n") - 1);
        boolean containsRetAfterSecondLastNewLine = metodoString.indexOf("ret.", secondLastNewLineIndex) != -1;
        if (!containsRetAfterSecondLastNewLine) {
            method.append("ret.V;\n");
        }

        method.append("}\n");


        return method.toString();
    } // DONE

    private String varDeclVisit(JmmNode varDeclaration, String s) {

        StringBuilder variable = new StringBuilder();
        JmmNode pai = varDeclaration.getJmmParent();
        JmmNode id = varDeclaration.getJmmChild(0);

        if (pai.getKind().equals("ClassStmt")) {
            variable.append(".field private ");
        }
        if (id.getKind().equals("Array")) {
            variable.append(varDeclaration.get("name"));
            variable.append(".array");
            variable.append(getVarType(id.getJmmChild(0).get("value"), id));
            variable.append(";\n");
        }
        else { // working for boolean
            variable.append(varDeclaration.get("name"));

            variable.append(getVarType(id.get("value"), id));
            variable.append(";\n");
        }


        return variable.toString();
    } // DONE for array, int and boolean

    private String numberVisit(JmmNode numberDeclaration, String s) {


        return numberDeclaration.get("value");
    }



    private String identifierVisit(JmmNode idDeclaration, String s) {

        List<Symbol> campos = symbolTable.getFields();

        if (idDeclaration.get("value").equals("true")) return "true";
        else if (idDeclaration.get("value").equals("false")) return "false";

        if (checkField(idDeclaration.get("value"), idDeclaration)) {
                String tipo = getVarType(idDeclaration.get("value"), idDeclaration);
                return "temp" + ++varCounter + tipo + " :="
                        + tipo + " " + "getfield(this, " + idDeclaration.get("value")
                        + tipo + ")" + tipo + ";\nt" + varCounter
                        + tipo;
            }
        String metodo = getMethodName(idDeclaration);
        if (!metodo.equals("")) { // if method
            List<Symbol> parameters = symbolTable.getParameters(metodo);
            var counter = -1;
            for (Symbol symbol : parameters) {
                counter++;
                if (symbol.getName().equals(idDeclaration.get("value"))) {
                    return "$" + (counter + 1) + "." + idDeclaration.get("value");
                }
            }
        }

        return idDeclaration.get("value");
    }

    public String getMethodName(JmmNode node) {
        //
        while(node!=null && !node.getKind().equals("MethodDeclaration")){
            //
            node = node.getJmmParent();
        }
        //

        if(node!=null && node.hasAttribute("methodName"))
            return node.get("methodName");

        return "main";
    } // DONE

    private boolean checkField(String nome, JmmNode id) {
        String methodName = getMethodName(id);

        for (String method : symbolTable.getMethods()) {
            if (methodName.equals(method) || methodName.equals("")) {
                for (Symbol symbol : symbolTable.getParameters(method))
                    if (nome.equals(symbol.getName()))
                        return false;

                for (Symbol symbol : symbolTable.getLocalVariables(method)) {
                    if (nome.equals(symbol.getName())) {
                        return false;
                    }
                }
            }
        }

        for (Symbol symbol : symbolTable.getFields())
            if (nome.equals(symbol.getName())) {

                return true;
            }
        return false;
    }

    private String getVarType(String nome, JmmNode id) {
        String methodName = getMethodName(id);

        if (checkIfImport(nome)) {
            return ".V";
        }

        if (nome.equals(symbolTable.getClassName())) {
            return "."+nome;
        }

        if (nome.equals("boolean") || nome.equals("false") || nome.equals("true")) {
            return ".bool";
        }

        for (String method : symbolTable.getMethods()) {
            if (methodName.equals(method) || methodName.equals("")){
                for (Symbol symbol : symbolTable.getParameters(method))
                    if (nome.equals(symbol.getName()))
                        return OllirUtils.getTypeOfOllir(symbol.getType());

                for (Symbol symbol : symbolTable.getLocalVariables(method))
                    if (nome.equals(symbol.getName())) {
                        return OllirUtils.getTypeOfOllir(symbol.getType());
                    }
            }
        }

        for (Symbol symbol : symbolTable.getFields())
            if (nome.equals(symbol.getName())) {

                return OllirUtils.getTypeOfOllir(symbol.getType());
            }



        return ".i32"; // int
    } // DONE

    private String defaultVisit(JmmNode defaultNode, String s) {
        StringBuilder visitStr = new StringBuilder();
        for (JmmNode child : defaultNode.getChildren()) {
            visitStr.append(visit(child, s));
        }
        return visitStr.toString();
    } // DONE


}
