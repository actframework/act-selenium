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
import org.osgl.$;
import org.osgl.util.C;
import org.osgl.util.E;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

public class ByJavaScript extends By implements Serializable {
    private final String script;

    public ByJavaScript(String script) {
        this.script = $.requireNotNull(script, "Cannot find elements with a null JavaScript expression.");
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        JavascriptExecutor js = getJavascriptExecutorFromSearchContext(context);

        // call the JS, inspect and validate response
        Object response = js.executeScript(script);
        List<WebElement> elements = getElementListFromJsResponse(response);

        // filter out the elements that aren't descendants of the context node
        if (context instanceof WebElement) {
            filterOutElementsWithoutCommonAncestor(elements, (WebElement)context);
        }

        return elements;
    }

    private static JavascriptExecutor getJavascriptExecutorFromSearchContext(SearchContext context) {
        if (context instanceof JavascriptExecutor) {
            // context is most likely the whole WebDriver
            return (JavascriptExecutor)context;
        }
        if (context instanceof WrapsDriver) {
            // context is most likely some WebElement
            WebDriver driver = ((WrapsDriver)context).getWrappedDriver();
            E.unexpectedIfNot(driver instanceof JavascriptExecutor, "This WebDriver doesn't support JavaScript.");
            return (JavascriptExecutor)driver;
        }
        throw new IllegalStateException("We can't invoke JavaScript from this context.");
    }

    @SuppressWarnings("unchecked")  // cast thoroughly checked
    private static List<WebElement> getElementListFromJsResponse(Object response) {
        if (response == null) {
            // no element found
            return C.list();
        }
        if (response instanceof WebElement) {
            // a single element found
            return C.newList((WebElement)response);
        }
        if (response instanceof List) {
            // found multiple things, check whether every one of them is a WebElement
            for (Object o : (List) response) {
                if (!(o instanceof WebElement)) {
                    throw E.unexpected("The JavaScript query returned something that isn't a WebElement.");
                }
            }
            return (List<WebElement>)response;  // cast is checked as far as we can tell
        }
        throw new IllegalArgumentException("The JavaScript query returned something that isn't a WebElement.");
    }

    private static void filterOutElementsWithoutCommonAncestor(List<WebElement> elements, WebElement ancestor) {
        for (Iterator<WebElement> iter = elements.iterator(); iter.hasNext(); ) {
            WebElement elem = iter.next();

            // iterate over ancestors
            while (!elem.equals(ancestor) && !elem.getTagName().equals("html")) {
                elem = elem.findElement(By.xpath("./.."));
            }

            if (!elem.equals(ancestor)) {
                iter.remove();
            }
        }
    }

    @Override
    public String toString() {
        return "By.javaScript: \"" + script + "\"";
    }

}
