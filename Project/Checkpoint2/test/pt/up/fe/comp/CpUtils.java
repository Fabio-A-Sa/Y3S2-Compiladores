/**
 * Copyright 2022 SPeCS.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License. under the License.
 */

package pt.up.fe.comp;

import static org.junit.Assert.fail;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.specs.comp.ollir.ArrayType;
import org.specs.comp.ollir.AssignInstruction;
import org.specs.comp.ollir.BinaryOpInstruction;
import org.specs.comp.ollir.CallInstruction;
import org.specs.comp.ollir.ClassType;
import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.Element;
import org.specs.comp.ollir.ElementType;
import org.specs.comp.ollir.Instruction;
import org.specs.comp.ollir.Method;
import org.specs.comp.ollir.Node;
import org.specs.comp.ollir.OpInstruction;
import org.specs.comp.ollir.OperationType;
import org.specs.comp.ollir.ReturnInstruction;
import org.specs.comp.ollir.SingleOpInstruction;
import org.specs.comp.ollir.Type;

import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.specs.util.SpecsCollections;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.utilities.LineStream;

/**
 * Utility methods used in checkpoint tests.
 *
 * @author Joao Bispo
 *
 */
public class CpUtils {

    // (if((\w\w)|_icmp\w+)\s+\w+)
    // (if_icmpeq | if_icmpne | if_icmplt | if_icmpge | if_icmpgt | if_icmple | ifeq | ifne | iflt | ifge | ifgt | ifle)
    public static final String IF_REGEX = "((if_icmpeq|if_icmpne|if_icmplt|if_icmpge|if_icmpgt|if_icmple|ifeq|ifne|iflt|ifge|ifgt|ifle)\\s+\\w+)";
    public static final String GOTO_REGEX = "(goto\\s+\\w+)";

    static final String FIELD_PREFIX = "\\.field\\s+((public|private)\\s+)?(')?";
    static final String FIELD_SUFFIX = "(')?\\s+";

    public static String toMessage(String message, OllirResult result) {
        var finalMessage = new StringBuilder();

        finalMessage.append(message).append("\n");

        finalMessage.append("\nOLLIR code:\n").append(result.getOllirCode()).append("\n");

        var numReports = result.getReports().size();
        if (numReports == 0) {
            finalMessage.append("\nNo reports\n");
        } else {
            finalMessage.append("\nReports (" + result.getReports().size() + "):\n");
            result.getReports().forEach(report -> finalMessage.append(report.toString() + "\n"));
        }
        finalMessage.append("\n");

        return finalMessage.toString();
        // return message + "\n\nOLLIR:\n" + ollir.getOllirCode();
    }

    public static String toMessage(String message, JasminResult result) {
        var finalMessage = new StringBuilder();

        finalMessage.append(message).append("\n");

        finalMessage.append("\nJasmin code:\n").append(result.getJasminCode()).append("\n");

        var numReports = result.getReports().size();
        if (numReports == 0) {
            finalMessage.append("\nNo reports\n");
        } else {
            finalMessage.append("\nReports (" + result.getReports().size() + "):\n");
            result.getReports().forEach(report -> finalMessage.append(report.toString() + "\n"));
        }
        finalMessage.append("\n");

        return finalMessage.toString();
        // return message + "\n\nJasmin:\n" + result.getJasminCode();
    }

    public static String toMessage(String message, SymbolTable result) {
        var finalMessage = new StringBuilder();

        if (message != null && !message.isBlank()) {
            finalMessage.append(message).append("\n");
        }

        finalMessage.append("\nSymbol table:\n").append(result.print()).append("\n");

        finalMessage.append("\n");

        return finalMessage.toString();
        // return message + "\n\nJasmin:\n" + result.getJasminCode();
    }

    public static void testOllirToJasmin(String resource, String expectedOutput) {
        // If AstToJasmin pipeline, do not execute test
        if (TestUtils.hasAstToJasminClass()) {
            return;
        }

        var ollirResult = new OllirResult(SpecsIo.getResource(resource), Collections.emptyMap());

        var result = TestUtils.backend(ollirResult);

        var testName = new File(resource).getName();
        System.out.println(testName + ":\n" + result.getJasminCode());
        var runOutput = result.runWithFullOutput();
        Assert.assertEquals("Error while running compiled Jasmin: " + runOutput.getOutput() + "\n\nJasmin code:\n"
                + result.getJasminCode(), 0, runOutput.getReturnValue());
        System.out.println("\n Result: " + runOutput.getOutput());

        if (expectedOutput != null) {
            Assert.assertEquals("Output different from what was expected.\n\nJasmin code:\n" + result.getJasminCode(),
                    expectedOutput, runOutput.getOutput());
        }
    }

