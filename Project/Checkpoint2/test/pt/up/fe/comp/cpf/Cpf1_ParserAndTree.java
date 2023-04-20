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
import pt.up.fe.specs.util.SpecsIo;
import pt.up.fe.specs.util.SpecsStrings;

public class Cpf1_ParserAndTree {

    static JasminResult getJmmResult(String filename) {
        return TestUtils.backend(SpecsIo.getResource("pt/up/fe/comp/cpf/1_parser_and_tree/" + filename));
    }

    @Test
    public void section1_OpPrecedence_1_AddMultConstants() {
        var result = getJmmResult("AddMultConstants.jmm");
        TestUtils.noErrors(result.getReports());
        CpUtils.assertEquals("Wrong results", "7\n12\n9", SpecsStrings.normalizeFileContents(result.run(), true),
                result);
    }

}
