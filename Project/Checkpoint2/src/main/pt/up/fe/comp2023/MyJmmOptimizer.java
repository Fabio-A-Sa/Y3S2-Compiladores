package pt.up.fe.comp2023;

import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.comp.jmm.ollir.JmmOptimization;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.comp2023.OllirGenerator;

import java.util.Collections;


public class MyJmmOptimizer implements JmmOptimization {
    @Override
    public OllirResult toOllir(JmmSemanticsResult semanticsResult) {
        OllirGenerator geradorDeOllir = new OllirGenerator(semanticsResult.getSymbolTable());
        geradorDeOllir.visit(semanticsResult.getRootNode());
        String ollirCode = geradorDeOllir.getCode();
        System.out.println("This is the ollir code: \n" + ollirCode + "\n End code!");
        return new OllirResult(semanticsResult, ollirCode, Collections.emptyList());
    }

}
