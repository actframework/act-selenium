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
import act.test.Scenario;
import act.test.TestSession;
import act.util.NoAutoRegister;
import org.osgl.$;

/**
 * A command negate another command's run result.
 *
 * E.g. !exists negate the run result of exists
 */
@NoAutoRegister
public class NegateCommand extends ReversibleSeleniumCommand {

    public ReversibleSeleniumCommand c;

    public NegateCommand(ReversibleSeleniumCommand c) {
        this.c = $.requireNotNull(c);
    }

    @Override
    public boolean run(TestSession session, Scenario scenario) throws Exception {
        return !c.run(session, scenario);
    }
}
