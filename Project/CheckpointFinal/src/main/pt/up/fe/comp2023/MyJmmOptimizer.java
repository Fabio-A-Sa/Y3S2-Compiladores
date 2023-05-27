package pt.up.fe.comp2023;

import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.ast.JmmNode;
import pt.up.fe.comp.jmm.ollir.JmmOptimization;
import pt.up.fe.comp.jmm.ollir.OllirResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class MyJmmOptimizer implements JmmOptimization {
    @Override
    public OllirResult toOllir(JmmSemanticsResult semanticsResult) {
        OllirGenerator geradorDeOllir = new OllirGenerator(semanticsResult.getSymbolTable());
        geradorDeOllir.visit(semanticsResult.getRootNode());
        String ollirCode = geradorDeOllir.getCode();
        System.out.println("This is the ollir code: \n" + ollirCode + "\n End code!");
        return new OllirResult(semanticsResult, ollirCode, Collections.emptyList());
    }

    @Override
    public JmmSemanticsResult optimize(JmmSemanticsResult semanticsResult) {
        if(semanticsResult.getConfig().containsKey("optimize") && semanticsResult.getConfig().get("optimize").equals("true")) {
            ConstantPropagationVisitor constantPropagationVisitor = new ConstantPropagationVisitor((TableGenerator) semanticsResult.getSymbolTable());
            ConstantFoldVisitor constantFoldVisitor = new ConstantFoldVisitor();
            int counterProp, counterFold;

            do {
                constantPropagationVisitor.resetCounter();
                constantFoldVisitor.resetCounter();

                constantPropagationVisitor.visit(semanticsResult.getRootNode());
                constantFoldVisitor.visit(semanticsResult.getRootNode());

                counterProp = constantPropagationVisitor.getCounter();
                counterFold = constantFoldVisitor.getCounter();
            } while (counterProp != 0 || counterFold != 0);

        }

        return semanticsResult;
    }

    @Override
    public OllirResult optimize(OllirResult ollirResult) {
        if (ollirResult.getConfig().containsKey("registerAllocation")
                && (Integer.parseInt(ollirResult.getConfig().get("registerAllocation")) >= 0)) {
            int n = Integer.parseInt(ollirResult.getConfig().get("registerAllocation"));
            RegisterAllocation optimizer = new RegisterAllocation(ollirResult, n);
            optimizer.allocateRegisters();
        }
        return ollirResult;
    }
}
