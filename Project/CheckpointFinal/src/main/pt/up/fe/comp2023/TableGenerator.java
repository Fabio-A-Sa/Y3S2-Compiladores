package pt.up.fe.comp2023;

import java.util.*;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

public class TableGenerator extends AJmmVisitor<String, String> implements SymbolTable{

    String className;
    String extendedClass;
    List<String> imports;
    List<String> staticMethods;
    Map<String, Type> methodRets;           // <methodName, returnType>
    Map<String, List<Symbol>> methodArgs;   // <methodName, arguments>
    Map<String, List<Symbol>> methodVars;   // <methodName, scopeVariables>
    List<Report> reports;

    public TableGenerator () {
        this.imports = new ArrayList<String>();
        this.staticMethods = new ArrayList<String>();
        this.methodRets = new HashMap<String, Type>();
        this.methodArgs = new HashMap<String, List<Symbol>>();
        this.methodVars = new HashMap<String, List<Symbol>>();
        this.reports = new ArrayList<>();
        this.extendedClass = "";
    }

    void fill(JmmNode rootNode, List<Report> reports) {
        this.visit(rootNode);
        reports.addAll(this.reports);
    }

    void insertElement(Map<String, List<Symbol>> field, String key, Symbol value) {
        if (field.containsKey(key)) {
            field.get(key).add(value);
        } else {
            List<Symbol> newList = new ArrayList<>();
            newList.add(value);
            field.put(key, newList);
        }
    }

    boolean isArray(JmmNode jmmNode) {
        return jmmNode.getKind().equals("Array");
    }

    protected void buildVisitor() {
        addVisit("Program", this::dealWithProgram);
        addVisit("ImportStmt", this::dealWithImportStmt);
        addVisit("ClassStmt", this::dealWithClassStmt);
        addVisit("VarDeclaration", this::dealWithVarDeclaration);
        addVisit("Array", this::dealWithArray);
        addVisit("MethodDeclaration", this::dealWithMethodDeclaration);
        addVisit("String", this::dealWithType);
        addVisit("Double", this::dealWithType);
        addVisit("Boolean", this::dealWithType);
        addVisit("Int", this::dealWithType);
        addVisit("Integer", this::dealWithType);
        addVisit("Id", this::dealWithType);
        addVisit("Identifier", this::dealWithType);
        addVisit("ReturnType", this::dealWithReturnType);
        addVisit("Length", this::dealWithLength);
        addVisit("FunctionCall", this::dealWithFunctionCall);
        addVisit("ArrayAccess", this::dealWithArrayAccess);
        addVisit("ReturnStmt", this::dealWithReturnStmt);
        addVisit("IfExpr", this::dealWithIfExpr);
        addVisit("ElseifExpr", this::dealWithIfExpr);
        addVisit("Brackets", this::dealWithBrackets);
        addVisit("WhileStmt", this::dealWithWhileStmt);
        addVisit("Negation", this::dealWithNegation);
        setDefaultVisit(this::dealWithDefault);
    }

    private String dealWithNegation(JmmNode jmmNode, String s) {
        JmmNode expression = jmmNode.getChildren().get(0);
        Type type = getNodeType(expression);

        if(!type.getName().equals("boolean") || type.isArray())
            addReport(expression, "Negation can only be applied to a boolean expression");

        return visit(expression, s);
    }

    private String dealWithWhileStmt(JmmNode jmmNode, String s) {
        JmmNode expression = jmmNode.getChildren().get(0);
        JmmNode statement = jmmNode.getChildren().get(1);

        Type exprType = getNodeType(expression);
        if(exprType.isArray() || !exprType.getName().equals("boolean"))
            addReport(expression, "Invalid while condition");

        for(JmmNode child: jmmNode.getChildren())
            visit(child, s);
        return s;
    }

    private String dealWithBrackets(JmmNode jmmNode, String s) {
        for(JmmNode child: jmmNode.getChildren()) {
            if(child.getKind().equals("Assignment")) {
                verificateExpression(s, child);
                verificateAssignment(child);
            }
            visit(child, s);
        }

        return s;
    }