    public static void testOllirToJasmin(String resource) {
        testOllirToJasmin(resource, null);
    }

    public static void testJmmCompilation(String resource, Consumer<OllirResult> ollirTester, String executionOutput) {

        // If AstToJasmin pipeline, generate Jasmin
        if (TestUtils.hasAstToJasminClass()) {

            var result = TestUtils.backend(SpecsIo.getResource(resource));

            var testName = new File(resource).getName();
            System.out.println(testName + ":\n" + result.getJasminCode());
            var runOutput = result.runWithFullOutput();
            Assert.assertEquals("Error while running compiled Jasmin: " + runOutput.getOutput() + "\n\nJasmin code:\n"
                            + result.getJasminCode(), 0,
                    runOutput.getReturnValue());
            System.out.println("\n Result: " + runOutput.getOutput());

            if (executionOutput != null) {
                Assert.assertEquals(
                        "Output different from what was expected.\n\nJasmin code:\n" + result.getJasminCode(),
                        executionOutput, runOutput.getOutput());
            }

            return;
        }

        var result = TestUtils.optimize(SpecsIo.getResource(resource));
        var testName = new File(resource).getName();
        System.out.println(testName + ":\n" + result.getOllirCode());

        if (ollirTester != null) {
            ollirTester.accept(result);
        }
    }

    public static void testJmmCompilation(String resource) {
        CpUtils.testJmmCompilation(resource, null);
    }

    public static void testJmmCompilation(String resource, Consumer<OllirResult> ollirTester) {
        testJmmCompilation(resource, ollirTester, null);
    }

    public static void assertEquals(String message, Object expected, Object actual, OllirResult ollir) {
        Assert.assertEquals(toMessage(message, ollir), expected,
                actual);
    }

    public static void assertEquals(String message, int expected, int actual, OllirResult ollir) {
        Assert.assertEquals(toMessage(message, ollir), expected,
                actual);
    }

    public static void assertEquals(String message, long expected, long actual, OllirResult ollir) {
        Assert.assertEquals(toMessage(message, ollir), expected,
                actual);
    }

    static public void assertTrue(String message, boolean condition, OllirResult ollir) {
        Assert.assertTrue(toMessage(message, ollir), condition);
    }

    public static <T> void assertThat(String reason, T actual, Matcher<? super T> matcher, OllirResult ollir) {
        Assert.assertThat(toMessage(reason, ollir), actual, matcher);
    }

    public static void assertEquals(String message, Object expected, Object actual, JasminResult result) {
        Assert.assertEquals(toMessage(message, result), expected, actual);
    }

    public static void assertEquals(String message, int expected, int actual, JasminResult result) {
        Assert.assertEquals(toMessage(message, result), expected, actual);
    }

    public static void assertNotEquals(String message, Object expected, Object actual, JasminResult jasminResult) {
        Assert.assertNotEquals(toMessage(message, jasminResult), expected, actual);
    }

    public static void assertNotNull(String message, Object obj, JasminResult jasminResult) {
        Assert.assertNotNull(toMessage(message, jasminResult), obj);
    }

    static public void assertTrue(String message, boolean condition, JasminResult result) {
        Assert.assertTrue(toMessage(message, result), condition);
    }

    public static void assertEquals(String message, int expected, int actual, SymbolTable results) {
        Assert.assertEquals(toMessage(message, results), expected, actual);
    }

    public static void assertEquals(String message, Object expected, Object actual, SymbolTable result) {
        Assert.assertEquals(toMessage(message, result), expected, actual);
    }

    public static org.specs.comp.ollir.Method getMethod(OllirResult result, String methodName) {
        ClassUnit classUnit = result.getOllirClass();

        for (var method : classUnit.getMethods()) {
            if (method.getMethodName().equals(methodName)) {
                return method;
            }
        }

        throw new RuntimeException(toMessage("Could not find OLLIR method with name '" + methodName + "'", result));
    }

    public static String toString(Type ollirType) {
        return toString(ollirType.getTypeOfElement(), ollirType);
    }

    public static String toString(ElementType elementType, Type ollirType) {
        // var elementType = ollirType.getTypeOfElement();
        switch (elementType) {
            case BOOLEAN:
                return "bool";
            case INT32:
                return "int";
            case STRING:
                return "String";
            case VOID:
                return "void";
            case OBJECTREF:
                var objectRef = (ClassType) ollirType;
                return objectRef.getName();
            case ARRAYREF:
                var arrayType = (ArrayType) ollirType;
                return toString(arrayType.getTypeOfElements(), null)
                        + SpecsStrings.buildLine("[]", arrayType.getNumDimensions());
            default:
                throw new NotImplementedException(elementType);
        }
    }

