/*
 * Copyright 2007 The Kuali Foundation
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
package org.kuali.rice.kim.service;

import java.util.Set;

import org.kuali.rice.kim.bo.role.impl.RoleMemberImpl;

/**
 * This is an internal serverice that was created as a proxy for kew
 * updates
 *
 * @author Kuali Rice Team (kuali-rice@googlegroups.com)
 *
 */
public interface ResponsibilityInternalService {

	void updateActionRequestsForResponsibilityChange(Set<String> responsibilityIds);
	void saveRoleMember(RoleMemberImpl roleMember);
	void removeRoleMember(RoleMemberImpl roleMember);
	void updateActionRequestsForRoleChange(String roleId);
	
}
