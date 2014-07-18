package org.fastcode.popup.actions.snippet;

import static org.eclipse.jdt.ui.JavaUI.openInEditor;
import static org.eclipse.jdt.ui.JavaUI.revealInEditor;
import static org.fastcode.common.FastCodeConstants.AUTO_CHECKIN;
import static org.fastcode.common.FastCodeConstants.BOOLEAN;
import static org.fastcode.common.FastCodeConstants.COLON;
import static org.fastcode.common.FastCodeConstants.COMMA;
import static org.fastcode.common.FastCodeConstants.DELIMITER;
import static org.fastcode.common.FastCodeConstants.DIR;
import static org.fastcode.common.FastCodeConstants.DOT;
import static org.fastcode.common.FastCodeConstants.DOUBLE;
import static org.fastcode.common.FastCodeConstants.EMPTY_STR;
import static org.fastcode.common.FastCodeConstants.ENCLOSING_CLASS_STR;
import static org.fastcode.common.FastCodeConstants.ENCLOSING_FILE_STR;
import static org.fastcode.common.FastCodeConstants.EQUAL;
import static org.fastcode.common.FastCodeConstants.FC_OBJ_CREATED;
import static org.fastcode.common.FastCodeConstants.FIELD_ANNOTATIONS_STR;
import static org.fastcode.common.FastCodeConstants.FIELD_CLASS_STR;
import static org.fastcode.common.FastCodeConstants.FIELD_MODIFIER_STR;
import static org.fastcode.common.FastCodeConstants.FIELD_NAME_STR;
import static org.fastcode.common.FastCodeConstants.FILE_SEPARATOR;
import static org.fastcode.common.FastCodeConstants.FLOAT;
import static org.fastcode.common.FastCodeConstants.FORWARD_SLASH;
import static org.fastcode.common.FastCodeConstants.HASH;
import static org.fastcode.common.FastCodeConstants.INITIATED;
import static org.fastcode.common.FastCodeConstants.INT;
import static org.fastcode.common.FastCodeConstants.JAVA_EXTENSION;
import static org.fastcode.common.FastCodeConstants.LEFT_CURL;
import static org.fastcode.common.FastCodeConstants.LEFT_PAREN;
import static org.fastcode.common.FastCodeConstants.METHOD_ANNOTATIONS_STR;
import static org.fastcode.common.FastCodeConstants.METHOD_ARGS_STR;
import static org.fastcode.common.FastCodeConstants.METHOD_BODY_STR;
import static org.fastcode.common.FastCodeConstants.METHOD_COMMENTS_STR;
import static org.fastcode.common.FastCodeConstants.METHOD_EXCEPTIONS_STR;
import static org.fastcode.common.FastCodeConstants.METHOD_MODIFIER_STR;
import static org.fastcode.common.FastCodeConstants.METHOD_NAME_STR;
import static org.fastcode.common.FastCodeConstants.METHOD_RETURN_TYPE_STR;
import static org.fastcode.common.FastCodeConstants.METHOD_RETURN_TYPE_VOID;
import static org.fastcode.common.FastCodeConstants.MODIFIER_PRIVATE;
import static org.fastcode.common.FastCodeConstants.MODIFIER_PUBLIC;
import static org.fastcode.common.FastCodeConstants.NAMES;
import static org.fastcode.common.FastCodeConstants.NEWLINE;
import static org.fastcode.common.FastCodeConstants.NODE;
import static org.fastcode.common.FastCodeConstants.OPTIONAL;
import static org.fastcode.common.FastCodeConstants.PARENT;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_CLASS;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_FIELDS;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_FILE;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_FOLDER;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_INNERCLASSES;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_INTERFACE;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_MESSAGE;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_METHODS;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_NAME;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_PACKAGE;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_PROJECT;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_TARGET;
import static org.fastcode.common.FastCodeConstants.PLACEHOLDER_TITLE;
import static org.fastcode.common.FastCodeConstants.RIGHT_CURL;
import static org.fastcode.common.FastCodeConstants.SPACE;
import static org.fastcode.common.FastCodeConstants.TEMPLATE_TAG_PREFIX;
import static org.fastcode.common.FastCodeConstants.TYPE;
import static org.fastcode.common.FastCodeConstants.UNDERSCORE;
import static org.fastcode.common.FastCodeConstants.XML_COMPLETE_END;
import static org.fastcode.common.FastCodeConstants.XML_END;
import static org.fastcode.common.FastCodeConstants.XML_EXTENSION;
import static org.fastcode.common.FastCodeConstants.XML_START;
import static org.fastcode.setting.GlobalSettings.getInstance;
import static org.fastcode.util.FastCodeUtil.closeInputStream;
import static org.fastcode.util.FastCodeUtil.findEditor;
import static org.fastcode.util.ImportUtil.createImport;
import static org.fastcode.util.ImportUtil.createImportOfMethodAndField;
import static org.fastcode.util.SourceUtil.buildClass;
import static org.fastcode.util.SourceUtil.createCodeFormatter;
import static org.fastcode.util.SourceUtil.createFolder;
import static org.fastcode.util.SourceUtil.doesFieldExistsInType;
import static org.fastcode.util.SourceUtil.findFileFromPath;
import static org.fastcode.util.SourceUtil.formatCode;
import static org.fastcode.util.SourceUtil.getDefaultPathFromProject;
import static org.fastcode.util.SourceUtil.getEditorPartFromFile;
import static org.fastcode.util.SourceUtil.getFQNameFromFieldTypeName;
import static org.fastcode.util.SourceUtil.getFileContents;
import static org.fastcode.util.SourceUtil.getIFileFromFile;
import static org.fastcode.util.SourceUtil.getJavaProject;
import static org.fastcode.util.SourceUtil.getPackageRootFromProject;
import static org.fastcode.util.SourceUtil.getPackagesInProject;
import static org.fastcode.util.SourceUtil.getPathFromUser;
import static org.fastcode.util.SourceUtil.getRepositoryServiceClass;
import static org.fastcode.util.SourceUtil.getSuperInterfacesType;
import static org.fastcode.util.SourceUtil.getWorkingJavaProjectFromUser;
import static org.fastcode.util.SourceUtil.implementInterfaceMethods;
import static org.fastcode.util.SourceUtil.isFileSaved;
import static org.fastcode.util.SourceUtil.isNativeType;
import static org.fastcode.util.SourceUtil.overrideBaseClassMethods;
import static org.fastcode.util.StringUtil.format;
import static org.fastcode.util.StringUtil.formatXml;
import static org.fastcode.util.StringUtil.formatXmlWithCDATA;
import static org.fastcode.util.StringUtil.getAttributes;
import static org.fastcode.util.StringUtil.getGlobalSettings;
import static org.fastcode.util.StringUtil.getTemplateTagEnd;
import static org.fastcode.util.StringUtil.getTemplateTagStart;
import static org.fastcode.util.StringUtil.isEmpty;
import static org.fastcode.util.StringUtil.isJavaReservedWord;
import static org.fastcode.util.StringUtil.isValidPackageName;
import static org.fastcode.util.StringUtil.isValidVariableName;
import static org.fastcode.util.StringUtil.isValidXml;
import static org.fastcode.util.StringUtil.parseClassName;
import static org.fastcode.util.StringUtil.parseFieldName;
import static org.fastcode.util.StringUtil.parseMethodName;
import static org.fastcode.util.StringUtil.parseType;
import static org.fastcode.util.StringUtil.replacePlaceHolderWithBlank;
import static org.fastcode.util.StringUtil.replacePlaceHolders;
import static org.fastcode.util.StringUtil.replaceSpecialChars;
import static org.fastcode.util.VersionControlUtil.addOrUpdateFileStatusInCache;
import static org.fastcode.util.VersionControlUtil.isPrjConfigured;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.codehaus.groovy.eclipse.core.model.GroovyRuntime;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.CheckedTreeSelectionDialog;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.fastcode.Activator;
import org.fastcode.common.Action;
import org.fastcode.common.Actions;
import org.fastcode.common.FastCodeConstants.ACTION_ENTITY;
import org.fastcode.common.FastCodeConstants.ACTION_TYPE;
import org.fastcode.common.FastCodeConstants.CLASS_TYPE;
import org.fastcode.common.FastCodeConstants.TemplateTag;
import org.fastcode.common.FastCodeEntityHolder;
import org.fastcode.common.FastCodeField;
import org.fastcode.common.FastCodeFile;
import org.fastcode.common.FastCodeFolder;
import org.fastcode.common.FastCodeMethod;
import org.fastcode.common.FastCodeObject;
import org.fastcode.common.FastCodePackage;
import org.fastcode.common.FastCodeProject;
import org.fastcode.common.FastCodeSelectionDialog;
import org.fastcode.common.FastCodeType;
import org.fastcode.common.StringSelectionDialog;
import org.fastcode.exception.FastCodeRepositoryException;
import org.fastcode.preferences.VersionControlPreferences;
import org.fastcode.setting.GlobalSettings;
import org.fastcode.util.CreateSimilarDescriptorClass;
import org.fastcode.util.FastCodeContext;
import org.fastcode.util.FastCodeFileForCheckin;
import org.fastcode.util.RepositoryService;
import org.fastcode.util.SourceUtil;
import org.fastcode.versioncontrol.FastCodeCheckinCache;
import static org.fastcode.util.SourceUtil.getImagefromFCCacheMap;

public class TemplateTagsProcessor {
	final List<Action>			subActions					= new ArrayList<Action>();
	StringBuilder				existingMembersBuilder		= new StringBuilder();
	IJavaProject				javaProject					= null;
	VersionControlPreferences	versionControlPreferences	= VersionControlPreferences.getInstance();
	boolean						overWrite					= false;

	/**
	 * This method will parse the snippet and search for <fc:import>,<fc:method
	 * name="",target="">,<fc:field> ,<fc:file>,<fc:class>,<fc:message>,<fc:exit>,<fc:folder>,<fc:package>,<fc:project> tags and createImport,createMethod
	 * ,createField,createFile,createClass,craeteFolder,cretaePackage,createProject according to the tag.
	 *
	 * @param compUnit
	 * @param snippet
	 * @param editorPart
	 * @param hasSubAction
	 * @param placeHolders
	 * @param spacesBeforeCursor
	 * @return
	 * @throws Exception
	 */
	public String processTemplateTags(final ICompilationUnit compUnit, final String snippet, final IEditorPart editorPart,
			final boolean hasSubAction, final Map<String, Object> placeHolders, final String spacesBeforeCursor) throws Exception {

		final StringBuilder snippetBuilder = new StringBuilder();
		final Map<String, Object> contextMap = new HashMap<String, Object>();
		Map<Object, List<FastCodeEntityHolder>> fastCodeObjectsMap = new HashMap<Object, List<FastCodeEntityHolder>>();
		contextMap.put(FC_OBJ_CREATED, fastCodeObjectsMap);
		contextMap.put("changes_for_File", fastCodeObjectsMap);
		final List<Action> actionList = new ArrayList<Action>();
		final List<Action> messsageActionList = new ArrayList<Action>();
		int start = 0;
		boolean simpleTagFound = false;
		while (true) {

			TemplateTag tagFound = null;
			String tagBody = null;

			int startTag = snippet.indexOf(XML_START + TEMPLATE_TAG_PREFIX + COLON, start);
			int endTag = 0;

			if (startTag == -1) {
				snippetBuilder.append(snippet.substring(start));
				break;
			}

			for (final TemplateTag templateTag : TemplateTag.values()) {

				if (startTag == snippet.indexOf(getTemplateTagStart(templateTag), startTag)) {
					snippetBuilder.append(snippet.substring(start, startTag));

					final int startTagEnd = snippet.indexOf(XML_END, startTag);
					final int startCompleteTagEnd = snippet.indexOf(XML_COMPLETE_END, startTag);

					simpleTagFound = startCompleteTagEnd == startTagEnd - 1;

					if (simpleTagFound) {
						endTag = startTagEnd + 1;
					} else {
						endTag = snippet.indexOf(getTemplateTagEnd(templateTag), startTag);
						if (endTag == -1) {
							throw new Exception("Unable to find end tag for " + getTemplateTagStart(templateTag));
						}
					}

					tagBody = simpleTagFound ? snippet.substring(startTag, startTagEnd + 1) : snippet.substring(startTag, endTag
							+ getTemplateTagEnd(templateTag).length());

					final String insideTagBody = startCompleteTagEnd == startTagEnd - 1 ? null : snippet.substring(startTagEnd + 1, endTag);
					tagFound = templateTag;
					final Action action = populateAction(tagFound, tagBody, insideTagBody, compUnit, hasSubAction, placeHolders,
							contextMap, spacesBeforeCursor);

					if (!hasSubAction && action != null) {
						final boolean found = action.getEntity() == ACTION_ENTITY.Import ? isDuplicateImportAction(actionList, action)
								: false;
						if (!found) {
							actionList.add(action);
						}
					}
					startTag = simpleTagFound ? endTag : endTag + getTemplateTagEnd(tagFound).length();
				}

			}
			if (start == startTag) {
				throw new Exception("Unable to find tag , Please provide correct fc tag in the xml and try again.");
			}
			start = startTag;//simpleTagFound ? endTag : endTag + getTemplateTagEnd(tagFound).length();

			if (start == snippet.length()) {
				break;
			}
			// Keep going Until you hit the end of line. This is to remove the
			// blank line problem.
			char ch = snippet.charAt(start);
			while (Character.isWhitespace(ch) && start < snippet.length()) {
				start++;
				ch = snippet.charAt(start);
				if (ch == NEWLINE.charAt(0)) {
					start++;
					break;
				}
			}

		}
		if (!hasSubAction && this.existingMembersBuilder.length() != 0) {
			MessageDialog.openWarning(new Shell(), "Warning", "Member(s):  " + this.existingMembersBuilder.toString() + "already exist");

		}

		//final IPreferenceStore preferenceStore = new ScopedPreferenceStore(new InstanceScope(), FAST_CODE_PLUGIN_ID);
		//this.autoCheckinEnabled = preferenceStore.getBoolean(P_ENABLE_AUTO_CHECKIN);

		if (!hasSubAction && !actionList.isEmpty()) {
			for (final Action action : actionList) {
				if (action.getEntity() == ACTION_ENTITY.Message) {
					createMessageFromTag(action.getEntityName(), action.getSource());
					messsageActionList.add(action);
				} else if (action.getEntity() == ACTION_ENTITY.Exit) {
					createMessageFromTag(action.getType().name(), action.getLabelMsg());
					return EMPTY_STR;
				} else if (action.getEntity() == ACTION_ENTITY.Info) {
					messsageActionList.add(action);
					MessageDialog.openInformation(new Shell(), "Information", action.getSource());
				}
			}
			if (!messsageActionList.isEmpty()) {
				for (final Action action : messsageActionList) {
					actionList.remove(action);
				}
			}
			if (!actionList.isEmpty()) {
				final Actions actions = new Actions.Builder().withActions(actionList.toArray(new Action[0])).build();
				// actions.setActions(actionList.toArray(new Action[0]));
				final CheckedTreeSelectionDialog checkedTreeSelectionDialog = new CheckedTreeSelectionDialog(new Shell(),
						new ActionLabelProvider(), new ActionContentProvider());
				checkedTreeSelectionDialog.setTitle("Action(s) Selection");
				checkedTreeSelectionDialog.setMessage("Select the action(s)");
				checkedTreeSelectionDialog.setInput(actions);
				checkedTreeSelectionDialog.setInitialElementSelections(filterActionList(actionList));//checkedTreeSelectionDialog.setInitialElementSelections(actionList);
				checkedTreeSelectionDialog.setExpandedElements(actionList.toArray(new Action[0]));
				checkedTreeSelectionDialog.setContainerMode(true);

				if (checkedTreeSelectionDialog.open() == Window.CANCEL) {
					return EMPTY_STR;
				}
				final StringBuilder existWarningBuilder = new StringBuilder();

				if (checkedTreeSelectionDialog.getResult() != null) {
					for (final Object selection : checkedTreeSelectionDialog.getResult()) {
						final Action actionSelected = (Action) selection;
						if (actionSelected.isExist()) {
							if (actionSelected.getEntity() == ACTION_ENTITY.File || actionSelected.getEntity() == ACTION_ENTITY.Class) {
								existWarningBuilder.append(actionSelected.getEntity() + SPACE + actionSelected.getEntityName() + SPACE
										+ COMMA);
							}
						}

					}
					if (existWarningBuilder.length() != 0) {
						this.overWrite = MessageDialog.openQuestion(new Shell(), "Overwrite", existWarningBuilder.toString()
								+ "already exist, Would you like to overwrite?");
					}
				}

				/*checkedTreeSelectionDialog.getTreeViewer().addCheckStateListener(new ICheckStateListener() {

					public void checkStateChanged(CheckStateChangedEvent event) {
						if (event.getChecked()) {
							System.out.println(event.getElement());
						}
					}
				});*/
				if (checkedTreeSelectionDialog.getResult() != null) {
					for (final Object selection : checkedTreeSelectionDialog.getResult()) {
						final Action actionSelected = (Action) selection;
						if (actionSelected.getSubAction() != null) {
							for (final Action subAction : actionSelected.getSubAction()) {
								if (subAction.getEntity() == ACTION_ENTITY.Exit) {
									return EMPTY_STR;
								}
								createActionEntity(subAction, compUnit, editorPart, contextMap, placeHolders, spacesBeforeCursor);
							}
						}
						if (actionSelected.getEntity() == ACTION_ENTITY.Exit) {
							return EMPTY_STR;
						}
						createActionEntity(actionSelected, compUnit, editorPart, contextMap, placeHolders, spacesBeforeCursor);
					}
				}
			}
		}

		final List<String> classNameList = new ArrayList<String>();
		for (final Entry<String, Object> entry : contextMap.entrySet()) {
			if (entry.getValue() instanceof FastCodeObject) {
				final Object obj = ((FastCodeObject) entry.getValue()).getObject();
				if (obj instanceof ICompilationUnit) {
					classNameList.add(((ICompilationUnit) obj).getElementName());
				}
			}
		}
		if (classNameList.size() > 0) {
			String className = null;
			final FastCodeSelectionDialog classNameSelectionDialog = new StringSelectionDialog(new Shell(), "Select Class",
					"Select Class to jump to", classNameList.toArray(new String[0]), false);
			if (classNameSelectionDialog.open() != Window.CANCEL) {
				className = (String) classNameSelectionDialog.getResult()[0];
			}

			for (final Entry<String, Object> entry : contextMap.entrySet()) {
				if (entry.getValue() instanceof FastCodeObject) {
					final Object obj = ((FastCodeObject) entry.getValue()).getObject();
					if (obj instanceof ICompilationUnit) {
						if (!isEmpty(className) && className.equals(((ICompilationUnit) obj).getElementName())) {
							final IEditorPart javaEditor = openInEditor((ICompilationUnit) obj);
							revealInEditor(javaEditor, (IJavaElement) (ICompilationUnit) obj);
							break;
						}
					}
				}
			}
		}

		fastCodeObjectsMap = (Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED);

		if (!fastCodeObjectsMap.isEmpty()) {
			try {
				final boolean addToCache = false;
				final RepositoryService repositoryService = getRepositoryServiceClass();
				repositoryService.commitToRepository(fastCodeObjectsMap, addToCache);
			} catch (final FastCodeRepositoryException ex) {
				ex.printStackTrace();
			}
		}
		/*for (final Map.Entry<Object, StringBuilder> entry:commitMessageMap.entrySet()) {
			if (entry.getKey() instanceof IType) {
				//((IType)entry.getKey()).getUnderlyingResource().touch(new NullProgressMonitor());
				final File file = new File(((IType)entry.getKey()).getResource().getLocationURI());
				checkIn(file, entry.getValue().toString());
			}
		}*/

		return snippetBuilder.toString().trim();
	}

