package pt.up.fe.comp2023;

import org.specs.comp.ollir.*;
import java.util.*;

public class DataFlowAnalysis {

    private Map<Node, List<Operand>> def; // Write to a variable (e.g., instruction a = 0 will have `a` in its def set)
    private Map<Node, List<Operand>> use; // Use: Read of a variable (e.g., instruction a = b + c will have `b` and `c` in its use set)
    private Map<Node, List<Operand>> in;  // In: What variables are live in any of the incoming edges of the node
    private Map<Node, List<Operand>> out; // Out: What variables are live in any of the outgoing edges of the node

    public DataFlowAnalysis() {
        this.def = new HashMap<>();
        this.use = new HashMap<>();
        this.in = new HashMap<>();
        this.out = new HashMap<>();
    }

    public List<Map<Node, List<Operand>>> analyze(Method method) {

        // Define initial instructions set
        List<Instruction> instructions = new ArrayList<>(method.getInstructions());
        Collections.reverse(instructions);

        // Define the initial sets of each instruction
        for (Instruction instruction : instructions) {
            def.put(instruction, calculateDefs(instruction, method.getVarTable()));
            use.put(instruction, calculateUses(instruction));
            in.put(instruction, new ArrayList<>());
            out.put(instruction, new ArrayList<>());
        }

        // Computes the cycle as long as it can be improved
        while (true) {

            // Initially in and out are empty lists of operands
            Map<Node, List<Operand>> temp_in = new HashMap<>(this.in);
            Map<Node, List<Operand>> temp_out = new HashMap<>(this.out);

            for (Instruction instruction : instructions) {

                // Analyze the follow two nodes
                boolean haveOneSucc = instruction.getSucc1() != null; // at least one successor
                boolean haveTwoSucc = instruction.getSucc2() != null; // at least two successors
                boolean succIsLeaf = instruction.getSucc1().getNodeType() == NodeType.END; // the first successor is leaf

                // instruction->out = nextInstruction->in, if successor is not leaf
                List<Operand> instruction_out_operands = new ArrayList<>();
                if (haveOneSucc && !succIsLeaf) {
                    instruction_out_operands = this.in.get(instruction.getSucc1());
                    if (haveTwoSucc) instruction_out_operands.addAll(this.in.get(instruction.getSucc2()));
                }
                this.out.replace(instruction, instruction_out_operands);

                List<Operand> temp_operands_out = this.out.get(instruction);
                boolean haveOutOperands = temp_operands_out.size() != 0;
                if (haveOutOperands) {

                    // For each out operand, checks its permanence in the temp list
                    int operand_index = 0;
                    while (operand_index < method.getVarTable().size()) {

                        boolean operandNull = temp_operands_out.get(operand_index) == null;
                        boolean isDefined = this.def.get(instruction).get(operand_index) != null;

                        if (!operandNull && isDefined) {
                            temp_out.remove(operand_index); // remove operand from temp_out
                            operand_index--;                // to avoid out of the bounds exception
                        } else {
                            temp_operands_out.set(operand_index, null); // don't remove operand from temp_out
                        }
                        operand_index++;
                    }

                    // method->out += nextMethod->in, to the next loop
                    temp_operands_out.addAll(this.in.get(instruction.getSucc2()));

                    // method->in = all non-null operands will be added
                    this.in.replace(instruction, temp_operands_out);
                }
            }

            // Checks if the in and out sets are stabilized
            boolean is_stabilized = true;
            for (Instruction instruction : instructions) {
                boolean in_stabilized = this.in.get(instruction).equals(temp_in.get(instruction));
                boolean out_stabilized = this.out.get(instruction).equals(temp_out.get(instruction));
                is_stabilized = is_stabilized && in_stabilized && out_stabilized;
            }

            if (is_stabilized) break;
        }

        // If stabilized, return in and out sets of give method
        List<Map<Node, List<Operand>>> method_in_out = new ArrayList<>();
        method_in_out.add(this.in);
        method_in_out.add(this.out);
        return method_in_out;
    }

    private List<Operand> calculateDefs(Instruction instruction, HashMap<String, Descriptor> methodVarTable) {

        // Definition of a variable exists only if instruction is an assignment
        boolean isAssign = instruction.getInstType() == InstructionType.ASSIGN;
        if (isAssign) {

            AssignInstruction assignInstruction = (AssignInstruction) instruction;
            Element element = assignInstruction.getDest();
            ElementType elementType = element.getType().getTypeOfElement();
            Descriptor descriptor = methodVarTable.get(((Operand) element).getName());

            // Definition of a variable exists only if:
            // -> assignment without "this" keyword
            // -> the variable is not literal
            // -> is not a parameter variable
            // -> is not a field class variable
            if (elementType == ElementType.THIS || element.isLiteral() ||
                    descriptor.getScope() == VarScope.PARAMETER || descriptor.getScope() == VarScope.FIELD)
                return null;

            // Returns the list of operands that are defined in this instruction
            List<Operand> operand = new ArrayList<>();
            operand.add((Operand) element);
            return operand;
        }
        return null;
    }

