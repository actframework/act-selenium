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
import org.osgl.util.S;

public abstract class ElementAction extends SeleniumCommand {

    protected By by;

    @Override
    public void init(Object param) {
        String selector = S.string(param);
        if (S.isBlank(selector)) {
            return;
        }
        by = new ByJQuery(selector);
    }

    @Override
    public boolean run(TestSession session, Scenario scenario) {
        WebElement element = null != by ? selectOne(by, session) : selectedElement(session);
        E.unexpectedIf(null == element, "No element selected to action");
        actOnElement(element);
        return true;
    }

    protected abstract void actOnElement(WebElement element);


}
