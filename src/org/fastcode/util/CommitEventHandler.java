package org.fastcode.util;

import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNProperty;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;

public class CommitEventHandler implements ISVNEventHandler {

	@Override
	public void handleEvent(final SVNEvent event, final double progress) {
		final SVNEventAction action = event.getAction();
		if (action == SVNEventAction.COMMIT_MODIFIED) {
		} else if (action == SVNEventAction.COMMIT_DELETED) {
		} else if (action == SVNEventAction.COMMIT_REPLACED) {
		} else if (action == SVNEventAction.COMMIT_DELTA_SENT) {
		} else if (action == SVNEventAction.COMMIT_ADDED) {
			/*
			 * Gets the MIME-type of the item.
			 */
			final String mimeType = event.getMimeType();
			if (SVNProperty.isBinaryMimeType(mimeType)) {
				/*
				 * If the item is a binary file
				 */
			} else {
			}
		} else if (action == SVNEventAction.COMMIT_COMPLETED) {
		}

	}

	@Override
	public void checkCancelled() throws SVNCancelException {
	}

}