    private String dealWithIfExpr(JmmNode jmmNode, String s) {
        JmmNode expression = jmmNode.getChildren().get(0);
        JmmNode statement = jmmNode.getChildren().get(1);

        Type exprType = getNodeType(expression);
        if(exprType.isArray() || !exprType.getName().equals("boolean"))
            addReport(expression, "Invalid if condition");

        for(JmmNode child: jmmNode.getChildren())
            visit(child, s);
        return s;
    }

    private String dealWithReturnStmt(JmmNode jmmNode, String s) {
        JmmNode stmt = jmmNode.getChildren().get(0);

        Type returnStmtType = getNodeType(stmt);
        Type returnType = getNodeType(jmmNode.getJmmParent());

        if(!returnStmtType.getName().equals("Imported")) {
            if(!returnStmtType.getName().equals(returnType.getName()) || returnStmtType.isArray()!=returnType.isArray()) {
                addReport(jmmNode, "Invalid return");
            }
        }

        String methodName = getMethodName(jmmNode.getJmmParent());

        for(JmmNode child : jmmNode.getChildren())
            visit(child, s);

        verificateNode(methodName, stmt);

        return s;
    }

    private String dealWithArrayAccess(JmmNode jmmNode, String s) {
        // expression '[' expression ']'
        JmmNode accessNode = jmmNode.getChildren().get(0);
        JmmNode indexNode = jmmNode.getChildren().get(1);

        Type accessNodeType = getNodeType(accessNode);

        if(!accessNodeType.isArray()){
            addReport(jmmNode, "Invalid array access operation. This operation is only valid for arrays");
            return s;
        }

        Type indexNodeType = getNodeType(indexNode);
        if(!indexNodeType.getName().equals("int")){
            addReport(jmmNode, "Invalid array access operation. Index must be of type integer but got " + indexNodeType.getName());
            return s;
        }
        return s;
    }

    private String dealWithFunctionCall(JmmNode jmmNode, String s) {
        while(true) {
            if(jmmNode.getNumChildren()==0)
                break;

            JmmNode child = jmmNode.getChildren().get(0);
            if(child.getKind().equals("Parameter")) {
                verificateParameters(jmmNode);
                break;
            }

            String name ="";
            if(child.getKind().equals("Identifier"))
                name = child.get("value");
            else if(child.getKind().equals("FunctionCall"))
                name = child.get("name");

            if(containsImport(name))
                return s;

            verificateParameters(jmmNode);
            jmmNode = jmmNode.getChildren().get(0);
        }

        verificateReturnType(jmmNode);

        return s;
    }

    private String dealWithLength(JmmNode jmmNode, String s) {
        JmmNode object = jmmNode.getChildren().get(0);
        Type type = getNodeType(object);

        if(!type.isArray())
            addReport(jmmNode, "Built-in \"length\" is only valid over arrays");

        return s;
    }

    private String dealWithReturnType(JmmNode jmmNode, String s) {
        String type;
        if(jmmNode.getNumChildren() == 0)
            return jmmNode.get("value");
        else
            type = visit(jmmNode.getChildren().get(0), s);
        return type;
    }

    private String dealWithType(JmmNode jmmNode, String s) {
        return jmmNode.get("value");
    }

    private String dealWithProgram(JmmNode jmmNode, String s) {
        for (JmmNode child : jmmNode.getChildren()) visit(child);
        return s;
    }

    private String dealWithImportStmt(JmmNode jmmNode, String s) {
        String imports = jmmNode.get("value").substring(1, jmmNode.get("value").length()-1);
        String final_import = String.join(".", imports.split(", "));

        this.imports.add(final_import);

        return s;
    }

    private String dealWithClassStmt(JmmNode jmmNode, String s) {
        this.className = jmmNode.get("className");
        if (jmmNode.hasAttribute("extendedClass")) this.extendedClass = jmmNode.get("extendedClass");

        for(JmmNode child : jmmNode.getChildren())
            visit(child, "global");

        for(JmmNode child : jmmNode.getChildren()) {
            if(child.getKind().equals("MethodDeclaration")){
                String methodName="main";
                if(child.hasAttribute("methodName"))
                    methodName= child.get("methodName");
                verificateMethod(child, methodName);
            }
        }

        return s;
    }

