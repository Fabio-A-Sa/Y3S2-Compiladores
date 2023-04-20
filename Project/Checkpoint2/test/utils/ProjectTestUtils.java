package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.specs.comp.ollir.ArrayType;
import org.specs.comp.ollir.AssignInstruction;
import org.specs.comp.ollir.CallInstruction;
import org.specs.comp.ollir.CallType;
import org.specs.comp.ollir.ClassType;
import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.Element;
import org.specs.comp.ollir.ElementType;
import org.specs.comp.ollir.LiteralElement;
import org.specs.comp.ollir.Method;
import org.specs.comp.ollir.Node;
import org.specs.comp.ollir.Operand;
import org.specs.comp.ollir.Type;

import pt.up.fe.comp.jmm.jasmin.JasminResult;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;
import pt.up.fe.specs.util.SpecsSystem;
import pt.up.fe.specs.util.exceptions.NotImplementedException;
import pt.up.fe.specs.util.system.ProcessOutputAsString;
import pt.up.fe.specs.util.utilities.LineStream;

public class ProjectTestUtils {

    // private static final File RANDOM_TEST_FOLDER = SpecsIo.newRandomFolder();

    public static File getRandomFolder() {
        var folder = SpecsIo.newRandomFolder();
        SpecsIo.deleteFolderContents(folder);
        return folder;
        // return RANDOM_TEST_FOLDER;
    }

    /**
     * Helper method assumes repo is working dir.
     * 
     * @param repoFolder
     * @return
     */
    public static File prepareRunningFolder() {
        return prepareRunningFolder(SpecsIo.getWorkingDir());
    }

    /**
     * Prepares a folder for running the compiler. Includes fixes such as copying the .properties files
     * 
     * @return
     */
    public static File prepareRunningFolder(File repoFolder) {
        // Use random folder
        var folder = getRandomFolder();

        System.out.println("Running folder of compiler will be " + folder.getAbsolutePath());

        // Fix: some compilers use TestUtils and need the .properties
        var propertiesFiles = SpecsIo.getFiles(repoFolder, "properties");
        propertiesFiles.stream().forEach(prop -> SpecsIo.copy(prop, new File(folder, prop.getName())));

        return folder;
    }

    public static File getGeneratedJasmin(File workingFolder) {
        var jasminFiles = SpecsIo.getFilesRecursive(workingFolder, "j");

        if (jasminFiles.isEmpty()) {
            throw new RuntimeException("No .j file found in " + workingFolder);
        }

        if (jasminFiles.size() > 1) {
            throw new RuntimeException("More than one .j file found in " + workingFolder + ": " + jasminFiles);
        }

        return jasminFiles.get(0);
    }

