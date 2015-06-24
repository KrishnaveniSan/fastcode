/**
 * This class has been generated by Fast Code Eclipse Plugin
 * For more information please go to http://fast-code.sourceforge.net/
 * @author : Biswarup
 * Created : 09/08/2014 04:13:34
 */
package org.fastcode.handler;

import static org.fastcode.common.FastCodeConstants.COLON;
import static org.fastcode.common.FastCodeConstants.COMMA;
import static org.fastcode.common.FastCodeConstants.DOT;
import static org.fastcode.common.FastCodeConstants.EMPTY_STR;
import static org.fastcode.common.FastCodeConstants.FC_OBJ_CREATED;
import static org.fastcode.common.FastCodeConstants.FILE_SEPARATOR;
import static org.fastcode.common.FastCodeConstants.OPTIONAL;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_NAME;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_PACKAGE;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_PROJECT;
import static org.fastcode.common.FastCodeConstants.SPACE;
import static org.fastcode.common.FastCodeConstants.TEMPLATE_TAG_PREFIX;
import static org.fastcode.common.FastCodeConstants.TYPE;
import static org.fastcode.common.FastCodeConstants.XML_START;
import static org.fastcode.util.SourceUtil.getDefaultPathFromProject;
import static org.fastcode.util.SourceUtil.getIFileFromFile;
import static org.fastcode.util.SourceUtil.getJavaProject;
import static org.fastcode.util.SourceUtil.getWorkingJavaProjectFromUser;
import static org.fastcode.util.StringUtil.isEmpty;
import static org.fastcode.util.StringUtil.isJavaReservedWord;
import static org.fastcode.util.StringUtil.isValidPackageName;
import static org.fastcode.util.VersionControlUtil.addOrUpdateFileStatusInCache;
import static org.fastcode.util.VersionControlUtil.isPrjConfigured;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.fastcode.common.Action;
import org.fastcode.common.FastCodeConstants.ACTION_ENTITY;
import org.fastcode.common.FastCodeConstants.ACTION_TYPE;
import org.fastcode.common.FastCodeConstants.TemplateTag;
import org.fastcode.common.FastCodeEntityHolder;
import org.fastcode.common.FastCodePackage;
import org.fastcode.common.FastCodeProject;
import org.fastcode.exception.FastCodeRepositoryException;
import org.fastcode.popup.actions.snippet.TemplateTagsProcessor;
import org.fastcode.preferences.VersionControlPreferences;

public class FCPackageTagHandler implements FCTagHandler {

	@Override
	public Action populateTagAction(final TemplateTag tagFound, final String tagBody, final String insideTagBody,
			final ICompilationUnit compUnit, final boolean hasSubAction1, final Map<String, Object> placeHolders,
			final Map<String, Object> contextMap, final String spacesBeforeCursor, final Map<String, String> attributes,
			final StringBuilder existingMembersBuilder, final List<Action> actionList) throws Exception {
		String memberName = null;
		boolean optional = false;
		final ACTION_TYPE actionType = ACTION_TYPE.Create;
		String typeToCreate = null;
		memberName = attributes.containsKey(PLACEHOLDER_NAME) ? attributes.get(PLACEHOLDER_NAME) : null;
		typeToCreate = attributes.containsKey(TYPE) ? attributes.get(TYPE) : "source";//it represents what kind of class will be created in that package...so that we can take the appropriate source path
		optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;

		if (memberName == null || memberName.equals(EMPTY_STR)) {
			throw new Exception("Please provide attribute \"name\" for <fc:package> tag  in the XML and try again");
		} else if (!isValidPackageName(memberName)) {
			throw new Exception(
					"Attribute \"name\" contains  invalid package name ,Please provide correct one  for <fc:package> tag  in the XML and try again");
		}
		for (final String pkg1 : memberName.split("\\.")) {
			if (isJavaReservedWord(pkg1)) {
				throw new Exception(
						"Name attribute contains Java Reserved word,Please provide attribute \"name\" correct one for  <fc:package> tag  in the XML and try again");
			}
		}
		if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
			throw new Exception("There should not be any other tags inside <fc:package>,  exiting....");
		}

		IJavaProject javaProject1 = getJavaProject(placeHolders.get(PLACEHOLDER_PROJECT) instanceof FastCodeProject ? ((FastCodeProject) placeHolders
				.get(PLACEHOLDER_PROJECT)).getName() : (String) placeHolders.get(PLACEHOLDER_PROJECT));

