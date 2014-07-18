package org.fastcode.common.test;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;

public class MockAnnotation implements IAnnotation {

	private String	name;
	private String	type;

	public String getElementName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMemberValuePair[] getMemberValuePairs() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ISourceRange getNameRange() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getOccurrenceCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean exists() {
		// TODO Auto-generated method stub
		return false;
	}

	public IJavaElement getAncestor(final int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getAttachedJavadoc(final IProgressMonitor arg0) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IResource getCorrespondingResource() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public int getElementType() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getHandleIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJavaModel getJavaModel() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJavaProject getJavaProject() {
		// TODO Auto-generated method stub
		return null;
	}

	public IOpenable getOpenable() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJavaElement getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	public IPath getPath() {
		// TODO Auto-generated method stub
		return null;
	}

	public IJavaElement getPrimaryElement() {
		// TODO Auto-generated method stub
		return null;
	}

	public IResource getResource() {
		// TODO Auto-generated method stub
		return null;
	}

	public ISchedulingRule getSchedulingRule() {
		// TODO Auto-generated method stub
		return null;
	}

	public IResource getUnderlyingResource() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isStructureKnown() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public Object getAdapter(final Class adapter) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSource() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ISourceRange getSourceRange() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

}