    private String dealWithVarDeclaration(JmmNode jmmNode, String scope) {
        JmmNode firstChild = jmmNode.getChildren().get(0);
        String varType = visit(firstChild);

        Type newType = new Type(varType, this.isArray(firstChild) || jmmNode.hasAttribute("op"));
        Symbol newVar = new Symbol(newType, jmmNode.get("name"));

        //Verifica se a variável que está a ser declarada, já foi declarada previamente
        if(containsField(jmmNode.get("name"), scope) ) {
            addReport(firstChild, "Duplicate field \"" + jmmNode.get("name") + "\"");
        }
        else {
            this.insertElement(this.methodVars, scope, newVar);
        }

        return scope;
    }

    private String dealWithArray(JmmNode jmmNode, String s) {
        return visit(jmmNode.getChildren().get(0), s);
    }

    private String dealWithMethodDeclaration(JmmNode jmmNode, String s) {
        String methodName, returnType;

        if (jmmNode.hasAttribute("methodName")) {
            // example: int getSum(int a, int b)
            methodName = jmmNode.get("methodName");
            returnType = visit(jmmNode.getChildren().get(0), s);
            this.methodArgs.put(methodName, new ArrayList<Symbol>());

            if(jmmNode.getNumChildren()>0)
                for(JmmNode child : jmmNode.getChildren())
                    if(child.getKind().equals("Argument"))
                        dealWithArgument(child, methodName);

        } else {
            // example: static void main (String[] args)

            methodName = "main";
            returnType = "static void";
            List<Symbol> symbols = new ArrayList<Symbol>();
            symbols.add(new Symbol(new Type("String", true), "args"));
            this.methodArgs.put(methodName, symbols);
            this.staticMethods.add(methodName);
        }

        if(jmmNode.hasAttribute("isStatic"))
            this.staticMethods.add(methodName);

        if(jmmNode.getNumChildren() != 0) {
            Boolean isArray = false;
            JmmNode fstChild = jmmNode.getChildren().get(0);
            if(!fstChild.getChildren().get(0).hasAttribute("value"))
                isArray = true;

            Type type = new Type(returnType,isArray);
            List<Symbol> l = new ArrayList<>();
            this.methodRets.put(methodName, type);
            this.methodVars.put(methodName, l);
        }
        else {
            Type type = new Type(returnType,true);
            this.methodRets.put(methodName, type);
        }

        return s;
    }

    private String dealWithArgument(JmmNode jmmNode, String scope) {
        JmmNode firstChild = jmmNode.getChildren().get(0);
        String varType = visit(firstChild);

        Type newType = new Type(varType, this.isArray(firstChild));
        Symbol newVar = new Symbol(newType, jmmNode.get("argName"));

        this.insertElement(this.methodArgs, scope, newVar);
        for (JmmNode child : jmmNode.getChildren()) visit(child, scope);

        return scope;
    }

    public String dealWithDefault(JmmNode jmmNode, String s) {
        for(JmmNode child : jmmNode.getChildren())
            visit(child, s);
        return s;
    }

    @Override
    public List<String> getImports() {
        return this.imports;
    }

    @Override
    public String getClassName() {
        return this.className;
    }

    @Override
    public String getSuper() {
        return this.extendedClass;
    }

    @Override
    public List<Symbol> getFields() {
        return this.getLocalVariables("global");
    }

    @Override
    public List<String> getMethods() {
        return new ArrayList<String>(this.methodRets.keySet());
    }

    @Override
    public Type getReturnType(String s) {
        return this.methodRets.get(s);
    }

    @Override
    public List<Symbol> getParameters(String s) {
        return this.methodArgs.get(s) == null ? List.of() : this.methodArgs.get(s);
    }

