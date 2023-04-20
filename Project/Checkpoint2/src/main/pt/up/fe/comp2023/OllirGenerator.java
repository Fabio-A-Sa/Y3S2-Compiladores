package pt.up.fe.comp2023;

import org.specs.comp.ollir.Ollir;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;


import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OllirGenerator extends AJmmVisitor<String, String> {
    static int varCounter;
    static int markerCounter;
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
        // addVisit("Object", this::objectVisit);
        //TODO
        //addVisit("FunctionCall", this::memberCallVisit);
        setDefaultVisit(this::defaultVisit);
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
                    if (!parameter.getKind().equals("BinaryOp") && !parameter.getKind().equals("FunctionCall")) {
                        String parametroVisitado = visit(parameter);
                        if (parametroVisitado.contains("getfield")) {
                            String tempvar = parametroVisitado.split("\n")[0] + "\n";
                            String thistemp = parametroVisitado.split("\n")[1];
                            methodString.insert(0, tempvar);
                            methodString.append(thistemp);
                        }
                        else {
                            methodString.append(parametroVisitado).append(getVarType(parameter.get("value"), parameter));
                        }
                    }
                    else if (!parameter.hasAttribute("op")){ // function call
                        String thistemp = "t" + varCounter + getVarType(parameter.get("name"), parameter);
                        String tempVar = "t" + varCounter++ + getVarType(parameter.get("name"), parameter)
                                + " :=" + getVarType(parameter.get("name"), parameter) + " " + visit(parameter);
                        methodString.insert(0, tempVar);
                        methodString.append(thistemp);
                    }
                    else {
                        String thistemp = "t" + varCounter + getBinOp(parameter.get("op"));
                        String tempVar = "t" + varCounter++ +  getBinOp(parameter.get("op"))
                                + " :=" +  getBinOp(parameter.get("op")) + " " + visit(parameter) + ";\n";
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
                        String thistemp = "t" + varCounter + getVarType(parameter.get("name"), parameter);
                        String tempVar = "t" + varCounter++ + getVarType(parameter.get("name"), parameter)
                                + " :=" + getVarType(parameter.get("name"), parameter) + " " + visit(parameter);
                        methodString.insert(0, tempVar);
                        methodString.append(thistemp);
                    }
                    else {
                        String thistemp = "t" + varCounter + getBinOp(parameter.get("op"));
                        String tempVar = "t" + varCounter++ +  getBinOp(parameter.get("op"))
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


    } // Need to fix function calls with binaryOps as parameters



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
                newString.append("new(array, ");
                newString.append(visit(child.getJmmChild(0)));
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

            methodString.append("t").append(++varCounter).append(".i32 :=.i32 arraylength(").append(idLenStr).append(getVarType(id.get("value"), id)).append(").i32;\n"); //todo check type
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
            negationCode.append(expressionToNegate.get("value")).append("!.bool\n");
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
            operationStr.append("t").append(++varCounter).append(retType).append(" :=").append(retType).append(" ");

        if (leftNewVar.equals("")) operationStr.append(leftStr).append(opType);
        else operationStr.append(leftNewVar);


        operationStr.append(getOperator(operation));


        if (rightNewVar.equals("")) operationStr.append(rightStr).append(opType);
        else operationStr.append(rightNewVar);

        if (OllirUtils.isOperation(operation.getJmmParent()))
            operationStr.append(";\nt").append(varCounter).append(retType);

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

    public String arrayAccessVisit(JmmNode arrayAccessDeclaration, String s){
        StringBuilder retStr = new StringBuilder();
        JmmNode identifier = arrayAccessDeclaration.getChildren().get(0);
        JmmNode indexNode = arrayAccessDeclaration.getChildren().get(1);
        String idString = visit(identifier);
        String indexString = visit(indexNode);

        String type = getVarType(identifier.get("value"), identifier).replace(".array", "");

        if (idString.contains("\n")) {
            retStr.append(idString.substring(0, idString.lastIndexOf("\n") + 1));
            idString = idString.substring(idString.lastIndexOf("\n") + 1);
            if (idString.contains(".")) idString = idString.substring(0, idString.indexOf("."));
        }

        if (indexString.contains("\n")){
            retStr.append(indexString.substring(0, indexString.lastIndexOf("\n") + 1));
            indexString = indexString.substring(indexString.lastIndexOf("\n") + 1);
        }

        if (indexNode.getKind().equals("Integer")) {
            retStr.append("t").append(++varCounter).append(".i32 :=.i32 ").append(indexString).append(".i32;\n");
            indexString = ("t" + varCounter);
        }


        if (indexString.contains("$")){
            retStr.append("t").append(++varCounter).append(".i32 :=.i32 ").append(indexString).append(type).append(";\n");
            indexString = ("t" + varCounter);
        }

        if (OllirUtils.isOperation(indexNode)){
            retStr.append("t").append(++varCounter).append(".i32 :=.i32 ").append(indexString).append(";\n");
            indexString = ("t" + varCounter);
        }

        if (indexString.contains(".")) indexString = indexString.substring(0, indexString.lastIndexOf("."));

        retStr.append("t").append(++varCounter).append(type);
        retStr.append(" :=").append(type).append(" ");
        retStr.append(idString).append("[").append(indexString).append(".i32").append("]").append(type);
        retStr.append(";\nt").append(varCounter).append(type);//typecheck

        return retStr.toString();
    }

    private String returnVisit(JmmNode returnDeclaration, String s) {
        String temp = "t" + varCounter;
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
            returnString.append("t").append(++varCounter).append(tipo).append(" :=").append(tipo).append(" ").append(expression).append(";\n");
            expression = ("t" + varCounter);
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
            expression = ("t" + varCounter);
        }
        if (expression.contains(".") && !expression.contains("$")) {
            expression = expression.substring(0, expression.lastIndexOf("."));
        }
        returnString.append("ret").append(tipo).append(" ");

        if(isTemp)
            returnString.append("t" + varCounter).append(tipo).append(";\n");
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
                tipo = getVarType(assignmentDeclaration.getJmmChild(0).get("value"), assignmentDeclaration.getJmmChild(0)).replace(".array", ""); // tirar o .array do tipo
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
            if (id.getKind().equals("ArrayAccess")) {
                if (id.getJmmChild(0).getKind().equals("Identifier")) {
                    String previous = identifierSt.substring(0, identifierSt.lastIndexOf(";\n"));
                    if (previous.contains("\n")) {
                        metodo.append(previous.substring(0, previous.lastIndexOf("\n") + 1));
                    }
                    identifierSt = previous.substring(previous.lastIndexOf(" ") + 1);
                }
            } else if (!id.getKind().equals("NewClass")) {
                String pref = identifierSt.substring(0, identifierSt.lastIndexOf("\n") + 1);
                identifierSt = identifierSt.substring(identifierSt.lastIndexOf("\n") + 1);
                if (identifierSt.contains(".")) {
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


            if (!assignmentSt.contains(tipo)) {
                assignmentSt += tipo;
            }

            else if (assignmentSt.lastIndexOf(tipo) != assignmentSt.length()-tipo.length()) {
                assignmentSt = assignmentSt.replace(assignmentSt.substring(assignmentSt.lastIndexOf("."), assignmentSt.lastIndexOf(";")), tipo);
            }

            if (assignmentNode.getKind().equals("FunctionCall")) {
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
                metodo.append("t").append(++varCounter).append(tipo).append(" :=").append(tipo).append(" ");
                metodo.append(assignmentSt).append(";\n");
                assignmentSt = "t" + varCounter + tipo;
            }


            if (!assignmentSt.contains(".")) {
                assignmentSt = assignmentSt + ".i32";
            }

            String campoId = "";
            if (id.getKind().equals("ArrayAccess")) {
                metodo.append(identifierSt).append(" :=").append(tipo).append(" ").append(assignmentSt).append(";\n");
                assignmentSt = identifierSt.substring(0, identifierSt.indexOf("[")) + ".array" + tipo;
                campoId = id.getJmmChild(0).get("name") + ".array" + tipo;
            } else {
                campoId = id.get("value") + tipo;

                if (OllirUtils.isOperation(assignmentNode)) {
                    metodo.append("t").append(++varCounter).append(tipo).append(" :=").append(tipo).append(" ")
                            .append(assignmentSt).append(";\n");
                    assignmentSt = ("t" + varCounter + tipo);
                }
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
        return classStmt.toString();
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

        if (idDeclaration.get("value").equals("true")) return "true.bool";
        else if (idDeclaration.get("value").equals("false")) return "false.bool";

        if (checkField(idDeclaration.get("value"), idDeclaration)) {
                String tipo = getVarType(idDeclaration.get("value"), idDeclaration);
                return "t" + ++varCounter + tipo + " :="
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
