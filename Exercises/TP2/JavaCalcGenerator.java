package pt.up.fe.comp2023;

import pt.up.fe.comp.jmm.ast.AJmmVisitor ;
import pt.up.fe.comp.jmm.ast.JmmNode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JavaCalcGenerator extends AJmmVisitor<String,String> {
    private String className;
    private Map<String, Integer> counterReads;
    private Map<String, Integer> counterWrites;

    public JavaCalcGenerator(String classname) {
        this.className = classname;
        this.counterReads = new HashMap<String, Integer>();
        this.counterWrites = new HashMap<String, Integer>();
    }

    protected void buildVisitor() {
        addVisit("Program", this::dealWithProgram);
        addVisit("Assignment", this::dealWithAssignment);
        addVisit("Integer", this::dealWithLiteral);
        addVisit("Identifier", this::dealWithLiteral);
        addVisit("ExprStmt", this::dealWithExpression);
        addVisit("BinaryOp", this::dealWithBinaryOp);
        addVisit("Parenthesis", this::dealWithParenthesis);
    }

    private String dealWithProgram(JmmNode jmmNode, String s) {
        s = (s!=null ? s : "");
        String ret = s + "public class " + this.className + " {\n";
        String s2 = s + "\t";
        ret += s2 + "public static void main(String[] args) {\n";

        for (JmmNode child: jmmNode.getChildren()) {
            ret += visit(child, s2 + "\t");
            ret += "\n";
        }

        ret += s2 + "}\n";
        ret += s + "}\n";

        Set<String> keys = this.counterWrites.keySet();
        for (String key : keys) {
            ret += "'" + key + "': " + this.counterWrites.get(key).toString() + " writes and " + this.counterReads.get(key).toString() + " reads\n";
        }
        return ret;
    }

    private String dealWithAssignment(JmmNode jmmNode, String s) {
        this.incrementCounter("writes", jmmNode.get("var"));
        return s + "int " + jmmNode.get("var")
                + " = " + visit(jmmNode.getChildren().get(0), "") + ";";
    }

    private String dealWithLiteral(JmmNode jmmNode, String s) {
        this.incrementCounter("reads", jmmNode.get("value"));

        return s + jmmNode.get("value");
    }

    private String dealWithExpression(JmmNode jmmNode, String s) {
        String result = s + "System.out.println(";
        for (JmmNode child: jmmNode.getChildren()) {
            result += visit(child, "");
        }
        result += ");";
        return result;
    }

    private String dealWithBinaryOp(JmmNode jmmNode, String s) {
        JmmNode first = jmmNode.getChildren().get(0);
        JmmNode second = jmmNode.getChildren().get(1);
        return visit(first, "") + " " + jmmNode.get("op").substring(1, jmmNode.get("op").length()-1) + " " + visit(second, "");
    }

    private String dealWithParenthesis(JmmNode jmmNode, String s) {
        return "(" + visit(jmmNode.getChildren().get(0), "") + ")";
    }

    private void incrementCounter(String type, String key) {
        if (type.equals("writes")) {
            if (this.counterWrites.containsKey(key)) {
                this.counterWrites.put(key, this.counterWrites.get(key)+1);
            } else {
                this.counterWrites.put(key, 1);
            }
        } else {
            if (this.counterReads.containsKey(key)) {
                this.counterReads.put(key, this.counterReads.get(key)+1);
            } else {
                this.counterReads.put(key, 1);
            }
        }
    }
}