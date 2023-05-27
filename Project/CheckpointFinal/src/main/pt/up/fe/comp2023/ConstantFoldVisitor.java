package pt.up.fe.comp2023;

import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.ast.JmmNodeImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ConstantFoldVisitor extends AJmmVisitor<Boolean, Boolean> {
    private int counter = 0;

    @Override
    protected void buildVisitor() {
        addVisit("BinaryOp", this::visitBinaryOp);
        addVisit("WhileStmt", this::visitWhileStmt);
        addVisit("Parentesis", this::visitParentesis);

        setDefaultVisit(this::defaultVisit);
    }

    private Boolean visitParentesis(JmmNode jmmNode, Boolean aBoolean) {
        visit(jmmNode.getChildren().get(0));

        JmmNode child = jmmNode.getChildren().get(0);
        if(child.getKind().equals("Integer")) {
            JmmNode parentNode = jmmNode.getJmmParent();
            int index = jmmNode.getIndexOfSelf();
            parentNode.removeJmmChild(index);
            parentNode.add(child, index);
        }

        return aBoolean;
    }

    private Boolean visitWhileStmt(JmmNode jmmNode, Boolean aBoolean) {
        return aBoolean;
    }

    private Boolean defaultVisit(JmmNode jmmNode, Boolean aBoolean) {
        for(JmmNode node : jmmNode.getChildren())
            visit(node, aBoolean);
        return aBoolean;
    }

    private Boolean visitBinaryOp(JmmNode jmmNode, Boolean aBoolean) {
        System.out.println("\nVisiting BinaryOp: " + jmmNode);
        System.out.println("    Assignment: " + jmmNode.getJmmParent().getChildren());
        String op = jmmNode.get("op");

        visit(jmmNode.getChildren().get(0), aBoolean);
        visit(jmmNode.getChildren().get(1), aBoolean);

        List<String> acceptableOps = new ArrayList<>(List.of("*", "+", "-", "/"));

        if(!acceptableOps.contains(op))
            return false;

        JmmNode leftOp = jmmNode.getChildren().get(0);
        JmmNode rightOp = jmmNode.getChildren().get(1);

        System.out.println("    Left op: " + leftOp);
        System.out.println("    Right op: " + rightOp);

        int newValue = 0;

        if(leftOp.getKind().equals("Integer") && rightOp.getKind().equals("Integer")) {
            switch (op) {
                case "*" -> newValue = Integer.parseInt(leftOp.get("value")) * Integer.parseInt(rightOp.get("value"));
                case "/" -> newValue = Integer.parseInt(leftOp.get("value")) / Integer.parseInt(rightOp.get("value"));
                case "+" -> newValue = Integer.parseInt(leftOp.get("value")) + Integer.parseInt(rightOp.get("value"));
                case "-" -> newValue = Integer.parseInt(leftOp.get("value")) - Integer.parseInt(rightOp.get("value"));
                default -> {
                }
            }

            System.out.println("    New value: " + newValue);

            JmmNode newNode = new JmmNodeImpl("Integer");
            newNode.put("value", String.valueOf(newValue));

            JmmNode parentNode = jmmNode.getJmmParent();
            int index = jmmNode.getIndexOfSelf();
            parentNode.removeJmmChild(index);
            parentNode.add(newNode, index);

            System.out.println("    Parent Node: " + parentNode);
            System.out.println("    Filhos: " + parentNode.getChildren());

            counter++;
        }

        return aBoolean;
    }

    public int getCounter() {
        return this.counter;
    }

    public void resetCounter() {
        this.counter = 0;
    }
}
