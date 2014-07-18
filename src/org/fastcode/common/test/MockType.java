package org.fastcode.common.test;

import java.io.InputStream;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.jobs.ISchedulingRule;
import org.eclipse.jdt.core.CompletionRequestor;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ICompletionRequestor;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IInitializer;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IOpenable;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.ITypeParameter;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.IWorkingCopy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.WorkingCopyOwner;

public class MockType implements IType {

	private final String	strField;
	private IField			field;
	private IMethod			method;

	public MockType(final String strField, final IField field) {
		super();
		this.strField = strField;
		this.field = field;
	}

	public MockType(final String strField, final IMethod method) {
		super();
		this.strField = strField;
		this.method = method;
	}

	public void codeComplete(final char[] arg0, final int arg1, final int arg2, final char[][] arg3, final char[][] arg4, final int[] arg5,
			final boolean arg6, final ICompletionRequestor arg7) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public void codeComplete(final char[] arg0, final int arg1, final int arg2, final char[][] arg3, final char[][] arg4, final int[] arg5,
			final boolean arg6, final CompletionRequestor arg7) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public void codeComplete(final char[] arg0, final int arg1, final int arg2, final char[][] arg3, final char[][] arg4, final int[] arg5,
			final boolean arg6, final ICompletionRequestor arg7, final WorkingCopyOwner arg8) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public void codeComplete(final char[] arg0, final int arg1, final int arg2, final char[][] arg3, final char[][] arg4, final int[] arg5,
			final boolean arg6, final CompletionRequestor arg7, final IProgressMonitor arg8) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public void codeComplete(final char[] arg0, final int arg1, final int arg2, final char[][] arg3, final char[][] arg4, final int[] arg5,
			final boolean arg6, final CompletionRequestor arg7, final WorkingCopyOwner arg8) throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public void codeComplete(final char[] arg0, final int arg1, final int arg2, final char[][] arg3, final char[][] arg4, final int[] arg5,
			final boolean arg6, final CompletionRequestor arg7, final WorkingCopyOwner arg8, final IProgressMonitor arg9)
			throws JavaModelException {
		// TODO Auto-generated method stub

	}

	public IField createField(final String arg0, final IJavaElement arg1, final boolean arg2, final IProgressMonitor arg3)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return this.field;
	}

	public IInitializer createInitializer(final String arg0, final IJavaElement arg1, final IProgressMonitor arg2)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMethod createMethod(final String arg0, final IJavaElement arg1, final boolean arg2, final IProgressMonitor arg3)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IType createType(final String arg0, final IJavaElement arg1, final boolean arg2, final IProgressMonitor arg3)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IMethod[] findMethods(final IMethod arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public IJavaElement[] getChildrenForCategory(final String arg0) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getElementName() {
		// TODO Auto-generated method stub
		return null;
	}

	public IField getField(final String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public IField[] getFields() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFullyQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFullyQualifiedName(final char arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFullyQualifiedParameterizedName() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IInitializer getInitializer(final int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public IInitializer[] getInitializers() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getKey() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMethod getMethod(final String arg0, final String[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	public IMethod[] getMethods() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IPackageFragment getPackageFragment() {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getSuperInterfaceNames() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getSuperInterfaceTypeSignatures() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSuperclassName() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getSuperclassTypeSignature() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public IType getType(final String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeParameter getTypeParameter(final String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getTypeParameterSignatures() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeParameter[] getTypeParameters() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeQualifiedName() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTypeQualifiedName(final char arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public IType[] getTypes() throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isAnnotation() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isAnonymous() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isClass() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isEnum() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isInterface() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isLocal() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isMember() throws JavaModelException {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isResolved() {
		// TODO Auto-generated method stub
		return false;
	}

	public ITypeHierarchy loadTypeHierachy(final InputStream arg0, final IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newSupertypeHierarchy(final IProgressMonitor arg0) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newSupertypeHierarchy(final ICompilationUnit[] arg0, final IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newSupertypeHierarchy(final IWorkingCopy[] arg0, final IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newSupertypeHierarchy(final WorkingCopyOwner arg0, final IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newTypeHierarchy(final IProgressMonitor arg0) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newTypeHierarchy(final IJavaProject arg0, final IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newTypeHierarchy(final ICompilationUnit[] arg0, final IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newTypeHierarchy(final IWorkingCopy[] arg0, final IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newTypeHierarchy(final WorkingCopyOwner arg0, final IProgressMonitor arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public ITypeHierarchy newTypeHierarchy(final IJavaProject arg0, final WorkingCopyOwner arg1, final IProgressMonitor arg2)
			throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public String[][] resolveType(final String arg0) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
	}

	public String[][] resolveType(final String arg0, final WorkingCopyOwner arg1) throws JavaModelException {
		// TODO Auto-generated method stub
		return null;
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
		return null;
	}

	/**
	 *
	 * getter method for strField
	 * @return
	 *
	 */
	public String getStrField() {
		return this.strField;
	}

}
