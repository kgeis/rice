/*
 * Copyright 2007-2008 The Kuali Foundation
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
package org.kuali.rice.kns.bo;

import org.kuali.rice.core.api.mo.common.active.Inactivatable;

/**
 * Helper interface (for use by generics) when objects are both Defaultable and Inactivateable.
 * 
 * @see org.kuali.rice.kns.bo.Defaultable
 * @see org.kuali.rice.kns.bo.Inactivateable
 * @author Kuali Rice Team (rice.collab@kuali.org)
 *
 */
public interface DefaultableInactivateable extends org.kuali.rice.core.api.mo.common.Defaultable, Inactivatable {

}
