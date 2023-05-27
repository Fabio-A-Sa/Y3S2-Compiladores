package pt.up.fe.comp2023;

import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.JmmNode;

public class OllirUtils extends pt.up.fe.comp.jmm.ollir.OllirUtils {
    public static String getCode(Symbol symbol) {
        return symbol.getName() + getTypeOfOllir(symbol.getType());
    }

    public static String getTypeOfOllir(Type type) {
        StringBuilder codigo = new StringBuilder();
        codigo.append(".");

        if (type == null) {
            codigo.append("V");
            return ".V";
        }

        if (type.isArray())
            codigo.append("array.");
        String tipoJmm = type.getName();
        switch (tipoJmm) {
            case "int":
                codigo.append("i32");
                break;
            case "boolean":
                codigo.append("bool");
                break;
            case "void":
                codigo.append("V");
                break;
            default:
                codigo.append(tipoJmm);
                break;
        }
        return codigo.toString();
    }

    public static boolean isOperation(JmmNode jmmNode) {
        String operation = jmmNode.getKind();
        if (operation.equals("BinaryOp")) {
            return true;
        }
        return false;
    }

    public static String getOperatorOfOllir(JmmNode jmmOperator){

        if (jmmOperator.hasAttribute("op")) {
            switch (jmmOperator.get("op")) {
                case "+":
                    return ".i32";
                case "<":
                    return ".bool";
                case "<=":
                    return ".bool";
                case ">":
                    return ".bool";
                case ">=":
                    return ".bool";
                case "-":
                    return ".i32";
                case "*":
                    return ".i32";
                case "/":
                    return ".i32";
                case "&&":
                    return ".bool";
                case "!":
                    return ".bool";
                default:
                    return ".V";
            }
        }
        else {
            return ".V";
        }
    }
}
