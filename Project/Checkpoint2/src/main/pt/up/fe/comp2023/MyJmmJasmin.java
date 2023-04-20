package pt.up.fe.comp2023;

import org.specs.comp.ollir.*;
import pt.up.fe.comp.jmm.jasmin.JasminBackend;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class MyJmmJasmin implements JasminBackend {

    ClassUnit classUnit;
    StringBuilder jasminCode;
    String superClass;
    int limit_stack = 99;       // For checkpoint 2
    int limit_locals = 99;      // For checkpoint 2

    @Override
    public JasminResult toJasmin(OllirResult ollirResult) {

        try {

            // ClassUnit setup
            this.classUnit = ollirResult.getOllirClass();
            this.classUnit.checkMethodLabels();
            this.classUnit.buildCFGs();
            this.classUnit.buildVarTables();

            // Jasmin code construction
            this.jasminCode = new StringBuilder();
            this.dealWithClassName();
            this.dealWithSuperClass();
            this.dealWithFields();
            this.dealWithMethods();

            List<Report> reportList = new ArrayList<>();
            return new JasminResult(ollirResult, jasminCode.toString(), reportList);

        } catch (OllirErrorException ollirErrorException) {
            Report newReport = Report.newError(Stage.GENERATION, -1, -1, "OllirErrorException", ollirErrorException);
            return new JasminResult(this.classUnit.getClassName(), null, Collections.singletonList(newReport));
        }
    }

    private void dealWithClassName() {
        this.jasminCode.append(".class " + this.classUnit.getClassName() + "\n");
    }

    private void dealWithSuperClass() {
        this.superClass = (this.classUnit.getSuperClass() == null) ? "java/lang/Object"
                : this.getImportedClassName(this.classUnit.getSuperClass());
        this.jasminCode.append(".super " + this.superClass + "\n");
    }

    private void dealWithFields() {
        for (Field field : this.classUnit.getFields()) {
            String name = field.getFieldName();
            String access = this.getFieldAccess(field);
            String type = this.getFieldType(field.getFieldType());
            this.jasminCode.append(".field " + access + name + " " + type + "\n");
        }
        this.jasminCode.append("\n");
    }

    private void dealWithMethods() {
        for (Method method : this.classUnit.getMethods()) {

            // Method header
            this.jasminCode.append(".method " + this.getMethodAccess(method));
            this.jasminCode.append((method.isConstructMethod() ? "<init>" : method.getMethodName()) + "(");

            // Method arguments
            for (Element argument : method.getParams())
                this.jasminCode.append(this.getFieldType(argument.getType()));
            this.jasminCode.append(")" + this.getFieldType(method.getReturnType()) + "\n");

            // Method instructions
            this.jasminCode.append("\t.limit stack " + this.limit_stack + "\n");
            this.jasminCode.append("\t.limit locals " + this.limit_locals + "\n");
            this.jasminCode.append(this.dealWithInstructions(method));

            // Method return
            if ((method.getInstructions().size() == 0 ||
                method.getInstructions().get(method.getInstructions().size()-1).getInstType() != InstructionType.RETURN) &&
                method.getReturnType().getTypeOfElement() == ElementType.VOID)
                this.jasminCode.append("\treturn\n");

            // Method footer
            this.jasminCode.append(".end method\n\n");
        }
    }

    private String dealWithInstructions(Method method) {
        String instructions = "";
        for (Instruction instruction : method.getInstructions()) {

            // Instruction labels (if any)
            for (Map.Entry<String, Instruction> label : method.getLabels().entrySet())
                if (label.getValue().equals(instruction)) instructions += "\t" + label.getKey() + ":\n";

            // Instruction type
            instructions += this.getInstruction(method.getVarTable(), instruction);

            // Instruction pop (if type == CALL and return != VOID)
            if (instruction.getInstType() == InstructionType.CALL &&
                    ((CallInstruction) instruction).getReturnType().getTypeOfElement() != ElementType.VOID)
                instructions += "\tpop";

            instructions += "\n";
        }
        return instructions;
    }

    private String dealWithAssignments(Map<String, Descriptor> table, AssignInstruction instruction) {

        String assignment = "";
        Operand operand = (Operand) instruction.getDest();

        // Array assignment
        if (instruction.getDest() instanceof ArrayOperand) {
            assignment += "aload";
            assignment += this.getVariableIndex(table, operand.getName());
            assignment += this.loadVariable(table, ((ArrayOperand) operand).getIndexOperands().get(0));
        }

        // (Binary) Operation assigment with increment
        if (instruction.getRhs().getInstType() == InstructionType.BINARYOPER) {
            BinaryOpInstruction binaryInstruction = (BinaryOpInstruction) instruction.getRhs();
            if (binaryInstruction.getOperation().getOpType() == OperationType.ADD) {

                Element leftOperand =  binaryInstruction.getLeftOperand();
                Element rightOperand = binaryInstruction.getRightOperand();
                Operand true_operand = null;
                LiteralElement true_element = null;

                if (leftOperand.isLiteral() && !rightOperand.isLiteral()) {
                    true_operand = (Operand) binaryInstruction.getRightOperand();
                    true_element = (LiteralElement) binaryInstruction.getLeftOperand();
                } else if (!leftOperand.isLiteral() && rightOperand.isLiteral()) {
                    true_operand = (Operand) binaryInstruction.getLeftOperand();
                    true_element = (LiteralElement) binaryInstruction.getRightOperand();
                }

                if (true_operand != null && true_element != null && operand.getName().equals(instruction.getDest()) &&
                        this.between(Integer.parseInt(true_element.getLiteral()), -128, 127)) {
                    return "\tiinc " + table.get(operand.getName()).getVirtualReg() + " " + Integer.parseInt(true_element.getLiteral()) + "\n";
                }
            }
        }

        // Common part
        assignment += this.getInstruction(table, instruction.getRhs()) + this.storeVariable(table, operand);
        return assignment + "\n";
    }

    private String dealWithCall(Map<String, Descriptor> table, CallInstruction instruction) {
        return switch(instruction.getInvocationType()) {
            case invokespecial -> this.getCallSpecial(table, instruction);
            case invokestatic -> this.getCallStatic(table, instruction);
            case invokevirtual -> this.getCallVirtual(table, instruction);
            case NEW ->
                    instruction.getReturnType().getTypeOfElement() == ElementType.OBJECTREF ?
                            this.getCallObject(table, instruction) : this.getCallArray(table, instruction);
            case arraylength -> this.getCallLenght(table, instruction);
            case ldc -> this.getCallLDC(table, instruction);
            default -> null;
        };
    }

    private String dealWithGoto(Map<String, Descriptor> table, Instruction instruction) {
        return "\t; TODO <goto>\n";
    }

    private String dealWithBranch(Map<String, Descriptor> table, Instruction instruction) {
        return "\t; TODO <branch>\n";
    }

    private String dealWithReturn(Map<String, Descriptor> table, ReturnInstruction instruction) {

        // Return with value
        String result = "";
        if (instruction.hasReturnValue())
            result += this.loadVariable(table, instruction.getOperand());
        result += "\t";

        // Returns with operation
        if (instruction.getOperand() != null)
            result += (instruction.getOperand().getType().getTypeOfElement() == ElementType.INT32 ||
                        instruction.getOperand().getType().getTypeOfElement() == ElementType.BOOLEAN) ? "i" : "a";

        return result + "return\n";
    }

    private String dealWithPut(Map<String, Descriptor> table, PutFieldInstruction instruction) {
        Element first = instruction.getFirstOperand();
        Element second = instruction.getSecondOperand();
        Element third = instruction.getThirdOperand();
        return "\t" + this.loadVariable(table, first) + this.loadVariable(table, third) +
                "\tputfield " + this.getImportedClassName(((Operand) first).getName()) +
                "/" + ((Operand) second).getName() +  " " + this.getFieldType(second.getType()) + "\n";
    }

    private String dealWithGet(Map<String, Descriptor> table, GetFieldInstruction instruction) {
        Element first = instruction.getFirstOperand();
        Element second = instruction.getSecondOperand();
        return "\t" + this.loadVariable(table, first) + "\tgetfield " + this.getImportedClassName(((Operand) first).getName()) +
                "/" + ((Operand) second).getName() + " " + this.getFieldType(second.getType()) + "\n";
    }

    private String dealWithUnary(Map<String, Descriptor> table, Instruction instruction) {
        return "\t; TODO <unary>\n";
    }

    private String dealWithBinary(Map<String, Descriptor> table, BinaryOpInstruction instruction) {
        Element left = instruction.getLeftOperand();
        Element right = instruction.getRightOperand();
        return this.loadVariable(table, left) + this.loadVariable(table, right) +
                "\t" + this.getOperation(instruction.getOperation()) + "\n";
    }

    private String dealWithNoper(Map<String, Descriptor> table, SingleOpInstruction instruction) {
        return this.loadVariable(table, instruction.getSingleOperand()) + "\n";
    }

    private String getInstruction(Map<String, Descriptor> table, Instruction instruction) {
        return switch (instruction.getInstType()) {
            case ASSIGN -> this.dealWithAssignments(table, (AssignInstruction) instruction);
            case CALL -> this.dealWithCall(table, (CallInstruction) instruction);
            case GOTO -> this.dealWithGoto(table, (GotoInstruction) instruction);
            case BRANCH -> this.dealWithBranch(table, (CondBranchInstruction) instruction);
            case RETURN -> this.dealWithReturn(table, (ReturnInstruction) instruction);
            case PUTFIELD -> this.dealWithPut(table, (PutFieldInstruction) instruction);
            case GETFIELD -> this.dealWithGet(table, (GetFieldInstruction) instruction);
            case UNARYOPER -> this.dealWithUnary(table, (UnaryOpInstruction) instruction);
            case BINARYOPER -> this.dealWithBinary(table, (BinaryOpInstruction) instruction);
            case NOPER -> this.dealWithNoper(table, (SingleOpInstruction) instruction);
        };
    }

    private String getVariableIndex(Map<String, Descriptor> table, String variableName) {
        if (variableName.equals("this")) return "_0";
        int number = table.get(variableName).getVirtualReg();
        return (number < 4 ? "_" : " ") + number;
    }

    private String loadVariable(Map<String, Descriptor> table, Element element) {
        if (element instanceof LiteralElement) return this.loadLiteralVariable(table, (LiteralElement) element) + "\n";
        if (element instanceof ArrayOperand) return this.loadArrayVariable(table, (ArrayOperand) element) + "\n";
        if (element instanceof Operand) return this.loadOperandVariable(table, (Operand) element) + "\n";
        return null;
    }

    private String storeVariable(Map<String, Descriptor> table, Operand operand) {
        return switch (operand.getType().getTypeOfElement()) {
            case ARRAYREF, STRING, THIS, OBJECTREF -> "\tastore" + this.getVariableIndex(table, operand.getName()) + "\n";
            case BOOLEAN, INT32 ->
                table.get(operand.getName()).getVarType().getTypeOfElement() == ElementType.ARRAYREF ?
                        "\tiastore\n" :
                        "\tistore" + this.getVariableIndex(table, operand.getName()) + "\n";
            default -> null;
        };
    }

    private String loadLiteralVariable(Map<String, Descriptor> table, LiteralElement element) {
        String result = "";
        String literal = element.getLiteral();
        ElementType elementType = element.getType().getTypeOfElement();
        if (elementType != ElementType.INT32 && elementType != ElementType.BOOLEAN) {
            result += "\tldc " + literal;
        } else {
            int value = Integer.parseInt(literal);

            // Priority
            if (this.between(value, -1, 5)) result += "\ticonst_";
            else if (this.between(value, -128, 127)) result += "\tbipush ";
            else if (this.between(value, -32768, 32767)) result += "\tsipush ";
            else result += "\tldc ";

            result += (value == -1) ? "m1" : value;
        }
        return result;
    }

    private String loadArrayVariable(Map<String, Descriptor> table, ArrayOperand element) {
        return  "\taload" + this.getVariableIndex(table, element.getName()) +
                "\n" + this.loadVariable(table, element.getIndexOperands().get(0)) +
                "\tiload";
    }

    private String loadOperandVariable(Map<String, Descriptor> table, Operand operand) {
        return switch (operand.getType().getTypeOfElement()) {
            case THIS -> "aload_0";
            case STRING, ARRAYREF, OBJECTREF -> "\taload" + this.getVariableIndex(table, operand.getName());
            case BOOLEAN, INT32 -> "\tiload" + this.getVariableIndex(table, operand.getName());
            default -> null;
        };
    }

    private String getCallSpecial(Map<String, Descriptor> table, CallInstruction instruction) {

        // Name
        String special = "\t" + this.loadVariable(table, instruction.getFirstArg()) + "\tinvokespecial ";
        Type elementType = instruction.getFirstArg().getType();
        special += elementType.getTypeOfElement() == ElementType.THIS ? this.superClass :
                    this.getImportedClassName(((ClassType) elementType).getName());
        special += "/<init>";

        // Arguments
        special += "(";
        for (Element element : instruction.getListOfOperands())
            special += this.getFieldType(element.getType());
        special += ")";

        // Type
        return special + this.getFieldType(instruction.getReturnType()) + "\n";
    }

    private String getCallStatic(Map<String, Descriptor> table, CallInstruction instruction) {

        // Header
        String string = "";
        Operand first = (Operand) instruction.getFirstArg();
        LiteralElement second = (LiteralElement) instruction.getSecondArg();
        for (Element staticElement : instruction.getListOfOperands())
            string += this.loadVariable(table, staticElement);

        string  += "\tinvokestatic " + this.getImportedClassName(first.getName())
                + "/" + second.getLiteral().replace("\"", "");

        // Arguments
        string += "(";
        for (Element element : instruction.getListOfOperands())
            string += this.getFieldType(element.getType());
        string += ")";

        // Type
        return string + this.getFieldType(instruction.getReturnType()) + "\n";
    }

    private String getCallVirtual(Map<String, Descriptor> table, CallInstruction instruction) {

        // Header
        String string = this.loadVariable(table, instruction.getFirstArg());
        Operand first = (Operand) instruction.getFirstArg();
        LiteralElement second = (LiteralElement) instruction.getSecondArg();
        for (Element staticElement : instruction.getListOfOperands())
            string += this.loadVariable(table, staticElement);

        string  += "\tinvokevirtual " + this.getImportedClassName(((ClassType) first.getType()).getName())
                + "/" + second.getLiteral().replace("\"", "");

        // Arguments
        string += "(";
        for (Element element : instruction.getListOfOperands())
            string += this.getFieldType(element.getType());
        string += ")";

        // Type
        return string + this.getFieldType(instruction.getReturnType()) + "\n";
    }

    private String getCallObject(Map<String, Descriptor> table, CallInstruction instruction) {
        String object = "";
        for (Element objetElement : instruction.getListOfOperands())
            object += this.loadVariable(table, objetElement);
        String className = ((Operand) instruction.getFirstArg()).getName();
        return object + "\tnew " + this.getImportedClassName(className) + "\n";
    }

    private String getCallArray(Map<String, Descriptor> table, CallInstruction instruction) {
        String array = "";
        for (Element arrayElement : instruction.getListOfOperands())
            array += this.loadVariable(table, arrayElement);
        return array + "\tnewarray int\n";
    }

    private String getCallLenght(Map<String, Descriptor> table, CallInstruction instruction) {
        return this.loadVariable(table, instruction.getFirstArg()) + "\tarraylenght\n";
    }

    private String getCallLDC(Map<String, Descriptor> table, CallInstruction instruction) {
        return this.loadVariable(table, instruction.getFirstArg());
    }

    private String getFieldAccess(Field field) {
        String access = "";
        if (field.getFieldAccessModifier() != AccessModifiers.DEFAULT) {
            switch (field.getFieldAccessModifier()) {
                case PUBLIC -> access += "public ";
                case PROTECTED -> access += "protected ";
                case PRIVATE -> access += "private ";
            }
        }
        if (field.isFinalField()) access += "final ";
        if (field.isStaticField()) access += "static ";
        return access;
    }

    private String getMethodAccess(Method method) {
        String access = "";
        if (method.getMethodAccessModifier() != AccessModifiers.DEFAULT) {
            switch (method.getMethodAccessModifier()) {
                case PUBLIC -> access += "public ";
                case PROTECTED -> access += "protected ";
                case PRIVATE -> access += "private ";
            }
        }
        if (method.isFinalMethod()) access += "final ";
        if (method.isStaticMethod()) access += "static ";
        return access;
    }

    private String getFieldType(Type type) {
        return switch (type.getTypeOfElement()) {
            case ARRAYREF -> this.getArrayType(type);
            case OBJECTREF -> this.getObjectType(type);
            default -> this.getType(type.getTypeOfElement());
        };
    }

    private String getType(ElementType type) {
        return switch (type) {
            case INT32 -> "I";
            case BOOLEAN -> "Z";
            case VOID -> "V";
            case STRING -> "Ljava/lang/String;";
            default -> null;
        };
    }

    private String getArrayType(Type type) {
        return "[" + this.getType(((ArrayType) type).getArrayType());
    }

    private String getObjectType(Type type) {
        return "L" + this.getImportedClassName(((ClassType) type).getName()) + ";";
    }

    private String getImportedClassName(String basicClassName) {

        // .this object
        if (basicClassName.equals("this"))
            return this.classUnit.getClassName();

        // imported object
        for (String importedClass : this.classUnit.getImports()) {
            if (importedClass.endsWith(basicClassName)) {
                return this.normalizeClassName(importedClass);
            }
        }

        // default object name
        return basicClassName;
    }

    private String getOperation(Operation operation) {
        return switch (operation.getOpType()) {
            case ADD -> "iadd";
            case MUL -> "imul";
            case SUB -> "isub";
            case DIV -> "idiv";
            default -> null;
        };
    }

    private boolean boolOperation(OperationType operation) {
        return  operation == OperationType.EQ ||
                operation == OperationType.NEQ ||
                operation == OperationType.GTH ||
                operation == OperationType.GTE ||
                operation == OperationType.LTE ||
                operation == OperationType.LTH;
    }

    private String normalizeClassName(String className) {
        return className.replaceAll("\\.", "/");
    }

    private boolean between(int value, int lower, int upper) {
        return value <= upper && value >= lower;
    }
}