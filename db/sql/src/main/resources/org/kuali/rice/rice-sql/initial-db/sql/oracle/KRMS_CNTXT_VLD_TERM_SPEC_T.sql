--
-- Copyright 2005-2013 The Kuali Foundation
--
-- Licensed under the Educational Community License, Version 2.0 (the "License");
-- you may not use this file except in compliance with the License.
-- You may obtain a copy of the License at
--
-- http://www.opensource.org/licenses/ecl2.php
--
-- Unless required by applicable law or agreed to in writing, software
-- distributed under the License is distributed on an "AS IS" BASIS,
-- WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
-- See the License for the specific language governing permissions and
-- limitations under the License.
--

TRUNCATE TABLE KRMS_CNTXT_VLD_TERM_SPEC_T DROP STORAGE
/
INSERT INTO KRMS_CNTXT_VLD_TERM_SPEC_T (CNTXT_ID,CNTXT_TERM_SPEC_PREREQ_ID,PREREQ,TERM_SPEC_ID)
  VALUES ('CONTEXT1','T1000','N','T1002')
/
INSERT INTO KRMS_CNTXT_VLD_TERM_SPEC_T (CNTXT_ID,CNTXT_TERM_SPEC_PREREQ_ID,PREREQ,TERM_SPEC_ID)
  VALUES ('CONTEXT1','T1001','N','T1003')
/
INSERT INTO KRMS_CNTXT_VLD_TERM_SPEC_T (CNTXT_ID,CNTXT_TERM_SPEC_PREREQ_ID,PREREQ,TERM_SPEC_ID)
  VALUES ('CONTEXT1','T1002','N','T1004')
/
INSERT INTO KRMS_CNTXT_VLD_TERM_SPEC_T (CNTXT_ID,CNTXT_TERM_SPEC_PREREQ_ID,PREREQ,TERM_SPEC_ID)
  VALUES ('CONTEXT1','T1003','N','T1005')
/
INSERT INTO KRMS_CNTXT_VLD_TERM_SPEC_T (CNTXT_ID,CNTXT_TERM_SPEC_PREREQ_ID,PREREQ,TERM_SPEC_ID)
  VALUES ('CONTEXT1','T1004','N','T1006')
/
INSERT INTO KRMS_CNTXT_VLD_TERM_SPEC_T (CNTXT_ID,CNTXT_TERM_SPEC_PREREQ_ID,PREREQ,TERM_SPEC_ID)
  VALUES ('CONTEXT1','T1005','N','T1007')
/
INSERT INTO KRMS_CNTXT_VLD_TERM_SPEC_T (CNTXT_ID,CNTXT_TERM_SPEC_PREREQ_ID,PREREQ,TERM_SPEC_ID)
  VALUES ('CONTEXT1','T1006','N','T1000')
/
INSERT INTO KRMS_CNTXT_VLD_TERM_SPEC_T (CNTXT_ID,CNTXT_TERM_SPEC_PREREQ_ID,PREREQ,TERM_SPEC_ID)
  VALUES ('CONTEXT1','T1007','N','T1001')
/