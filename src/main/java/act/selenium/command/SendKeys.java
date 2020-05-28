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

import act.selenium.SeleniumCommand;
import act.selenium.util.ByJQuery;
import act.test.Scenario;
import act.test.TestSession;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.osgl.util.E;

import java.util.Map;

public class SendKeys extends SeleniumCommand {

    protected By by;
    private String keys;

    @Override
    public void init(Object param) {
        E.unexpectedIf(null == param, "keys expected");
        if (param instanceof String) {
            keys = param.toString();
        } else if (param instanceof Map) {
            Map map = (Map) param;
            Object o = map.get("target");
            if (null != o) {
                String target = o.toString();
                by = new ByJQuery(target);
            }
            o = map.get("keys");
            if (null != o) {
                keys = o.toString();
            }
        }
    }

    @Override
    public boolean run(TestSession session, Scenario scenario) throws Exception {
        WebElement selected = null != by ? selectOne(by, session) : selectedElement(session);
        E.illegalStateIf(null == selected, "No input selected");
        selected.sendKeys(keys);
        return true;
    }
}
