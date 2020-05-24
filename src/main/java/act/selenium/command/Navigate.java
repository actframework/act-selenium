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

import act.Act;
import act.selenium.SeleniumCommand;
import act.selenium.SeleniumEngine;
import act.selenium.util.ByJQuery;
import act.test.Scenario;
import act.test.TestSession;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.osgl.util.C;
import org.osgl.util.E;
import org.osgl.util.S;

import java.util.List;

/**
 * Navigate to a URL
 */
public class Navigate extends SeleniumCommand {

    private String url;

    @Override
    protected List<String> aliases() {
        return C.list("nav", "browse", "open");
    }

    @Override
    public void init(Object param) {
        String urlPath = S.string(param);
        E.invalidConfigurationIf(S.isBlank(urlPath), "URL path expected");
        SeleniumEngine engine = Act.getInstance(SeleniumEngine.class);
        this.url = engine.fullUrl(urlPath);
    }

    @Override
    public boolean run(TestSession session, Scenario scenario) throws Exception {
        WebDriver driver = driver(session);
        driver.get(url);
        ByJQuery.ensureJQuery((JavascriptExecutor) driver);
        return true;
    }

}
