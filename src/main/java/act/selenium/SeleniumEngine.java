package act.selenium;

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
import act.test.Scenario;
import act.test.TestEngine;
import act.test.TestSession;
import act.util.ProgressGauge;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.osgl.$;
import org.osgl.inject.annotation.Configuration;
import org.osgl.util.S;

import javax.validation.ValidationException;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static act.selenium.SeleniumCommand.KEY_DRIVER;

public class SeleniumEngine implements TestEngine {

    public static final String NAME = "selenium";

    @Configuration("test.selenium.host")
    private String defaultHost;

    @Configuration("test.selenium.port")
    private int defaultPort;

    private Set<WebDriver> drivers = new HashSet<>();

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isEmpty(Scenario scenario) {
        List steps = scenario.getValue("steps");
        return $.not(steps);
    }

    @Override
    public void validate(Scenario scenario, TestSession session) throws ValidationException {
        List steps = scenario.getValue("steps");
        if (null == steps || steps.isEmpty()) {
            throw new ValidationException("No steps found");
        }
        for (Object step : steps) {
            if (!(step instanceof Map)) {
                throw new ValidationException("Step shall be a Map structure, found: " + step.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean run(Scenario scenario, TestSession session, ProgressGauge gauge) {
        List steps = scenario.getValue("steps");
        for (Object o : steps) {
            gauge.step();
            SeleniumCommand cmd = $.convert(o).to(SeleniumCommand.class);
            try {
                boolean success = cmd.run(session, scenario);
                if (!success) {
                    scenario.errorMessage = "Failed running selenium step: " + cmd;
                    return false;
                }
            } catch (Exception e) {
                scenario.errorMessage = "Error running selenium step: " + cmd;
                scenario.cause = e;
                return false;
            }
        }
        return true;
    }

    public String fullUrl(String urlPath) {
        if (urlPath.startsWith("http")) {
            return urlPath;
        }
        urlPath = S.ensure(urlPath).startWith("/");
        if (S.blank(defaultHost)) {
            defaultHost = "localhost";
        }
        if (0 == defaultPort) {
            defaultPort = Act.appConfig().httpPort();
        }
        String scheme = defaultPort == 443 ? "https" : "http";
        return S.concat(scheme, "://", defaultHost, ":", defaultPort, urlPath);
    }

    @Override
    public void setup() {
        SeleniumCommand.registerTypeConverters();
    }

    @Override
    public void setupSession(TestSession session) {
        WebDriver driver = (WebDriver) session.constants.get(KEY_DRIVER);
        if (null == driver) {
            driver = createDriver();
            session.constants.put(KEY_DRIVER, driver);
        }
    }

    @Override
    public void teardownSession(TestSession session) {
    }

    @Override
    public void teardown() {
        for (WebDriver driver: drivers) {
            driver.quit();
        }
        drivers.clear();
    }

    private WebDriver createDriver() {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("headless");
        WebDriver driver = new ChromeDriver(options);
        drivers.add(driver);
        return driver;
    }

}
