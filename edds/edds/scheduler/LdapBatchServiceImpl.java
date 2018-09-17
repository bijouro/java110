
package com.saerom.edds.edds.scheduler;

import com.saerom.edds.edds.task.LdapBatchTask;

/**
 * Scheduler Service Implementation
 *
 */
public class LdapBatchServiceImpl implements LdapBatchService {

	private LdapBatchTask  ldapBatchTask;
	
	/**
	 * Execute LdapSearchTask
	 * 
	 */
	public void executeLdapBatchTask() {
		getLdapBatchTask().execute();
	}
	
	/**
	 * Get LdapBatchTask
	 * 
	 * @return LdapBatchTask
	 */
	public LdapBatchTask getLdapBatchTask() {
		return ldapBatchTask;
	}

	/**
	 * Set LdapBatchTask 
	 * 
	 * @param  ldapBatchTask
	 */
	public void setLdapBatchTask(LdapBatchTask ldapBatchTask) {
		this.ldapBatchTask = ldapBatchTask;
	}
}
