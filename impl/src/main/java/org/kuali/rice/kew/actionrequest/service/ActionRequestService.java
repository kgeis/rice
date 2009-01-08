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
package org.kuali.rice.kew.actionrequest.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.kuali.rice.kew.actionrequest.ActionRequestValue;
import org.kuali.rice.kew.actiontaken.ActionTakenValue;
import org.kuali.rice.kew.engine.ActivationContext;
import org.kuali.rice.kew.engine.node.RouteNodeInstance;
import org.kuali.rice.kew.exception.KEWUserNotFoundException;
import org.kuali.rice.kew.routeheader.DocumentRouteHeaderValue;
import org.kuali.rice.kew.user.Recipient;
import org.kuali.rice.kew.user.WorkflowUser;


/**
 * Service to handle the building, sorting, saving, activating and deactivating of action request graphs.  These lists are 
 * what determine role and delegation behaviors in graphs of action requests.  
 * 
 * Fetching that is being done is also taking into account the 'weight' of action request codes.
 * 
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 */
public interface ActionRequestService {
	public ActionRequestValue initializeActionRequestGraph(ActionRequestValue actionRequest, DocumentRouteHeaderValue document, RouteNodeInstance nodeInstance);
	
    public void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest);
    
    public void deactivateRequests(ActionTakenValue actionTaken, List actionRequests);
    
    public void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest, boolean simulate);

    public void deactivateRequest(ActionTakenValue actionTaken, ActionRequestValue actionRequest, ActivationContext activationContext);

    public void deactivateRequests(ActionTakenValue actionTaken, List actionRequests, boolean simulate);

    public void deactivateRequests(ActionTakenValue actionTaken, List actionRequests, ActivationContext activationContext);

    public void deleteActionRequestGraph(ActionRequestValue actionRequest);
    
    public List findAllValidRequests(String principalId, Long routeHeaderId, String requestCode) throws KEWUserNotFoundException;
    
    public List findAllValidRequests(String principalId, Collection actionRequests, String requestCode) throws KEWUserNotFoundException;

    public List findPendingByDoc(Long routeHeaderId);

    public void saveActionRequest(ActionRequestValue actionRequest) throws KEWUserNotFoundException;

    public void activateRequest(ActionRequestValue actionRequest) throws KEWUserNotFoundException;
    
    public void activateRequest(ActionRequestValue actionRequest, boolean simulate) throws KEWUserNotFoundException;

    public void activateRequest(ActionRequestValue actionRequest, ActivationContext activationContext) throws KEWUserNotFoundException;

    public void activateRequests(Collection actionRequests) throws KEWUserNotFoundException;
    
    public void activateRequests(Collection actionRequests, boolean simulate) throws KEWUserNotFoundException;

	public void activateRequests(Collection actionRequests, ActivationContext activationContext) throws KEWUserNotFoundException;

    public List activateRequestNoNotification(ActionRequestValue actionRequest, boolean simulate) throws KEWUserNotFoundException;

    public List activateRequestNoNotification(ActionRequestValue actionRequest, ActivationContext activationContext) throws KEWUserNotFoundException;

    public ActionRequestValue findByActionRequestId(Long actionRequestId);

    public List findPendingRootRequestsByDocId(Long routeHeaderId);
    
    public List findPendingRootRequestsByDocIdAtRouteLevel(Long routeHeaderId, Integer routeLevel);
    
    public List findPendingByDocIdAtOrBelowRouteLevel(Long routeHeaderId, Integer routeLevel);
    
    public List findPendingRootRequestsByDocIdAtOrBelowRouteLevel(Long routeHeaderId, Integer routeLevel);
    
    public List findPendingRootRequestsByDocumentType(Long documentTypeId);

    public List findAllActionRequestsByRouteHeaderId(Long routeHeaderId);

    public List findPendingByActionRequestedAndDocId(String actionRequestedCdCd, Long routeHeaderId);
    
    /**
     * 
     * This method gets a list of ids of all principals who have a pending action request for a document.
     * 
     * @param actionRequestedCd
     * @param routeHeaderId
     * @return
     */
    public List<String> getPrincipalIdsWithPendingActionRequestByActionRequestedAndDocId(String actionRequestedCd, Long routeHeaderId);
    
    public List findByStatusAndDocId(String statusCd, Long routeHeaderId);

    public void alterActionRequested(List actionRequests, String actionRequestCd)throws KEWUserNotFoundException;

    public List findByRouteHeaderIdIgnoreCurrentInd(Long routeHeaderId);
    
    public List findActivatedByGroup(String groupId);
    
    public void updateActionRequestsForResponsibilityChange(Set responsibilityIds);
    
    public ActionRequestValue getRoot(ActionRequestValue actionRequest);
    
    public List getRootRequests(Collection actionRequests);
    
    public boolean isDuplicateRequest(ActionRequestValue actionRequest);
 
    public List findPendingByDocRequestCdRouteLevel(Long routeHeaderId, String requestCode, Integer routeLevel);
    
    public List findPendingByDocRequestCdNodeName(Long routeHeaderId, String requestCode, String nodeName);
        
    /**
     * Returns the highest priority delegator in the list of action requests.
     */
    public Recipient findDelegator(List actionRequests)  throws KEWUserNotFoundException;
    
    /**
     * Returns the closest delegator for the given ActionRequest
     */
    public Recipient findDelegator(ActionRequestValue actionRequest) throws KEWUserNotFoundException;
    
    public ActionRequestValue findDelegatorRequest(ActionRequestValue actionRequest);
    
    public void deleteByRouteHeaderId(Long routeHeaderId);
    
    public void deleteByActionRequestId(Long actionRequestId);
    
    public void validateActionRequest(ActionRequestValue actionRequest);
    
    public List<ActionRequestValue> findPendingRootRequestsByDocIdAtRouteNode(Long routeHeaderId, Long nodeInstanceId);
    
    public List findRootRequestsByDocIdAtRouteNode(Long documentId, Long nodeInstanceId);
    
    public List getDelegateRequests(ActionRequestValue actionRequest);
    
    /**
     * If this is a role request, then this method returns a List of the action request for each recipient within the
     * role.  Otherwise, it will return a List with just the original action request.
     */
    public List getTopLevelRequests(ActionRequestValue actionRequest);
    
    public boolean isValidActionRequestCode(String actionRequestCode);
    
    /**
     * Checks if the given user has any Action Requests on the given document.
     */
    public boolean doesUserHaveRequest(WorkflowUser user, Long documentId);

}
