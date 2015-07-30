package org.fastcode.util;

import static org.fastcode.common.FastCodeConstants.COMMA;
import static org.fastcode.common.FastCodeConstants.DIR_TO_SKIP;
import static org.fastcode.common.FastCodeConstants.EMPTY_STR;
import static org.fastcode.common.FastCodeConstants.FC_PLUGIN;
import static org.fastcode.common.FastCodeConstants.FORWARD_SLASH;
import static org.fastcode.util.FastCodeUtil.getEmptyArrayForNull;
import static org.fastcode.util.SourceUtil.getRepositoryServiceClass;
import static org.fastcode.util.SourceUtil.isBinaryResource;
import static org.fastcode.util.SourceUtil.isFileReferenced;
import static org.fastcode.util.SourceUtil.refreshProject;
import static org.fastcode.util.StringUtil.isEmpty;
import static org.fastcode.util.VersionControlUtil.addFileToCache;
import static org.fastcode.util.VersionControlUtil.isPrjConfigured;

import java.io.File;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourceAttributes;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.fastcode.common.FastCodeConstants.CHECK_IN;
import org.fastcode.exception.FastCodeRepositoryException;
import org.fastcode.popup.actions.snippet.FastCodeCache;
import org.fastcode.preferences.VersionControlPreferences;
import org.fastcode.setting.GlobalSettings;
import org.fastcode.versioncontrol.FastCodeCheckinCache;

public class FastCodeResourceChangeListener implements IResourceChangeListener {
	final FastCodeCache		fastCodeCache	= FastCodeCache.getInstance();

	private IResourceDelta	affectedChild;
	IFile					affectedFile;
	boolean					checkinYesNo;
	String					message;
	String					title;
	private static IResourceChangeListener resourceChangeListener = new FastCodeResourceChangeListener();

