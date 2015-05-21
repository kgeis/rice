/**
 * Copyright 2005-2015 The Kuali Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl2.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.engine.node;

import org.kuali.rice.kew.engine.RouteContext;

/**
 * The JoinEngine is responsible for maintaining join state and determining when the join condition
 * has been satisfied.
 *
 * @author Kuali Rice Team (rice.collab@kuali.org)
 */
public interface JoinEngine {

    public void addExpectedJoiner(RouteNodeInstance nodeInstance, Branch branch);
    
    public void addActualJoiner(RouteNodeInstance nodeInstance, Branch branch);
    
    public boolean isJoined(RouteNodeInstance nodeInstance);
    
    public void createExpectedJoinState(RouteContext context, RouteNodeInstance joinInstance, RouteNodeInstance previousNodeInstance);
}