    public static boolean hasBytecode(String bytecodeInstruction, String jasminCode) {
        try (var lines = LineStream.newInstance(jasminCode)) {

            while (lines.hasNextLine()) {
                var line = lines.nextLine().strip();

                if (!line.equals(bytecodeInstruction)) {
                    continue;
                }

                return true;
            }

            throw new RuntimeException("Instruction not found");

        } catch (Exception e) {
            throw new RuntimeException(
                    "Exception while looking for instruction " + bytecodeInstruction + " in code:\n\n" + jasminCode);
        }
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

            throw new RuntimeException("Instruction not found");

        } catch (Exception e) {
            throw new RuntimeException(
                    "Exception while looking for instruction " + instructionPrefix + " in code:\n\n" + jasminCode);
        }
    }

    public static String getJasminMethod(String code) {
        return getJasminMethod(code, null);
    }

    public static String getJasminMethod(String code, String methodName) {
        if (methodName == null) {
            methodName = "\\w+";
        }

        var regex = "\\.method\\s+((public|private)\\s)?+" + methodName + "((.|\\s)+?)\\.end\\s+method";
        var results = SpecsStrings.getRegex(code, regex);

        if (results.isEmpty()) {
            throw new RuntimeException("Could not find method '" + methodName + "' in the following code:\n" + code);
        }

        return results.get(2);
    }

    public static String toString(Element ollirElement) {
        var string = toString(ollirElement.getType()) + " ";

        if (ollirElement.isLiteral()) {
            var literal = (LiteralElement) ollirElement;
            return string + literal.getLiteral();
        }

        var operand = (Operand) ollirElement;
        return string + operand.getName();
        // return toString(ollirType.getTypeOfElement(), ollirType);
    }

    public static String toString(Type ollirType) {
        var elementType = ollirType.getTypeOfElement();

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
                return toString(arrayType.getElementType())
                        + SpecsStrings.buildLine("[]", arrayType.getNumDimensions());
            default:
                throw new NotImplementedException(elementType);
        }
    }



    public static Method getLastMethod(ClassUnit classUnit) {
        var methods = classUnit.getMethods();

        return methods.get(methods.size() - 1);
    }

    public static Method getMethod(ClassUnit classUnit, String methodName) {
        for (var method : classUnit.getMethods()) {
            if (method.getMethodName().equals(methodName)) {
                return method;
            }
        }

        throw new RuntimeException("Could not find OLLIR method with name '" + methodName + "'");
    }

    public static ProcessOutputAsString runMain(File workingDir, String... args) {
        // Assumes it is running on the root of the repository
        return runMain(SpecsIo.getWorkingDir(), workingDir, args);
    }

    public static ProcessOutputAsString runMain(File repoFolder, File workingDir, String... args) {
        return runMain(repoFolder, workingDir, Arrays.asList(args));
    }

    public static ProcessOutputAsString runMain(File repoFolder, File workingDir, List<String> args) {

        // Get build.gradle
        var gradleFile = SpecsIo.existingFile(repoFolder, "build.gradle");
        if (gradleFile == null) {
            throw new RuntimeException("Could not find build.gradle in folder '" + repoFolder.getAbsolutePath() + "'");
        }

        var mainClass = extractMainClass(gradleFile);

        var repoFolderCanonical = SpecsIo.getCanonicalFile(repoFolder);
        // var repoName = repoFolderCanonical.getName();
        // var compilerJar = SpecsIo.existingFile(repoName + ".jar");

        List<String> classpath = new ArrayList<>();
        // var classpath = new StringBuilder();
        // Jar will not be build
        // classpath.append(new File(repoFolderCanonical, repoName + ".jar").getAbsolutePath());
        classpath.add(new File(repoFolderCanonical, "build/classes/java/main").getAbsolutePath());
        classpath.add(new File(repoFolderCanonical, "libs/utils.jar").getAbsolutePath());
        classpath.add(new File(repoFolderCanonical, "libs/gson-2.8.2.jar").getAbsolutePath());
        classpath.add(new File(repoFolderCanonical, "libs/ollir.jar").getAbsolutePath());
        classpath.add(new File(repoFolderCanonical, "libs/jasmin.jar").getAbsolutePath());

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(classpath.stream().collect(Collectors.joining(System.getProperty("path.separator"))));
        command.add(mainClass);
        command.addAll(args);

        return SpecsSystem.runProcess(command, workingDir, true, true);
        // java -cp
        // "%~dp0comp2021-1e.jar;%~dp0libs/utils.jar;%~dp0libs/gson-2.8.2.jar;%~dp0libs/ollir.jar;%~dp0libs/jasmin.jar"
        // %*

        // System.out.println("REPO FOLDER: " + SpecsIo.getCanonicalFile(repoFolder).getName());
        // System.out.println("WORKING DIR: " + workingDir.getAbsolutePath());

        // OLD METHOD
        /*
        try {
        
            // Get class with main
            Class<?> analysisClass = Class.forName(mainClass);
        
            Method[] methods = analysisClass.getMethods();
            for (Method m : methods) {
                if ("main".equals(m.getName())) {
                    // for static methods we can use null as instance of class
                    m.invoke(null, new Object[] { args });
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Could not invoke main of compiler", e);
        }
        */
    }

    private static String extractMainClass(File gradleFile) {
        try (var lines = LineStream.newInstance(gradleFile)) {
            while (lines.hasNextLine()) {
                var line = lines.nextLine();

                if (!line.contains("Main-Class")) {
                    continue;
                }

                // Found line, extract class
                var colonIndex = line.indexOf(':');
                if (colonIndex == -1) {
                    throw new RuntimeException("Could not find colon in line '" + line + "'");
                }

                var className = line.substring(colonIndex + 1).strip();

                // Remove quotes and single quotes
                if (className.startsWith("'") || className.startsWith("\"")) {
                    className = className.substring(1);
                }

                if (className.endsWith("'") || className.endsWith("\"")) {
                    className = className.substring(0, className.length() - 1);
                }

                return className;
            }
        } catch (Exception e) {
            throw new RuntimeException("Exception while extracting main file from Gradle file", e);
        }

        throw new RuntimeException(
                "Could not find main class in Gradle build file '" + gradleFile.getAbsolutePath() + "'");
    }

    /**
     * Verifies if the given code matches (contains) the given regex
     * 
     * @param code
     * @param regex
     */
    public static void matches(String code, String regex) {
        matches(code, Pattern.compile(regex));
    }

    public static void matches(String code, Pattern regex) {
        var matches = SpecsStrings.matches(code, regex);
        assertTrue("Expected code to match /" + regex + "/:\n" + code + "", matches);
    }

    public static void runJasmin(JasminResult jasminResult, String expected) {
        var output = SpecsStrings.normalizeFileContents(jasminResult.run(), true);

        // No expected output, just run test
        if(expected == null) {
            return;
        }

        assertEquals(
                "Jasmin execution, expected '" + expected + "', got '" + output + "':\n" + jasminResult.getJasminCode(),
                expected, output);
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

    public static void findInvoke(OllirResult ollirResult, CallType invokeType) {
        var nodes = getOllirNodes(ollirResult.getOllirClass(),
                inst -> inst instanceof CallInstruction &&
                        ((CallInstruction) inst).getInvocationType() == invokeType);

        assertTrue(
                "Expected to find one " + invokeType + ", instead found " + nodes.size() + ":\n"
                        + ollirResult.getOllirCode(),
                nodes.size() == 1);
    }

    /*
    public static void checkOllirNodes(OllirResult ollirResult, Predicate<Node> filter, Predicate<Node> checker,
            String messageIfEmpty, String messageIf) {
    
        var nodes = getOllirNodes(ollirResult, filter, messageIfEmpty);
    
        checkOllirNodes(ollirResult, nodes, checker);
    }
    
    public static void checkOllirNodes(OllirResult ollirResult, List<Node> nodes, Predicate<Node> checker) {
    
        for (var node : nodes) {
            // checker.accept(node);
            if (!checker.test(node)) {
                fail("Ollir node did not pass check: " + node + "\n" + ollirResult.getOllirCode());
            }
        }
    }
    */
}