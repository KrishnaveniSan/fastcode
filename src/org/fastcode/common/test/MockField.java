package org.fastcode.common.test;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;

public class MockField implements IField {

	private final String	name;
	private final String	type;
	private final String	signatureType;

	public MockField(final String name, final String type, final String signatureType) {
		super();
		this.name = name;
		this.type = type;
		this.signatureType = signatureType;
	}

	public Object getConstant() throws JavaModelException {
		// TODO Auto-generated method stub

		return null;
	}

	public String getElementName() {

		return this.name;
	}

	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeSignature() throws JavaModelException {
		return this.signatureType;
	}

	public boolean isEnumConstant() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isResolved() {
		// TODO Auto-generated method stub
		return false;
	}

	public String[] getCategories() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IClassFile getClassFile() {
		// TODO Auto-generated method stub
		return null;
	}

	public ICompilationUnit getCompilationUnit() {
		// TODO Auto-generated method stub
		return null;
	}

	public IType getDeclaringType() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getFlags() throws JavaModelException {
		// TODO Auto-generated method stub
		return 0;
	}

	public ISourceRange getJavadocRange() throws JavaModelException {
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

	public IType getType(final String arg0, final int arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeRoot getTypeRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isBinary() {
		// TODO Auto-generated method stub
		return false;
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

	public void copy(final IJavaElement arg0, final IJavaElement arg1, final String arg2, final boolean arg3, final IProgressMonitor arg4)
			throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public void delete(final boolean arg0, final IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public void move(final IJavaElement arg0, final IJavaElement arg1, final String arg2, final boolean arg3, final IProgressMonitor arg4)
			throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public void rename(final String arg0, final boolean arg1, final IProgressMonitor arg2) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public IJavaElement[] getChildren() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public IAnnotation getAnnotation(final String arg0) {
		// TODO Auto-generated method stub

		return null;
	}

	public IAnnotation[] getAnnotations() throws JavaModelException {
		// TODO Auto-generated method stub
		return new IAnnotation[] {};

	}

	/**
	 *
	 * getter method for name
	 * @return
	 *
	 */
	public String getName() {
		return this.name;
	}

	/**
	 *
	 * getter method for type
	 * @return
	 *
	 */
	public String getType() {
		return this.type;
	}

	/**
	 *
	 * getter method for signatureType
	 * @return
	 *
	 */
	public String getSignatureType() {
		return this.signatureType;
	}

}
