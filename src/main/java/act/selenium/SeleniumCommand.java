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

import act.test.Scenario;
import act.test.TestSession;
import act.test.util.NamedLogic;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.osgl.util.converter.TypeConverterRegistry;

import java.util.List;

public abstract class SeleniumCommand extends NamedLogic {

    public static final String KEY_DRIVER = "_selenium_driver";
    public static final String KEY_SELECTED = "_selenium_selected_";

    @Override
    protected Class<? extends NamedLogic> type() {
        return SeleniumCommand.class;
    }

    /**
     * Run the command.
     *
     * @param session the current test session
     * @param scenario the current test scenario
     *
     * @throws Exception if there are any issue with running the command.
     *                   In case exception raise, then the test is marked
     *                   as fail
     */
    public abstract boolean run(TestSession session, Scenario scenario) throws Exception;

    public static void registerTypeConverters() {
        TypeConverterRegistry.INSTANCE.register(new FromLinkedHashMap(SeleniumCommand.class));
        TypeConverterRegistry.INSTANCE.register(new FromString(SeleniumCommand.class));
    }

    protected static WebDriver driver(TestSession session) {
        return (WebDriver) session.constants.get(KEY_DRIVER);
    }

    protected static JavascriptExecutor jsExec(TestSession session) {
        return (JavascriptExecutor) driver(session);
    }

    protected static Object evalJavascript(String exp, TestSession session) {
        if (!exp.contains("return ")) {
            exp = "return " + exp;
        }
        return jsExec(session).executeScript(exp);
    }

    protected static void selectElements(List<WebElement> webElements, TestSession session) {
        session.cache(KEY_SELECTED, webElements);
    }

    protected static List<WebElement> selectedElements(TestSession session) {
        return (List<WebElement>)session.cached(KEY_SELECTED);
    }

    protected static WebElement selectedElement(TestSession session) {
        List<WebElement> list = selectedElements(session);
        if (null == list || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    protected static List<WebElement> select(By by, TestSession session) {
        WebDriver driver = driver(session);
        return driver.findElements(by);
    }

    protected static WebElement selectOne(By by, TestSession session) {
        WebDriver driver = driver(session);
        List<WebElement> list = driver.findElements(by);
        if (null == list || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

}