    private List<Operand> calculateUses(Instruction instruction) {
        return switch (instruction.getInstType()) {
            case CALL -> this.dealWithInstructionCall((CallInstruction) instruction);
            case NOPER -> this.dealWithInstructionNOPER((SingleOpInstruction) instruction);
            case ASSIGN -> this.dealWithInstructionAssigns((AssignInstruction) instruction);
            case BRANCH-> this.dealWithInstructionBranch((CondBranchInstruction) instruction);
            case RETURN -> this.dealWithInstructionReturn((ReturnInstruction) instruction);
            case GETFIELD -> this.dealWithInstructionGetField((GetFieldInstruction) instruction);
            case PUTFIELD -> this.dealWithInstructionPutField((PutFieldInstruction) instruction);
            case UNARYOPER -> this.dealWithInstructionUnaryOper((UnaryOpInstruction) instruction);
            case BINARYOPER -> this.dealWithInstructionBinayOper((BinaryOpInstruction) instruction);
            default -> null;
        };
    }

    private List<Operand> dealWithInstructionCall(CallInstruction instruction) {

        List<Operand> operands = new ArrayList<>();

        // Add first argument if it is operand
        if (instruction.getFirstArg() instanceof Operand) operands.add((Operand) instruction.getFirstArg());

        // If it is not object-call, add second argument
        if(instruction.getNumOperands() != 0 && instruction.getInvocationType() != CallType.NEW &&
                instruction.getSecondArg() instanceof Operand) {
            operands.add((Operand) instruction.getSecondArg());
        }

        // Try to add remaining operands
        for (Element operand : instruction.getListOfOperands()) {
            if (operand instanceof Operand) operands.add((Operand) operand);
        }

        return operands;
    }

    private List<Operand> dealWithInstructionNOPER(SingleOpInstruction instruction) {

        // Add single operand of noper instruction, if it is operand (non-literal)
        List<Operand> operands = new ArrayList<>();
        if (instruction.getSingleOperand() instanceof Operand) operands.add((Operand) instruction.getSingleOperand());
        return operands;
    }

    private List<Operand> dealWithInstructionAssigns(AssignInstruction instruction) {

        // a = b + c  -->  uses(b + c)
        return this.calculateUses(instruction.getRhs());
    }

    private List<Operand> dealWithInstructionBranch(CondBranchInstruction instruction) {

        // Add all operands of branch instruction, if it is operand (non-literal)
        List<Operand> operands = new ArrayList<>();
        for (Element operand : instruction.getOperands()) {
            if (operand instanceof Operand) operands.add((Operand) operand);
        }
        return operands;
    }

    private List<Operand> dealWithInstructionReturn(ReturnInstruction instruction) {

        // Add single operand of return instruction, if it is non-void return
        List<Operand> operands = new ArrayList<>();
        if (instruction.hasReturnValue() && instruction.getOperand() instanceof Operand) {
            operands.add((Operand) instruction.getOperand());
        }
        return operands;
    }

    private List<Operand> dealWithInstructionGetField(GetFieldInstruction instruction) {

        // Try to add the two operand of get instruction
        List<Operand> operands = new ArrayList<>();
        if (instruction.getFirstOperand() instanceof Operand) operands.add((Operand) instruction.getFirstOperand());
        if (instruction.getSecondOperand() instanceof Operand) operands.add((Operand) instruction.getSecondOperand());
        return operands;
    }

    private List<Operand> dealWithInstructionPutField(PutFieldInstruction instruction) {

        // Try to add the third operand of put instruction
        List<Operand> operands = new ArrayList<>();
        if (instruction.getThirdOperand() instanceof Operand) operands.add((Operand) instruction.getThirdOperand());
        return operands;
    }

    private List<Operand> dealWithInstructionUnaryOper(UnaryOpInstruction instruction) {

        // Add single operand of unary instruction, if it is non-literal
        List<Operand> operands = new ArrayList<>();
        if (instruction.getOperand() instanceof Operand) operands.add((Operand) instruction.getOperand());
        return operands;
    }

    private List<Operand> dealWithInstructionBinayOper(BinaryOpInstruction instruction) {

        // Add multiple operands of binary instruction, if it is non-literal
        List<Operand> operands = new ArrayList<>();
        if (instruction.getOperands() != null) {
            for (Element operand : instruction.getOperands()) {
                if (operand instanceof Operand) operands.add((Operand) operand);
            }
        }
        return operands;
    }
}
