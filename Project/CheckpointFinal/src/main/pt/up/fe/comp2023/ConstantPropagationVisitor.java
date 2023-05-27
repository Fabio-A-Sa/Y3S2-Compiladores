package pt.up.fe.comp2023;

import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.ast.JmmNodeImpl;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConstantPropagationVisitor extends AJmmVisitor<Boolean, Boolean> {
    private final Map<String, JmmNode> constants = new HashMap<>();
    private final Map<String, List<JmmNode>> removals = new HashMap<>(); //assignmentRemovals
    private final Map<String, JmmNode> varDeclarations = new HashMap<>();
    private final TableGenerator symbolTable;
    private int counter = 0;

    public ConstantPropagationVisitor(TableGenerator symbolTable) {
        this.symbolTable = symbolTable;
    }

    @Override
    protected void buildVisitor() {
        addVisit("MethodDeclaration", this::visitMethodDeclaration);
        addVisit("VarDeclaration", this::visitVarDeclaration);
        addVisit("Assignment", this::visitAssignment);
        addVisit("Identifier", this::visitIdentifier);
        addVisit("WhileStmt", this::visitWhileStmt);

        setDefaultVisit(this::defaultVisit);
    }

    private Boolean visitWhileStmt(JmmNode jmmNode, Boolean aBoolean) {
        JmmNode condition = jmmNode.getChildren().get(0);
        JmmNode codeInsideLoop = jmmNode.getChildren().get(1);

        visit(condition);
        visit(codeInsideLoop);

        return aBoolean;
    }


    private Boolean visitAssignment(JmmNode jmmNode, Boolean aBoolean) {
        System.out.println("\nInside visit assignment: " + jmmNode);

        JmmNode left = jmmNode.getChildren().get(0);
        JmmNode right = jmmNode.getChildren().get(1);

        System.out.println("    Left node: " + left);
        System.out.println("    Right node: " + right);

        visit(left, aBoolean);
        visit(right, aBoolean);

        if(!left.getKind().equals("Identifier")) {
            return false;
        }

        String leftName = left.get("value");

        boolean isBoolean = right.getKind().equals("Identifier") && (right.get("value").equals("true") || right.get("value").equals("false"));
        boolean isInteger = right.getKind().equals("Integer");

        if((!isInteger && !isBoolean ) || jmmNode.getChildren().size()>2) {
            constants.remove(leftName);
            varDeclarations.remove(leftName);
        }
        else if(symbolTable.isLocalVariable(left, leftName)) {
            constants.put(leftName, right);
            if(!removals.containsKey(leftName)) {
                removals.put(leftName, new ArrayList<>());
            }
            removals.get(leftName).add(jmmNode);
        }

        System.out.println("    Constants: " + constants);
        System.out.println("    VarDeclarations: " + varDeclarations);
        System.out.println("    Removals: " + removals);

        return true;
    }

    private Boolean visitIdentifier(JmmNode jmmNode, Boolean aBoolean) {
        System.out.println("\nInside identifier: " + jmmNode);
        System.out.println("    Parent Kind: " + jmmNode.getJmmParent().getKind());
        System.out.println("    First Child Kind do Parent: " + jmmNode.getJmmParent().getChildren().get(0).getKind());
        //System.out.println("    Node value: " + jmmNode.getJmmParent().getChildren().get(0).get("value"));

        boolean isAssignment = jmmNode.getJmmParent().getKind().equals("Assignment")
                && jmmNode.getJmmParent().getChildren().get(0).getKind().equals("Identifier")
                && jmmNode.getJmmParent().getChildren().get(0).get("value").equals(jmmNode.get("value"));

        if(isAssignment)
            return false;

        if(constants.containsKey(jmmNode.get("value"))) {
            JmmNode newNode = constants.get(jmmNode.get("value"));
            System.out.println("    New node: " + newNode);
            JmmNode parent = jmmNode.getJmmParent();
            int index = jmmNode.getIndexOfSelf();
            parent.removeJmmChild(index);
            parent.add(newNode, index);
            counter++;
        }

        System.out.println("    Constants: " + constants);
        System.out.println("    VarDeclarations: " + varDeclarations);
        System.out.println("    Removals: " + removals);
        return true;
    }

    private Boolean visitVarDeclaration(JmmNode jmmNode, Boolean aBoolean) {
        System.out.println("\nInside visit varDeclaration: " + jmmNode);
        varDeclarations.put(jmmNode.get("name"), jmmNode);
        System.out.println("    Constants: " + constants);
        System.out.println("    VarDeclarations: " + varDeclarations);
        System.out.println("    Removals: " + removals);
        return true;
    }

    private Boolean defaultVisit(JmmNode jmmNode, Boolean aBoolean) {
        for(JmmNode child : jmmNode.getChildren())
            visit(child, aBoolean);

        return aBoolean;
    }

    private Boolean visitMethodDeclaration(JmmNode jmmNode, Boolean aBoolean) {
        constants.clear();
        removals.clear();
        varDeclarations.clear();;

        for(JmmNode child : jmmNode.getChildren()) {
            visit(child, aBoolean);
        }

        removeAssignsAndDeclarations();

        return true;
    }

    public void removeAssignsAndDeclarations() {
        System.out.println("\nInside remove assigns and declarations");
        System.out.println("    Constants: " + constants);
        System.out.println("    VarDeclarations: " + varDeclarations);
        System.out.println("    Removals: " + removals);
        for(Map.Entry<String, JmmNode> assignment : constants.entrySet()) {
            if(removals.containsKey(assignment.getKey())) {
                List<JmmNode> assigns = removals.get(assignment.getKey());
                for(JmmNode assign : assigns) {
                    assign.getJmmParent().removeJmmChild(assign);
                    counter++;
                }
            }
            if(varDeclarations.containsKey(assignment.getKey())) {
                JmmNode declaration = varDeclarations.get(assignment.getKey());
                declaration.getJmmParent().removeJmmChild(declaration);
                counter++;
            }
        }
    }

    public int getCounter() {
        return this.counter;
    }

    public void resetCounter() {
        this.counter = 0;
    }

}
