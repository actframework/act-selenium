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

import act.selenium.command.NegateCommand;
import act.test.util.ReversibleLogic;

public abstract class ReversibleSeleniumCommand<T extends ReversibleSeleniumCommand>
        extends SeleniumCommand
        implements ReversibleLogic<T>
{
    @Override
    public T reversed() {
        if (this instanceof NegateCommand) {
            return (T) ((NegateCommand) this).c;
        }
        return (T)new NegateCommand(this);
    }
}