    @Override
    public List<Symbol> getLocalVariables(String s) {
        return this.methodVars.get(s) == null ?  List.of() : this.methodVars.get(s);
    }

    public boolean containsImport(String className) {
        for(String importName : this.imports) {
            String[] importedClasses = importName.split("\\.");
            String lastClass = importedClasses[importedClasses.length -1];
            if(lastClass.equals(className))
                return true;
        }
        return false;
    }

    public boolean containsField(String varName, String scope) {
        List<Symbol> classFields = this.methodVars.get(scope);

        if(classFields == null)
            return false;

        for(Symbol field : classFields) {
            if(field.getName().equals(varName))
                return true;
        }
        return false;
    }

    public boolean containsArg(String varName, String scope) {
        List<Symbol> methodArgs = this.methodArgs.get(scope);

        if(methodArgs == null)
            return false;

        for(Symbol arg : methodArgs) {
            if(arg.getName().equals(varName))
                return true;
        }
        return false;
    }

    public String getMethodName(JmmNode node) {
        while(node!=null && !node.getKind().equals("MethodDeclaration")){
            node = node.getJmmParent();
        }

        if(node!=null && node.hasAttribute("methodName"))
            return node.get("methodName");

        return "main";
    }

    public Type getVariableType(String var, String methodName){
        List<Symbol> args = this.methodArgs.get(methodName);
        List<Symbol> localVars = this.methodVars.get(methodName);
        List<Symbol> globalVars = this.methodVars.get("global");

        if(this.imports.contains(var) || this.extendedClass.equals(var))
            return new Type(var, false);

        if(args != null)
            for(Symbol arg : args){
                if(arg.getName().equals(var))
                    return arg.getType();
            }

        if(localVars != null)
            for(Symbol localVar : localVars) {
                if(localVar.getName().equals(var))
                    return localVar.getType();
            }

        if(globalVars != null)
            for(Symbol globalVar : globalVars) {
                if(globalVar.getName().equals(var))
                    return globalVar.getType();
            }

        return new Type("Undefined", false);
    }

    public Type getNodeType(JmmNode node) {
        String type = node.getKind();

        if(type.equals("Identifier")) {
            if(node.get("value").equals("true") || node.get("value").equals("false"))
                return new Type("boolean", false);
            String methodName = getMethodName(node);
            return getVariableType(node.get("value"), methodName);
        }
        else if(type.equals("BinaryOp")) {
            String op = node.get("op");
            verificateOperands(node);

            if(op.equals("*") || op.equals("/") || op.equals("+") || op.equals("-"))
                return new Type("int", false);
            else
                return new Type("boolean", false);
        }
        else if(type.equals("Parentesis"))
            return getNodeType(node.getChildren().get(0));
        else if(type.equals("Integer")) {
            return new Type("int", false);
        }
        else if(type.equals("ArrayAccess")) {
            String arrayType = getNodeType(node.getChildren().get(0)).getName();
            return new Type(arrayType, false);
        }
        else if(type.equals("FunctionCall")) {
            return getReturnType(node);
        }
        else if(type.equals("NewClass")) {
            String name = node.get("classname");
            return new Type(name, false);
        }
        else if(type.equals("ArrayDeclaration")) {
            return new Type("int", true);
        }
        else if(type.equals("MethodDeclaration")) {
            return getReturnType(node);
        }
        else if(type.equals("Object")) {
            return new Type(this.className, false);
        }
        else if(type.equals("Negation")) {
            return new Type("boolean", false);
        }
        else if(type.equals("Length")) {
            return new Type("int", false);
        }

        return new Type("Undefined", false);
    }

    public void addReport(JmmNode node, String message) {
        Report report = new Report(ReportType.ERROR, Stage.SEMANTIC, -1, -1, message);
        reports.add(report);
    }

    public void verificateVarDefined(String methodName, JmmNode varNode, Boolean isStatic) {
        String varName = varNode.get("value");

        if(isStatic)
            if(!containsField(varName, methodName) && !containsArg(varName, methodName)) {
                addReport(varNode, "Variable \"" + varName + "\" is undefined");
                return;
            }

        if(!containsField(varName, methodName) && !containsField(varName, "global") && !containsArg(varName, methodName)){
            addReport(varNode, "Variable \"" + varName + "\" is undefined");
        }
    }

