package pt.up.fe.comp2023;

import org.specs.comp.ollir.*;
import pt.up.fe.comp.jmm.report.*;
import java.util.*;

public class Graph {

    // Graph constitution
    private final Map<Integer, Set<Integer>> connections;
    private final Map<Integer, List<Integer>> colors;
    private final Map<Node, List<Operand>> method_in_operands;
    private final Map<Node, List<Operand>> method_out_operands;

    // Registers
    private Integer min_number_registers;
    private Integer number_registers;

    // Auxiliar datastructures
    private final Map<String, Descriptor> table;
    private final Stack<Integer> stack;
    private final Method method;

    // Reports
    private final List<Report> reports;

    public Graph(Method method, Map<Node, List<Operand>> method_in_operands,
                 Map<Node, List<Operand>> method_out_operands, List<Report> reports) {
        this.connections = new HashMap<>();
        this.colors = new HashMap<>();
        this.reports = reports;
        this.method = method;
        this.method_in_operands = method_in_operands;
        this.method_out_operands = method_out_operands;
        this.table = this.method.getVarTable();
        this.stack = new Stack<>();
        this.min_number_registers = this.getMinNumberRegisters();
    }

    private int getMinNumberRegisters() {

        int value = 0;
        for (Descriptor descriptor : this.table.values()) {

            // It is mandatory to have a register for fields and parameters
            VarScope scope = descriptor.getScope();
            if (scope == VarScope.FIELD || scope == VarScope.PARAMETER) value++;
        }

        // If not a static method, one more register is needed
        return this.method.isStaticMethod() ? value : value + 1;
    }

    private void initConnections() {
        for (Descriptor descriptor : this.table.values()) {

            // If the variable is not field or parameter, insert virtual index/register
            VarScope scope = descriptor.getScope();
            if (scope != VarScope.FIELD && scope != VarScope.PARAMETER)
                this.connections.put(descriptor.getVirtualReg(), new HashSet<>());
        }
    }

    private Descriptor getDescriptor(Integer register_index) {
        for (Descriptor descriptor : this.table.values())
            if (register_index == descriptor.getVirtualReg()) return descriptor;
        return null;
    }

    private void compute(Map<Node, List<Operand>> method_operands) {
        for (Node node : method_operands.keySet()) {

            // Computes all possible pairs
            for (Operand first_operand : method_operands.get(node)) {
                for (Operand second_operand : method_operands.get(node)) {

                    // Get variable names
                    String first_name = first_operand.getName();
                    String second_name = second_operand.getName();

                    // No self-dependency is allowed
                    if (first_name.equals(second_name)) continue;

                    // Add the second operand as "dependency" of the first operand
                    Descriptor descriptor = this.table.get(first_name);
                    if (this.connections.get(descriptor.getVirtualReg()) != null)
                        this.connections.get(this.table.get(first_name).getVirtualReg())
                                                .add(this.table.get(second_name).getVirtualReg());
                }
            }
        }
    }

    public void build() {
        this.initConnections();
        this.compute(this.method_in_operands);
        this.compute(this.method_out_operands);
    }

    private boolean possible(int n) {

        // Minimum number of registers
        if (n == 0) {
            this.number_registers = this.min_number_registers;
            return true;
        }

        // Invalid number of registers
        if (n < this.min_number_registers) {
            this.reports.add(new Report(ReportType.ERROR, Stage.OPTIMIZATION, -1, "You need at least " + min_number_registers + " registers to store all variables."));
            return false;
        }

        // Selected number of registers
        else
            this.number_registers = n;

        return true;
    }

    private boolean updateColors() {

        Integer register = this.stack.pop(); // virtual register index
        boolean next_loop = true;

        for (int color : this.colors.keySet()) {

            boolean not_colored = true;
            for (int variable : this.connections.get(register)) {

                // if the variable is already painted, you don't need to paint again
                if (this.colors.get(color).contains(variable)) not_colored = false;
            }

            // If variable doesn't have color, paint it
            if (not_colored) {
                this.colors.get(color).add(register);
                next_loop = false;
                break;
            }
        }

        return next_loop;
    }

    public Map<String, Descriptor> getNewTable(int n) {

        // If there is not enough registers, abort
        if (!this.possible(n)) System.exit(1);

        // Add more colors, if n is greater than minimum number of registers
        for (int i = this.min_number_registers; i < this.number_registers; i++) {
            this.colors.put(i, new ArrayList<>());
        }

        // Initialize stack for the coloring algorithm
        Map<Integer, Set<Integer>> connections_copy = new HashMap<>(this.connections);
        this.fill_stack(connections_copy);

        // While there are colorless variables
        while (!stack.isEmpty()) {

            if (this.updateColors()) {
                stack.clear();
                return getNewTable(this.number_registers + 1);
            }
        }

        // Create a new variable table based on graph color
        return this.updateTable();
    }

    private void fill_stack(Map<Integer, Set<Integer>> connections_set) {

        // Initialize stack
        this.stack.clear();

        // Put connections index
        while (!connections_set.isEmpty()) {
            for (var entry : connections_set.entrySet()) {

                // If register index is allowed
                if (entry.getValue().size() < this.number_registers) {
                    this.stack.push(entry.getKey());
                    connections_set.remove(entry.getKey());
                    break;
                }
            }
        }
    }

    private Map<String, Descriptor> updateTable() {

        Map<String, Descriptor> new_table = new HashMap<>(this.table);

        for (Map.Entry<Integer, List<Integer>> entry : this.colors.entrySet()) {

            // Get color and its values/registers
            int color = entry.getKey();
            for (Integer index : entry.getValue()) {

                // Change colors from this.table to new_table
                // Only if entries have the same descriptor index
                for (Map.Entry<String, Descriptor> new_entry : new_table.entrySet()) {
                    Descriptor descriptor = new_entry.getValue();
                    if (this.getDescriptor(index) == descriptor);
                        new_entry.setValue(new Descriptor(descriptor.getScope(), color, descriptor.getVarType()));
                }
            }
        }

        return new_table;
    }
}