	public static FastCodeResourceChangeListener getInstance() {
		return (FastCodeResourceChangeListener) resourceChangeListener;
	}

	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event == null) {
			return;
		}
		final IResourceDelta delta = event.getDelta();
		if (delta == null) {
			return;
		}
		getLastChild(delta);
		if (this.affectedChild != null && this.affectedChild.getResource().getProject().getName().equals(FC_PLUGIN)) {
			return;
		}

		final VersionControlPreferences versionControlPreferences = VersionControlPreferences.getInstance();
		if (!(versionControlPreferences.isEnable() && versionControlPreferences.isEnableResChngListener())) {
			return;
		}
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {


			final String resourcePath = this.affectedChild.getResource().getFullPath().toString();
			if (isEmpty(resourcePath)) {
				return;
			}

			if (this.affectedChild.getKind() == IResourceDelta.CHANGED) {
				return;
			}
			try {
				final IResource resource = this.affectedChild.getResource();
				if (resource == null) {
					return;
				}
				if (isDirExcluded(resource)) {
					return;
				}

				/*if (!RepositoryProvider.isShared(resource.getProject()) || !resource.getProject().isOpen()) { //) {
					return;
				}*/

				if (isEmpty(resource.getProject().getPersistentProperties()) || !resource.getProject().isOpen()
						|| !resource.getProject().isAccessible()) {
					return;
				}

				final boolean prjConfigured = !isEmpty(isPrjConfigured(resource.getProject().getName()));
				if (!prjConfigured) {
					return;
				}

				if (resource.isHidden()) { // || !resource.isAccessible()) {
					return;
				}

				final ResourceAttributes resourceAttributes = resource.getResourceAttributes();
				if (resourceAttributes != null && resourceAttributes.isReadOnly() || isBinaryResource(resource)) {
					return;
				}

				final File file = new File(resource.getLocationURI());
				final FastCodeCheckinCache checkinCache = FastCodeCheckinCache.getInstance();

				for (final FastCodeFileForCheckin fileForCheckin : checkinCache.getFilesToCheckIn()) {
					if (fileForCheckin.getFileFullName().equals(file.getAbsolutePath())) {
						return;
					}
				}
				final RepositoryService checkin = getRepositoryServiceClass();
				//file.getName().contains(".java") &&
				/*if (this.affectedChild.getFlags() == IResourceDelta.MOVED_TO || this.affectedChild.getFlags() == IResourceDelta.MOVED_FROM) {
					System.out.println(this.affectedChild.getMovedToPath().toString());
					System.out.println(this.affectedChild.getFullPath());
					System.out.println("KKKKKK " + this.affectedChild.getMovedToPath().toOSString());
					final String[] resPathArr = this.affectedChild.getMovedToPath().toString().split(FILE_SEPARATOR);
					StringBuilder resFQName = new StringBuilder();
					for (int i = 3; i < resPathArr.length; i++) {
						resFQName = resFQName.append(EMPTY_STR.equalsIgnoreCase(resFQName.toString()) ? resPathArr[i] : DOT + resPathArr[i]);
					}
					final IResource movedToRes = getResourceFromWorkspace(resFQName.toString());
					checkin.checkInFile(new File(movedToRes.getLocationURI()), "", resource.getProject());
					return;
				}*/

				if (this.affectedChild.getKind() == IResourceDelta.ADDED) { //|| this.affectedChild.getKind() == IResourceDelta.CHANGED || ) {
					final IWorkspace workspace = ResourcesPlugin.getWorkspace();
					final IPath location = Path.fromOSString(file.getAbsolutePath());
					this.affectedFile = workspace.getRoot().getFileForLocation(location);
					if (checkin.isFileInRepository(file)) {
						return;
					}
					this.message = "Do you want to check in file ";
					this.title = "Check In";
					final String comment = EMPTY_STR;
					/*final List<String> cmntsFromRepo = checkin.getPreviousComments(getIFileFromFile(file).getProject().getName());
					final List<String> cmntsFromCache = getPreviousCommentsFromCache(getIFileFromFile(file));

					final FastCodeCheckinCommentsData comboData = new FastCodeCheckinCommentsData();
					comboData.setComntsFromCache(cmntsFromCache);
					comboData.setComntsFromRepo(cmntsFromRepo);
					final FastCodeCheckinCommentsDialog fastCodeCombo = new FastCodeCheckinCommentsDialog(new Shell(), comboData);
					if (fastCodeCombo.open() == Window.CANCEL) {
						comment = EMPTY_STR;
					} else {
						comment = comboData.getFinalComment();
					}*/

					//String userAnswer = EMPTY_STR;
					/*final UserQuestion userQuestion = new UserQuestion(FastCodeResourceChangeListener.this.affectedFile);
					final FutureTask<String> futureUserAnswer = new FutureTask<String>(userQuestion);
					Display.getDefault().syncExec(futureUserAnswer);
					try {
						comment = futureUserAnswer.get();
					} catch (final InterruptedException ex) {
						throw new FastCodeRepositoryException(ex.getMessage(), ex.getCause());
					} catch (final ExecutionException ex) {
						throw new FastCodeRepositoryException(ex.getMessage(), ex.getCause());
					}*/

					checkInNow(new Callback() {

						@Override
						public void onSuccess(final boolean value) throws FastCodeRepositoryException {
							if (value) {

								try {
									final String userAnswer = EMPTY_STR;
									/*final VersionControlUtil versionControlUtil = new VersionControlUtil();
									final UserQuestion userQuestion = versionControlUtil.new UserQuestion(FastCodeResourceChangeListener.this.affectedFile);
									final FutureTask<String> futureUserAnswer = new FutureTask<String>(userQuestion);
									Display.getDefault().syncExec(futureUserAnswer);
									try {
										userAnswer = futureUserAnswer.get();
									} catch (final InterruptedException ex) {
										throw new FastCodeRepositoryException(ex.getMessage(), ex.getCause());
									} catch (final ExecutionException ex) {
										throw new FastCodeRepositoryException(ex.getMessage(), ex.getCause());
									}*/
									checkin.checkInFile(file, userAnswer, resource.getProject());
									refreshProject(resource.getProject().getName());
								} catch (final FastCodeRepositoryException ex) {
									throw ex;
								}
							} else {
								addFileToCache(file, comment);
							}
						}
					});
					return;
				}

				if (this.affectedChild.getKind() == IResourceDelta.REMOVED) { //|| this.affectedChild.getKind() == IResourceDelta.CHANGED || ) {
					final IWorkspace workspace = ResourcesPlugin.getWorkspace();
					final IPath location = Path.fromOSString(file.getAbsolutePath());
					this.affectedFile = workspace.getRoot().getFileForLocation(location);
					final boolean fileReferenced = isFileReferenced(file, IJavaSearchConstants.TYPE);

					if (fileReferenced) {
						if (!MessageDialog.openQuestion(new Shell(), "File(s) check in",
								"Will check in only the selected file and not any dependencies. Do you want to proceed??")) {
							return;
						}
					}
					this.message = "Do you want to delete file from repository ";
					this.title = "Commit deletion";

					if (!checkin.isFileInRepository(file)) {
						return;
					}


					checkInNow(new Callback() {

						@Override
						public void onSuccess(final boolean value) throws FastCodeRepositoryException {
							if (value) {

								try {
									final String userAnswer = "Deleted";
									/*final UserQuestion userQuestion = new UserQuestion(FastCodeResourceChangeListener.this.affectedFile);
									final FutureTask<String> futureUserAnswer = new FutureTask<String>(userQuestion);
									Display.getDefault().syncExec(futureUserAnswer);
									try {
										userAnswer = futureUserAnswer.get();
									} catch (final InterruptedException ex) {
										throw new FastCodeRepositoryException(ex.getMessage(), ex.getCause());
									} catch (final ExecutionException ex) {
										throw new FastCodeRepositoryException(ex.getMessage(), ex.getCause());
									}*/
									checkin.checkInFile(file, userAnswer, resource.getProject());
									refreshProject(resource.getProject().getName());
								} catch (final FastCodeRepositoryException ex) {
									throw ex;
								}
							}
						}
					});
					return;
				}

			} catch (final Exception ex1) {
				ex1.printStackTrace();
			}

		} else if (event.getType() == IResourceChangeEvent.PRE_REFRESH) {
		} else if (event.getType() == IResourceChangeEvent.POST_BUILD) {
		} else if (event.getType() == IResourceChangeEvent.PRE_BUILD) {
		} else if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
		} else if (event.getType() == IResourceChangeEvent.PRE_DELETE) {
		}
	}

	private boolean isDirExcluded(final IResource resource) {
		final VersionControlPreferences versionControlPreferences = VersionControlPreferences.getInstance();
		final String prefix = FORWARD_SLASH + resource.getProject().getName() + FORWARD_SLASH;
		boolean dirExclude = false;
		boolean dirSkip = false;
		for (final String excludDir : getEmptyArrayForNull(versionControlPreferences.getExcludeDir())) {
			if (isEmpty(excludDir)) {
				continue;
			}
			if (resource.getFullPath().toString().startsWith(prefix + excludDir)) {
				dirExclude = true;
				break;
			}
		}

		final GlobalSettings globalSettings = GlobalSettings.getInstance();
		final String dirToSkip = globalSettings.getPropertyValue(DIR_TO_SKIP.toUpperCase(), EMPTY_STR);
		for (final String dir : getEmptyArrayForNull(dirToSkip.split(COMMA))) {
			if (isEmpty(dir)) {
				continue;
			}
			if (resource.getFullPath().toString().contains(dir)) {
				dirSkip = true;
				break;
			}
		}

		if (dirExclude || dirSkip) {
			return true;
		}

		return false;
	}

	private void getLastChild(final IResourceDelta delta) {
		final IResourceDelta children[] = delta.getAffectedChildren();
		for (final IResourceDelta child : children) {
			if (child.getAffectedChildren() != null && child.getAffectedChildren().length == 0) {
				this.affectedChild = child;
				return;
			}

			getLastChild(child);
		}
	}

	/*private IResourceDelta getAffectedChildren(final IResourceDelta delta, StringBuilder resourcePath) {

		if (delta.getAffectedChildren() != null && delta.getAffectedChildren().length > 0) {
			IResourceDelta resDelta = null;
			for (final IResourceDelta delt1 : delta.getAffectedChildren()) {
				resDelta = delt1;
				//resourceDelta = delt1;
				resourcePath = resourcePath.replace(0, resourcePath.length(), delt1.getFullPath().toString());
				//resDelta =
						getAffectedChildren(delt1, resourcePath);

			}
			//
			return resDelta;
		}
		return delta;
	}*/

	public void checkInNow(final Callback callback) throws FastCodeRepositoryException {
		final VersionControlPreferences versionControlPreferences = VersionControlPreferences.getInstance();
		if (isEmpty(versionControlPreferences.getCheckIn())) {
			MessageDialog.openWarning(new Shell(), "Check in Preference not set, adding changes to cache",
					"Please set the check in preference in window->preferences->Fast code Preference-> Version Control");
			callback.onSuccess(false);
			return;
		}
		if (versionControlPreferences.getCheckIn().equals(CHECK_IN.CHECK_IN.getValue())) {
			callback.onSuccess(true);
			return;
		} else if (versionControlPreferences.getCheckIn().equals(CHECK_IN.ASK_BEFORE_CHECKIN.getValue())) {
			Display.getDefault().syncExec(new Runnable() {
				@Override
				public void run() {
					final boolean answer = MessageDialog.openQuestion(new Shell(), FastCodeResourceChangeListener.this.title,
							FastCodeResourceChangeListener.this.message + FastCodeResourceChangeListener.this.affectedChild.getFullPath()
									+ " now?");
					try {
						callback.onSuccess(answer);
					} catch (final FastCodeRepositoryException ex) {
						//throw new FastCodeRepositoryException(ex.getMessage(), ex.getCause());
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}
			});
			return;
		} else if (versionControlPreferences.getCheckIn().equals(CHECK_IN.DONOT_CHECKIN.getValue())) {
			callback.onSuccess(false);
			return;
		}
		callback.onSuccess(false);
	}

}

interface Callback {
	void onSuccess(boolean value) throws FastCodeRepositoryException;
}
