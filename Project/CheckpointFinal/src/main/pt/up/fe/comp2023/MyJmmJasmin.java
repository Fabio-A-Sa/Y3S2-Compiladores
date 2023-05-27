package pt.up.fe.comp2023;

import org.specs.comp.ollir.*;
import pt.up.fe.comp.jmm.jasmin.JasminBackend;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.Stage;
import java.util.*;

public class MyJmmJasmin implements JasminBackend {

    // Class details
    ClassUnit classUnit;
    StringBuilder jasminCode;
    String superClass;

    // Limit locals and limit stack
    int limit_stack;
    int current_stack_size;
    int limit_locals;
    int call_arguments;

    // To construct unique labels for control flow
    int jump_index;

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

            // Reports
            List<Report> reportList = new ArrayList<>();
            return new JasminResult(ollirResult, jasminCode.toString(), reportList);

        } catch (OllirErrorException ollirErrorException) {
            Report newReport = Report.newError(Stage.GENERATION, -1, -1, "Exception during Ollir to Jasmin Traduction", ollirErrorException);
            return new JasminResult(this.classUnit.getClassName(), null, Collections.singletonList(newReport));
        }
    }

    // .class <CLASS NAME>
    private void dealWithClassName() {
        this.jasminCode.append(".class " + this.classUnit.getClassName() + "\n");
    }

    // .super <SUPER CLASS> | default: .super java/lang/Object
    private void dealWithSuperClass() {
        this.superClass = (this.classUnit.getSuperClass() == null) ? "java/lang/Object"
                : this.getImportedClassName(this.classUnit.getSuperClass());
        this.jasminCode.append(".super " + this.superClass + "\n");
    }

    // .field <PUBLIC,PRIVATE,PROTECTED,STATIC,FINAL> <FIELD NAME> <TYPE>
    private void dealWithFields() {
        for (Field field : this.classUnit.getFields()) {
            String name = field.getFieldName();
            String access = this.getFieldAccess(field);
            String type = this.getFieldType(field.getFieldType());
            this.jasminCode.append(".field " + access + name + " " + type + "\n");
        }
        this.jasminCode.append("\n");
    }

    // .method <PUBLIC,PRIVATE,PROTECTED,STATIC> (<init>)? ([ARG TYPE]*) <RETURN TYPE>
    //      .limit stack <N>
    //      .limit locals <N>
    //      <INSTRUCTION>*
    // .end method
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
            this.limit_locals = this.getLimitLocals(method);
            this.limit_stack = 0;
            this.current_stack_size = 0;
            this.jump_index = 0;
            String allInstructions = this.dealWithInstructions(method);
            this.jasminCode.append("\t.limit stack " + this.limit_stack + "\n");
            this.jasminCode.append("\t.limit locals " + this.limit_locals + "\n");
            this.jasminCode.append(allInstructions);

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
                    ((CallInstruction) instruction).getReturnType().getTypeOfElement() != ElementType.VOID) {
                instructions += "\tpop";
                this.popStack(1);
            }
            instructions += "\n";
        }
        return instructions;
    }

    private String dealWithArrayAssignment(Map<String, Descriptor> table, AssignInstruction instruction) {

        String assignment = "";
        Operand operand = (Operand) instruction.getDest();
        this.pushStack(1);

        // aload <REG_INDEX>
        assignment += "aload" + this.getVariableIndex(table, operand.getName()) + "\n";

        // iconst <ARRAY_INDEX>
        assignment += this.loadVariable(table, ((ArrayOperand) operand).getIndexOperands().get(0));

        // compute RHS instruction
        assignment += this.getInstruction(table, instruction.getRhs());

        // iastore
        assignment += this.storeVariable(table, operand) + "\n";
        return assignment;
    }

    private String dealWithIncAssignment(Map<String, Descriptor> table, AssignInstruction instruction) {

        BinaryOpInstruction binaryInstruction = (BinaryOpInstruction) instruction.getRhs();
        OperationType operation = binaryInstruction.getOperation().getOpType(); // iadd or isub

        // iinc with SUB and ADD ops
        if (operation == OperationType.SUB || operation == OperationType.ADD) {

            Element leftOperand = binaryInstruction.getLeftOperand();
            Element rightOperand = binaryInstruction.getRightOperand();

            // variable1 = variable2 op literal
            // variable1 = literal op variable2
            if ((leftOperand.isLiteral() && !rightOperand.isLiteral()) ||
                    (!leftOperand.isLiteral() && rightOperand.isLiteral())) {

                Operand operand = leftOperand.isLiteral() ? (Operand) binaryInstruction.getRightOperand() : (Operand) binaryInstruction.getLeftOperand();
                LiteralElement literal = leftOperand.isLiteral() ? (LiteralElement) binaryInstruction.getLeftOperand() : (LiteralElement) binaryInstruction.getRightOperand();

                // variable1.name == variable2.name
                boolean sameName = operand.getName().equals(((Operand) instruction.getDest()).getName());

                // increment at most 1 byte (-2^8, 2^8-1)
                // sub op: a = a - value => a = a + (-value)
                int value = Integer.parseInt(literal.getLiteral());
                if (operation == OperationType.SUB) value = - value;
                boolean oneByte = this.between(value, -128, 127);

                if (sameName && oneByte) {
                    // iinc <REG_INDEX> <VALUE>
                    return "\tiinc " + table.get(operand.getName()).getVirtualReg() + " " + value + "\n";
                }
            }
        }
        return null;
    }


    private String dealWithAssignments(Map<String, Descriptor> table, AssignInstruction instruction) {

        // Array assignment
        if (instruction.getDest() instanceof ArrayOperand) {
            return this.dealWithArrayAssignment(table, instruction);
        }

        // Assignment with binary operation -> iinc instruction (optimization)
        if (instruction.getRhs().getInstType() == InstructionType.BINARYOPER) {
            String ret = this.dealWithIncAssignment(table, instruction);
            if (ret != null) return ret;
        }

        // Assignment with binary operation -> default (iinc instruction not allowed)
        return this.getInstruction(table, instruction.getRhs()) + this.storeVariable(table, (Operand) instruction.getDest()) + "\n";
    }

    private String dealWithCall(Map<String, Descriptor> table, CallInstruction instruction) {

        this.call_arguments = 0;
        String answer;

        switch(instruction.getInvocationType()) {
            case invokespecial:
                answer = this.getCallSpecial(table, instruction);
                break;
            case invokestatic:
                answer = this.getCallStatic(table, instruction);
                break;
            case invokevirtual:
                answer = this.getCallVirtual(table, instruction);
                break;
            case NEW:
                answer = instruction.getReturnType().getTypeOfElement() == ElementType.OBJECTREF ?
                         this.getCallObject(table, instruction) : this.getCallArray(table, instruction);
                break;
            case arraylength:
                answer = this.getCallLength(table, instruction);
                break;
            case ldc:
                answer = this.getCallLDC(table, instruction);
                break;
            default:
                answer = null;
                break;
        }

        // The stack has to go back to the size before the call
        this.popStack(call_arguments);
        return answer;
    }

    // goto <LABEL>
    private String dealWithGoto(Map<String, Descriptor> table, GotoInstruction instruction) {
        return "\tgoto " + instruction.getLabel() + "\n";
    }

    private String dealWithBranch(Map<String, Descriptor> table, CondBranchInstruction instruction) {

        String result[];
        String operation;
        String branch;
        Instruction condition = this.getCondition(instruction);

        switch (condition.getInstType()) {
            case UNARYOPER -> {
                result = this.dealWithUnaryBranch(table, (UnaryOpInstruction) condition);
                operation = result[0];
                branch = result[1];
            }
            case BINARYOPER -> {
                result = this.dealWithBinaryBranch(table, (BinaryOpInstruction) condition);
                operation = result[0];
                branch = result[1];
            }
            default -> {
                operation = "ifne";
                branch = this.getInstruction(table, condition);
            }
        }

        // <OPERATION> <TRUE-LABEL>
        branch += "\t" + operation + " " + instruction.getLabel() + "\n";

        // Otimization: Jasmin only uses if_icmp(lt|ge) when he really needs to
        if (operation.equals("if_icmplt") || operation.equals("if_icmge")) {
            this.popStack(2);           // variable1 op variable2
        } else this.popStack(1);        // variable1 op literal1

        return branch;
    }

    private String[] dealWithBinaryBranch(Map<String, Descriptor> table, BinaryOpInstruction instruction) {
        String[] arguments = new String[2];
        Integer literal = null;

        switch (instruction.getOperation().getOpType()) {
            case LTH: // <

                // variable < literal
                if (instruction.getRightOperand() instanceof LiteralElement) {
                    literal = Integer.parseInt(((LiteralElement) instruction.getRightOperand()).getLiteral());
                    arguments[0] = "iflt";
                    arguments[1] = this.loadVariable(table, instruction.getLeftOperand());
                }

                // literal < variable
                if (instruction.getLeftOperand() instanceof LiteralElement) {
                    literal = Integer.parseInt(((LiteralElement) instruction.getLeftOperand()).getLiteral());
                    arguments[0] = "ifgt";
                    arguments[1] = this.loadVariable(table, instruction.getRightOperand());
                }

                // variable1 < variable2
                if (!(literal != null && literal == 0)){
                    arguments[0] = "if_icmplt";
                    arguments[1] = this.loadVariable(table, instruction.getLeftOperand()) +
                                   this.loadVariable(table, instruction.getRightOperand());
                }
                break;

            case GTE: // >=

                // variable >= literal
                if (instruction.getRightOperand() instanceof LiteralElement) {
                    literal = Integer.parseInt(((LiteralElement) instruction.getRightOperand()).getLiteral());
                    arguments[0] = "ifle";
                    arguments[1] = this.loadVariable(table, instruction.getLeftOperand());
                }

                // literal >= variable
                if (instruction.getLeftOperand() instanceof LiteralElement) {
                    literal = Integer.parseInt(((LiteralElement) instruction.getLeftOperand()).getLiteral());
                    arguments[0] = "ifge";
                    arguments[1] = this.loadVariable(table, instruction.getRightOperand());
                }

                // variable1 >= variable2
                if (!(literal != null && literal == 0)){
                    arguments[0] = "if_icmpge";
                    arguments[1] = this.loadVariable(table, instruction.getLeftOperand()) +
                            this.loadVariable(table, instruction.getRightOperand());
                }
                break;

            case ANDB:
                arguments[0] = "ifne";
                arguments[1] = this.getInstruction(table, instruction);
                break;

            default:
                return null;
        };
        return arguments;
    }

    private String[] dealWithUnaryBranch(Map<String, Descriptor> table, UnaryOpInstruction instruction) {
        String[] arguments = new String[2];
        if (instruction.getOperation().getOpType() == OperationType.NOTB) { // Not Binary
            arguments[0] = "ifeq";
            arguments[1] = this.loadVariable(table, instruction.getOperand());
        }
        return arguments;
    }

    private Instruction getCondition(CondBranchInstruction instruction) {

        // Operator Conditional Instruction
        if (instruction instanceof OpCondInstruction)
            return ((OpCondInstruction) instruction).getCondition();

        // Single Operator Conditional Instruction
        if (instruction instanceof SingleOpCondInstruction)
            return ((SingleOpCondInstruction) instruction).getCondition();

        return null;
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

        this.popStack(2); // first and second operands
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

    private String dealWithUnary(Map<String, Descriptor> table, UnaryOpInstruction instruction) {
        String unary = this.loadVariable(table, instruction.getOperand()) + "\t" + this.getOperation(instruction.getOperation());
        if (instruction.getOperation().getOpType() == OperationType.NOTB) unary += this.getBooleanJumps();
        return unary + "\n";
    }

    private String dealWithBinary(Map<String, Descriptor> table, BinaryOpInstruction instruction) {

        Element left = instruction.getLeftOperand();
        Element right = instruction.getRightOperand();
        String answer = this.loadVariable(table, left) + this.loadVariable(table, right) +
                "\t" + this.getOperation(instruction.getOperation());

        if (this.boolOperation(instruction.getOperation().getOpType())) {
            answer += this.getBooleanJumps();
        } else answer += "\n";

        this.popStack(1);
        return answer;
    }

    private String getBooleanJumps() {

        String jumps = " true" + this.jump_index + "\n\ticonst_0\n\tgoto jump" + this.jump_index +
                      "\ntrue" + this.jump_index + ":\n\ticonst_1\njump" + this.jump_index + ":\n";

        this.jump_index++; // increment jump index for next conditional jumps
        return jumps;
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

        // Otimization: (i|a)load_<INDEX>
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
        switch (operand.getType().getTypeOfElement()) {

            case ARRAYREF, STRING, THIS, OBJECTREF:
                this.popStack(1);
                return "\tastore" + this.getVariableIndex(table, operand.getName()) + "\n";

            case BOOLEAN, INT32:
                if (table.get(operand.getName()).getVarType().getTypeOfElement() == ElementType.ARRAYREF) {
                    this.popStack(3);
                    return "\tiastore\n";
                } else {
                    this.popStack(1);
                    return "\tistore" + this.getVariableIndex(table, operand.getName()) + "\n";
                }
            default:
                return null;
        }
    }

    private String loadLiteralVariable(Map<String, Descriptor> table, LiteralElement element) {
        String result = "";
        String literal = element.getLiteral();
        ElementType elementType = element.getType().getTypeOfElement();
        if (elementType != ElementType.INT32 && elementType != ElementType.BOOLEAN) {
            result += "\tldc " + literal;
        } else {
            int value = Integer.parseInt(literal);

            // Otimization: iload -> iconst, bipush, sipush, ldc
            if (this.between(value, -1, 5)) result += "\ticonst_";
            else if (this.between(value, -128, 127)) result += "\tbipush ";
            else if (this.between(value, -32768, 32767)) result += "\tsipush ";
            else result += "\tldc ";

            result += (value == -1) ? "m1" : value;
        }

        this.pushStack(1);
        return result;
    }

    private String loadArrayVariable(Map<String, Descriptor> table, ArrayOperand element) {
        this.pushStack(1);
        String store = "\taload" + this.getVariableIndex(table, element.getName()) + "\n";
        String load = this.loadVariable(table, element.getIndexOperands().get(0)) + "\n" + "\tiaload";
        this.popStack(1);
        return store + load;
    }

    private String loadOperandVariable(Map<String, Descriptor> table, Operand operand) {
        this.pushStack(1);
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
        Type returnType = instruction.getReturnType();
        this.call_arguments = returnType.getTypeOfElement() == ElementType.VOID ? this.call_arguments : this.call_arguments - 1;

        return special + this.getFieldType(returnType) + "\n";
    }

    private String getCallStatic(Map<String, Descriptor> table, CallInstruction instruction) {

        // Header
        String string = "";
        Operand first = (Operand) instruction.getFirstArg();
        LiteralElement second = (LiteralElement) instruction.getSecondArg();
        this.call_arguments = 0;
        for (Element staticElement : instruction.getListOfOperands()) {
            this.call_arguments += 1;
            string += this.loadVariable(table, staticElement);
        }

        string  += "\tinvokestatic " + this.getImportedClassName(first.getName())
                + "/" + second.getLiteral().replace("\"", "");

        // Arguments
        string += "(";
        for (Element element : instruction.getListOfOperands())
            string += this.getFieldType(element.getType());
        string += ")";

        // Type
        Type returnType = instruction.getReturnType();
        this.call_arguments = returnType.getTypeOfElement() == ElementType.VOID ? this.call_arguments : this.call_arguments - 1;

        return string + this.getFieldType(returnType) + "\n";
    }

    private String getCallVirtual(Map<String, Descriptor> table, CallInstruction instruction) {

        // Header
        String string = this.loadVariable(table, instruction.getFirstArg());
        Operand first = (Operand) instruction.getFirstArg();
        LiteralElement second = (LiteralElement) instruction.getSecondArg();

        this.call_arguments = 1;
        for (Element staticElement : instruction.getListOfOperands()) {
            string += this.loadVariable(table, staticElement);
            this.call_arguments += 1;
        }

        string  += "\tinvokevirtual " + this.getImportedClassName(((ClassType) first.getType()).getName())
                + "/" + second.getLiteral().replace("\"", "");

        // Arguments
        string += "(";
        for (Element element : instruction.getListOfOperands())
            string += this.getFieldType(element.getType());
        string += ")";

        // Type
        Type returnType = instruction.getReturnType();
        this.call_arguments = returnType.getTypeOfElement() == ElementType.VOID ? this.call_arguments : this.call_arguments - 1;

        return string + this.getFieldType(returnType) + "\n";
    }

    private String getCallObject(Map<String, Descriptor> table, CallInstruction instruction) {

        this.call_arguments = -1;
        String object = "";

        for (Element objetElement : instruction.getListOfOperands()) {
            object += this.loadVariable(table, objetElement);
            this.call_arguments += 1;
        }

        String className = ((Operand) instruction.getFirstArg()).getName();
        return object + "\tnew " + this.getImportedClassName(className) + "\n";
    }

    private String getCallArray(Map<String, Descriptor> table, CallInstruction instruction) {

        this.call_arguments = -1;
        String array = "";

        for (Element arrayElement : instruction.getListOfOperands()) {
            array += this.loadVariable(table, arrayElement);
            this.call_arguments += 1;
        }

        return array + "\tnewarray int\n";
    }

    private String getCallLength(Map<String, Descriptor> table, CallInstruction instruction) {
        return this.loadVariable(table, instruction.getFirstArg()) + "\tarraylength\n";
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
            case ANDB -> "iand";
            case NOTB -> "ifeq";
            case LTH -> "if_icmplt";
            case GTE -> "if_icmpte";
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

    private void popStack(int quantity) {
        this.current_stack_size -= quantity;
    }

    private void pushStack(int quantity) {
        this.current_stack_size += quantity;
        this.limit_stack = Math.max(this.current_stack_size, this.limit_stack);
    }

    private int getLimitLocals(Method method) {

        Set<Integer> virtualRegs = new TreeSet<>();
        virtualRegs.add(0); // init, base case

        // Verify all variables
        for (Descriptor variable : method.getVarTable().values()) {
            virtualRegs.add(variable.getVirtualReg()); // Variable index
        }
        return virtualRegs.size();
    }

    private boolean between(int value, int lower, int upper) {
        return value <= upper && value >= lower;
    }
}