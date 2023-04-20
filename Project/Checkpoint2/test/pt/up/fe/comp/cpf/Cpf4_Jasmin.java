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

package pt.up.fe.comp.cpf;

import org.junit.Test;
import pt.up.fe.comp.CpUtils;
import pt.up.fe.comp.TestUtils;
import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.specs.util.SpecsCheck;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import utils.ProjectTestUtils;

import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Cpf4_Jasmin {

    //  private static boolean USE_OLLIR_EXPERIMENTAL = false;
/*
    public static void enableOllirInputs() {
        USE_OLLIR_EXPERIMENTAL = true;
    }

    public static boolean useOllirInputs() {
        return USE_OLLIR_EXPERIMENTAL;
    }
*/
    static JasminResult getJasminResult(String filename) {
        /*
        if (USE_OLLIR_EXPERIMENTAL) {
            filename = SpecsIo.removeExtension(filename) + ".ollir";
            return TestUtils.backend(new OllirResult(SpecsIo.getResource("pt/up/fe/comp/cpf/4_jasmin/" + filename),
                    Collections.emptyMap()));
        }

        return TestUtils.backend(SpecsIo.getResource("pt/up/fe/comp/cpf/4_jasmin/" + filename));
*/

        var resource = "pt/up/fe/comp/cpf/4_jasmin/" + filename;

        SpecsCheck.checkArgument(resource.endsWith(".ollir"), () -> "Expected resource to end with .ollir: " + resource);

        // If AstToJasmin pipeline, change name of the resource and execute other test
        if (TestUtils.hasAstToJasminClass()) {

            // Rename resource
            var jmmResource = SpecsIo.removeExtension(resource) + ".jmm";

            // Test Jmm resource
            var result = TestUtils.backend(SpecsIo.getResource(jmmResource));

            return result;
        }

        var ollirResult = new OllirResult(SpecsIo.getResource(resource), Collections.emptyMap());

        var result = TestUtils.backend(ollirResult);

        return result;

    }

    public static void testOllirToJasmin(String resource, String expectedOutput) {
        SpecsCheck.checkArgument(resource.endsWith(".ollir"), () -> "Expected resource to end with .ollir: " + resource);

        // If AstToJasmin pipeline, change name of the resource and execute other test
        if (TestUtils.hasAstToJasminClass()) {

            // Rename resource
            var jmmResource = SpecsIo.removeExtension(resource) + ".jmm";

            // Test Jmm resource
            var result = TestUtils.backend(SpecsIo.getResource(jmmResource));
            ProjectTestUtils.runJasmin(result, expectedOutput);

            return;
        }

        var ollirResult = new OllirResult(SpecsIo.getResource(resource), Collections.emptyMap());

        var result = TestUtils.backend(ollirResult);

        ProjectTestUtils.runJasmin(result, null);
    }

    public static void testOllirToJasmin(String resource) {
        testOllirToJasmin(resource, null);
    }


    private static final String JASMIN_METHOD_REGEX_PREFIX = "\\.method\\s+((public|private)\\s+)?(\\w+)\\(\\)";

    /*checks if method declaration is correct (array)*/
    @Test
    public void section1_Basic_Method_Declaration_Array() {
        JasminResult jasminResult = getJasminResult("basic/BasicMethodsArray.ollir");
        CpUtils.matches(jasminResult, JASMIN_METHOD_REGEX_PREFIX + "\\[I");
    }

    /*checks if the index for loading a argument is correct (should be 1) */
    @Test
    public void section2_Arithmetic_BytecodeIndex_IloadArg() {
        var jasminResult = getJasminResult("arithmetic/ByteCodeIndexes1.ollir");
        var methodCode = CpUtils.getJasminMethod(jasminResult);

        int iloadIndex = CpUtils.getBytecodeIndex("iload", methodCode);
        assertEquals(1, iloadIndex);
    }

    /*checks if the index for storing a var is correct (should be > 1) */
    @Test
    public void section2_Arithmetic_BytecodeIndex_IstoreVar() {
        var jasminResult = getJasminResult("arithmetic/ByteCodeIndexes2.ollir");
        var methodCode = CpUtils.getJasminMethod(jasminResult);

        int istoreIndex = CpUtils.getBytecodeIndex("istore", methodCode);
        assertTrue("Expected index to be greater than one, is " + istoreIndex, istoreIndex > 1);
    }

    @Test
    public void section2_Arithmetic_Simple_and() {
        CpUtils.runJasmin(getJasminResult("arithmetic/Arithmetic_and.ollir"), "0");
    }

    @Test
    public void section2_Arithmetic_Simple_less() {
        CpUtils.runJasmin(getJasminResult("arithmetic/Arithmetic_less.ollir"), "1");
    }


    /*checks if an addition is correct (more than 2 values)*/
    @Test
    public void section3_ControlFlow_If_Simple() {
        CpUtils.runJasmin(getJasminResult("control_flow/SimpleIfElseStat.ollir"), "Result: 5\nResult: 8");
    }

    /*checks if an addition is correct (more than 2 values)*/
    @Test
    public void section3_ControlFlow_If_Not_Simple() {
        CpUtils.runJasmin(getJasminResult("control_flow/SimpleIfElseNot.ollir"), "10\n200");
    }

    /*checks if the code of a simple WHILE statement is well executed */
    @Test
    public void section3_ControlFlow_While_Simple() {
        CpUtils.runJasmin(getJasminResult("control_flow/SimpleWhileStat.ollir"), "Result: 0\nResult: 1\nResult: 2");
    }

    /*checks if the code of a more complex IF ELSE statement (similar a switch statement) is well executed */
    @Test
    public void section3_ControlFlow_Mixed_Switch() {
        CpUtils.runJasmin(getJasminResult("control_flow/SwitchStat.ollir"),
                "Result: 1\nResult: 2\nResult: 3\nResult: 4\nResult: 5\nResult: 6\nResult: 7");
    }

    /*checks if the code of a more complex IF ELSE statement (similar a switch statement) is well executed */
    @Test
    public void section3_ControlFlow_Mixed_Nested() {
        CpUtils.runJasmin(getJasminResult("control_flow/IfWhileNested.ollir"), "Result: 1\nResult: 2\nResult: 1");
    }

    /*checks if the code of a call to a function with multiple arguments (using boolean expressions in the call) is
    well executed*/
    @Test
    public void section4_Calls_Misc_ConditionArgs() {
        CpUtils.runJasmin(getJasminResult("calls/ConditionArgsFuncCall.ollir"), "Result: 10");

    }


    /*checks if an array is correctly initialized*/
    @Test
    public void section5_Arrays_Init_Array() {
        CpUtils.runJasmin(getJasminResult("arrays/ArrayInit.ollir"), "Result: 5");

    }

    /*checks if the access to the elements of array is correct*/
    @Test
    public void section5_Arrays_Store_Array() {
        CpUtils.runJasmin(getJasminResult("arrays/ArrayAccess.ollir"),
                "Result: 1\nResult: 2\nResult: 3\nResult: 4\nResult: 5");

    }

    /*checks multiple expressions as indexes to access the elements of an array*/
    @Test
    public void section5_Arrays_Load_ComplexArrayAccess() {
        CpUtils.runJasmin(getJasminResult("arrays/ComplexArrayAccess.ollir"),
                "Result: 1\nResult: 2\nResult: 3\nResult: 4\nResult: 5");

    }

    /*checks if array has correct signature ?*/
    @Test
    public void section5_Arrays_Signature_ArrayAsArg() {
        var jasminResult = getJasminResult("arrays/ArrayAsArgCode.ollir");
        var methodCode = CpUtils.getJasminMethod(jasminResult);

        CpUtils.matches(methodCode, "invokevirtual\\s+ArrayAsArg(/|\\.)(\\w+)\\(\\[I\\)I");
    }

    /*checks if array is being passed correctly as an argument to a function*/
    @Test
    public void section5_Arrays_As_Arg_Simple() {
        CpUtils.runJasmin(getJasminResult("arrays/ArrayAsArg.ollir"), "Result: 2");
    }

    /*checks if array is being passed correctly as an argument to a function (index of aload > 1)*/
    @Test
    public void section5_Arrays_As_Arg_Aload() {
        var jasminResult = getJasminResult("arrays/ArrayAsArgCode.ollir");
        var methodCode = CpUtils.getJasminMethod(jasminResult);

        int aloadIndex = CpUtils.getBytecodeIndex("aload", methodCode);
        assertTrue("Expected aload index to be greater than 1, is " + aloadIndex + ":\n" + methodCode, aloadIndex > 1);
    }

    /*checks if the .limits locals is not a const 99 value */
    @Test
    public void section6_Limits_Locals_Not_99() {
        var jasminResult = getJasminResult("limits/LocalLimits.ollir");
        var methodCode = CpUtils.getJasminMethod(jasminResult);
        var numLocals = Integer.parseInt(SpecsStrings.getRegexGroup(methodCode, CpUtils.getLimitLocalsRegex(), 1));
        assertTrue("limit locals should be less than 99:\n" + methodCode, numLocals >= 0 && numLocals < 99);

        // Make sure the code compiles
        jasminResult.compile();
    }

    /*checks if the .limits locals is the expected value (with a tolerance of 2) */
    @Test
    public void section6_Limits_Locals_Simple() {

        var jasminResult = getJasminResult("limits/LocalLimits.ollir");
        var methodCode = CpUtils.getJasminMethod(jasminResult);
        var numLocals = Integer.parseInt(SpecsStrings.getRegexGroup(methodCode, CpUtils.getLimitLocalsRegex(), 1));

        // Find store or load with numLocals - 1
        var regex = CpUtils.getLocalsRegex(numLocals);
        CpUtils.matches(methodCode, regex);

        // Makes sure the code compiles
        jasminResult.compile();
    }

    /*checks if the .limits stack is not a const 99 value */
    @Test
    public void section6_Limits_Stack_Not_99() {
        var jasminResult = getJasminResult("limits/LocalLimits.ollir");
        var methodCode = CpUtils.getJasminMethod(jasminResult);
        var numStack = Integer.parseInt(SpecsStrings.getRegexGroup(methodCode, CpUtils.getLimitStackRegex(), 1));
        assertTrue("limit stack should be less than 99:\n" + methodCode, numStack >= 0 && numStack < 99);

        // Make sure the code compiles
        jasminResult.compile();
    }

    /*checks if the .limits stack is the expected value (with a tolerance of 2) */
    @Test
    public void section6_Limits_Stack_Simple() {

        var jasminResult = getJasminResult("limits/LocalLimits.ollir");
        var methodCode = CpUtils.getJasminMethod(jasminResult);
        var numStack = Integer.parseInt(SpecsStrings.getRegexGroup(methodCode, CpUtils.getLimitStackRegex(), 1));

        int expectedLimit = 3;
        int errorMargin = 2;
        int upperLimit = expectedLimit + errorMargin;

        assertTrue(
                "limit stack should be = " + expectedLimit + " (accepted if <= " + upperLimit
                        + "), but is " + numStack + ":\n" + methodCode,
                numStack <= upperLimit && numStack >= expectedLimit);

        // Make sure the code compiles
        jasminResult.compile();
    }
}