    public void verificateNode(String methodName, JmmNode node) {
        Boolean isStatic = false;
        if(this.staticMethods.contains(methodName))
            isStatic = true;

        if(node.getKind().equals("Identifier")) {
            if(!node.get("value").equals("true") && !node.get("value").equals("false"))
                verificateVarDefined(methodName, node, isStatic);
        }
        else if(node.getKind().equals("BinaryOp")) {
            verificateExpression(methodName, node);
            verificateOperands(node);
        }
        else if(node.getKind().equals("ArrayDeclaration") || node.getKind().equals("ArrayAccess")){
            verificateExpression(methodName, node);
        }
        else if(node.getKind().equals("FunctionCall")) {
            dealWithFunctionCall(node, "");
        }
        else if(node.getKind().equals("Object")) {
            if(this.staticMethods.contains(methodName))
                addReport(node, "\"This\" can't be used in a static method");
        }
    }

    public void verificateExpression(String methodName, JmmNode node) {
        verificateNode(methodName, node.getChildren().get(0));

        if(node.getNumChildren() != 1)
            verificateNode(methodName, node.getChildren().get(1));
    }

    public void verificateMethod(JmmNode jmmNode,String methodName) {
        for (JmmNode child : jmmNode.getChildren()){
            visit(child, methodName); // os nós-filho vão ter o scope deste método

            //Verificar se os assignments estão a ser bem feitos
            if(child.getKind().equals("Assignment")) {
                verificateExpression(methodName, child);
                verificateAssignment(child);
            }
        }
    }

    public void verificateParameters(JmmNode functionCallNode) {

        String methodName = functionCallNode.get("name");
        List<Symbol> arguments = this.methodArgs.get(methodName);

        List<JmmNode> parameters = new ArrayList<>();
        int numParameters;

        //Caso o método seja importado ou venha de outra classe, assume-se que os parâmetros estão certos
        if(!this.methodArgs.containsKey(methodName)) {
            return;
        }

        if(functionCallNode.getNumChildren()==0 || (functionCallNode.getNumChildren()==1 && !functionCallNode.getChildren().get(0).getKind().equals("Parameter"))) {
            numParameters = 0;
        }
        else {
            numParameters = functionCallNode.getChildren().get(functionCallNode.getNumChildren()-1).getNumChildren();
        }

        if(numParameters != arguments.size()) {
            addReport(functionCallNode, "Function call \"" + methodName + "\" wrong number of arguments");
            return;
        }

        if(numParameters != 0)
            parameters = functionCallNode.getChildren().get(functionCallNode.getNumChildren()-1).getChildren();
            for(Symbol arg : arguments) {
                String requiredType= arg.getType().getName();
                Boolean isArray = arg.getType().isArray();

                Type type = getNodeType(parameters.get(0));
                if(parameters.get(0).getKind().equals("FunctionCall")) {
                    verificateParameters(parameters.get(0));
                }

                if(!requiredType.equals(type.getName()) || type.isArray()!=isArray)
                    addReport(parameters.get(0), "Parameter \"" + parameters.get(0) + "\" has invalid type");

                parameters.remove(0);
            }
    }

    public void verificateOperands(JmmNode binaryOpNode) {
        // (a<1) ||  true
        JmmNode op1 = binaryOpNode.getChildren().get(0);
        JmmNode op2 = binaryOpNode.getChildren().get(1);
        String op = binaryOpNode.get("op");

        Type op1Type = getNodeType(op1);
        Type op2Type = getNodeType(op2);

        if(!op1Type.getName().equals(op2Type.getName()) || (op1Type.isArray()!= op2Type.isArray())) {
            addReport(binaryOpNode, "Operands must have the same type");
        }

        if((op.equals("||") || op.equals("&&")) &&
                (!op1Type.getName().equals("boolean") || !op2Type.getName().equals("boolean"))){
            addReport(binaryOpNode, "Invalid operands for logical operator \"" + op + "\"");
        }
        else if((!op1Type.getName().equals("int") || !op2Type.getName().equals("int")) &&
                (op1Type.isArray() || op2Type.isArray())){
            addReport(binaryOpNode, "Invalid operands for arithmetic operator \"" + op + "\"");
        }
    }

