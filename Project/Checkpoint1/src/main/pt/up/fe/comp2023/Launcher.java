package pt.up.fe.comp2023;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.parser.JmmParserResult;
import pt.up.fe.comp.jmm.parser.JmmParser;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsLogs;
import pt.up.fe.specs.util.SpecsSystem;

public class Launcher {

    public static void main(String[] args) {

        // Setups console logging and other things
        SpecsSystem.programStandardInit();

        // Parse arguments as a map with predefined options
        var config = parseArgs(args);

        // Get input file
        File inputFile = new File(config.get("inputFile"));

        // Check if file exists
        if (!inputFile.isFile()) {
            throw new RuntimeException("Expected a path to an existing input file, got '" + inputFile + "'.");
        }

        // Read contents of input file
        String code = SpecsIo.read(inputFile);

        // Parse stage
        SimpleParser parser = new SimpleParser();
        JmmParserResult parserResult = parser.parse(code, config);  // Code to JmmParserResult
        if (parserResult.getReports().size() > 0) {                 // Check errors
            for (Report report : parserResult.getReports())
                System.out.println(report.toString());
            return;
        }
        TestUtils.noErrors(parserResult.getReports());
        System.out.println(parserResult.getRootNode().toTree());    // Print AST

        // Semantic stage
        MyJmmAnalysis analysis = new MyJmmAnalysis();
        JmmSemanticsResult jmmSemanticsResult = analysis.semanticAnalysis(parserResult); // JmmParserResult to JmmSemanticResult
        System.out.println(jmmSemanticsResult.getSymbolTable().print());                 // Print SymbolTable
    }

    private static Map<String, String> parseArgs(String[] args) {
        SpecsLogs.info("Executing with args: " + Arrays.toString(args));

        // Check if there is at least one argument
        if (args.length != 1) {
            throw new RuntimeException("Expected a single argument, a path to an existing input file.");
        }

        // Create config
        Map<String, String> config = new HashMap<>();
        config.put("inputFile", args[0]);
        config.put("optimize", "false");
        config.put("registerAllocation", "-1");
        config.put("debug", "false");

        return config;
    }
}
