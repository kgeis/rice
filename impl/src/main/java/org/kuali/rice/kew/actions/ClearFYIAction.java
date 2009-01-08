/*
 * Copyright 2005-2006 The Kuali Foundation.
 *
 *
 * Licensed under the Educational Community License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.opensource.org/licenses/ecl1.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.kuali.rice.kew.actions;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.MDC;
import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.exception.KEWUserNotFoundException;
import org.kuali.rice.kew.exception.InvalidActionTakenException;
import org.kuali.rice.kew.exception.ResourceUnavailableException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.user.WorkflowUser;
import org.kuali.rice.kew.util.KEWConstants;
import org.kuali.rice.kew.util.Utilities;
import org.kuali.rice.kim.bo.entity.KimPrincipal;


/**
 *
 * ClearFYIAction deactivates the user requests.
 *
 * The routeheader is first checked to make sure the action is valid for the document.
 * Next the user is checked to make sure he/she has not taken a previous action on this
 * document at the actions responsibility or below. Any requests related to this user
 * are deactivated.
 *
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public class ClearFYIAction extends ActionTakenEvent {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(ClearFYIAction.class);

    /**
     * @param rh
     *            RouteHeader for the document upon which the action is taken.
     * @param user
     *            User taking the action.
     */
    public ClearFYIAction(DocumentRouteHeaderValue rh, KimPrincipal principal) {
        super(KEWConstants.ACTION_TAKEN_FYI_CD, rh, principal);
    }

    /**
     * @param rh
     *            RouteHeader for the document upon which the action is taken.
     * @param user
     *            User taking the action.
     * @param annotation
     *            User comment on the action taken
     */
    public ClearFYIAction(DocumentRouteHeaderValue rh, KimPrincipal principal, String annotation) {
        super(KEWConstants.ACTION_TAKEN_FYI_CD, rh, principal, annotation);
    }
    
    /**
     * Method to check if the Action is currently valid on the given document
     * @return  returns an error message to give system better identifier for problem
     */
    public String validateActionRules() throws KEWUserNotFoundException {
        return validateActionRules(getActionRequestService().findAllValidRequests(getPrincipal().getPrincipalId(), routeHeader.getRouteHeaderId(), KEWConstants.ACTION_REQUEST_FYI_REQ));
    }

    private String validateActionRules(List<ActionRequestValue> actionRequests) throws KEWUserNotFoundException {
        if (!getRouteHeader().isValidActionToTake(getActionPerformedCode())) {
            return "Document is not in a state to have FYI processed";
        }
        if (!isActionCompatibleRequest(actionRequests)) {
            return "No request for the user is compatible " + "with the ClearFYI action";
        }
        return "";
    }

    public boolean isActionCompatibleRequest(List requests) throws KEWUserNotFoundException {

        // can always cancel saved or initiated document
        if (routeHeader.isStateInitiated() || routeHeader.isStateSaved()) {
            return true;
        }

        boolean actionCompatible = false;
        Iterator ars = requests.iterator();
        ActionRequestValue actionRequest = null;

        while (ars.hasNext()) {
            actionRequest = (ActionRequestValue) ars.next();

            //FYI request matches all but deny and cancel
            if (KEWConstants.ACTION_REQUEST_FYI_REQ.equals(actionRequest.getActionRequested())) {
                actionCompatible = true;
                break;
            }
        }

        return actionCompatible;
    }

    /**
     * Processes the clear FYI action. - Checks to make sure the document status allows the action. - Checks that the user has not taken a previous action. - Deactivates the pending requests for this user
     *
     * @throws InvalidActionTakenException
     * @throws ResourceUnavailableException
     */
    public void recordAction() throws InvalidActionTakenException, KEWUserNotFoundException {
        MDC.put("docId", getRouteHeader().getRouteHeaderId());
        updateSearchableAttributesIfPossible();

        LOG.debug("Clear FYI for document : " + annotation);
        LOG.debug("Checking to see if the action is legal");

        List actionRequests = getActionRequestService().findAllValidRequests(getPrincipal().getPrincipalId(), getRouteHeaderId(), KEWConstants.ACTION_REQUEST_FYI_REQ);
        String errorMessage = validateActionRules(actionRequests);
        if (!Utilities.isEmpty(errorMessage)) {
            throw new InvalidActionTakenException(errorMessage);
        }

        ActionTakenValue actionTaken = saveActionTaken(findDelegatorForActionRequests(actionRequests));

        LOG.debug("Deactivate all pending action requests");
        getActionRequestService().deactivateRequests(actionTaken, actionRequests);
        notifyActionTaken(actionTaken);
    }
}