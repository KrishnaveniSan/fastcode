/**
 * This class has been generated by Fast Code Eclipse Plugin
 * For more information please go to http://fast-code.sourceforge.net/
 * @author : Biswarup
 * Created : 09/08/2014 03:33:31
 */

package org.fastcode.handler;

import static org.fastcode.common.FastCodeConstants.COLON;
import static org.fastcode.common.FastCodeConstants.COMMA;
import static org.fastcode.common.FastCodeConstants.DOT;
import static org.fastcode.common.FastCodeConstants.EMPTY_STR;
import static org.fastcode.common.FastCodeConstants.HASH;
import static org.fastcode.common.FastCodeConstants.OPTIONAL;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_CLASS;
import static org.fastcode.common.FastCodeConstants.SPACE;
import static org.fastcode.common.FastCodeConstants.TEMPLATE_TAG_PREFIX;
import static org.fastcode.common.FastCodeConstants.XML_START;
import static org.fastcode.util.ImportUtil.createImport;
import static org.fastcode.util.ImportUtil.createImportOfMethodAndField;
import static org.fastcode.util.SourceUtil.isNativeType;
import static org.fastcode.util.StringUtil.isEmpty;
import static org.fastcode.util.StringUtil.parseType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.fastcode.common.Action;
import org.fastcode.common.FastCodeConstants.ACTION_ENTITY;
import org.fastcode.common.FastCodeConstants.ACTION_TYPE;
import org.fastcode.common.FastCodeConstants.TemplateTag;
import org.fastcode.common.FastCodeType;
import org.fastcode.setting.GlobalSettings;

public class FCImportTagHandler implements FCTagHandler {

