package pt.up.fe.comp.cp2eval;

import org.junit.Test;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.specs.util.SpecsIo;

public class SemanticAnalysisTest {

    @Test
    public void symbolTable() {

        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/SymbolTable.jmm"));
        System.out.println("Symbol Table:\n" + result.getSymbolTable().print());
    }

    @Test
    public void t1VarNotDeclared() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/VarNotDeclared.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t1ClassNotImported() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/ClassNotImported.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t1NoReports() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/T1NoReports.jmm"));
        TestUtils.noErrors(result);
    }


    @Test
    public void t2IntPlusObject() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/IntPlusObject.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t2BoolTimesInt() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/BoolTimesInt.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t2NoReports() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/T2NoReports.jmm"));
        TestUtils.noErrors(result);
    }

    @Test
    public void t3ArrayPlusInt() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/ArrayPlusInt.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t3ArrayAccessOnInt() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/ArrayAccessOnInt.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t3ArrayIndexNotInt() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/ArrayIndexNotInt.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t3NoReports() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/T3NoReports.jmm"));
        TestUtils.noErrors(result);
    }

    @Test
    public void t4AssignIntToBool() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/AssignIntToBool.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t4ObjectAssignmentFail() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/ObjectAssignmentFail.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t4ObjectAssignmentPassExtends() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/ObjectAssignmentPassExtends.jmm"));
        TestUtils.noErrors(result);
    }

    @Test
    public void t4ObjectAssignmentPassImports() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/ObjectAssignmentPassImports.jmm"));
        TestUtils.noErrors(result);
    }

    @Test
    public void t5IntInIfCondition() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/IntInIfCondition.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t5ArrayInWhileCondition() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/ArrayInWhileCondition.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t5NoReports() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/T5NoReports.jmm"));
        TestUtils.noErrors(result);
    }

    @Test
    public void t6CallToUndeclaredMethod() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/CallToUndeclaredMethod.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t6CallToMethodAssumedInExtends() {
        var result = TestUtils
                .analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/CallToMethodAssumedInExtends.jmm"));
        TestUtils.noErrors(result);
    }

    @Test
    public void t6CallToMethodAssumedInImport() {
        var result = TestUtils
                .analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/CallToMethodAssumedInImport.jmm"));
        TestUtils.noErrors(result);
    }

    @Test
    public void t7IncompatibleArguments() {
        var result = TestUtils
                .analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/IncompatibleArguments.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t7IncompatibleReturn() {
        var result = TestUtils
                .analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/IncompatibleReturn.jmm"));
        TestUtils.mustFail(result);
    }

    @Test
    public void t7NoReports() {
        var result = TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/T7NoReports.jmm"));
        TestUtils.noErrors(result);
    }

    @Test
    public void t8AssumeArguments() {
        var result = TestUtils
                .analyse(SpecsIo.getResource("pt/up/fe/comp/cp2eval/semanticanalysis/AssumeArguments.jmm"));
        TestUtils.noErrors(result);
    }
}
