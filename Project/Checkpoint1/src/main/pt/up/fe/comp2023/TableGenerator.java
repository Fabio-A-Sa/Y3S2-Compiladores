package pt.up.fe.comp2023;

import java.util.*;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;

import javax.sound.midi.SysexMessage;

public class TableGenerator extends AJmmVisitor<String, String> implements SymbolTable{

    String className;
    String extendedClass;
    List<String> imports;
    Map<String, Type> methodRets;           // <methodName, returnType>
    Map<String, List<Symbol>> methodArgs;   // <methodName, arguments>
    Map<String, List<Symbol>> methodVars;   // <methodName, scopeVariables>

    public TableGenerator () {
        this.imports = new ArrayList<String>();
        this.methodRets = new HashMap<String, Type>();
        this.methodArgs = new HashMap<String, List<Symbol>>();
        this.methodVars = new HashMap<String, List<Symbol>>();
    }

    void fill(JmmNode rootNode) {
        this.visit(rootNode);
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
        addVisit("Argument", this::dealWithArgument);
        addVisit("String", this::dealWithType);
        addVisit("Double", this::dealWithType);
        addVisit("Boolean", this::dealWithType);
        addVisit("Int", this::dealWithType);
        addVisit("Integer", this::dealWithType);
        addVisit("Id", this::dealWithType);
        addVisit("Identifier", this::dealWithType);
        addVisit("ReturnType", this::dealWithReturnType);
        setDefaultVisit(this::dealWithDefault);
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
        for (JmmNode child : jmmNode.getChildren()) visit(child, "global");
        return s;
    }

    private String dealWithVarDeclaration(JmmNode jmmNode, String scope) {
        JmmNode firstChild = jmmNode.getChildren().get(0);
        String varType = visit(firstChild);

        Type newType = new Type(varType, this.isArray(firstChild) || jmmNode.hasAttribute("op"));
        Symbol newVar = new Symbol(newType, jmmNode.get("name"));
        this.insertElement(this.methodVars, scope, newVar);
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
        } else {
            // example: static void main (String[] args)

            methodName = "main";
            returnType = "static void";
            List<Symbol> symbols = new ArrayList<Symbol>();
            symbols.add(new Symbol(new Type("String", true), methodName));
            this.methodArgs.put(methodName, symbols);
        }

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

        for (JmmNode child : jmmNode.getChildren()) visit(child, methodName); // os nós-filho vão ter o scope deste método


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
}
