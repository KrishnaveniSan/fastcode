package org.fastcode.util;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ISaveContext;
import org.eclipse.core.resources.ISaveParticipant;
import org.eclipse.core.resources.ISavedState;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class FastCodeSaveParticipant implements ISavedState, ISaveParticipant {

	public void doneSaving(final ISaveContext arg0) {
		// TODO Auto-generated method stub

	}

	public void prepareToSave(final ISaveContext arg0) throws CoreException {
		// TODO Auto-generated method stub

	}

	public void rollback(final ISaveContext arg0) {
		// TODO Auto-generated method stub

	}

	public void saving(final ISaveContext arg0) throws CoreException {
		// TODO Auto-generated method stub

	}

	public IPath[] getFiles() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSaveNumber() {
		// TODO Auto-generated method stub
		return 0;
	}

	public IPath lookup(final IPath arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public void processResourceChangeEvents(final IResourceChangeListener arg0) {
		// TODO Auto-generated method stub

	}



}