	/* (non-Javadoc)
	 * @see org.fastcode.handler.FCTagHandler#populateAction(org.fastcode.common.FastCodeConstants.TemplateTag, java.lang.String, java.lang.String, org.eclipse.jdt.core.ICompilationUnit, boolean, java.util.Map, java.util.Map, java.lang.String, java.util.Map, java.util.List, java.lang.StringBuilder, java.util.List)
	 */
	@Override
	public Action populateTagAction(final TemplateTag tagFound, final String tagBody, final String insideTagBody,
			final ICompilationUnit compUnit, final boolean hasSubAction1, final Map<String, Object> placeHolders,
			final Map<String, Object> contextMap, final String spacesBeforeCursor, final Map<String, String> attributes,
			final StringBuilder existingMembersBuilder, final List<Action> actionList) throws Exception {
		boolean optional = false;
		final ACTION_TYPE actionType = ACTION_TYPE.Import;

		/*
		 * if (compUnit.getJavaProject().getProject().isSynchronized(0)) {
		 * throw new Exception("Project: " +
		 * compUnit.getJavaProject().getProject().getName() +
		 * " is not synchronized ,Please refresh and try again."); }
		 */
		if (compUnit == null) {
			throw new Exception("There is no Java class open in the editor..Import cannot be done in a non Java file....");
		}
		if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
			throw new Exception("There should not be any other tags inside <fc:import>,  exiting....");
		}
		IType type = null;
		optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
		String classesToimprt = attributes.containsKey(PLACEHOLDER_CLASS) ? attributes.get(PLACEHOLDER_CLASS) : null;
		String lblMgs = EMPTY_STR;
		if (!isEmpty(insideTagBody)) {
			final String classToImport = insideTagBody;
			type = validateClassToImport(insideTagBody, classToImport, compUnit);
			if (type == null) {
				return null;
			}
			lblMgs = !isEmpty(insideTagBody) && insideTagBody.contains(HASH) ? classToImport : type.getFullyQualifiedName();
		} else {
			if (!isEmpty(classesToimprt)) {
				final List<String> classToImportList = new ArrayList<String>();
				for (final String clasToImport : classesToimprt.trim().split(COMMA)) {
					if (!isEmpty(clasToImport)) {
						classToImportList.add(clasToImport.trim());
					}
				}
				//final FCImportTagHandler fcTagHandler = new FCImportTagHandler();
				for (final String clasToImport : classesToimprt.trim().split(COMMA)) {
					if (!isEmpty(clasToImport)) {
						final IType type2 = validateClassToImport(clasToImport, clasToImport.trim(), compUnit);
						if (type2 == null) {
							classToImportList.remove(clasToImport.trim());
						}
					}
				}
				String importNames = EMPTY_STR;
				if (!classToImportList.isEmpty()) {
					for (final String importName : classToImportList) {
						importNames = importNames + importName + SPACE + COMMA + SPACE;

					}
					classesToimprt = importNames;
				}
				lblMgs = classesToimprt.substring(0, classesToimprt.lastIndexOf(COMMA));
			}

		}
		final Action actionImport = new Action.Builder().withEntity(ACTION_ENTITY.Import).withType(actionType)
				.withSource(isEmpty(insideTagBody) ? classesToimprt : insideTagBody.trim()).withLabelMsg("Import   " + lblMgs)
				.withOptional(optional).build();
		/*if (hasSubAction1 && actionImport != null) {
			final TemplateTagsProcessor templateTagsProcessor = new TemplateTagsProcessor();
			templateTagsProcessor.getSubActions().add(actionImport);
			actionImport.setTarget(compUnit);
		}*/
		return actionImport;
	}

	/**
	 * @param insideTagBody
	 * @param classToImport
	 * @param compUnit
	 * @return
	 * @throws Exception
	 */
	public IType validateClassToImport(final String insideTagBody, String classToImport, final ICompilationUnit compUnit) throws Exception {
		IType type = null;
		if (isNativeType(classToImport)) {
			return null;
		}
		final String fileExtension = compUnit.getResource().getFileExtension();
		if (insideTagBody.contains(HASH)) {
			final String part1 = insideTagBody.substring(0, insideTagBody.indexOf(HASH)).trim();
			final String part2 = insideTagBody.substring(insideTagBody.indexOf(HASH) + 1, insideTagBody.length()).trim();
			classToImport = part1 + DOT + part2;
			type = compUnit.getJavaProject().findType(part1);
			if (doesImportExistInType(classToImport, compUnit)) {
				return null;
			}
		} else {

			final FastCodeType fastCodeType = parseType(classToImport, compUnit);
			type = compUnit.getJavaProject().findType(fastCodeType.getFullyQualifiedName());
			if (type == null || !type.exists()) {
				//throw new Exception("Java class " + fastCodeType.getFullyQualifiedName() + " does not exit");
				MessageDialog.openError(new Shell(), "Error", "Java class " + fastCodeType.getFullyQualifiedName() + " does not exit");
				return null;
			}
			for (final FastCodeType codeType : fastCodeType.getParameters()) {
				if (codeType.getParameters().size() > 0) {
					type = compUnit.getJavaProject().findType(codeType.getFullyQualifiedName());
					if (type == null || !type.exists()) {
						//throw new Exception("Java class " + codeType.getFullyQualifiedName() + " does not exit");
						MessageDialog.openError(new Shell(), "Error", "Java class " + codeType.getFullyQualifiedName() + " does not exit");
						return null;
					}
					if (isImportFromDefaultPkg(type, fileExtension)) {
						return null;
					}
					if (doesImportExistInType(type.getFullyQualifiedName(), compUnit)) {
						return null;
					}
					if (type.equals(compUnit.findPrimaryType())) {
						return null;
					}
					for (final FastCodeType type2 : codeType.getParameters()) {
						type = compUnit.getJavaProject().findType(type2.getFullyQualifiedName());
						if (type == null || !type.exists()) {
							//throw new Exception("Java class " + type2.getFullyQualifiedName() + " does not exit");
							MessageDialog.openError(new Shell(), "Error", "Java class " + type2.getFullyQualifiedName() + " does not exit");
							return null;
						}
						if (isImportFromDefaultPkg(type, fileExtension)) {
							return null;
						}
						if (doesImportExistInType(type.getFullyQualifiedName(), compUnit)) {
							return null;
						}
						if (type.equals(compUnit.findPrimaryType())) {
							return null;
						}
					}
				}
				if (type == null || !type.exists()) {
					//throw new Exception("Java class " + classToImport + " does not exit");
					MessageDialog.openError(new Shell(), "Error", "Java class " + classToImport + " does not exit");
					return null;
				}
				if (isImportFromDefaultPkg(type, fileExtension)) {
					return null;
				}
				if (doesImportExistInType(type.getFullyQualifiedName(), compUnit)) {
					return null;
				}
				if (type.equals(compUnit.findPrimaryType())) {
					return null;
				}
			}
			if (doesImportExistInType(type.getFullyQualifiedName(), compUnit)) {
				return null;
			}
		}
		if (type == null || !type.exists()) {
			//throw new Exception("Java class " + classToImport + " does not exit");
			MessageDialog.openError(new Shell(), "Error", "Java class " + classToImport + " does not exit");
			return null;
		}
		if (isImportFromDefaultPkg(type, fileExtension)) {
			return null;
		}
		if (type.equals(compUnit.findPrimaryType())) {
			return null;
		}
		return type;
	}

	/**
	 * @param classToImport
	 * @param compUnit
	 * @return
	 */
	private boolean doesImportExistInType(final String classToImport, final ICompilationUnit compUnit) {
		final IImportDeclaration importToCreate = compUnit.getImport(classToImport);
		if (importToCreate != null && importToCreate.exists()) {
			return true;
		}
		return false;

	}

	/**
	 * @param type
	 * @param fileExtension
	 * @return
	 */
	private boolean isImportFromDefaultPkg(final IType type, final String fileExtension) {
		final String pkg = type.getPackageFragment().getElementName();
		final String defaultPackageProperty = "DEFAULT_PACKAGES_" + fileExtension.toUpperCase();
		final GlobalSettings globalSettings = GlobalSettings.getInstance();
		final String defaultPackages[] = globalSettings.getPropertyValue(defaultPackageProperty, EMPTY_STR).split(COMMA);
		for (final String packge : defaultPackages) {
			if (packge.equals(pkg)) {
				return true;
			}
		}
		return false;

	}

	/**
	 * @param classToImport
	 * @param compUnit
	 * @throws Exception
	 */
	public void createImportFromTag(final String classToImport, final ICompilationUnit compUnit) throws Exception {

		for (String clasToImprt : classToImport.trim().split(COMMA)) {
			if (!isEmpty(clasToImprt)) {
				clasToImprt = clasToImprt.trim();
				if (clasToImprt.contains(HASH)) {
					createImportOfMethodAndField(clasToImprt, compUnit);
					return;
				}
				if (isNativeType(clasToImprt)) {
					return;
				}

				try {
					final FastCodeType fastCodeType = parseType(clasToImprt, compUnit);
					IType type = compUnit.getJavaProject().findType(fastCodeType.getFullyQualifiedName());
					String fileExtension = compUnit.getResource().getFileExtension();
					IImportDeclaration importDeclaration = createImport(compUnit, type, fileExtension);
					for (final FastCodeType codeType : fastCodeType.getParameters()) {
						if (codeType.getParameters().size() > 0) {
							type = compUnit.getJavaProject().findType(codeType.getFullyQualifiedName());
							importDeclaration = createImport(compUnit, type, fileExtension);
							for (final FastCodeType type2 : codeType.getParameters()) {
								type = compUnit.getJavaProject().findType(type2.getFullyQualifiedName());
								fileExtension = compUnit.getResource().getFileExtension();
								importDeclaration = createImport(compUnit, type, fileExtension);
							}
						} else {
							type = compUnit.getJavaProject().findType(codeType.getFullyQualifiedName());
							fileExtension = compUnit.getResource().getFileExtension();
							importDeclaration = createImport(compUnit, type, fileExtension);
						}
					}

				} catch (final Exception ex) {
					MessageDialog.openError(new Shell(), "Error", "Unable to Create Import " + clasToImprt + "for " + ex.getMessage());
					ex.printStackTrace();
				}
			}
		}
	}
}