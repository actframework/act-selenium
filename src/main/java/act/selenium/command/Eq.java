package act.selenium.command;

/*-
 * #%L
 * ACT Selenium
 * %%
 * Copyright (C) 2020 ActFramework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import act.selenium.ReversibleSeleniumCommand;
import act.selenium.SeleniumCommand;
import act.test.Scenario;
import act.test.TestSession;
import org.osgl.$;
import org.osgl.util.E;

import java.util.Map;

public class Eq extends ReversibleSeleniumCommand {

    private Object expected;
    private Object found;

    @Override
    public void init(Object param) {
        if (param instanceof Map) {
            Map<String, Object> map = $.cast(param);
            expected = map.get("expected");
            found = map.get("found");
            E.invalidConfigurationIf(null == expected, "'expected' required");
            E.invalidConfigurationIf(null == found, "'found' required");
        } else {
            throw E.invalidConfiguration("A map structure with 'expected' and 'found' keys is required");
        }
    }

    @Override
    public boolean run(TestSession session, Scenario scenario) throws Exception {
        if (found instanceof String) {
            found = evalJavascript((String) found, session);
        }
        return $.eq(expected, found);
    }
}