	/**
	 * @param actionList
	 * @param action
	 * @param found
	 * @return
	 */
	private boolean isDuplicateImportAction(final List<Action> actionList, final Action action) {
		for (final Action importAction : actionList) {
			if (importAction != null) {
				if (importAction.getSource().equals(action.getSource())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param classToImport
	 * @param compUnit
	 * @throws Exception
	 */
	private void createImportFromTag(final String classToImport, final ICompilationUnit compUnit) throws Exception {

		if (classToImport.contains(HASH)) {
			createImportOfMethodAndField(classToImport, compUnit);
			return;
		}
		if (isNativeType(classToImport)) {
			return;
		}

		try {
			final FastCodeType fastCodeType = parseType(classToImport, compUnit);
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
			MessageDialog.openError(new Shell(), "Error", "Unable to Create Import");
			ex.printStackTrace();
		}
	}

	/**
	 * @param methodName
	 * @param methodSource
	 * @param typeToCreate
	 * @param targetFastCodeType.
	 * @param contextMap
	 * @param spacesBeforeCursor
	 * @param compUnit
	 * @param placeHolders
	 * @param editorPart
	 * @return
	 * @throws Exception
	 */
	private IMethod createMethodFromTag(final String methodName, final String methodSource, final FastCodeType targetFastCodeType,
			final String typeToCreate, final Map<String, Object> contextMap, final String spacesBeforeCursor, final Map<String, Object> placeHolders,
			final ICompilationUnit compUnit, final IEditorPart editorPart) throws Exception {
		IType type = null;
		if (targetFastCodeType.getiType() == null) {
			IJavaProject javaProject = null;
			if (javaProject == null) {
				javaProject = getJavaProject(placeHolders.get(PLACEHOLDER_PROJECT) instanceof FastCodeProject ? ((FastCodeProject) placeHolders
						.get(PLACEHOLDER_PROJECT)).getName() : (String) placeHolders.get(PLACEHOLDER_PROJECT));
			}

			if (javaProject == null) {
				javaProject = getWorkingJavaProjectFromUser();//did for j2ee base
			}
			createClassFromTag(targetFastCodeType.getName(), targetFastCodeType.getPackage().getName(), javaProject, EMPTY_STR, contextMap,
					placeHolders, compUnit, isEmpty(typeToCreate) ? "class" : typeToCreate, spacesBeforeCursor, false, false, editorPart);
			type = ((ICompilationUnit) ((FastCodeObject) contextMap.get("Class_" + targetFastCodeType.getName() + ".java")).getObject())
					.findPrimaryType();
		} else {
			type = targetFastCodeType.getiType();
		}
		type.getCompilationUnit().becomeWorkingCopy(null);

		format(methodSource, spacesBeforeCursor);
		IMethod method = null;
		/*if (isExist) {
			for (final IMethod method1 : type.getMethods()) {
				if (method1.getElementName().equals(methodName)) {
					final IMethod existMethod = type.getMethod(methodName, method1.getParameterTypes());
					existMethod.delete(true, new NullProgressMonitor());
					method = type.createMethod(methodSource, null, false, null);
				}
			}
		} else {*/
		method = type.createMethod(methodSource, null, false, null);
		//}
		if (method == null || !method.exists()) {
			type.getCompilationUnit().discardWorkingCopy();
			throw new Exception("Unable to create method.");
		}

		try {
			final File newFileObj = new File(type.getResource().getLocationURI().toString());
			//final FastCodeCheckinCache checkinCache = FastCodeCheckinCache.getInstance();
			/*addOrUpdateFileStatusInCache(newFileObj);
			//checkinCache.getFilesToCheckIn().add(new FastCodeFileForCheckin(INITIATED, newFileObj.getAbsolutePath()));
			final boolean prjShared = !isEmpty(type.getResource().getProject().getPersistentProperties());
			final boolean prjConfigured = !isEmpty(isPrjConfigured(type.getResource().getProject().getName()));*/
			if ((Boolean) placeHolders.get(AUTO_CHECKIN)) {
				if (proceedWithAutoCheckin(newFileObj, type.getResource().getProject())) {
					final IFile file = (IFile) type.getResource(); //.getLocationURI());
					List<FastCodeEntityHolder> chngsForType = ((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED))
							.get(file); //do contains key
					if (chngsForType == null) {
						chngsForType = new ArrayList<FastCodeEntityHolder>();
						final List<Object> fastCodeMethList = new ArrayList<Object>();
						fastCodeMethList.add(new FastCodeMethod(method));
						chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_METHODS, fastCodeMethList)); //fastCodeCache.getCommentKey().get(fastCodeCache.getCommentKey().indexOf("create.class.field"))
					} else {
						boolean isNew = true;
						Object fastCodeMethList = null;
						for (final FastCodeEntityHolder fcEntityHolder : chngsForType) {
							if (fcEntityHolder.getEntityName().equals(PLACEHOLDER_METHODS)) {
								fastCodeMethList = fcEntityHolder.getFastCodeEntity();
								isNew = false;
								break;
							}
						}

						if (isNew) {
							fastCodeMethList = new ArrayList<Object>();
							((List<Object>) fastCodeMethList).add(new FastCodeMethod(method));
							chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_METHODS, fastCodeMethList));
						} else {
							((List<Object>) fastCodeMethList).add(new FastCodeMethod(method));
						}
					}
					((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).put(file, chngsForType);
				}
			}
		} catch (final FastCodeRepositoryException ex) {
			ex.printStackTrace();
		}
		type.getCompilationUnit().commitWorkingCopy(false, null);
		type.getCompilationUnit().discardWorkingCopy();
		return method;
	}

	/**
	 * @param fieldName
	 * @param fieldSource
	 * @param target
	 * @param classToImport
	 * @param contextMap
	 * @param spacesBeforeCursor
	 * @param placeHolders
	 * @param editorPart
	 * @return
	 * @throws Exception
	 */
	private IMember createFieldFromTag(final String fieldName, final String fieldSource, final FastCodeType targetFastCodeType,
			final String classToImport, final Map<String, Object> contextMap, final String spacesBeforeCursor, final Map<String, Object> placeHolders,
			final IEditorPart editorPart) throws Exception {

		IType type = null;
		if (targetFastCodeType.getiType() == null) {
			IJavaProject javaProject = null;
			if (javaProject == null) {
				javaProject = getJavaProject(placeHolders.get(PLACEHOLDER_PROJECT) instanceof FastCodeProject ? ((FastCodeProject) placeHolders
						.get(PLACEHOLDER_PROJECT)).getName() : (String) placeHolders.get(PLACEHOLDER_PROJECT));
			}

			if (javaProject == null) {
				javaProject = getWorkingJavaProjectFromUser();//did for j2ee base
			}
			createClassFromTag(targetFastCodeType.getName(), targetFastCodeType.getPackage().getName(), null, EMPTY_STR, contextMap,
					placeHolders, null, "class", spacesBeforeCursor, false, false, editorPart);
			type = ((ICompilationUnit) ((FastCodeObject) contextMap.get("Class_" + targetFastCodeType.getName() + ".java")).getObject())
					.findPrimaryType();
		} else {
			type = targetFastCodeType.getiType();
		}
		type.getCompilationUnit().becomeWorkingCopy(null);
		format(fieldSource, spacesBeforeCursor);

		if (!isEmpty(classToImport)) {
			createImportFromTag(classToImport, type.getCompilationUnit());
		}
		final IField field = type.createField(fieldSource, null, false, new NullProgressMonitor());

		if (field == null || !field.exists()) {
			type.getCompilationUnit().discardWorkingCopy();
			throw new Exception("Unable to create field.");
		}

		try {
			final boolean prjShared = !isEmpty(type.getResource().getProject().getPersistentProperties());
			final boolean prjConfigured = !isEmpty(isPrjConfigured(type.getResource().getProject().getName()));
			if (this.versionControlPreferences.isEnable() && prjShared && prjConfigured) {
				final IFile file = (IFile) type.getResource(); //.getLocationURI();
				List<FastCodeEntityHolder> chngesForType = ((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED))
						.get(file);
				if (chngesForType == null) {
					chngesForType = new ArrayList<FastCodeEntityHolder>();
					final List<Object> fastCodeFieldList = new ArrayList<Object>();
					fastCodeFieldList.add(new FastCodeField(field));
					chngesForType.add(new FastCodeEntityHolder(PLACEHOLDER_FIELDS, fastCodeFieldList));
				} else {
					boolean isNew = true;
					Object fastCodeFieldList = null;
					for (final FastCodeEntityHolder fcEntityHolder : chngesForType) {
						if (fcEntityHolder.getEntityName().equals(PLACEHOLDER_FIELDS)) {
							fastCodeFieldList = fcEntityHolder.getFastCodeEntity();
							isNew = false;
							break;
						}
					}

					if (isNew) {
						fastCodeFieldList = new ArrayList<Object>();
						((List<Object>) fastCodeFieldList).add(new FastCodeField(field));
						chngesForType.add(new FastCodeEntityHolder(PLACEHOLDER_FIELDS, fastCodeFieldList));
					} else {
						((List<Object>) fastCodeFieldList).add(new FastCodeField(field));
					}
				}
				((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).put(file, chngesForType);
			}
		} catch (final FastCodeRepositoryException ex) {
			ex.printStackTrace();
		}
		type.getCompilationUnit().commitWorkingCopy(false, null);
		type.getCompilationUnit().discardWorkingCopy();
		return field;
	}

	/**
	 *
	 * @param insideTagBody
	 * @param contextMap
	 * @param placeHolders
	 * @param tagBody
	 * @param string
	 * @throws Exception
	 */
	private void createFileFromTag(final String name, final Object dir, final String insideTagBody, final Map<String, Object> contextMap,
			final Map<String, Object> placeHolders, final boolean isExist) throws Exception {

		InputStream inputStream = null;
		if (!isEmpty(insideTagBody)) {
			inputStream = new ByteArrayInputStream(insideTagBody.trim().getBytes());
		} else {
			inputStream = new ByteArrayInputStream(EMPTY_STR.getBytes());
		}
		IFolder folder = dir instanceof String ? ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path((String) dir))
				: ((FastCodeFolder) dir).getFolder();
		if (folder == null || !folder.exists()) {
			folder = createFolder(new Path((String) dir));
		}
		final IFile file = folder.getFile(new Path(name));
		boolean createFileAlone = true;
		try {
			final File newFileObj = new File(file.getLocationURI());

			/*final FastCodeCheckinCache checkinCache = FastCodeCheckinCache.getInstance();
			checkinCache.getFilesToCheckIn().add(new FastCodeFileForCheckin(INITIATED, newFileObj.getAbsolutePath()));*/
			/*addOrUpdateFileStatusInCache(newFileObj);
			final boolean prjShared = !isEmpty(file.getProject().getPersistentProperties());
			final boolean prjConfigured = !isEmpty(isPrjConfigured(file.getProject().getName()));
			createFileAlone = !(this.versionControlPreferences.isEnable() && prjShared && prjConfigured);*/
			if ((Boolean) placeHolders.get(AUTO_CHECKIN)) {
				if (proceedWithAutoCheckin(newFileObj, file.getProject())) {
					final RepositoryService repositoryService = getRepositoryServiceClass();
					try {
						if (repositoryService.isFileInRepository(newFileObj)) { // && !MessageDialog.openQuestion(new Shell(), "File present in repository", "File already present in repository. Click yes to overwrite")) {
							/*MessageDialog.openWarning(new Shell(), "File present in repository", name + " is already present in repository. Please synchronise and try again.");
							return;*/
							createFileAlone = MessageDialog
									.openQuestion(
											new Shell(),
											"File present in repository",
											"File "
													+ newFileObj.getName()
													+ " already present in repository. Click yes to just create the file, No to return without any action.");
							if (!createFileAlone) {
								return;
							}
						}
					} catch (final Throwable th) {
						th.printStackTrace();
						createFileAlone = true;
					}
				}
			}
		} catch (final FastCodeRepositoryException ex1) {
			ex1.printStackTrace();
		}
		try {
			if (isExist) {
				/*final boolean overWrite = MessageDialog.openQuestion(new Shell(), "Overwrite File", "File " + dir + FORWARD_SLASH + name
						+ " already exists, Would you like to overwrite?");*/
				if (this.overWrite) {
					//backUpExistingExportFile(file, name, folder.getProjectRelativePath().toString());
					file.setContents(inputStream, false, true, new NullProgressMonitor());
				} else {
					return;
				}
			} else {
				file.create(inputStream, true, null);
			}
		} catch (final Exception ex) {
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
		} finally {
			closeInputStream(inputStream);
		}

		final IWorkbench wb = PlatformUI.getWorkbench();
		final IWorkbenchPage page = wb.getActiveWorkbenchWindow().getActivePage();
		IDE.openEditor(page, file);
		contextMap.put("File_" + file.getName(), new FastCodeObject(file, ACTION_ENTITY.File.getValue()));
		/*
		 * final IEditorPart editorPart = getEditorPartFromFile(file);
		 * editorPart.doSave(new NullProgressMonitor());
		 */
		if (!createFileAlone) {
			List<FastCodeEntityHolder> chngsForType = ((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).get(file);//.getLocation().toFile());
			if (chngsForType == null) {
				chngsForType = new ArrayList<FastCodeEntityHolder>();
				chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_FILE, new FastCodeFile(file.getName(), file.getProjectRelativePath()
						.toString())));
			}
			((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).put(file, chngsForType);
		}
	}

	/**
	 * @param nodeName
	 * @param rootNodeName
	 * @param insideTagBody
	 * @param target
	 * @param editorPart
	 * @param contextMap
	 * @param placeHolders
	 * @throws Exception
	 */
	private void createXMLTag(final String nodeName, final String rootNodeName, final String insideTagBody, final String target,
			IEditorPart editorPart, final Map<String, Object> contextMap, final Map<String, Object> placeHolders) throws Exception {

		final String rootNodeEndTag = XML_START + FORWARD_SLASH + rootNodeName + XML_END;
		final String rootNodeStartTag = XML_START + rootNodeName + XML_END;
		IFile file = null;
		if (!isEmpty(target)) {
			file = target.startsWith(HASH) ? ((FastCodeFile) placeHolders.get(target.replace(HASH, EMPTY_STR).trim())).getFile()
					: findFileFromPath(target);
		} else {
			file = findFileFromPath(getFullPathFromEditor(editorPart));
		}

		if (file == null) {
			MessageDialog.openError(new Shell(), "Error", "File " + target + " not found.");
		}
		final String xmlFileContents = getFileContents(file);

		if (isEmpty(xmlFileContents)) {
			MessageDialog.openError(new Shell(), "Error", "Empty XML file.");
			return;
		}
		int positionToInsertSnippet = xmlFileContents.indexOf(rootNodeEndTag);

		String finalSnippet;
		if (positionToInsertSnippet == -1) {
			finalSnippet = rootNodeStartTag + insideTagBody.trim() + rootNodeEndTag;
			positionToInsertSnippet = xmlFileContents.length() + 1;
		} else {
			finalSnippet = insideTagBody.trim();
		}

		if (!isEmpty(target)) {
			editorPart = getEditorPartFromFile(file);
		}
		finalSnippet = finalSnippet.replace("&lt;", "<");
		finalSnippet = finalSnippet.replace("&gt;", ">");
		final ITextEditor editor = (ITextEditor) editorPart.getAdapter(ITextEditor.class);
		final IDocumentProvider documentProvider = editor.getDocumentProvider();
		final IDocument document = documentProvider.getDocument(editor.getEditorInput());
		document.replace(positionToInsertSnippet, 0, NEWLINE + formatXmlWithCDATA(finalSnippet));
		editor.doSave(new NullProgressMonitor());

		/*List<Object> fileList = ((Map<Object, Map<String, List>>) contextMap.get(COMMIT_MESSAGE)).get(file).get("CREATE_FILE");
		if (fileList == null) {
			fileList = new ArrayList<Object>();
		}
		fileList.add(new FastCodeFile(file.getName(), file.getFullPath().toString()));


		((Map<Object, Map<String, List<Object>>>) contextMap.get(COMMIT_MESSAGE)).get(file).put("CREATE_FILE", fileList);*/
	}

	/**
	 * @param editorPart
	 * @return
	 */
	private String getFullPathFromEditor(final IEditorPart editorPart) {
		final String editorInputPath = editorPart.getEditorInput().toString();
		return editorInputPath.substring(editorInputPath.indexOf(LEFT_PAREN) + 1, editorInputPath.length() - 1);
	}

	/**
	 * @param message
	 * @param tagBody
	 */
	private void createMessageFromTag(final String title, final String message) {
		/*
		 * final int startMethodTagEnd = tagBody.indexOf(XML_END);
		 *
		 * Map<String, String> attributes = null; if (startMethodTagEnd >
		 * getTemplateTagStart(templateTag).length() + 1) { attributes =
		 * getAttributes
		 * (tagBody.substring(getTemplateTagStart(templateTag).length() + 1,
		 * startMethodTagEnd)); }
		 */
		MessageDialog.openWarning(new Shell(), title, message);
	}

	/**
	 * @param insideTagBody
	 * @param tagBody
	 * @param templateTag
	 * @param contextMap
	 * @param placeHolders
	 * @param spacesBeforeCursor
	 * @param overrideMethods
	 * @param exist
	 * @param editorPart
	 * @param type
	 * @throws JavaModelException
	 * @throws Exception
	 */
	private void createClassFromTag(final String className, final Object packge, final Object project, String insideTagBody,
			final Map<String, Object> contextMap, final Map<String, Object> placeHolders, final ICompilationUnit compUnit,
			final String typeToCreate, final String spacesBeforeCursor, boolean overrideMethods, final boolean exist, final IEditorPart editorPart)
			throws JavaModelException, Exception {

		if (typeToCreate.equals(ACTION_ENTITY.Innerclass.getValue())) {
			compUnit.becomeWorkingCopy(null);
			final File newFileObj = new File(compUnit.getResource().getLocationURI().toString());
			/*final FastCodeCheckinCache checkinCache = FastCodeCheckinCache.getInstance();
			checkinCache.getFilesToCheckIn().add(new FastCodeFileForCheckin(INITIATED, newFileObj.getAbsolutePath()));*/

			try {
				//addOrUpdateFileStatusInCache(newFileObj);
				final IType innerClassType = SourceUtil.createInnerClass(insideTagBody, compUnit);
				/*final boolean prjShared = !isEmpty(compUnit.getResource().getProject().getPersistentProperties());
				final boolean prjConfigured = !isEmpty(isPrjConfigured(compUnit.getResource().getProject().getName()));*/
				if ((Boolean) placeHolders.get(AUTO_CHECKIN)) {
					if (proceedWithAutoCheckin(newFileObj, compUnit.getResource().getProject())) {
						final IFile file = (IFile) innerClassType.getResource(); //.getLocationURI());
						List<FastCodeEntityHolder> chngsForType = ((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED))
								.get(file);
						if (chngsForType == null) {
							chngsForType = new ArrayList<FastCodeEntityHolder>();
							final List<Object> innerClassList = new ArrayList<Object>();
							innerClassList.add(new FastCodeType(innerClassType));
							chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_INNERCLASSES, innerClassList));
						} else {
							boolean isNew = true;
							Object fastCodeFieldList = null;
							for (final FastCodeEntityHolder fcEntityHolder : chngsForType) {
								if (fcEntityHolder.getEntityName().equals(PLACEHOLDER_INNERCLASSES)) {
									fastCodeFieldList = fcEntityHolder.getFastCodeEntity();
									isNew = false;
									break;
								}
							}

							if (isNew) {
								fastCodeFieldList = new ArrayList<Object>();
								((List<Object>) fastCodeFieldList).add(innerClassType);
								chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_INNERCLASSES, fastCodeFieldList));
							} else {
								((List<Object>) fastCodeFieldList).add(innerClassType);
							}

							/*Object innerClassList = chngsForType.get("innerClasses");
							if (innerClassList == null) {
								innerClassList = new ArrayList<Object>();
							}
							((List<Object>) innerClassList).add(new FastCodeType(innerClassType));
							chngsForType.put("innerClasses", innerClassList);*/
						}
						((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).put(file, chngsForType);
					}
				}
			} catch (final FastCodeRepositoryException ex) {
				ex.printStackTrace();
			}
			compUnit.commitWorkingCopy(false, null);
			compUnit.discardWorkingCopy();
			return;
		}
		final IJavaProject prj = (IJavaProject) project;
		/*IJavaProject prj = getJavaProject(project);// getJavaProject(attributes.get(PLACEHOLDER_PROJECT));
		if (prj == null) {
			prj = getJavaProject(placeHolders.get(PLACEHOLDER_PROJECT) instanceof FastCodeProject ? ((FastCodeProject) placeHolders
					.get(PLACEHOLDER_PROJECT)).getName() : (String) placeHolders.get(PLACEHOLDER_PROJECT));
		}

		if (prj == null) {
			prj = this.javaProject = this.javaProject == null ? getWorkingJavaProjectFromUser() : this.javaProject;//did for j2ee base
		}*/
		if (project == null) {
			throw new Exception("Can not continue without java  project.");
		}
		final String srcPath = typeToCreate.equals(ACTION_ENTITY.Test.getValue()) ? getDefaultPathFromProject((IJavaProject) project,
				typeToCreate, EMPTY_STR) : getDefaultPathFromProject((IJavaProject) project, "source", EMPTY_STR);

		IPackageFragment pkgFrgmt = packge instanceof String ? getPackageFragment((IJavaProject) project, srcPath, (String) packge,
				typeToCreate.equals(ACTION_ENTITY.Test.getValue()) ? typeToCreate : "source") : (IPackageFragment) packge;

		if (pkgFrgmt == null) {
			/*final boolean prjShared = !isEmpty(prj.getProject().getPersistentProperties());
			final boolean prjConfigured = !isEmpty(isPrjConfigured(prj.getProject().getName()));*/
			File file = null;
			if ((Boolean) placeHolders.get(AUTO_CHECKIN)) {
				if (proceedWithAutoCheckin(file, prj.getProject())) {
					final String prjURI = prj.getResource().getLocationURI().toString();
					final String path = prjURI.substring(prjURI.indexOf(COLON) + 1);
					final String newPackURL = path + srcPath + FILE_SEPARATOR + ((String) packge).replace(DOT, FILE_SEPARATOR);
					file = new File(newPackURL);
					//final FastCodeCheckinCache checkinCache = FastCodeCheckinCache.getInstance();
					addOrUpdateFileStatusInCache(file);
					//checkinCache.getFilesToCheckIn().add(new FastCodeFileForCheckin(INITIATED, file.getAbsolutePath()));
				}
			}
			pkgFrgmt = createPackage(prj, (String) packge, typeToCreate, contextMap);// createPackage(prj,
			// attributes.get(PLACEHOLDER_PACKAGE));
			if ((Boolean) placeHolders.get(AUTO_CHECKIN)) {
				if (proceedWithAutoCheckin(null, prj.getProject())) {
					final IFile ifile = getIFileFromFile(file);
					List<FastCodeEntityHolder> chngsForType = ((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED))
							.get(ifile);
					if (chngsForType == null) {
						chngsForType = new ArrayList<FastCodeEntityHolder>();
						chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_PACKAGE, new FastCodePackage(pkgFrgmt)));
					}
					((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).put(ifile, chngsForType);
				}
			}
		}

		boolean createFileAlone = true;
		String path;
		try {
			final boolean prjShared = !isEmpty(pkgFrgmt.getResource().getProject().getPersistentProperties());
			final boolean prjConfigured = !isEmpty(isPrjConfigured(pkgFrgmt.getResource().getProject().getName()));
			createFileAlone = !(this.versionControlPreferences.isEnable() && prjShared && prjConfigured);

			final String prjURI = pkgFrgmt.getResource().getLocationURI().toString();
			path = prjURI.substring(prjURI.indexOf(COLON) + 1);
			final File newFileObj = new File(path + FORWARD_SLASH + className + DOT + JAVA_EXTENSION);
			if (this.versionControlPreferences.isEnable() && prjShared && prjConfigured) {
				final RepositoryService repositoryService = getRepositoryServiceClass();
				try {
					if (repositoryService.isFileInRepository(newFileObj)) { // && !MessageDialog.openQuestion(new Shell(), "File present in repository", "File already present in repository. Click yes to overwrite")) {
						/*MessageDialog.openWarning(new Shell(), "File present in repository", className + " is already present in repository. Please synchronise and try again.");
						return;*/
						createFileAlone = MessageDialog
								.openQuestion(
										new Shell(),
										"File present in repository",
										"File "
												+ newFileObj.getName()
												+ " already present in repository. Click yes to just create the file, No to return without any action.");
						if (!createFileAlone) {
							return;
						}
					}
				} catch (final Throwable th) {
					th.printStackTrace();
					createFileAlone = true;
				}
			}
			final FastCodeCheckinCache checkinCache = FastCodeCheckinCache.getInstance();
			checkinCache.getFilesToCheckIn().add(new FastCodeFileForCheckin(INITIATED, newFileObj.getAbsolutePath()));
		} catch (final FastCodeRepositoryException ex) {
			ex.printStackTrace();
		}

		/*if (parseClassName(insideTagBody) == null) {
			insideTagBody = MODIFIER_PUBLIC + SPACE + typeToCreate + SPACE + className + SPACE + LEFT_CURL + NEWLINE + insideTagBody
					+ NEWLINE + RIGHT_CURL;
		}*/

		//format(insideTagBody, spacesBeforeCursor);
		final Object codeFormatter = createCodeFormatter(prj.getProject());
		if (!isEmpty(insideTagBody)) {
			insideTagBody = formatCode(insideTagBody.trim(), codeFormatter);
		}
		ICompilationUnit compilationUnit = null;
		if (exist) {
			if (this.overWrite) {
				final IType type = prj.findType(pkgFrgmt.getElementName() + DOT + className.trim());
				//type.getCompilationUnit().delete(true, new NullProgressMonitor());
				insideTagBody = buildClass(insideTagBody, pkgFrgmt, prj, className);

				//type.getCompilationUnit().getBuffer().setContents(insideTagBody);
				final ReplaceEdit replaceEdit = new ReplaceEdit(0, type.getCompilationUnit().getSource().length(), insideTagBody);
				type.getCompilationUnit().applyTextEdit(replaceEdit, new NullProgressMonitor());
				compilationUnit = type.getCompilationUnit();
				compilationUnit.becomeWorkingCopy(null);
				compilationUnit.commitWorkingCopy(false, null);
				compilationUnit.discardWorkingCopy();

				//refreshProject(prj.getElementName());
			} else {
				return;
			}
		} else {

			compilationUnit = SourceUtil.createClass(insideTagBody, pkgFrgmt, prj, className);
		}

		if (compilationUnit == null) {
			return;
		}
		if (!typeToCreate.equals(ACTION_ENTITY.Interface.getValue())) {
			if (compilationUnit.findPrimaryType().getSuperclassName() != null) {
				final IType superClassType = prj.findType(getFQNameFromFieldTypeName(compilationUnit.findPrimaryType().getSuperclassName(),
						compilationUnit));
				if (superClassType != null && superClassType.exists()) {
					if (Flags.isAbstract(superClassType.getFlags())/*Modifier.isAbstract(Class.forName(superClassType.getFullyQualifiedName()).getModifiers())*/) {
						overrideMethods = true;
					}
				}
			}
			if (overrideMethods) {
				final String superInterfaces[] = compilationUnit.findPrimaryType().getSuperInterfaceNames();
				if (superInterfaces != null) {
					for (final String superInertafce : superInterfaces) {
						final IType superIntType = prj.findType(getFQNameFromFieldTypeName(superInertafce, compilationUnit));
						final FastCodeContext fastCodeContext = new FastCodeContext(superIntType);
						final CreateSimilarDescriptorClass createSimilarDescriptorClass = new CreateSimilarDescriptorClass.Builder()
								.withClassType(CLASS_TYPE.CLASS).build();
						implementInterfaceMethods(superIntType, fastCodeContext, compilationUnit.findPrimaryType(), null,
								createSimilarDescriptorClass);
						final IType[] superInterfaceType = getSuperInterfacesType(superIntType);
						if (superInterfaceType != null) {
							for (final IType type : superInterfaceType) {
								if (type == null || !type.exists()) {
									continue;
								}
								final FastCodeContext context = new FastCodeContext(type);
								implementInterfaceMethods(type, context, compilationUnit.findPrimaryType(), null,
										createSimilarDescriptorClass);
							}
						}
					}
				}
				overrideBaseClassMethods(compilationUnit);
			}
		}
		/*
		 * final IType newType = compilationUnit.findPrimaryType(); String
		 * superClassName = newType.getSuperclassName(); if
		 * (!isEmpty(superClassName)) { final IType superClassType =
		 * prj.findType(getFQNameFromFieldTypeName(newType.getSuperclassName(),
		 * newType.getCompilationUnit())); final FastCodeContext fastCodeContext
		 * = new FastCodeContext(superClassType); final
		 * CreateSimilarDescriptorClass createSimilarDescriptorClass = new
		 * CreateSimilarDescriptorClass.Builder().withClassType(
		 * CLASS_TYPE.CLASS).build(); for (final IMethod method :
		 * superClassType.getMethods()) { if (method.isConstructor()) {
		 * overrideConstructor(method, newType); final MethodBuilder
		 * methodBuilder = new SimilarMethodBuilder(fastCodeContext);
		 * methodBuilder.buildMethod(method, newType, null,
		 * createSimilarDescriptorClass); } } }
		 */
		contextMap.put("Class_" + compilationUnit.getElementName(), new FastCodeObject(compilationUnit, ACTION_ENTITY.Class.getValue()));

		if (!createFileAlone) {
			final IFile file = (IFile) compilationUnit.findPrimaryType().getResource(); //.getLocationURI());
			List<FastCodeEntityHolder> chngsForType = ((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).get(file);
			if (chngsForType == null) {
				chngsForType = new ArrayList<FastCodeEntityHolder>();
				chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_CLASS, new FastCodeType(compilationUnit.findPrimaryType())));
			}
			((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).put(file, chngsForType);
		}
		/*final Map<String, Object> listofChange = ((Map<Object, Map<String, Object>>) contextMap.get("changes_for_File")).get(file);
		if (chngsForType == null) {
			chngsForType = new HashMap<String, Object>();
			chngsForType.put("class", CREATE_CLASS); //fastCodeCache.getCommentKey().get(fastCodeCache.getCommentKey().indexOf("create.class.field"))
		}
		((Map<Object, Map<String, Object>>) contextMap.get("changes_for_File")).put(file, listofChange);*/
	}

	/**
	 * @param prj
	 * @param packageName
	 * @param typeToCreate
	 * @param contextMap
	 * @return
	 * @throws Exception
	 */
	private IPackageFragment createPackage(final IJavaProject prj, final String packageName, final String typeToCreate,
			final Map<String, Object> contextMap) throws Exception {
		IPackageFragmentRoot packageFragmentRoot = null;
		final GlobalSettings globalSettings = getInstance();
		final String path = globalSettings.isUseDefaultForPath() ? getDefaultPathFromProject(prj, typeToCreate, EMPTY_STR)
				: getPathFromUser("Choose Source Path to create package- " + packageName);

		if (path == null) {
			MessageDialog.openError(new Shell(), "Error",
					"Cannot proceed without providing path for src/resource/test folder of the peoject " + prj.getElementName()
							+ "...exiting..");
			return null;
		}

		packageFragmentRoot = getPackageRootFromProject(prj, path);
		final IPackageFragment packageFragment = packageFragmentRoot.createPackageFragment(packageName, true, null);
		return packageFragment;
	}

	/**
	 * @param tagFound
	 * @param tagBody
	 * @param insideTagBody
	 * @param compUnit
	 * @param placeHolders
	 * @param spacesBeforeCursor
	 * @param contextMap
	 * @param hasSubAction
	 * @return
	 * @throws Exception
	 */
	private Action populateAction(final TemplateTag tagFound, final String tagBody, String insideTagBody, final ICompilationUnit compUnit,
			final boolean hasSubAction1, final Map<String, Object> placeHolders, final Map<String, Object> contextMap,
			final String spacesBeforeCursor) throws Exception {

		final int startTagEnd = tagBody.indexOf(XML_END);

		final Map<String, String> attributes;
		String memberName = null;
		String targetClass = null;
		String dir = null;
		String memberSource = null;
		boolean optional = false;
		ACTION_TYPE actionType = ACTION_TYPE.Create;
		String typeToCreate = null;
		String delimiter = null;
		final int tagEnd = tagFound == TemplateTag.IMPORT ? getTemplateTagStart(tagFound).length() + 1 : getTemplateTagStart(tagFound)
				.length();
		if (startTagEnd > tagEnd) {
			attributes = getAttributes(tagBody.substring(tagFound == TemplateTag.IMPORT ? getTemplateTagStart(tagFound).length() + 1
					: getTemplateTagStart(tagFound).length(), startTagEnd));
		} else {
			attributes = Collections.EMPTY_MAP;
		}

		switch (tagFound) {
		case IMPORT:

			if (!validateCompUnit(compUnit)) {
				throw new Exception("There is no Java class open in the editor..Import cannot be done in a non Java file....");
			}
			/*
			 * if (compUnit.getJavaProject().getProject().isSynchronized(0)) {
			 * throw new Exception("Project: " +
			 * compUnit.getJavaProject().getProject().getName() +
			 * " is not synchronized ,Please refresh and try again."); }
			 */

			String classToImport = insideTagBody;
			IType type = null;
			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:import>,  exiting....");
			}
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
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
					throw new Exception("Java class " + fastCodeType.getFullyQualifiedName() + " does not exit");
				}
				for (final FastCodeType codeType : fastCodeType.getParameters()) {
					if (codeType.getParameters().size() > 0) {
						type = compUnit.getJavaProject().findType(codeType.getFullyQualifiedName());
						if (type == null || !type.exists()) {
							throw new Exception("Java class " + codeType.getFullyQualifiedName() + " does not exit");
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
								throw new Exception("Java class " + type2.getFullyQualifiedName() + " does not exit");
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
						throw new Exception("Java class " + classToImport + " does not exit");
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
				throw new Exception("Java class " + classToImport + " does not exit");
			}
			if (isImportFromDefaultPkg(type, fileExtension)) {
				return null;
			}
			if (type.equals(compUnit.findPrimaryType())) {
				return null;
			}
			final String lblMgs = !isEmpty(insideTagBody) && insideTagBody.contains(HASH) ? classToImport : type.getFullyQualifiedName();
			final Action actionImport = new Action.Builder().withEntity(ACTION_ENTITY.Import).withType(actionType)
					.withSource(isEmpty(insideTagBody) ? insideTagBody : insideTagBody.trim()).withLabelMsg("Import   " + lblMgs)
					.withOptional(optional).build();
			if (hasSubAction1) {
				this.subActions.add(actionImport);
				actionImport.setTarget(compUnit);
			}
			return actionImport;

		case METHOD:

			/*
			 * if (compUnit.getJavaProject().getProject().isSynchronized(0)) {
			 * throw new Exception("Project: " +
			 * compUnit.getJavaProject().getProject().getName() +
			 * " is not synchronized ,Please refresh and try again."); }
			 */
			Action actionMethod = null;
			memberName = attributes.containsKey(PLACEHOLDER_NAME) ? attributes.get(PLACEHOLDER_NAME) : null;
			if (memberName == null || memberName.equals(EMPTY_STR)) {
				throw new Exception("Please provide \"name\" attribute for <fc:method> tag  in the XML and try again");
			} else if (isJavaReservedWord(memberName) || !isValidVariableName(memberName)) {
				throw new Exception(
						"Attribute \"name\" contains either java reserve word or not valid for method name ,Please provide correct one  for <fc:method> tag  in the XML and try again");
			}
			targetClass = attributes.containsKey(PLACEHOLDER_TARGET) ? attributes.get(PLACEHOLDER_TARGET) : null;
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
			typeToCreate = attributes.containsKey(TYPE) ? attributes.get(TYPE) : null;

			if (!validateCompUnit(compUnit) && isEmpty(targetClass)) {
				throw new Exception(
						"<fc:method> needs Java class to be open in the editor. Compilation unit is null (no java class open in the editor) and there is no target class. Method cannot be created");
			}
			FastCodeType codeType = null;
			IType type1 = null;
			boolean createTargetClass = false;
			IJavaProject javaProj = null;
			if (compUnit == null) {
				final FastCodeProject codeProject = (FastCodeProject) placeHolders.get(PLACEHOLDER_PROJECT);
				if (codeProject.getJavaProject() != null) {
					javaProj = codeProject.getJavaProject();
				}
			} else {
				javaProj = compUnit.getJavaProject();
			}
			if (!isEmpty(targetClass)) {
				//have to check if class is there in placeholder
				codeType = targetClass.startsWith(HASH) ? (FastCodeType) placeHolders.get(targetClass.replace(HASH, EMPTY_STR).trim())
						: new FastCodeType(javaProj.findType(targetClass.trim()));
				if (codeType.getiType() == null) {
					//have to put code for creating the class

					createTargetClass = true;
					codeType = new FastCodeType(targetClass);

				}
			} else {
				codeType = new FastCodeType(compUnit.findPrimaryType());
			}
			boolean hasSubAction = false;
			if (!isEmpty(insideTagBody)
					&& insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON + ACTION_ENTITY.Import.getValue())) {
				hasSubAction = true;
			} else if (insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON + ACTION_ENTITY.Method.getValue())
					|| insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON + ACTION_ENTITY.Field.getValue())
					|| insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON + ACTION_ENTITY.File.getValue())
					|| insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON + ACTION_ENTITY.Class.getValue())
					|| insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON + ACTION_ENTITY.Folder.getValue())
					|| insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON + ACTION_ENTITY.Xml.getValue())
					|| insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON + ACTION_ENTITY.Message.getValue())
					|| insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON + ACTION_ENTITY.Exit.getValue())) {
				throw new Exception("There should not be any other tags inside <fc:method> except <fc:import>,  exiting....");
			}
			if (hasSubAction && createTargetClass) {
				throw new Exception("Target class does not exist and there is fc:import inside fc:method tag body.Cant proceed further");
			}
			//final IType type1 = targetClass == null ? compUnit.findPrimaryType() : compUnit.getJavaProject().findType(targetClass.trim());
			if (!createTargetClass) {
				type1 = codeType.getiType();
				validateTargetClassType(compUnit, targetClass, type1);
			}
			memberSource = hasSubAction && !createTargetClass ? processTemplateTags(type1.getCompilationUnit(), insideTagBody, null,
					hasSubAction, null, null) : insideTagBody;

			hasSubAction = false;

			if (isEmpty(memberSource)) {

				final String returnType = attributes.containsKey("returnType") ? attributes.get("returnType") : METHOD_RETURN_TYPE_VOID;
				final String parameters = attributes.containsKey("parameters") ? attributes.get("parameters") : EMPTY_STR;
				final String modifier = attributes.containsKey("modifier") ? attributes.get("modifier") : MODIFIER_PUBLIC;
				memberSource = buildMemberSrc(memberName, returnType, parameters, modifier, type1);
				if (isEmpty(memberSource)) {
					throw new Exception(
							"There is no details of method inside fc:method tag body and There is no attributes like returnType,parameters,modifiers.Please provide method details and try again");
				}
			}

			final String methodNameInsideTagbody = parseMethodName(replaceSpecialChars(memberSource.trim()));
			System.out.println(methodNameInsideTagbody);
			if (!memberName.equals(methodNameInsideTagbody)) {
				throw new Exception("Attribute \"name\" value " + memberName + " and method name inside <fc:method> tag body "
						+ methodNameInsideTagbody
						+ " does not match, Please provide same name in both the places for <fc:method> tag  in the XML and try again");
			}
			if (!createTargetClass) {
				boolean exist = false;
				for (final IMethod method : type1.getMethods()) {
					if (method.getElementName().equals(memberName)) {
						final IMethod newMethod = type1.getMethod(memberName, method.getParameterTypes());
						exist = newMethod != null && newMethod.exists();
					}

				}
				if (exist) {
					this.existingMembersBuilder.append("Method with name: " + memberName);
					this.existingMembersBuilder.append(SPACE + COMMA + SPACE);
					return null;
				}
				String methodBody = null;
				if (memberSource.contains(LEFT_CURL) && memberSource.contains(RIGHT_CURL)) {
					methodBody = memberSource.substring(memberSource.indexOf(LEFT_CURL) + 1, memberSource.lastIndexOf(RIGHT_CURL));
				}
				if (type1.isInterface() && !isEmpty(methodBody)) {
					throw new Exception(
							"Target class: "
									+ type1.getFullyQualifiedName()
									+ "   specified in the XML is an interface and method has method body.Can not create this method in an interface.Please choose another class");
				}
			}
			final String partLblMsg1 = typeToCreate == null ? "class " : typeToCreate;
			final String partLblMsg2 = typeToCreate == null ? EMPTY_STR : typeToCreate;

			actionMethod = new Action.Builder()
					.withEntityName(memberName)
					.withTarget(codeType)
					.withType(actionType)
					.withEntity(ACTION_ENTITY.Method)
					.withSource(isEmpty(memberSource) ? memberSource : memberSource.trim())
					.withSubAction(this.subActions)
					.withLabelMsg(
							createTargetClass ? "Create " + partLblMsg1 + " class " + targetClass + " and " + actionType.toString()
									+ partLblMsg2 + " Method   " + memberName + " in " + targetClass : actionType.toString() + partLblMsg2
									+ " Method   " + memberName + "  in class  " + type1.getElementName()).withOptional(optional)
					.withTypeToCreate(typeToCreate)
					/*.withExist(exist)*/.build();
			return actionMethod;

		case FIELD:

			/*
			 * if (compUnit.getJavaProject().getProject().isSynchronized(0)) {
			 * throw new Exception("Project: " +
			 * compUnit.getJavaProject().getProject().getName() +
			 * " is not synchronized ,Please refresh and try again."); }
			 */

			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:field>,  exiting....");
			}
			Action actionField = null;
			memberName = attributes.containsKey(PLACEHOLDER_NAME) ? attributes.get(PLACEHOLDER_NAME) : null;
			targetClass = attributes.containsKey(PLACEHOLDER_TARGET) ? attributes.get(PLACEHOLDER_TARGET) : null;
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
			final String fieldType = attributes.containsKey(PLACEHOLDER_CLASS) ? attributes.get(PLACEHOLDER_CLASS) : null;

			if (memberName == null || memberName.equals(EMPTY_STR)) {
				throw new Exception("Please provide attribute \"name\" for <fc:field> tag in the XML and try again");
			} else if (isJavaReservedWord(memberName) || !isValidVariableName(memberName)) {
				throw new Exception(
						"Attribute \"name\" contains either java reserve word or not valid for field name ,Please provide correct one  for <fc:field> tag  in the XML and try again");
			}

			if (!validateCompUnit(compUnit) && isEmpty(targetClass)) {
				throw new Exception(
						"This template needs Java class to be open in the editor. Compilation unit is null (no java class open in the editor) and there is no target class. Field cannot be created");
			}
			FastCodeType fastCodeType = null;
			boolean createTargetClass1 = false;
			IJavaProject javaProj1 = null;
			if (compUnit == null) {
				final FastCodeProject codeProject = (FastCodeProject) placeHolders.get(PLACEHOLDER_PROJECT);
				if (codeProject.getJavaProject() != null) {
					javaProj1 = codeProject.getJavaProject();
				}
			} else {
				javaProj1 = compUnit.getJavaProject();
			}
			if (!isEmpty(targetClass)) {
				//have to check if class is there in placeholder
				fastCodeType = targetClass.startsWith(HASH) ? (FastCodeType) placeHolders.get(targetClass.replace(HASH, EMPTY_STR).trim())
						: new FastCodeType(javaProj1.findType(targetClass.trim()));
				if (fastCodeType.getiType() == null) {
					//have to put code for creating the class

					createTargetClass1 = true;
					fastCodeType = new FastCodeType(targetClass);

				}
			} else {
				fastCodeType = new FastCodeType(compUnit.findPrimaryType());
			}

			if (!createTargetClass1) {
				validateTargetClassType(compUnit, targetClass, fastCodeType.getiType());

				if (doesFieldExistsInType(fastCodeType.getiType(), memberName)) {
					this.existingMembersBuilder.append("Field with Name:  " + memberName);
					this.existingMembersBuilder.append(SPACE + COMMA + SPACE);
					return null;
				}
			}
			if (isEmpty(insideTagBody)) {
				final String fieldType1 = attributes.containsKey("fieldType") ? attributes.get("fieldType") : "Object";
				final String modifier = attributes.containsKey("modifier") ? attributes.get("modifier") : MODIFIER_PRIVATE;
				insideTagBody = buildFieldSrc(fieldType1, modifier, memberName);
				if (isEmpty(insideTagBody)) {
					throw new Exception(
							"There is no details of field inside fc:field tag body and There is no attributes like fieldType,modifier.Please provide field details and try again?");
				}
			}
			final String fieldNameInsideTagBody = parseFieldName(insideTagBody.trim());
			System.out.println(fieldNameInsideTagBody);
			if (!memberName.equals(fieldNameInsideTagBody)) {
				throw new Exception("Attribute \"name\" value " + memberName + " and field name inside <fc:field> tag body "
						+ fieldNameInsideTagBody
						+ " does not match, Please provide same name in both the places for <fc:field> tag  in the XML and try again");
			}
			actionField = new Action.Builder()
					.withEntity(ACTION_ENTITY.Field)
					.withType(actionType)
					.withEntityName(memberName)
					.withTarget(fastCodeType)
					.withSource(isEmpty(insideTagBody) ? insideTagBody : insideTagBody.trim())
					.withLabelMsg(
							createTargetClass1 ? "Create class " + targetClass + " and " + actionType.toString() + " Field  " + memberName
									+ " in " + targetClass : actionType.toString() + " Field  " + memberName + "  in class  "
									+ fastCodeType.getName()).withOptional(optional).withClassToImport(fieldType).withSubAction(this.subActions)
					.build();
			return actionField;

		case FILE:
		case FILES:

			if (tagFound == TemplateTag.FILE) {
				memberName = attributes.containsKey(PLACEHOLDER_NAME) ? attributes.get(PLACEHOLDER_NAME) : null;
			} else if (tagFound == TemplateTag.FILES) {
				memberName = attributes.containsKey(NAMES) ? attributes.get(NAMES) : null;
			}
			dir = attributes.containsKey(DIR) ? attributes.get(DIR) : null;
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
			delimiter = attributes.containsKey(DELIMITER) ? attributes.get(DELIMITER) : SPACE;

			if (dir != null) {
				if (!dir.startsWith(HASH)) {
					dir = validateDir(dir, attributes, placeHolders);
				}
			} else {
				throw new Exception("Please provide attribute \"dir\" for <fc:" + tagFound.toString().toLowerCase()
						+ "> dir attribute in the XML and try again. ");
			}
			if (isEmpty(memberName)) {
				final String name = tagFound == TemplateTag.FILE ? PLACEHOLDER_NAME : NAMES;
				throw new Exception("Please provide attribute" + "\"" + name + "\" for <fc:" + tagFound.toString().toLowerCase()
						+ "> dir attribute in the XML and try again. ");
			}
			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:" + tagFound.toString().toLowerCase()
						+ ">,  exiting....");
			}
			final List<String> fileNamesList = new ArrayList<String>();
			if (tagFound == TemplateTag.FILE) {
				if (!isEmpty(memberName) && memberName.endsWith(XML_EXTENSION)) {
					insideTagBody = isValidXml(insideTagBody) ? formatXml(insideTagBody) : format(insideTagBody, EMPTY_STR);
				}

			} else if (tagFound == TemplateTag.FILES) {
				if (!isEmpty(memberName)) {
					for (final String file : memberName.split(delimiter)) {
						fileNamesList.add(file);
						if (!isEmpty(file.trim()) && file.trim().endsWith(XML_EXTENSION)) {
							if (!isEmpty(insideTagBody)) {
								insideTagBody = isValidXml(insideTagBody) ? formatXml(insideTagBody) : format(insideTagBody, EMPTY_STR);
							}
						}
					}
				}
				final String[] namesarr = memberName.split(delimiter);
				boolean duplicate = false;
				for (int j = 0; j < namesarr.length; j++) {
					for (int k = j + 1; k < namesarr.length; k++) {
						if (namesarr[k].equals(namesarr[j])) {
							duplicate = true;
							break;
						}
						if (duplicate) {
							break;
						}
					}
				}
				if (duplicate) {
					throw new Exception(
							"Attribute \"names\" contains duplicate file name,Please provide correct attribute \"names\"  for <fc:files> tag in the XML and try again");
				}
			}

			IFolder folder = dir.startsWith(HASH) ? ((FastCodeFolder) placeHolders.get(dir.replace(HASH, EMPTY_STR).trim())).getFolder()
					: ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(dir));

			if (folder.getProject() == null || !folder.getProject().exists()) {
				final boolean confirm = MessageDialog.openConfirm(new Shell(), "Confirmation", "Project:  " + folder.getProject().getName()
						+ "  Does not exist,\n Would You like to select another project for creating file?");
				if (!confirm) {
					return null;
				} else {
					IJavaProject project1 = null;
					if (folder.getProject() == null || !folder.getProject().exists()) {
						project1 = getWorkingJavaProjectFromUser();
						dir = project1.getElementName() + FORWARD_SLASH + folder.getParent().getName() + FORWARD_SLASH + folder.getName();
						folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(dir));
					}
				}
			}
			//folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(dir));
			boolean isFileExist = false;
			if (folder != null && folder.exists()) {
				if (tagFound == TemplateTag.FILE) {
					final IFile file = folder.getFile(new Path(memberName));
					if (file != null && file.exists()) {
						isFileExist = true;
					}
				} else if (tagFound == TemplateTag.FILES) {
					for (final String fileName : memberName.split(delimiter)) {
						final IFile file = folder.getFile(new Path(fileName));
						if (file != null && file.exists()) {
							this.existingMembersBuilder.append("File with Name:  " + fileName);
							this.existingMembersBuilder.append(SPACE + COMMA + SPACE);
							fileNamesList.remove(fileName);
							continue;
						}
					}
				}
				String fileNames = EMPTY_STR;
				if (!fileNamesList.isEmpty()) {
					for (final String fileName : fileNamesList) {
						fileNames = fileNames + fileName + delimiter;

					}
					memberName = fileNames;
				}

				if (isEmpty(memberName)) {
					return null;
				}
			}
			final String msg1 = folder == null || !folder.exists() ? "new folder" : EMPTY_STR;
			final String msg2 = isFileExist ? " (File already exists.)" : EMPTY_STR;
			final String actionTypeLbl = isFileExist ? "Overwrite " : actionType.toString();
			final String fileNames = memberName;
			final String labelMsgPart = memberName.contains(delimiter) ? fileNames.replace(delimiter, COMMA + SPACE) : memberName;
			/*
			 * if (folder.getProject().isSynchronized(0)) { throw new
			 * Exception("Project: " + folder.getProject().getName() +
			 * " is not synchronized ,Please refresh and try again."); }
			 */
			final Action actionFile = new Action.Builder()
					.withEntity(tagFound == TemplateTag.FILE ? ACTION_ENTITY.File : ACTION_ENTITY.Files)
					.withType(actionType)
					.withEntityName(memberName)
					.withDir(folder.getFullPath().toString())
					.withSource(isEmpty(insideTagBody) ? insideTagBody : insideTagBody.trim())
					.withLabelMsg(
							actionTypeLbl + SPACE + tagFound.toString().toLowerCase() + SPACE + labelMsgPart + "  in  " + msg1 + SPACE
									+ folder.getFullPath().toString() + msg2).withOptional(optional).withDelimiter(delimiter)
					.withExist(isFileExist).build();

			return actionFile;
			//break;

		case XML:

			/*
			 * if
			 * (ResourceUtil.getResource(editorPart.getEditorInput()).getProject
			 * ().isSynchronized(0)) { throw new Exception("Project: " +
			 * ResourceUtil
			 * .getResource(editorPart.getEditorInput()).getProject().getName()
			 * + " is not synchronized ,Please refresh and try again."); }
			 */
			final String nodeName = attributes.containsKey(NODE) ? attributes.get(NODE) : null;
			final String rootNodeName = attributes.containsKey(PARENT) ? attributes.get(PARENT) : null;
			final String target = attributes.containsKey(PLACEHOLDER_TARGET) ? attributes.get(PLACEHOLDER_TARGET) : null;
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;

			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:xml>,  exiting....");
			}

			final Action actionXML = new Action.Builder().withEntity(ACTION_ENTITY.Xml).withType(actionType).withNodeName(nodeName)
					.withRootNodeName(rootNodeName).withSource(isEmpty(insideTagBody) ? insideTagBody : insideTagBody.trim())
					.withTarget(target)
					.withLabelMsg(actionType.toString() + " Xml tag with root node   " + rootNodeName + "  and with node  " + nodeName)
					.withOptional(optional).build();

			return actionXML;

			//break;

		case CLASS:
		case CLASSES:
			if (tagFound == TemplateTag.CLASS) {
				memberName = attributes.containsKey(PLACEHOLDER_NAME) ? attributes.get(PLACEHOLDER_NAME) : null;
			} else if (tagFound == TemplateTag.CLASSES) {
				memberName = attributes.containsKey(NAMES) ? attributes.get(NAMES) : null;
			}
			final String pkg = attributes.containsKey(PLACEHOLDER_PACKAGE) ? attributes.get(PLACEHOLDER_PACKAGE) : null;
			final String project = attributes.containsKey(PLACEHOLDER_PROJECT) ? attributes.get(PLACEHOLDER_PROJECT) : null;
			typeToCreate = attributes.containsKey(TYPE) ? attributes.get(TYPE) : PLACEHOLDER_CLASS;
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
			delimiter = attributes.containsKey(DELIMITER) ? attributes.get(DELIMITER) : SPACE;
			final boolean overrideMethods = attributes.containsKey("override_methods") ? Boolean.valueOf(attributes.get("override_methods"))
					: false;
			/*
			 * IJavaProject prj = getJavaProject(project); if (prj == null) {
			 * prj = getJavaProject((String)
			 * placeHolders.get(PLACEHOLDER_PROJECT)); } if
			 * (prj.getProject().isSynchronized(0)) { throw new
			 * Exception("Project: " + prj.getProject().getName() +
			 * " is not synchronized ,Please refresh and try again."); }
			 */

			if (memberName == null || memberName.equals(EMPTY_STR)) {
				if (tagFound == TemplateTag.CLASS) {
					throw new Exception("Please provide attribute \"name\" for <fc:class> tag in the XML and try again");
				} else if (tagFound == TemplateTag.CLASSES) {
					throw new Exception("Please provide attribute \"names\" for <fc:classes> tag in the XML and try again");
				}
			}
			if (pkg == null) {
				throw new Exception("Please provide attribute \"package\" for <fc:" + tagFound.toString().toLowerCase()
						+ "> tag  in the XML and try again");
			}
			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:" + tagFound.toString().toLowerCase()
						+ ">,  exiting....");
			}
			IJavaProject javaProject = null;

			if (!isEmpty(project) && project.startsWith(HASH)) {
				final Object projct = placeHolders.get(project.replace(HASH, EMPTY_STR).trim());
				if (projct instanceof FastCodeProject) {
					javaProject = ((FastCodeProject) projct).getJavaProject();
					if (javaProject == null) {
						javaProject = getJavaProject(((FastCodeProject) projct).getProject());
					}
				} else {
					javaProject = getJavaProject((String) projct);
				}
			} else {
				getJavaProject(project);// getJavaProject(attributes.get(PLACEHOLDER_PROJECT));
			}
			if (javaProject == null) {
				javaProject = getJavaProject(placeHolders.get(PLACEHOLDER_PROJECT) instanceof FastCodeProject ? ((FastCodeProject) placeHolders
						.get(PLACEHOLDER_PROJECT)).getName() : (String) placeHolders.get(PLACEHOLDER_PROJECT));
			}

			if (javaProject == null) {
				javaProject = getWorkingJavaProjectFromUser();//did for j2ee base
			}
			final String defaultPath = typeToCreate.equals(ACTION_ENTITY.Test.getValue()) ? getDefaultPathFromProject(javaProject,
					typeToCreate, EMPTY_STR) : getDefaultPathFromProject(javaProject, "source", EMPTY_STR);

			IPackageFragment pkgFrgmt = null;
			pkgFrgmt = pkg.startsWith(HASH) ? ((FastCodePackage) placeHolders.get(pkg.replace(HASH, EMPTY_STR).trim()))
					.getPackageFragment() : getPackageFragment(javaProject, defaultPath, pkg,
					typeToCreate.equals(ACTION_ENTITY.Test.getValue()) ? typeToCreate : "source");
			String labelMsg = null;
			final String labelMsgPart1 = pkgFrgmt != null ? EMPTY_STR : "new ";
			boolean isClassExist = false;
			if (tagFound == TemplateTag.CLASS) {
				if (isJavaReservedWord(memberName) || !isValidVariableName(memberName)) {
					throw new Exception(
							"Attribute \"name\" contains either java reserved word or not valid for class name ,Please provide correct one for <fc:class> tag  in the XML and try again");
				}
				if (!isEmpty(insideTagBody)) {
					final String classNameInsideTagBody = parseClassName(replaceSpecialChars(insideTagBody.trim()));
					if (!memberName.equals(classNameInsideTagBody)) {
						System.out.println(classNameInsideTagBody);
						throw new Exception(
								"Attribute \"name\" value "
										+ memberName
										+ " and class name inside <fc:class> tag body "
										+ classNameInsideTagBody
										+ " does not match, Please provide same name in both the places for <fc:class> tag  in the XML and try again");
					}
				}

				if (pkgFrgmt != null) {
					final String fullClassName = pkgFrgmt.getElementName() + DOT + memberName.trim();

					//if (compUnit != null) {
					final IType type3 = javaProject.findType(fullClassName);
					if (type3 != null && type3.exists()) {
						isClassExist = true;
						/*this.existingMembersBuilder.append("Class with Name:  " + fullClassName);
						this.existingMembersBuilder.append(SPACE + COMMA + SPACE);
						return null;*/
					}
					//}
				}
				final String pkgNme = pkgFrgmt != null ? pkgFrgmt.getElementName() : pkg;
				final String msg3 = isClassExist ? " (Class already exists.)" : EMPTY_STR;
				final String actionTypeClassLbl = isClassExist ? "Overwrite " : actionType.toString();
				labelMsg = typeToCreate.equals(ACTION_ENTITY.Test.getValue()) ? actionTypeClassLbl + SPACE + typeToCreate + SPACE + "class"
						+ SPACE + memberName + "  in package  " + pkg + msg3 : actionTypeClassLbl + SPACE + typeToCreate + SPACE
						+ memberName + "  in " + labelMsgPart1 + "package  " + pkgNme + msg3; // + msg3;
			} else if (tagFound == TemplateTag.CLASSES) {
				final String[] namesarr = memberName.split(delimiter);
				boolean duplicate = false;
				for (int j = 0; j < namesarr.length; j++) {
					for (int k = j + 1; k < namesarr.length; k++) {
						if (namesarr[k].equals(namesarr[j])) {
							duplicate = true;
							break;
						}
						if (duplicate) {
							break;
						}
					}
				}
				if (duplicate) {
					throw new Exception(
							"Attribute \"names\" contains duplicate class name,Please provide correct  attribute \"names\"  for <fc:classes> tag in the XML and try again");
				}
				//StringBuilder existingClassName = new StringBuilder();
				final List<String> classNamesList = new ArrayList<String>();
				for (final String className : memberName.split(delimiter)) {
					if (isJavaReservedWord(className) || !isValidVariableName(className)) {
						throw new Exception(
								"Attribute \"names\" contains either java reserved word or not valid for class name ,Please provide correct one  for <fc:classes> tag  in the XML and try again");
					}
					classNamesList.add(className);
					if (pkgFrgmt != null) {
						final String fullClassName = pkgFrgmt.getElementName() + DOT + className.trim();
						//if (compUnit != null) {
						final IType type4 = javaProject.findType(fullClassName);
						if (type4 != null && type4.exists()) {
							this.existingMembersBuilder.append("Class with Name:  " + fullClassName);
							this.existingMembersBuilder.append(SPACE + COMMA + SPACE);
							classNamesList.remove(className);
							continue;
						}
						//}
					}
				}
				String classNames = EMPTY_STR;
				if (!classNamesList.isEmpty()) {
					for (final String className : classNamesList) {
						classNames = classNames + className + delimiter;

					}
				}
				memberName = classNames;

				if (isEmpty(memberName)) {
					return null;
				}
				labelMsg = typeToCreate.equals(ACTION_ENTITY.Test.getValue()) ? actionType.toString() + SPACE + typeToCreate + SPACE
						+ "classes" + SPACE + classNames.replace(delimiter, COMMA + SPACE) + "  in package  " + pkg : actionType.toString()
						+ SPACE + "classes" + SPACE + classNames.replace(delimiter, COMMA + SPACE) + "  in " + labelMsgPart1 + " package  "
						+ pkg;

			}

			final Action actionClass = new Action.Builder()
					.withEntity(tagFound == TemplateTag.CLASS ? ACTION_ENTITY.Class : ACTION_ENTITY.Classes).withType(actionType)
					.withEntityName(memberName).withPackge(pkgFrgmt == null ? pkg : pkgFrgmt).withProject(javaProject)
					.withSource(isEmpty(insideTagBody) ? insideTagBody : insideTagBody.trim()).withLabelMsg(labelMsg)
					.withOptional(optional).withTypeToCreate(typeToCreate).withDelimiter(delimiter).withOverrideMethods(overrideMethods)
					.withExist(isClassExist).build();

			return actionClass;
			//break;

		case FOLDER:

			dir = attributes.containsKey(DIR) ? attributes.get(DIR) : null;
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
			if (isEmpty(dir)) {
				throw new Exception("Please provide attribute \"dir\" for <fc:folder> tag  in the XML and try again. ");
			} else {
				dir = validateDir(dir, attributes, placeHolders);
			}
			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:folder>,  exiting....");
			}
			String prjt = null;
			if (dir.contains(FORWARD_SLASH)) {
				prjt = dir.substring(0, dir.indexOf(FORWARD_SLASH)).equals(EMPTY_STR) ? null : dir.substring(0, dir.indexOf(FORWARD_SLASH));
			}
			IProject project1 = null;
			if (prjt != null) {
				project1 = getJavaProject(prjt).getProject();
				final IWorkspaceRoot root = project1.getWorkspace().getRoot();
				//final IPath searchPath = project.getFullPath().append(dir);
				final IFolder existingFolder = root.getFolder(new Path(dir));
				if (existingFolder.exists()) {
					final String folderName = dir.contains(FORWARD_SLASH) ? dir.substring(dir.lastIndexOf(FORWARD_SLASH) + 1, dir.length())
							: dir;
					this.existingMembersBuilder.append("Folder with Name: " + folderName);
					this.existingMembersBuilder.append(SPACE + COMMA + SPACE);
					return null;
				}
			}
			final Action actionFolder = new Action.Builder().withEntity(ACTION_ENTITY.Folder).withType(actionType).withDir(dir)
					.withLabelMsg(actionType.toString() + SPACE + "Folder: " + dir).withOptional(optional).withProject(project1).build();
			return actionFolder;
			//break;

		case PACKAGE:

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

			final IPackageFragment pkgFrgmt1 = getPackageFragment(javaProject1, defaultPath1, memberName,
					typeToCreate.equals(ACTION_ENTITY.Test.getValue()) ? typeToCreate : "source");
			/*for (final IPackageFragment packageFragment : getPackagesInProject(javaProject, defaultPath)) {
				if (packageFragment.getElementName().equals(memberName)) {
					pkgFrgmt = packageFragment;
					break;
				}
			}*/
			if (pkgFrgmt1 != null) {
				this.existingMembersBuilder.append("Package with Name: " + memberName);
				this.existingMembersBuilder.append(SPACE + COMMA + SPACE);
				return null;

			}
			final Action actionPackage = new Action.Builder().withEntity(ACTION_ENTITY.Package).withType(actionType)
					.withEntityName(memberName).withTypeToCreate(typeToCreate).withProject(javaProject1)
					.withLabelMsg(actionType.toString() + SPACE + "Package: " + memberName).withOptional(optional).build();
			return actionPackage;
			//break;

		case PROJECT:

			memberName = attributes.containsKey(PLACEHOLDER_NAME) ? attributes.get(PLACEHOLDER_NAME) : null;
			typeToCreate = attributes.containsKey(TYPE) ? attributes.get(TYPE) : null;
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
			final String srcPath = attributes.containsKey("srcPath") ? attributes.get("srcPath") : "src";

			if (memberName == null || memberName.equals(EMPTY_STR)) {
				throw new Exception("Please provide attribute \"name\" for <fc:project> tag  in the XML and try again");
			}
			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:project>,  exiting....");
			}
			for (final IProject project11 : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
				if (project11.getName().equals(memberName)) {
					this.existingMembersBuilder.append("Project with Name " + memberName);
					this.existingMembersBuilder.append(SPACE + COMMA + SPACE);
					return null;
				}
			}

			if (typeToCreate == null || typeToCreate.equals(EMPTY_STR)) {
				throw new Exception("Please provide attribute \"type\" for <fc:project> tag  in the XML and try again");
			}
			final Action actionProject = new Action.Builder().withEntity(ACTION_ENTITY.Project).withType(actionType)
					.withEntityName(memberName).withTypeToCreate(typeToCreate).withProjectSrcPath(srcPath)
					.withLabelMsg(actionType.toString() + SPACE + typeToCreate + " Project: " + memberName).withOptional(optional).build();
			return actionProject;
			//break;

		case MESSAGE:

			final String title = attributes.containsKey(PLACEHOLDER_TITLE) ? attributes.get(PLACEHOLDER_TITLE) : null;
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:message>,  exiting....");
			}
			final Action actionMessage = new Action.Builder().withEntity(ACTION_ENTITY.Message).withType(actionType).withEntityName(title)
					.withSource(isEmpty(insideTagBody) ? insideTagBody : insideTagBody.trim()).withLabelMsg("Show  " + title)
					.withOptional(optional).build();

			return actionMessage;
			//break;

		case EXIT:

			final String message = attributes.containsKey(PLACEHOLDER_MESSAGE) ? attributes.get(PLACEHOLDER_MESSAGE) : null;
			optional = attributes.containsKey(OPTIONAL) ? Boolean.valueOf(attributes.get(OPTIONAL)) : false;
			actionType = ACTION_TYPE.Prompt;
			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:exit>,  exiting....");
			}
			final Action actionExit = new Action.Builder().withEntity(ACTION_ENTITY.Exit).withType(actionType)
					.withLabelMsg("Exit : " + message).withOptional(optional).build();

			return actionExit;
			//break;
		case PROPERTY:

			final String targetFile = attributes.containsKey(PLACEHOLDER_TARGET) ? attributes.get(PLACEHOLDER_TARGET) : null;
			//final IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(targetFile).makeAbsolute());
			final IFile file = !isEmpty(targetFile) && targetFile.startsWith(HASH) ? ((FastCodeFile) placeHolders.get(targetFile.replace(
					HASH, EMPTY_STR).trim())).getFile() : ResourcesPlugin.getWorkspace().getRoot()
					.getFile(new Path(targetFile).makeAbsolute());
			final String fileFullPath = !isEmpty(targetFile) && targetFile.startsWith(HASH) ? file.getFullPath().toString() : targetFile;

			/*	if (!file.getFileExtension().equals("properties")) {
					throw new Exception("The File selected is not a property file. Please select one properties file and try again.");
				}*/
			if (!file.isSynchronized(0)) {
				throw new Exception(file.getName() + " is not Synchronized, please refresh and try again.");
			}

			if (!isFileSaved(file.getName(), file)) {
				throw new Exception(file.getName() + "  is not saved, please save and try again");
			}
			if (!isEmpty(insideTagBody) && insideTagBody.contains(XML_START + TEMPLATE_TAG_PREFIX + COLON)) {
				throw new Exception("There should not be any other tags inside <fc:properties>,  exiting....");
			}

			final InputStream inputStream = file.getContents();
			final Properties properties = new Properties();
			properties.load(inputStream);
			final String propKey = insideTagBody.substring(0, insideTagBody.indexOf(EQUAL)).replaceAll("\\s*", EMPTY_STR).trim();

			if (properties.containsKey(propKey)) {
				closeInputStream(inputStream);
				this.existingMembersBuilder.append("Property key " + propKey + " in " + fileFullPath);
				this.existingMembersBuilder.append(SPACE + COMMA + SPACE);
				return null;
			}
			closeInputStream(inputStream);
			final Action actionProperty = new Action.Builder().withEntity(ACTION_ENTITY.Property).withType(actionType)
					.withSource(insideTagBody).withTarget(file).withLabelMsg("Create Property in " + fileFullPath).build();
			return actionProperty;

		case SNIPPET:

			final String source = replaceSpecialChars(insideTagBody);
			final String targt = attributes.containsKey(PLACEHOLDER_TARGET) ? attributes.get(PLACEHOLDER_TARGET) : null;
			final Object openFile = placeHolders.containsKey(ENCLOSING_CLASS_STR) ? ((FastCodeType) placeHolders.get(ENCLOSING_CLASS_STR))
					.getFullyQualifiedName() : ((FastCodeFile) placeHolders.get(ENCLOSING_FILE_STR)).getFullPath();
			Object targetObj = null;
			String lblmsg = openFile.toString();
			if (!isEmpty(targt) && targt.startsWith(HASH)) {
				targetObj = placeHolders.get(targt.replace(HASH, EMPTY_STR).trim());
				if (targetObj != null && targetObj instanceof FastCodeType) {
					targetObj = ((FastCodeType) targetObj).getiType();
					lblmsg = ((IType) targetObj).getFullyQualifiedName();
				} else if (targetObj != null && targetObj instanceof FastCodeFile) {
					targetObj = ((FastCodeFile) targetObj).getFile();
					lblmsg = ((IFile) targetObj).getFullPath().toString();
				}
			}
			//if target does not contain # ,it can be file/class.How to check that?

			/*final Object trgtObj = !isEmpty(targetFile) && targetFile.startsWith(HASH) ? ((FastCodeFile) placeHolders.get(targetFile.replace(HASH, EMPTY_STR).trim()))
					.getFile(): ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(targetFile).makeAbsolute());
			final String fileFullPath = !isEmpty(targetFile) && targetFile.startsWith(HASH) ? file.getFullPath().toString() : targetFile;*/

			final Action actionSnippet = new Action.Builder().withEntity(ACTION_ENTITY.Snippet).withType(actionType)
					.withSource(source.trim()).withLabelMsg("Create Snippet in " + lblmsg).withTarget(targetObj).build();
			return actionSnippet;
		case INFO:
			final Action actionInfo = new Action.Builder().withEntity(ACTION_ENTITY.Info).withType(ACTION_TYPE.Prompt)
					.withSource(insideTagBody.trim()).build();
			return actionInfo;

		}
		//break;
		return null;

	}

	/**
	 * @param fieldType1
	 * @param modifier
	 * @param memberName
	 * @return
	 * @throws Exception
	 */
	private String buildFieldSrc(final String fieldType1, final String modifier, final String memberName) throws Exception {
		final GlobalSettings globalSettings = GlobalSettings.getInstance();
		final Map<String, Object> placeHoldersForField = new HashMap<String, Object>();
		final String fieldBody = globalSettings.getFieldBody().trim();

		placeHoldersForField.put(FIELD_CLASS_STR, fieldType1);
		placeHoldersForField.put(FIELD_NAME_STR, memberName);
		placeHoldersForField.put(FIELD_MODIFIER_STR, modifier);
		placeHoldersForField.put(FIELD_ANNOTATIONS_STR, EMPTY_STR);
		final String fieldSrc = replacePlaceHolders(fieldBody, placeHoldersForField);
		return fieldSrc;
	}

	/**
	 * @param memberName
	 * @param returnType
	 * @param parameters
	 * @param modifier
	 * @param type1
	 * @return
	 * @throws Exception
	 */
	private String buildMemberSrc(final String memberName, final String returnType, final String parameters, final String modifier, final IType type1) throws Exception {
		final GlobalSettings globalSettings = GlobalSettings.getInstance();
		final Map<String, Object> placeHoldersForMethod = new HashMap<String, Object>();
		String fullMethodPattern = EMPTY_STR;
		placeHoldersForMethod.put(METHOD_NAME_STR, memberName);
		placeHoldersForMethod.put(METHOD_MODIFIER_STR, modifier);
		placeHoldersForMethod.put(METHOD_ARGS_STR, parameters);
		placeHoldersForMethod.put(METHOD_COMMENTS_STR, EMPTY_STR);
		placeHoldersForMethod.put(METHOD_ANNOTATIONS_STR, EMPTY_STR);
		placeHoldersForMethod.put(METHOD_RETURN_TYPE_STR, returnType);
		//placeHoldersForMethod.put(METHOD_EXCEPTIONS_STR, EMPTY_STR);
		getGlobalSettings(placeHoldersForMethod);
		String methodBody = EMPTY_STR;
		if (type1 != null && type1.exists()) {
			if (type1.isClass()) {
				fullMethodPattern = globalSettings.getClassMethodBody();
			} else if (type1.isInterface()) {
				fullMethodPattern = globalSettings.getInterfaceMethodBody();
			}
		}
		placeHoldersForMethod.put(METHOD_BODY_STR, methodBody);
		methodBody = replacePlaceHolders(methodBody, placeHoldersForMethod);
		if (returnType.equals(METHOD_RETURN_TYPE_VOID)) {
			methodBody = "return;";
		} else if (returnType.equals(INT) || returnType.equals("long") || returnType.equals("short") || returnType.equals(DOUBLE)
				|| returnType.equals(FLOAT)) {
			methodBody = "return 0;";
		} else if (returnType.equals(BOOLEAN)) {
			methodBody = "return false;";
		} else {
			methodBody = "return null;";
		}
		if (!placeHoldersForMethod.containsKey(METHOD_EXCEPTIONS_STR)) {
			fullMethodPattern = replacePlaceHolderWithBlank(fullMethodPattern, "throws", METHOD_EXCEPTIONS_STR, LEFT_CURL);
		}
		fullMethodPattern = replacePlaceHolders(fullMethodPattern, placeHoldersForMethod);
		return fullMethodPattern;
	}

	/**
	 * @param compUnit
	 * @param targetClass
	 * @param type
	 * @throws Exception
	 */

	private void validateTargetClassType(final ICompilationUnit compUnit, final String targetClass, final IType type) throws Exception {
		if (!isEmpty(targetClass) && targetClass.contains(FORWARD_SLASH)) {
			return;
		}
		//final IType type = targetClass == null ? compUnit.findPrimaryType() : compUnit.getJavaProject().findType(targetClass.trim());
		if (type == null || !type.exists()) {
			//throw new Exception("Target class:   " + targetClass + "   specified in the XML is not found.Please choose another class");
		} else if (type.isBinary()) {
			throw new Exception("Target class:   " + targetClass
					+ "   specified in the XML is a binary type.Can not modify.Please choose another class");
		} /*else if (type.isInterface()) {
			throw new Exception("Target class:   " + targetClass
					+ "   specified in the XML is an interface.Can not create.Please choose another class");
			}*/else if (type.isReadOnly()) {
			throw new Exception("Target class:   " + targetClass
					+ "   specified in the XML is read only.Can not modify.Please choose another class.");
		}
	}

	/**
	 * @param compUnit
	 * @return
	 */
	private boolean validateCompUnit(final ICompilationUnit compUnit) {
		return compUnit != null;

	}

	/**
	 * @param tagBody
	 * @param templateTag
	 * @return
	 */
	private Map<String, String> getAttributesForTag(final String tagBody, final TemplateTag templateTag) {
		final int startMethodTagEnd = tagBody.indexOf(XML_END);

		Map<String, String> attributes = null;
		if (startMethodTagEnd > getTemplateTagStart(templateTag).length()) {
			attributes = getAttributes(tagBody.substring(getTemplateTagStart(templateTag).length(), startMethodTagEnd));
		}

		return attributes;
	}

	/**
	 * @author
	 *
	 */
	private class ActionLabelProvider implements ILabelProvider {

		private Image	image;
		FastCodeCache	fastCodeCache	= FastCodeCache.getInstance();

		public void addListener(final ILabelProviderListener labelProviderListener) {

		}

		public void dispose() {
			/*	if (this.image != null && !this.image.isDisposed()) {
					this.image.dispose();
				}*/
		}

		public boolean isLabelProperty(final Object arg0, final String arg1) {
			// TODO Auto-generated method stub
			return false;
		}

		public void removeListener(final ILabelProviderListener arg0) {
			// TODO Auto-generated method stub

		}

		public Image getImage(final Object input) {
			if (input instanceof Action) {
				return getImageForAction((Action) input);
			}
			if (input instanceof Actions) {
				for (final Action action : ((Actions) input).getActions()) {
					return getImageForAction(action);
				}
			}
			return null;
		}

		/**
		 * @param input
		 * @return
		 */
		private Image getImageForAction(final Action input) {
			final ACTION_ENTITY actionEntity = input.getEntity();
			Image entityImage = null;
			final GlobalSettings globalSettings = GlobalSettings.getInstance();
			String image = globalSettings.getPropertyValue(actionEntity.getValue().toUpperCase() + UNDERSCORE + "IMAGE", EMPTY_STR);

			if (actionEntity == ACTION_ENTITY.Import) {
				if (this.fastCodeCache.getEntityImageMap().containsKey(actionEntity.getValue())) {
					return getImagefromFCCacheMap(actionEntity.getValue());
				}
				entityImage = this.getImage(image);
				populateFCCacheEntityImageMap(actionEntity.getValue(), entityImage);
				return entityImage;//("imp_obj.gif");
			} else if (actionEntity == ACTION_ENTITY.Method) {
				if (this.fastCodeCache.getEntityImageMap().containsKey(actionEntity.getValue())) {
					return getImagefromFCCacheMap(actionEntity.getValue());
				}
				entityImage = this.getImage(image);
				populateFCCacheEntityImageMap(actionEntity.getValue(), entityImage);
				return entityImage;//("method.gif");
			} else if (actionEntity == ACTION_ENTITY.Field) {
				if (this.fastCodeCache.getEntityImageMap().containsKey(actionEntity.getValue())) {
					return getImagefromFCCacheMap(actionEntity.getValue());
				}
				entityImage = this.getImage(image);
				populateFCCacheEntityImageMap(actionEntity.getValue(), entityImage);
				return entityImage;//("field.gif");
			} else if (actionEntity == ACTION_ENTITY.File || actionEntity == ACTION_ENTITY.Files || actionEntity == ACTION_ENTITY.Property) {
				if (this.fastCodeCache.getEntityImageMap().containsKey(actionEntity.getValue())) {
					return getImagefromFCCacheMap(actionEntity.getValue());
				}
				entityImage = this.getImage(image);
				populateFCCacheEntityImageMap(actionEntity.getValue(), entityImage);
				return entityImage;//("file_obj.gif");
			} else if (actionEntity == ACTION_ENTITY.Xml) {
				if (this.fastCodeCache.getEntityImageMap().containsKey(actionEntity.getValue())) {
					return getImagefromFCCacheMap(actionEntity.getValue());
				}
				entityImage = this.getImage(image);
				populateFCCacheEntityImageMap(actionEntity.getValue(), entityImage);
				return entityImage;//("xml_image.gif");
			} else if (actionEntity == ACTION_ENTITY.Class || actionEntity == ACTION_ENTITY.Classes) {
				if (actionEntity == ACTION_ENTITY.Classes) {
					image = globalSettings.getPropertyValue(PLACEHOLDER_CLASS.toUpperCase() + UNDERSCORE + "IMAGE", EMPTY_STR);
				}
				if (input.getTypeToCreate().equals(PLACEHOLDER_CLASS) || input.getTypeToCreate().equals(ACTION_ENTITY.Test.getValue())) {
					if (input.getTypeToCreate().equals(PLACEHOLDER_CLASS)
							&& this.fastCodeCache.getEntityImageMap().containsKey(input.getTypeToCreate())) {
						return getImagefromFCCacheMap(actionEntity.getValue());
					} else if (input.getTypeToCreate().equals(ACTION_ENTITY.Test.getValue())
							&& this.fastCodeCache.getEntityImageMap().containsKey(input.getTypeToCreate() + UNDERSCORE + PLACEHOLDER_CLASS)) {
						return getImagefromFCCacheMap(input.getTypeToCreate() + UNDERSCORE + PLACEHOLDER_CLASS);
					}
					entityImage = this.getImage(image);
					if (input.getTypeToCreate().equals(PLACEHOLDER_CLASS)) {
						populateFCCacheEntityImageMap(input.getTypeToCreate(), entityImage);
					} else if (input.getTypeToCreate().equals(ACTION_ENTITY.Test.getValue())) {
						populateFCCacheEntityImageMap(input.getTypeToCreate() + UNDERSCORE + PLACEHOLDER_CLASS, entityImage);
					}
					return entityImage;//("classs_obj.gif");
				} else if (input.getTypeToCreate().equals(PLACEHOLDER_INTERFACE)) {
					if (this.fastCodeCache.getEntityImageMap().containsKey(input.getTypeToCreate())) {
						return getImagefromFCCacheMap(input.getTypeToCreate());
					}
					image = globalSettings.getPropertyValue(PLACEHOLDER_INTERFACE.toUpperCase() + UNDERSCORE + "IMAGE", EMPTY_STR);
					entityImage = this.getImage(image);
					populateFCCacheEntityImageMap(input.getTypeToCreate(), entityImage);
					return entityImage;//("int_obj.gif");
				}
			} else if (actionEntity == ACTION_ENTITY.Folder) {
				if (this.fastCodeCache.getEntityImageMap().containsKey(actionEntity.getValue())) {
					return getImagefromFCCacheMap(actionEntity.getValue());
				}
				entityImage = this.getImage(image);
				populateFCCacheEntityImageMap(actionEntity.getValue(), entityImage);
				return entityImage;//("fldr_obj.gif");
			} else if (actionEntity == ACTION_ENTITY.Package) {
				if (this.fastCodeCache.getEntityImageMap().containsKey("new" + UNDERSCORE + actionEntity.getValue())) {
					return getImagefromFCCacheMap("new" + UNDERSCORE + actionEntity.getValue());
				}
				image = globalSettings.getPropertyValue("NEW" + UNDERSCORE + actionEntity.getValue().toUpperCase() + UNDERSCORE + "IMAGE",
						EMPTY_STR);
				entityImage = this.getImage(image);
				populateFCCacheEntityImageMap("new" + UNDERSCORE + actionEntity.getValue(), entityImage);
				return entityImage;//("package_obj.gif");
			} else if (actionEntity == ACTION_ENTITY.Project) {
				if (input.getTypeToCreate().equals("java")) {
					if (this.fastCodeCache.getEntityImageMap().containsKey(input.getTypeToCreate() + UNDERSCORE + actionEntity.getValue())) {
						return getImagefromFCCacheMap(input.getTypeToCreate() + UNDERSCORE + actionEntity.getValue());
					}
					image = globalSettings.getPropertyValue(input.getTypeToCreate().toUpperCase() + UNDERSCORE
							+ actionEntity.getValue().toUpperCase() + UNDERSCORE + "IMAGE", EMPTY_STR);
					entityImage = this.getImage(image);
					populateFCCacheEntityImageMap(input.getTypeToCreate() + UNDERSCORE + actionEntity.getValue(), entityImage);
					return entityImage;//("projects.gif");
				} else if (input.getTypeToCreate().equals("groovy")) {
					if (this.fastCodeCache.getEntityImageMap().containsKey(input.getTypeToCreate() + UNDERSCORE + actionEntity.getValue())) {
						return getImagefromFCCacheMap(input.getTypeToCreate() + UNDERSCORE + actionEntity.getValue());
					}
					image = globalSettings.getPropertyValue(input.getTypeToCreate().toUpperCase() + UNDERSCORE
							+ actionEntity.getValue().toUpperCase() + UNDERSCORE + "IMAGE", EMPTY_STR);
					entityImage = this.getImage(image);
					populateFCCacheEntityImageMap(input.getTypeToCreate() + UNDERSCORE + actionEntity.getValue(), entityImage);
					return entityImage;//("newgroovyprj_wiz.gif");
				} else if (input.getTypeToCreate().endsWith("php")) {
					if (this.fastCodeCache.getEntityImageMap().containsKey(input.getTypeToCreate() + UNDERSCORE + actionEntity.getValue())) {
						return getImagefromFCCacheMap(input.getTypeToCreate() + UNDERSCORE + actionEntity.getValue());
					}
					image = globalSettings.getPropertyValue(input.getTypeToCreate().toUpperCase() + UNDERSCORE
							+ actionEntity.getValue().toUpperCase() + UNDERSCORE + "IMAGE", EMPTY_STR);
					entityImage = this.getImage(image);
					populateFCCacheEntityImageMap(input.getTypeToCreate() + UNDERSCORE + actionEntity.getValue(), entityImage);
					return entityImage;//("add_php_project.gif");
				} else {
					if (this.fastCodeCache.getEntityImageMap().containsKey(actionEntity.getValue())) {
						return getImagefromFCCacheMap(actionEntity.getValue());
					}
					entityImage = this.getImage(image);
					populateFCCacheEntityImageMap(actionEntity.getValue(), entityImage);
					return entityImage;//("prj_obj.gif");
				}
			} /*else if (actionEntity == ACTION_ENTITY.LocalVar) {
				}*/
			return null;
		}

		/**
		 * @param entityName
		 * @param entityImage
		 */
		private void populateFCCacheEntityImageMap(final String entityName, final Image entityImage) {
			if (!this.fastCodeCache.getEntityImageMap().containsKey(entityName)) {
				this.fastCodeCache.getEntityImageMap().put(entityName, entityImage);
			}
		}

		/**
		 * Gets the image.
		 *
		 * @param imageName
		 *            the image name
		 * @return the image
		 */
		private Image getImage(String imageName) {
			URL url = null;
			if (imageName == null) {
				return null;
			}
			final Image image = PlatformUI.getWorkbench().getSharedImages().getImage(imageName);
			if (image != null && !image.isDisposed()) {
				// this.image = null;
				return image;
			}
			try {
				if (imageName.startsWith("org.eclipse.jdt.ui.")) {
					imageName = imageName.substring("org.eclipse.jdt.ui.".length());
				}
				url = new URL(Activator.getDefault().getDescriptor().getInstallURL(), "icons/" + imageName);
			} catch (final MalformedURLException ex) {
				ex.printStackTrace();
				return null;
			}
			final ImageDescriptor descriptor = ImageDescriptor.createFromURL(url);
			this.image = descriptor.createImage();
			return this.image;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
		 */
		public String getText(final Object input) {
			if (input instanceof Action) {
				return ((Action) input).getLabelMsg();
			}
			if (input instanceof Actions) {
				for (final Action action : ((Actions) input).getActions()) {
					return action.getLabelMsg();
				}
			}
			return null;
		}

	}

	/**
	 * @author
	 *
	 */
	private class ActionContentProvider implements ITreeContentProvider {

		public void dispose() {
			// TODO Auto-generated method stub

		}

		public void inputChanged(final Viewer arg0, final Object arg1, final Object arg2) {
			// TODO Auto-generated method stub

		}

		public Object[] getChildren(final Object input) {
			if (!(input instanceof Actions || input instanceof Action)) {
				return null;
			}
			Action action = null;
			Action[] subActions = null;
			try {
				if (input instanceof Action) {
					action = (Action) input;
					if (action.getSubAction() != null) {
						subActions = action.getSubAction().isEmpty() ? null : action.getSubAction().toArray(new Action[0]);
						return subActions;
					}
				}
				if (input instanceof Actions) {
					return ((Actions) input).getActions();
				}

			} catch (final Exception ex) {
				ex.printStackTrace();
				return null;
			}
			return null;
		}

		public Object[] getElements(final Object input) {
			return getChildren(input);
		}

		public Object getParent(final Object input) {
			return input instanceof Action ? input : null;
		}

		public boolean hasChildren(final Object input) {
			if (!(input instanceof Action || input instanceof Actions)) {
				return false;
			}
			try {
				if (input instanceof Action) {
					if (((Action) input).getSubAction() != null && !((Action) input).getSubAction().isEmpty()) {
						return true;
					}
				}
			} catch (final Exception ex) {
				ex.printStackTrace();
			}
			return false;

		}

	}

	/**
	 * @param actionSelected
	 * @param compUnit
	 * @param editorPart
	 * @param contextMap
	 * @param placeHolders
	 * @param spacesBeforeCursor
	 * @return
	 * @throws Exception
	 */
	private void createActionEntity(final Action actionSelected, final ICompilationUnit compUnit, final IEditorPart editorPart,
			final Map<String, Object> contextMap, final Map<String, Object> placeHolders, final String spacesBeforeCursor) throws Exception {
		final ACTION_ENTITY actionEntity = actionSelected.getEntity();
		switch (actionEntity) {
		case Import:
			final ICompilationUnit compUnitNew = (ICompilationUnit) (actionSelected.getTarget() == null ? compUnit : actionSelected
					.getTarget());
			createImportFromTag(actionSelected.getSource(), compUnitNew);
			break;
		case Method:
			createMethodFromTag(actionSelected.getEntityName(), actionSelected.getSource(), (FastCodeType) actionSelected.getTarget(),
					actionSelected.getTypeToCreate(), contextMap, spacesBeforeCursor, placeHolders, compUnit, editorPart);
			break;
		case Field:
			createFieldFromTag(actionSelected.getEntityName(), actionSelected.getSource(), (FastCodeType) actionSelected.getTarget(),
					actionSelected.getClassToImport(), contextMap, spacesBeforeCursor, placeHolders, editorPart);
			break;
		case File:
			createFileFromTag(actionSelected.getEntityName(), actionSelected.getDir(), actionSelected.getSource(), contextMap,
					placeHolders, actionSelected.isExist());
			break;
		case Xml:
			createXMLTag(actionSelected.getNodeName(), actionSelected.getRootNodeName(), actionSelected.getSource(),
					(String) actionSelected.getTarget(), editorPart, contextMap, placeHolders);
			break;
		case Class:
			createClassFromTag(actionSelected.getEntityName(), actionSelected.getPackge(), actionSelected.getProject(),
					actionSelected.getSource(), contextMap, placeHolders, compUnit, actionSelected.getTypeToCreate(), spacesBeforeCursor,
					actionSelected.isOverrideMethods(), actionSelected.isExist(), editorPart);
			break;
		case Folder:
			createFolderFromTag(actionSelected.getDir(), actionSelected.getProject(), placeHolders, contextMap);//createFolder(new Path(actionSelected.getDir()));
			break;
		case Package:
			createPackageFromTag(actionSelected.getEntityName(), actionSelected.getTypeToCreate(), actionSelected.getProject(), contextMap,
					placeHolders);
			break;
		case Project:
			createProjectFromTag(actionSelected.getEntityName(), actionSelected.getTypeToCreate(), actionSelected.getProjectSrcPath());
			break;
		/*case LocalVar:
			selectLocalVariables(actionSelected, compUnit, editorPart, placeHolders);
			break;*/
		case Files:
			for (final String fileName : actionSelected.getEntityName().split(actionSelected.getDelimiter())) {
				createFileFromTag(fileName.trim(), actionSelected.getDir(), actionSelected.getSource(), contextMap, placeHolders,
						actionSelected.isExist());
			}
			break;
		case Classes:
			for (final String className : actionSelected.getEntityName().split(actionSelected.getDelimiter())) {
				createClassFromTag(className, actionSelected.getPackge(), actionSelected.getProject(), actionSelected.getSource(),
						contextMap, placeHolders, compUnit, actionSelected.getTypeToCreate(), spacesBeforeCursor,
						actionSelected.isOverrideMethods(), actionSelected.isExist(), editorPart);
			}
			break;
		case Property:
			createPropertyFromTag(actionSelected.getTarget(), actionSelected.getSource());
			break;
		case Snippet:
			createSnippetFromTag(actionSelected.getSource(), actionSelected.getTarget(), editorPart, spacesBeforeCursor);
			break;
		default:
			break;

		}
	}

	/**
	 * @param actionSelected
	 * @param compUnit
	 * @param editorPart
	 * @param placeHolders
	 * @throws Exception
	 */
	/*
	private void selectLocalVariables(final Action actionSelected, final ICompilationUnit compUnit, final IEditorPart editorPart,
		final Map<String, Object> placeHolders) throws Exception {
	final List<FastCodeReturn> selectedFields = new ArrayList<FastCodeReturn>();
	final List<FastCodeReturn> membersToWorkOn = getLocalVariablesOfType(compUnit, editorPart, actionSelected.getLocalVarType());

	FastCodeSelectionDialog selectionDialog = null;
	if (membersToWorkOn.size() > 0) {
		selectionDialog = new VariableSelectionDialog(new Shell(), "Variable Selection", "Select local variable(s)",
				membersToWorkOn.toArray(new FastCodeReturn[0]), Boolean.valueOf(actionSelected.getLocalVarSelectionMode()));
		if (selectionDialog.open() == CANCEL) {
			return;
		}

		for (final Object member : selectionDialog.getResult()) {
			selectedFields.add((FastCodeReturn) member);
		}

		placeHolders.put(actionSelected.getLocalVarName(), Boolean.valueOf(actionSelected.getLocalVarSelectionMode()) ? selectedFields
				: selectedFields.get(0));
	}
	}*/

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
	 * @param actionList
	 * @return
	 */
	private List<Action> filterActionList(final List<Action> actionList) {
		final List<Action> initialSelectionActionList = new ArrayList<Action>();
		for (final Action action : actionList) {
			if (action != null) {
				if (!(action.isOptional() || action.isExist())) {
					initialSelectionActionList.add(action);
				}
			}
		}
		return initialSelectionActionList;
	}

	/**
	 * @param dir
	 * @param attributes
	 * @param placeHolders
	 * @return
	 * @throws Exception
	 */
	private String validateDir(String dir, final Map<String, String> attributes, final Map<String, Object> placeHolders) throws Exception {
		if (dir != null) {
			String project = null;
			if (dir.contains(FORWARD_SLASH)) {
				project = dir.substring(0, dir.indexOf(FORWARD_SLASH)).equals(EMPTY_STR) ? null : dir.substring(0,
						dir.indexOf(FORWARD_SLASH));
			}
			IJavaProject javaProject = null;

			if (project != null) {
				javaProject = getJavaProject(project);
				if (javaProject == null) {
					project = attributes.containsKey(PLACEHOLDER_PROJECT) ? attributes.get(PLACEHOLDER_PROJECT) : null;
					if (project != null) {
						javaProject = getJavaProject(project);
						if (javaProject == null) {
							if (placeHolders.containsKey(PLACEHOLDER_PROJECT)) {
								javaProject = getJavaProject(placeHolders.get(PLACEHOLDER_PROJECT) instanceof FastCodeProject ? ((FastCodeProject) placeHolders
										.get(PLACEHOLDER_PROJECT)).getName() : (String) placeHolders.get(PLACEHOLDER_PROJECT));
							} else {
								javaProject = getWorkingJavaProjectFromUser();
							}
						}
						dir = javaProject.getElementName() + FORWARD_SLASH + dir;
					} else {
						if (placeHolders.containsKey(PLACEHOLDER_PROJECT)) {
							javaProject = getJavaProject(placeHolders.get(PLACEHOLDER_PROJECT) instanceof FastCodeProject ? ((FastCodeProject) placeHolders
									.get(PLACEHOLDER_PROJECT)).getName() : (String) placeHolders.get(PLACEHOLDER_PROJECT));
						} else {
							javaProject = getWorkingJavaProjectFromUser();
						}
						dir = javaProject.getElementName() + FORWARD_SLASH + dir;
					}
				}
			}
		}
		return dir;
	}

	/**
	 * @param entityName
	 * @param typeToCreate
	 * @param project
	 * @param contextMap
	 * @param placeHolders
	 * @throws Exception
	 */
	private void createPackageFromTag(final String entityName, final String typeToCreate, final Object project,
			final Map<String, Object> contextMap, final Map<String, Object> placeHolders) throws Exception {
		final IJavaProject javaProject = (IJavaProject) project;

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
			if (this.versionControlPreferences.isEnable() && prjShared && prjConfigured) {
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
		pkgFrgmt = createPackage(javaProject, entityName, typeToCreate, contextMap);
		/*if (pkgFrgmt == null) {
			pkgFrgmt = createPackage(javaProject, entityName, typeToCreate, contextMap);
		}
		*/
		if (this.versionControlPreferences.isEnable() && prjShared && prjConfigured) {
			final IFile ifile = getIFileFromFile(file);
			List<FastCodeEntityHolder> chngsForType = ((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).get(ifile);
			if (chngsForType == null) {
				chngsForType = new ArrayList<FastCodeEntityHolder>();
				chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_PACKAGE, new FastCodePackage(pkgFrgmt)));
			}
			((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).put(ifile, chngsForType);
		}
	}

	/**
	 * @param entityName
	 * @param typeToCreate
	 * @param projectSrcPath
	 * @throws Exception
	 */
	private void createProjectFromTag(final String entityName, final String typeToCreate, final String projectSrcPath) throws Exception {
		final IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		//create eclipse project
		final IProject project = root.getProject(entityName);
		if (!project.exists()) {
			project.create(null);
			project.open(null);
		}
		final IProjectDescription description = project.getDescription();
		if (!isEmpty(typeToCreate)) {
			if (typeToCreate.equals("java")) {
				//set the java project nature
				description.setNatureIds(new String[] { JavaCore.NATURE_ID });
				project.setDescription(description, null);
				//create java project
				final IJavaProject javaProject = JavaCore.create(project);
				createSourceFolderInProject(project, projectSrcPath, javaProject);
			} else if (typeToCreate.equals("groovy")) {
				description.setNatureIds(new String[] { "org.eclipse.jdt.groovy.core.groovyNature", "org.eclipse.jdt.core.javanature" });
				project.setDescription(description, null);
				final IJavaProject javaProject = JavaCore.create(project);
				createSourceFolderInProject(project, projectSrcPath, javaProject);
				GroovyRuntime.addGroovyClasspathContainer(javaProject);//addMinimalGroovyClasspathContainer(javaProject);

			} else if (typeToCreate.equals("php")) {
				description.setNatureIds(new String[] { "org.eclipse.php.core.PHPNature" });
				project.setDescription(description, null);
			} else if (typeToCreate.equals("jee")) {
				description.setNatureIds(new String[] { "org.eclipse.jem.workbench.JavaEMFNature",
						"org.eclipse.wst.common.modulecore.ModuleCoreNature", "org.eclipse.wst.common.project.facet.core.nature",
						"org.eclipse.jdt.core.javanature", "org.eclipse.wst.jsdt.core.jsNature" });
				project.setDescription(description, null);
			}
		}

	}

	/**
	 * @param project
	 * @param projectSrcPath
	 * @param javaProject
	 * @throws Exception
	 */
	private void createSourceFolderInProject(final IProject project, final String projectSrcPath, final IJavaProject javaProject)
			throws Exception {

		if (!isEmpty(projectSrcPath)) {
			final IClasspathEntry[] buildPath = { JavaCore.newSourceEntry(project.getFullPath().append(projectSrcPath)),
					JavaRuntime.getDefaultJREContainerEntry() };

			javaProject.setRawClasspath(buildPath, project.getFullPath().append("bin"), null);
			createFolder(project.getFullPath().append(projectSrcPath));

		}
	}

	/**
	 * @param dir
	 * @param project
	 * @param placeHolders
	 * @param contextMap
	 * @throws Exception
	 */
	private void createFolderFromTag(final Object dir, final Object prj, final Map<String, Object> placeHolders,
			final Map<String, Object> contextMap) throws Exception {
		final String prjURI = getJavaProject((IProject) prj).getResource().getLocationURI().toString();
		final String path = prjURI.substring(prjURI.indexOf(COLON) + 1);
		final File file = new File(path + ((String) dir).replace(((IProject) prj).getName(), EMPTY_STR));
		/*final FastCodeCheckinCache checkinCache = FastCodeCheckinCache.getInstance();
		checkinCache.getFilesToCheckIn().add(new FastCodeFileForCheckin(INITIATED, file.getAbsolutePath()));*/
		//addOrUpdateFileStatusInCache(file);

		final IFolder folder = createFolder(new Path((String) dir));

		try {
			/*final boolean prjShared = !isEmpty(folder.getProject().getPersistentProperties());
			final boolean prjConfigured = !isEmpty(isPrjConfigured(folder.getProject().getName()));*/
			if ((Boolean) placeHolders.get(AUTO_CHECKIN)) {
				if (proceedWithAutoCheckin(file, folder.getProject())) {
					final IFile ifile = getIFileFromFile(file);
					List<FastCodeEntityHolder> chngsForType = ((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED))
							.get(ifile);
					if (chngsForType == null) {
						chngsForType = new ArrayList<FastCodeEntityHolder>();
						chngsForType.add(new FastCodeEntityHolder(PLACEHOLDER_FOLDER, new FastCodeFolder(folder)));
					}
					((Map<Object, List<FastCodeEntityHolder>>) contextMap.get(FC_OBJ_CREATED)).put(ifile, chngsForType);
				}
			}
		} catch (final FastCodeRepositoryException ex) {
			ex.printStackTrace();
		}
		final ISelection selection = new StructuredSelection(folder);
		final IViewReference[] views = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getViewReferences();
		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().resetPerspective();
		for (final IViewReference view : views) {
			if ("org.eclipse.jdt.ui.PackageExplorer".equals(view.getId())) {
				final IViewPart pExplorer = view.getView(true);
				pExplorer.getViewSite().getSelectionProvider().setSelection(selection);
				break;
			}
		}

	}

	/**
	 * @param targetFile
	 * @param tagBody
	 */
	private void createPropertyFromTag(final Object targetFile, final String tagBody) {
		InputStream inputStream = null;
		String fileContent = EMPTY_STR;
		try {
			fileContent = getFileContents((IFile) targetFile);

			if (isEmpty(fileContent)) {
				fileContent = tagBody.trim();
			} else {
				fileContent = fileContent + NEWLINE + tagBody.trim();
			}
			inputStream = new ByteArrayInputStream(fileContent.trim().getBytes());
			((IFile) targetFile).setContents(inputStream, false, true, new NullProgressMonitor());
		} catch (final Exception ex) {
			ex.printStackTrace();
		} finally {
			closeInputStream(inputStream);
		}
	}

	/**
	 * @param javaProject
	 * @param defaultPath
	 * @param pkgName
	 * @param type
	 * @return
	 * @throws Exception
	 */
	private IPackageFragment getPackageFragment(final IJavaProject javaProject, final String defaultPath, final String pkgName,
			final String type) throws Exception {
		IPackageFragment pkgFrgmt = null;
		for (final IPackageFragment packageFragment : getPackagesInProject(javaProject, defaultPath, type)) {
			if (packageFragment.getElementName().equals(pkgName)) {
				pkgFrgmt = packageFragment;
				break;
			}
		}
		return pkgFrgmt;
	}

	/**
	 * @param newFileObj
	 * @param project
	 * @return
	 * @throws Exception
	 */
	public static boolean proceedWithAutoCheckin(final File newFileObj, final IProject project) throws Exception {
		final VersionControlPreferences versionControlPreferences = VersionControlPreferences.getInstance();
		if (newFileObj != null) {
			addOrUpdateFileStatusInCache(newFileObj);
		}
		//checkinCache.getFilesToCheckIn().add(new FastCodeFileForCheckin(INITIATED, newFileObj.getAbsolutePath()));
		final boolean prjShared = !isEmpty(project.getPersistentProperties());
		final boolean prjConfigured = !isEmpty(isPrjConfigured(project.getName()));

		return versionControlPreferences.isEnable() && prjShared && prjConfigured;
	}

	/**
	 * @param snippet
	 * @param target
	 * @param editorPart
	 * @param spacesBeforeCursor
	 * @throws Exception
	 */
	private void createSnippetFromTag(String snippet, final Object target, IEditorPart editorPart, final String spacesBeforeCursor)
			throws Exception {
		snippet = format(snippet, spacesBeforeCursor);
		if (target != null) {
			IEditorPart edPart = null;
			if (target instanceof IFile) {
				edPart = getEditorPartFromFile((IFile) target);
			} else if (target instanceof IType) {
				edPart = findEditor((IType) target);
			}
			if (edPart != null) {
				editorPart = edPart;
			}
		}

		final ITextSelection selection = (ITextSelection) editorPart.getEditorSite().getSelectionProvider().getSelection();
		final ITextEditor editor = (ITextEditor) editorPart.getAdapter(ITextEditor.class);
		final IDocumentProvider documentProvider = editor.getDocumentProvider();
		final IDocument document = documentProvider.getDocument(editor.getEditorInput());
		final String finalSnippet = snippet.trim();
		document.replace(selection.getOffset(), 0, finalSnippet);
	}

	/*public class FastCodeCheckedTreeSelectionDialog extends CheckedTreeSelectionDialog {

		public FastCodeCheckedTreeSelectionDialog(Shell parent, ILabelProvider labelProvider, ITreeContentProvider contentProvider) {
			super(parent, labelProvider, contentProvider);

		}

		@Override
		protected CheckboxTreeViewer getTreeViewer() {
			CheckboxTreeViewer treeViewer = super.getTreeViewer();
			treeViewer.getTree().addListener(SWT.Selection, new Listener() {

				public void handleEvent(Event e) {
					if (e.detail == SWT.CHECK) {
						System.out.println(e.item);
					}
				}
			});
			return treeViewer;
		}
	}*/
}
