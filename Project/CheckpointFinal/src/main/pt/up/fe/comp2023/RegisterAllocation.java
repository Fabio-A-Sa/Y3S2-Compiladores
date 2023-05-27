package pt.up.fe.comp2023;

import org.specs.comp.ollir.*;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp.jmm.report.Report;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegisterAllocation {

    private int registers;
    private ClassUnit classUnit;
    private DataFlowAnalysis analysis;

    public RegisterAllocation(OllirResult ollirResult, int registers) {
        this.classUnit = ollirResult.getOllirClass();
        this.registers = registers;
        this.analysis = new DataFlowAnalysis();
    }

    public void allocateRegisters() {

        this.classUnit.buildCFGs();
        this.classUnit.buildVarTables();
        List<Report> reports = new ArrayList<>();

        // Allocate register for each method
        for (Method method : this.classUnit.getMethods()) {

            List<Map<Node, List<Operand>>> in_out_sets = this.analysis.analyze(method);
            Map<Node, List<Operand>> in = in_out_sets.get(0);  // in set
            Map<Node, List<Operand>> out = in_out_sets.get(1); // out set

            // Dependency graph with in and out sets
            Graph graph = new Graph(method, in, out, reports);
            graph.build();
            Map<String, Descriptor> new_table = graph.getNewTable(this.registers);
            if (new_table == null) return;

            // Select entries based on old and new versions
            Map<String, Descriptor> table = method.getVarTable();
            for (var old_entry : table.entrySet()) {
                for (var new_entry : new_table.entrySet()) {

                    // Replace descriptors/registers when entries match
                    if (old_entry.getKey().equals(new_entry.getKey())) method.getVarTable()
                            .replace(old_entry.getKey(), old_entry.getValue(), new_entry.getValue());

                    // If entry has removed, remove it from old table too
                    if (!new_table.containsKey(old_entry.getKey())) method.getVarTable().remove(old_entry.getKey());
                }
            }
        }
    }
}
