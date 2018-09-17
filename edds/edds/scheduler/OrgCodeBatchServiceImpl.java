
package com.saerom.edds.edds.scheduler;

import com.saerom.edds.edds.task.OrgCodeBatchTask;

/**
 * Scheduler Service Implementation
 *
 */
public class OrgCodeBatchServiceImpl implements OrgCodeBatchService {

	private OrgCodeBatchTask  orgCodeBatchTask;
	
	/**
	 * Execute LdapSearchTask
	 * 
	 */
	public void executeOrgCodeBatchTask() {
		getOrgCodeBatchTask().execute();
	}
	
	/**
	 * Get OrgCodeBatchTask
	 * 
	 * @return OrgCodeBatchTask
	 */
	public OrgCodeBatchTask getOrgCodeBatchTask() {
		return orgCodeBatchTask;
	}

	/**
	 * Set OrgCodeBatchTask 
	 * 
	 * @param  orgCodeBatchTask
	 */
	public void setOrgBatchTask(OrgCodeBatchTask orgCodeBatchTask) {
		this.orgCodeBatchTask = orgCodeBatchTask;
	}
}
