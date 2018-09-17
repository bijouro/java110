
package com.saerom.edds.edds.scheduler;

import com.saerom.edds.edds.task.DocSendTask;

/**
 * Scheduler Service Implementation
 *
 */
public class DocSendServiceImpl implements DocSendService {

	private DocSendTask  docSendTask;
	
	/**
	 * Execute DocSendTask
	 * 
	 */
	public void executeDocSendTask() {
		getDocSendTask().execute();
	}
	
	/**
	 * Get DocSendTask
	 * 
	 * @return docSendTask
	 */
	public DocSendTask getDocSendTask() {
		return docSendTask;
	}

	/**
	 * Set DocSendTask 
	 * 
	 * @param  docSendTask
	 */
	public void setDocSendTask(DocSendTask docSendTask) {
		this.docSendTask = docSendTask;
	}
}