    public void verificateAssignment(JmmNode assignmentNode) {
        JmmNode leftNode = assignmentNode.getChildren().get(0);
        JmmNode rightNode = assignmentNode.getChildren().get(1);

        Type leftNodeType = getNodeType(leftNode);
        Type rightNodeType = getNodeType(rightNode);

        if(containsImport(leftNodeType.getName()) && containsImport(rightNodeType.getName()))
            return;

        if(leftNodeType.getName().equals(this.extendedClass) || rightNodeType.getName().equals(this.extendedClass) || leftNodeType.getName().equals("Imported") || rightNodeType.getName().equals("Imported"))
            return;

        if(!leftNodeType.getName().equals(rightNodeType.getName()) || leftNodeType.isArray()!=rightNodeType.isArray()) {
            String expectedType = leftNodeType.getName();
            String receivedType = rightNodeType.getName();

            if(leftNodeType.isArray())
                expectedType += "[]";
            if(rightNodeType.isArray())
                receivedType += "[]";

            addReport(assignmentNode, "Type of the assignee must be compatible with the assigned. Expected " + expectedType + " but got " + receivedType);
        }
    }

    public boolean containsMethod(JmmNode node) {
        String name = "";
        if(node.getKind().equals("FunctionCall")) {
            name = node.get("name");
        }
        else if(node.getKind().equals("Identifier")) {
            name = node.get("value");
        }
        else if(node.getKind().equals("MethodDeclaration")) {
            name = node.get("methodName");
        }

        return containsImport(name) || this.className.equals(name) || this.extendedClass.equals(name) || this.methodArgs.containsKey(name);
    }

    public JmmNode getFirstCallFunction(JmmNode jmmNode) {
        while(true) {
            if(jmmNode.getNumChildren()==0)
                break;

            if(jmmNode.getChildren().get(0).getKind().equals("Parameter"))
                break;

            verificateParameters(jmmNode);
            jmmNode = jmmNode.getChildren().get(0);
            if(jmmNode.getKind().equals("Parentesis")) {
                jmmNode = jmmNode.getChildren().get(0);
            }
        }
        return jmmNode;
    }

    public boolean checkIfImportedOrExtended(JmmNode jmmNode) {
        if(jmmNode.getKind().equals("Identifier")) {
            Type type = getNodeType(jmmNode);
            if(type.getName().equals(this.extendedClass) || containsImport(type.getName()))
                return true;
        }
        else if(jmmNode.getKind().equals("FunctionCall")) {
            //if(!jmmNode.getJmmParent().getKind().equals("FunctionCall"))

            if(jmmNode.get("name").equals(this.extendedClass) || containsImport(jmmNode.get("name")))
                return true;
        }
        return false;
    }

    public Type getReturnType(JmmNode functionCallNode) {
        if(functionCallNode.getKind().equals("Identifier")) {
            Type nodeType = getNodeType(functionCallNode);
            JmmNode nextCall = functionCallNode.getJmmParent();
            if(nextCall.getKind().equals("FunctionCall") && (nodeType.getName().equals(this.extendedClass) || containsImport(nodeType.getName())))
                return new Type("Imported", false);
            else if(nextCall.getKind().equals("FunctionCall") && !(nodeType.getName().equals(this.extendedClass) || containsImport(nodeType.getName())))
                return getReturnType(nextCall);
            return nodeType;
        }
        else if(functionCallNode.getKind().equals("MethodDeclaration")) {
            String methodName = functionCallNode.get("methodName");
            if(!containsMethod(functionCallNode))
                return new Type("Undefined", false);
            else if(!this.methodArgs.containsKey(methodName) && (containsImport(methodName) || !this.extendedClass.equals("")))
                return new Type("Imported", false);
            else
                return this.methodRets.get(methodName);
        }
        else if(functionCallNode.getKind().equals("FunctionCall")) {
            String functionName = functionCallNode.get("name");
            if(!containsMethod(functionCallNode)) {
                JmmNode firstCall = getFirstCallFunction(functionCallNode);
                if(checkIfImportedOrExtended(firstCall) || !this.extendedClass.equals(""))
                    return new Type("Imported", false);
                return new Type("Undefined", false);
            }
            else if(!this.methodArgs.containsKey(functionName) && (containsImport(functionName) || !this.extendedClass.equals("")))
                return new Type("Imported", false);
            else
                return this.methodRets.get(functionName);
        }

        return new Type("Undefined", false);
    }