    /**
     * Tests if there an intruction of type c in the RHS of an assign.
     *
     * @param c
     * @param method
     */
    public static void assertAssignRhs(Class<? extends Instruction> c, Method method, OllirResult ollir) {

        var inst = method.getInstructions().stream()
                .filter(i -> i instanceof AssignInstruction)
                .map(instr -> (AssignInstruction) instr)
                .filter(assign -> c.isInstance(assign.getRhs()))
                .findFirst();

        assertTrue("Could not find a " + c.getName() + " in method " + method.getMethodName(),
                inst.isPresent(), ollir);
    }

    /**
     * Tests if there an intruction of type c in the list of instructions.
     *
     * @param c
     * @param method
     */
    public static <T> List<T> assertInstExists(Class<T> c, Method method, OllirResult ollir) {

        var inst = method.getInstructions().stream()
                // Unfold instruction inside AssignInstrucion
                .flatMap(
                        i -> i instanceof AssignInstruction ? Stream.of(i, ((AssignInstruction) i).getRhs())
                                : Stream.of(i))
                .filter(i -> c.isInstance(i))
                .map(c::cast)
                .collect(Collectors.toList());

        assertTrue("Could not find a " + c.getName() + " in method " + method.getMethodName(), !inst.isEmpty(),
                ollir);

        return inst;
    }

    public static Method assertMethodExists(String methodName, OllirResult ollir) {
        Method method = ollir.getOllirClass().getMethods().stream()
                .filter(m -> m.getMethodName().equals(methodName))
                .findFirst()
                .orElse(null);

        Assert.assertNotNull("Could not find method " + methodName + "\n\nOLLIR code:\n" + ollir.getOllirCode(),
                method);

        return method;
    }

    public static void assertReturnExists(Method method, OllirResult ollir) {

        var retInst = method.getInstructions().stream()
                .filter(inst -> inst instanceof ReturnInstruction)
                .findFirst();

        assertTrue("Could not find a return instruction in method " + method.getMethodName()
                + ollir.getOllirCode(), retInst.isPresent(), ollir);
    }

    public static void assertHasOperation(OperationType opType, Method method, OllirResult ollirResult) {

        var binOps = CpUtils.assertInstExists(BinaryOpInstruction.class, method, ollirResult);

        var binOpsOfOperation = binOps.stream()
                .filter(binOp -> binOp.getOperation().getOpType() == opType)
                .collect(Collectors.toList());

        CpUtils.assertTrue(
                "Could not find binary operation of type " + opType.name() + " in method " + method.getMethodName(),
                !binOpsOfOperation.isEmpty(),
                ollirResult);
    }

    public static List<Node> getOllirNodes(ClassUnit classUnit, Predicate<Node> filter) {
        var nodes = new ArrayList<Node>();

        for (var method : classUnit.getMethods()) {
            getOllirNodes(method, filter, nodes);
        }
        // assertTrue(filterMessage, !nodes.isEmpty());
        // if (nodes.isEmpty()) {
        // throw new RuntimeException();
        // }

        return nodes;
    }

    public static List<Node> getOllirNodes(Method method, Predicate<Node> filter) {
        var nodes = new ArrayList<Node>();
        getOllirNodes(method, filter, nodes);
        return nodes;
    }

    private static void getOllirNodes(Method method, Predicate<Node> filter, List<Node> filteredNodes) {
        for (var inst : method.getInstructions()) {
            getOllirNodes(inst, filter, filteredNodes);
        }

    }

    private static void getOllirNodes(Node currentNode, Predicate<Node> filter, List<Node> filteredNodes) {
        // Check if node passes the filter
        if (filter.test(currentNode)) {
            filteredNodes.add(currentNode);
        }

        // Special cases
        if (currentNode instanceof AssignInstruction) {
            var assign = (AssignInstruction) currentNode;
            getOllirNodes(assign.getRhs(), filter, filteredNodes);
        }
    }

    public static List<Element> getElements(Instruction inst) {
        if (inst instanceof SingleOpInstruction) {
            return Arrays.asList(((SingleOpInstruction) inst).getSingleOperand());
        }

        if (inst instanceof OpInstruction) {
            return ((OpInstruction) inst).getOperands();
        }

        if (inst instanceof AssignInstruction) {
            var assign = (AssignInstruction) inst;
            return SpecsCollections.concat(assign.getDest(), getElements(assign.getRhs()));
        }

        if (inst instanceof CallInstruction) {
            var call = (CallInstruction) inst;
            var operands = call.getListOfOperands();
            return operands != null ? operands : Collections.emptyList();
        }

        throw new NotImplementedException(inst.getClass());
    }

