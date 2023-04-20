package pt.up.fe.comp;

import org.junit.Test;
import pt.up.fe.comp.jmm.analysis.JmmSemanticsResult;
import pt.up.fe.specs.util.SpecsIo;


import java.awt.desktop.SystemEventListener;

import static org.junit.Assert.assertEquals;

/**
 * Test variable lookup.
 */
public class SymbolTableTest {

    static JmmSemanticsResult getSemanticsResult(String filename) {
        return TestUtils.analyse(SpecsIo.getResource("pt/up/fe/comp/jmm/"+filename));
    }
    
    static JmmSemanticsResult test(String filename, boolean fail) {
    	var semantics = getSemanticsResult(filename);
    	if(fail) {
    		TestUtils.mustFail(semantics.getReports());
    	}else 	{
       	 	TestUtils.noErrors(semantics.getReports());
    	}
    	return semantics;
    }
    
	
    /**
     * Test if fields are not being accessed from static methods.
     */
    @Test
    public void NumImports() {		
    	var semantics = test("symboltable/Imports.jmm",false);
    	assertEquals(2, semantics.getSymbolTable().getImports().size());
    }
    
    @Test
    public void ClassAndSuper() {		
    	var semantics = test("symboltable/Super.jmm",false);
    	assertEquals("Super", semantics.getSymbolTable().getClassName());
    	assertEquals("UltraSuper", semantics.getSymbolTable().getSuper());

    }
    
    @Test
    public void Fields() {		
    	var semantics = test("symboltable/MethodsAndFields.jmm",false);
    	var fields = semantics.getSymbolTable().getFields();
    	assertEquals(3, fields.size());
    	var checkInt = 0;
    	var checkBool = 0;
    	var checkObj = 0;
    	System.out.println("FIELDS: "+fields);
    	for(var f :fields){
    		switch(f.getType().getName()) {
    		case "MethodsAndFields": checkObj++; break;
    		case "boolean": checkBool++; break;
    		case "int": checkInt++;break;
    		}
    	};
    	assertEquals("Field of type int", 1, checkInt);
    	assertEquals("Field of type boolean", 1, checkBool);
    	assertEquals("Field of type object", 1, checkObj);

    }

	@Test
	public void LocalVars() {
		var semantics = test("symboltable/LocalVars.jmm",false);
		var st = semantics.getSymbolTable();
		var methods = st.getMethods();
		var method = methods.get(0);
		var localVars = st.getLocalVariables(method);

		assertEquals(1, methods.size());
		System.out.println("METHODS: "+methods);

		assertEquals(1, localVars.size());
		System.out.println("LOCAL VARS: "+localVars);
	}
	/*
	@Test
	public void Arrays() {
		var semantics = test("symboltable/Arrays.jmm",false);
		var fields = semantics.getSymbolTable().getFields();
		assertEquals(3, fields.size());

		var checkInt = 0;
		var checkBool = 0;
		var checkObj = 0;
		var checkString = 0;
		System.out.println("FIELDS: "+fields);
		for(var f :fields){
			switch(f.getType().getName()) {
				case "MethodsAndFields": checkObj++; break;
				case "boolean": checkBool++; break;
				case "int": checkInt++;break;
				case "string": checkString++;break;
			}
		};
		assertEquals("Field of type int", 1, checkInt);
		assertEquals("Field of type boolean", 1, checkBool);
		assertEquals("Field of type object", 0, checkObj);
		assertEquals("Field of type String", 1, checkString);
	}*/
    
    @Test
    public void Methods() {		
    	var semantics = test("symboltable/MethodsAndFields.jmm",false);
    	var st = semantics.getSymbolTable();
    	var methods = st.getMethods();
    	assertEquals(5, methods.size());
    	var checkInt = 0;
    	var checkBool = 0;
    	var checkObj = 0;
    	var checkAll = 0;
    	System.out.println("METHODS: "+methods);
    	for(var m :methods){
    		var ret = st.getReturnType(m);
    		var numParameters = st.getParameters(m).size();
    		switch(ret.getName()) {
	    		case "MethodsAndFields": 
	    			checkObj++; 
	    			assertEquals("Method "+m+" parameters",0,numParameters);
	    			break;
	    		case "boolean": 
	    			checkBool++; 
	    			assertEquals("Method "+m+" parameters",0,numParameters);
	    			break;
	    		case "int": 
	    			if(ret.isArray()){
	    				checkAll++;
	    				assertEquals("Method "+m+" parameters",3,numParameters);
	    			}else {
	    				checkInt++;
	    				assertEquals("Method "+m+" parameters",0,numParameters);
	    			}
	    			break;
	    			
    		}
    	}
    	assertEquals("Method with return type int", 1, checkInt);
    	assertEquals("Method with return type boolean", 1, checkBool);
    	assertEquals("Method with return type object", 1, checkObj);
    	assertEquals("Method with three arguments", 1, checkAll);
    }
    
    @Test
    public void Parameters() {		
    	var semantics = test("symboltable/Parameters.jmm",false);
    	var st = semantics.getSymbolTable();
    	var methods = st.getMethods();
    	assertEquals(1, methods.size());
    	
    	var parameters = st.getParameters(methods.get(0));
    	assertEquals(3, parameters.size());

    	assertEquals("Parameter 1", "int", parameters.get(0).getType().getName());
    	assertEquals("Parameter 2", "boolean", parameters.get(1).getType().getName());
    	assertEquals("Parameter 3", "Parameters", parameters.get(2).getType().getName());
    }
}