    public String verificateReturnType(JmmNode node) {
        JmmNode nextCall = node.getJmmParent();

        if(node.getKind().equals("FunctionCall")) {
            if(this.extendedClass.equals("") && !containsMethod(node)) {
                addReport(node, "Function \"" + node.get("name") + "\" doesn't exist");
                return "";
            }
            if(containsImport(node.get("name")) || !this.extendedClass.equals("")) {
                //Se o método vier de um import ou existir uma super classe não é necessário fazer mais verificações
                return "Imported";
            }

            Type returnType = getReturnType(node);

            if(returnType.isArray() && !nextCall.getKind().equals("Length")) {
                addReport(nextCall, "Function \"" + nextCall.get("name") + "\" is invalid for array type");
                return "";
            }
            if(returnType.getName().equals(this.className) && this.extendedClass.equals("") && nextCall.hasAttribute("name") && !this.methodArgs.containsKey(nextCall.get("name"))) {
                addReport(nextCall, "Class \"" + this.className + "\" doesn't have method \"" + returnType.getName() + "\"");
                return "";
            }
            if(containsImport(returnType.getName()) || !this.extendedClass.equals("")) {
                return "Imported";
            }
            if(!returnType.isArray() && !containsImport(returnType.getName()) && this.extendedClass.equals("") && !returnType.getName().equals(this.className) && !containsMethod(node)) {
                addReport(nextCall,"Function doesn't support method");
                return "";
            }

        }
        else if(node.getKind().equals("Identifier")) {
            Type nodeType = getNodeType(node);

            if(containsImport(node.get("value")) || node.get("value").equals(this.extendedClass)) {
                return "Imported";
            }
            if(nodeType.getName().equals(this.className) && this.extendedClass.equals("") && !this.methodArgs.containsKey(nextCall.get("name"))) {
                addReport(nextCall, "Class \"" + this.className + "\" doesn't have method \"" + nextCall.get("name") + "\"");
                return "";
            }
            if(nodeType.isArray() && !nextCall.getKind().equals("Length")) {
                addReport(nextCall, "Function \"" + nextCall.get("name") + "\" is invalid for array type");
                return "";
            }
            if(containsImport(nodeType.getName()) || nodeType.getName().equals(this.extendedClass)) {
                return "Imported";
            }
            if(this.methodArgs.containsKey(nodeType.getName()) && !this.methodArgs.containsKey(nextCall.get("name"))) {
                addReport(nextCall, "Class \"" + this.className + "\" doesn't have method \"" + nextCall.get("name") + "\"");
                return "";
            }
        }
        else if(node.getKind().equals("Object")) {
            String methodName = getMethodName(node);
            if(this.staticMethods.contains(methodName)) {
                addReport(node, "\"This\" can't be used in a static method");
                return "";
            }
        }

        if(!nextCall.getKind().equals("FunctionCall") || !nextCall.getKind().equals("Identifier"))
            return "";

        return verificateReturnType(nextCall);
    }

    public Boolean isLocalVariable(JmmNode node, String name) {
        String methodName = getMethodName(node);
        List<Symbol> localVars = getLocalVariables(methodName);

        for(Symbol var : localVars) {
            if(var.getName().equals(name))
                return true;
        }

        return false;
    }

}