    /**
     * Verifies if the given code matches (contains) the given regex
     *
     * @param code
     * @param regex
     */
    public static void matches(JasminResult result, String regex) {
        matches(result, Pattern.compile(regex));
    }

    public static void matches(JasminResult result, Pattern regex) {
        var matches = SpecsStrings.matches(result.getJasminCode(), regex);
        assertTrue("Expected code to match /" + regex + "/", matches, result);
    }

    public static void matches(String jasminCode, String regex) {
        matches(jasminCode, Pattern.compile(regex));
    }

    public static void matches(String jasminCode, Pattern regex) {
        var matches = SpecsStrings.matches(jasminCode, regex);
        Assert.assertTrue("Expected code to match /" + regex + "/ in the following code:\n\n" + jasminCode, matches);
    }

    public static void jasminHasField(JasminResult jasminResult, String fieldName, String fieldType) {
        var regex = FIELD_PREFIX + fieldName + FIELD_SUFFIX + fieldType;
        matches(jasminResult, regex);
    }

    public static void jasminHasField(JasminResult jasminResult, String fieldType) {
        var regex = FIELD_PREFIX + "\\w+" + FIELD_SUFFIX + fieldType;
        matches(jasminResult, regex);
    }

    public static String getJasminMethod(JasminResult jasminResult, String methodName) {
        var code = jasminResult.getJasminCode();

        if (methodName == null) {
            methodName = "\\w+";
        }

        var regex = "\\.method\\s+((public|private)\\s)?+" + methodName + "((.)+?)\\.end\\s+method";
        // var regex = "\\.method\\s+((public|private)\\s+)?" + methodName + "((.|\\s)+?)\\.end\\s+method";

        var results = SpecsStrings.getRegex(code, regex);

        assertTrue("Could not find method '" + methodName + "'", !results.isEmpty(), jasminResult);

        return results.get(2);
    }

    public static String getJasminMethod(JasminResult jasminResult) {
        return getJasminMethod(jasminResult, null);
    }

    public static Integer getBytecodeIndex(String instructionPrefix, String jasminCode) {
        try (var lines = LineStream.newInstance(jasminCode)) {

            while (lines.hasNextLine()) {
                var line = lines.nextLine().strip();

                if (!line.startsWith(instructionPrefix)) {
                    continue;
                }

                var substring = line.substring(instructionPrefix.length()).strip();

                if (substring.startsWith("_")) {
                    substring = substring.substring(1);
                }

                return Integer.parseInt(substring);
            }

            throw new RuntimeException("Could not find instruction with prefix " + instructionPrefix);
            // fail("Could not find instruction with prefix " + instructionPrefix + " in code:\n\n" + jasminCode);
        } catch (Exception e) {
            fail("getBytecodeIndex('" + instructionPrefix + "'): " + e.getMessage() + "\n\n" + jasminCode);
            // throw new RuntimeException(
            // "Exception while looking for instruction " + instructionPrefix + " in code:\n\n" + jasminCode);
        }

        return null;
    }

    public static void runJasmin(JasminResult jasminResult, String expected) {
		try {
        var output = SpecsStrings.normalizeFileContents(jasminResult.run(), true);
        assertEquals("Jasmin output", expected, output, jasminResult);
		} catch(Exception e) {
			throw new RuntimeException("Problems while running Jasmin code:\n" + jasminResult.getJasminCode(), e);
		}
    }

    private static final Pattern LIMIT_LOCALS = Pattern.compile("\\.limit\\s+locals\\s+([0-9]+)\\s+");

    private static final Pattern LIMIT_STACK = Pattern.compile("\\.limit\\s+stack\\s+([0-9]+)\\s+");

    public static Pattern getLimitLocalsRegex() {
        return LIMIT_LOCALS;
    }

    public static Pattern getLimitStackRegex() {
        return LIMIT_STACK;
    }

    public static String getLocalsRegex(int numLocals) {
        return "(a|i)(store|load)(_|\\s+)" + (numLocals - 1);
    }

    public static int countOccurences(JasminResult jasminResult, String word) {
        String code = jasminResult.getJasminCode();
        return (code.length() - code.replace(word, "").length()) / word.length();
    }

    public static int countOccurrencesRegex(JasminResult jasminResult, String regexString) {

        String jasminCode = jasminResult.getJasminCode();
        var matches = SpecsStrings.getRegexGroups(jasminCode, regexString,0);

        return matches.size();
    }
}
