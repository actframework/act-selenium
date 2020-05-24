package act.selenium.util;

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

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.osgl.$;
import org.osgl.util.S;

import java.io.Serializable;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class ByJQuery extends By implements Serializable {
    private final String selector;

    public ByJQuery(String selector) {
        $.requireNotNull(selector, "Cannot find elements with a null JQuery expression.");
        if (!(selector.startsWith("$(") || selector.startsWith("jQuery("))) {
            selector = S.concat("$(", selector, ")");
        }
        this.selector = selector;
    }

    public String getSelector() {
        return selector;
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        JavascriptExecutor jsExec = getJavaScriptExecutorFromContext(context);

        if (!isJQueryInThisPage(jsExec)) {
            injectJQuery(jsExec);
        }

        return new ByJavaScript("return " + selector).findElements(context);
    }

    private static JavascriptExecutor getJavaScriptExecutorFromContext(SearchContext context) {
        if (context instanceof JavascriptExecutor) {
            return (JavascriptExecutor)context;
        }
        if (context instanceof WrapsDriver) {
            WebDriver driver = ((WrapsDriver)context).getWrappedDriver();
            if (driver instanceof JavascriptExecutor) {
                return (JavascriptExecutor) driver;
            }
        }
        throw new IllegalStateException("Can't access a JavaScriptExecutor instance from the current search context.");
    }

    private static boolean isJQueryInThisPage(JavascriptExecutor driver) {
        Boolean loaded;
        try {
            loaded = (Boolean) driver.executeScript("return jQuery()!=null");
        } catch (WebDriverException e) {
            loaded = false;
        }
        return loaded;
    }

    private static void injectJQuery(JavascriptExecutor jsExec) {
        jsExec.executeScript(" var headID = document.getElementsByTagName(\"head\")[0];"
                + "var newScript = document.createElement('script');"
                + "newScript.type = 'text/javascript';"
                + "newScript.src = 'https://ajax.aspnetcdn.com/ajax/jQuery/jquery-3.4.1.min.js';"
                + "headID.appendChild(newScript);");
    }

    public static void ensureJQuery(JavascriptExecutor jsExec) {
        if (!isJQueryInThisPage(jsExec)) {
            injectJQuery(jsExec);
            while (!isJQueryInThisPage(jsExec)) {
                WebDriver driver = $.cast(jsExec);
                WebDriverWait wait = new WebDriverWait(driver, 5);
                wait.withTimeout(Duration.ofSeconds(1));
            }
        }
    }

    @Override
    public String toString() {
        return "By.jQuery: \"$(" + selector + ")\"";
    }
}
