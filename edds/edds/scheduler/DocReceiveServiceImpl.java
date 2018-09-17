
package com.saerom.edds.edds.scheduler;

import com.saerom.edds.edds.task.DocReceiveTask;

/**
 * Scheduler Service Implementation
 *
 */
public class DocReceiveServiceImpl implements DocReceiveService {

	private DocReceiveTask  docReceiveTask;
	
	/**
	 * Execute DocReceiveTask
	 * 
	 */
	public void executeDocReceiveTask() {
		getDocReceiveTask().execute();
	}
	
	/**
	 * Get DocReceiveTask
	 * 
	 * @return DocReceiveTask
	 */
	public DocReceiveTask getDocReceiveTask() {
		return docReceiveTask;
	}

	/**
	 * Set DocReceiveTask 
	 * 
	 * @param  DocReceiveTask
	 */
	public void setDocReceiveTask(DocReceiveTask docReceiveTask) {
		this.docReceiveTask = docReceiveTask;
	}
}
