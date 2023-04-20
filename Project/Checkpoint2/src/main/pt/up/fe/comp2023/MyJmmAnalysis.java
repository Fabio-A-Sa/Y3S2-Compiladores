package pt.up.fe.comp2023;

import pt.up.fe.comp.jmm.analysis.JmmAnalysis;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.parser.JmmParserResult;
import pt.up.fe.comp.jmm.report.Report;
import java.util.ArrayList;
import java.util.List;

public class MyJmmAnalysis implements JmmAnalysis {

    TableGenerator symbolTable;

    public MyJmmAnalysis() {
        this.symbolTable = new TableGenerator();
    }

    @Override
    public JmmSemanticsResult semanticAnalysis(JmmParserResult jmmParserResult) {
        List<Report> reports = new ArrayList<>();
        this.symbolTable.fill(jmmParserResult.getRootNode(), reports);
        return new JmmSemanticsResult(jmmParserResult, this.symbolTable, reports);
    }
}