		if (javaProject1 == null) {
			javaProject1 = getWorkingJavaProjectFromUser();
		}
		if (javaProject1 == null) {
			throw new Exception("Can not continue without java project.");
		}
		final String defaultPath1 = typeToCreate.equals(ACTION_ENTITY.Test.getValue()) ? getDefaultPathFromProject(javaProject1,
				typeToCreate, EMPTY_STR) : getDefaultPathFromProject(javaProject1, "source", EMPTY_STR);
		final TemplateTagsProcessor templateTagsProcessor = new TemplateTagsProcessor();
		final IPackageFragment pkgFrgmt1 = templateTagsProcessor.getPackageFragment(javaProject1, defaultPath1, memberName,
				typeToCreate.equals(ACTION_ENTITY.Test.getValue()) ? typeToCreate : "source");
		/*for (final IPackageFragment packageFragment : getPackagesInProject(javaProject, defaultPath)) {
			if (packageFragment.getElementName().equals(memberName)) {
				pkgFrgmt = packageFragment;
				break;
			}
		}*/
		if (pkgFrgmt1 != null) {
			existingMembersBuilder.append("Package with Name: " + memberName);
			existingMembersBuilder.append(SPACE + COMMA + SPACE);
			return null;

		}
		final Action actionPackage = new Action.Builder().withEntity(ACTION_ENTITY.Package).withType(actionType).withEntityName(memberName)
				.withTypeToCreate(typeToCreate).withProject(javaProject1)
				.withLabelMsg(actionType.toString() + SPACE + "Package: " + memberName).withOptional(optional).build();
		return actionPackage;
		//break;
	}

	/**
	 * @param entityName
	 * @param typeToCreate
	 * @param project
	 * @param contextMap
	 * @param placeHolders
	 * @throws Exception
	 */
	public void createPackageFromTag(final String entityName, final String typeToCreate, final Object project,
			final Map<String, Object> contextMap, final Map<String, Object> placeHolders) throws Exception {
		final IJavaProject javaProject = (IJavaProject) project;
		final VersionControlPreferences versionControlPreferences = VersionControlPreferences.getInstance();
		/*if (javaProject == null) {
			javaProject = getWorkingJavaProjectFromUser();
		}
		if (javaProject == null) {
			throw new Exception("Can not continue without project.");
		}*/
		String srcPath = null;
		if (typeToCreate.equals(ACTION_ENTITY.Test.getValue())) {
			srcPath = getDefaultPathFromProject(javaProject, typeToCreate, EMPTY_STR);
		} else {
			srcPath = getDefaultPathFromProject(javaProject, "source", EMPTY_STR);
		}
		IPackageFragment pkgFrgmt = null;
		/*for (final IPackageFragment packageFragment : getPackagesInProject(javaProject, srcPath)) {
			if (packageFragment.getElementName().equals(entityName)) {
				pkgFrgmt = packageFragment;
				break;
			}
		}*/

		boolean prjShared = false;
		boolean prjConfigured = false;
		File file = null;
		try {
			prjShared = !isEmpty(javaProject.getProject().getPersistentProperties());
			prjConfigured = !isEmpty(isPrjConfigured(javaProject.getProject().getName()));
			file = null;
			if (versionControlPreferences.isEnable() && prjShared && prjConfigured) {
				final String prjURI = javaProject.getResource().getLocationURI().toString();
				final String path = prjURI.substring(prjURI.indexOf(COLON) + 1);
				final String newPackURL = path + srcPath + FILE_SEPARATOR + entityName.replace(DOT, FILE_SEPARATOR);
				file = new File(newPackURL);
				//final FastCodeCheckinCache checkinCache = FastCodeCheckinCache.getInstance();
				addOrUpdateFileStatusInCache(file);
				//checkinCache.getFilesToCheckIn().add(new FastCodeFileForCheckin(INITIATED, file.getAbsolutePath()));
			}
		} catch (final FastCodeRepositoryException ex) {
			ex.printStackTrace();
		}
		final TemplateTagsProcessor templateTagsProcessor = new TemplateTagsProcessor();
		pkgFrgmt = templateTagsProcessor.createPackage(javaProject, entityName, typeToCreate, contextMap);
		/*if (pkgFrgmt == null) {
			pkgFrgmt = createPackage(javaProject, entityName, typeToCreate, contextMap);
		}
		*/
		if (versionControlPreferences.isEnable() && prjShared && prjConfigured) {
			final IFile ifile = getIFileFromFile(file);
			List<FastCodeEntityHolder> chngsForType = ((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).get(ifile);
			if (chngsForType == null) {
				chngsForType = new ArrayList<FastCodeEntityHolder>();
				chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_PACKAGE, new FastCodePackage(pkgFrgmt)));
			}
			((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).put(ifile, chngsForType);
		}
	}
}
