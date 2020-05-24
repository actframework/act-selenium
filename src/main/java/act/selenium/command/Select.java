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
import org.osgl.$;
import org.osgl.util.E;
import org.osgl.util.S;

import java.util.List;

/**
 * Select specific element(s) and store then into the scenario cache
 */
public class Select extends SeleniumCommand {

    private By by;

    @Override
    public void init(Object param) {
        String selector = S.string(param);
        E.invalidConfigurationIf(S.isBlank(selector), "selector required");
        by = new ByJQuery(selector);
    }

    @Override
    public boolean run(TestSession session, Scenario scenario) throws Exception {
        List<WebElement> selected = select(by, session);
        if ($.not(selected)) {
            return false;
        }
        selectElements(selected, session);
        return true;
    }
}
