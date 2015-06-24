package org.fastcode.dialog;

import static org.fastcode.common.FastCodeConstants.COMMON_CLASS_SUFFIX;
import static org.fastcode.common.FastCodeConstants.CURRENT_PACKAGE;
import static org.fastcode.common.FastCodeConstants.EMPTY_STR;
import static org.fastcode.common.FastCodeConstants.ENCLOSING_PACKAGE_STR;
import static org.fastcode.common.FastCodeConstants.FC_PLUGIN;
import static org.fastcode.common.FastCodeConstants.HYPHEN;
import static org.fastcode.common.FastCodeConstants.NEWLINE;
import static org.fastcode.util.MessageUtil.getChoiceFromMultipleValues;
import static org.fastcode.util.SourceUtil.getAlteredPackageName;
import static org.fastcode.util.SourceUtil.getDefaultPathFromProject;
import static org.fastcode.util.SourceUtil.getFolderFromPath;
import static org.fastcode.util.SourceUtil.getIJavaProjectFromName;
import static org.fastcode.util.SourceUtil.getPackagesInProject;
import static org.fastcode.util.StringUtil.isEmpty;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.JavaProject;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.fastcode.common.FastCodeColor;
import org.fastcode.common.FastCodePackage;
import org.fastcode.common.FastCodeProject;
import org.fastcode.common.OpenRequiredClassesData;
import org.fastcode.common.PackageSelectionDialog;
import org.fastcode.popup.actions.snippet.FastCodeCache;
import org.fastcode.setting.GlobalSettings;

public class OpenRequiredClassesDialog extends TrayDialog {
	private Combo				packageCombo;
	private Button				packageBrowseButton;
	private Button				closeOthers;
	private Text				errorMessageText;
	private Text				patternText;
	protected Shell				shell;
	OpenRequiredClassesData		openRequiredClassesData;
	protected final String		defaultMessage	= NEWLINE;
	private String				errorMessage;
	private Label				projectLabel;
	private Combo				projectCombo;
	Map<String, IProject>		prjMap			= new HashMap<String, IProject>();
	private IPackageFragment	currentPackage;

	/**
	 * @param parent
	 *
	 */
	@Override
	protected Control createDialogArea(final Composite parent) {
		final GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		parent.setLayout(layout);

		createErrorMessageText(parent);
		createProjectSelectionPane(parent);

		createPackageSelectionPane(parent);
		createPattern(parent);
		createCloseOthers(parent);
		this.packageCombo.setFocus();

		final String projectName = this.projectCombo.getText();
		if (!isEmpty(projectName)) {
			isPrjInSync(this.prjMap.get(projectName));
		}

		return parent;
	}

	private void createCloseOthers(final Composite parent) {
		final Composite composite = new Composite(parent, parent.getStyle());
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);

		final GridData gridDataLabel = new GridData();
		final Label label = new Label(composite, SWT.NONE);
		label.setText("Close Others:               ");
		label.setLayoutData(gridDataLabel);

		final GridData gridDataText = new GridData();
		gridDataText.grabExcessHorizontalSpace = true;

		this.closeOthers = new Button(composite, SWT.CHECK);
		this.closeOthers.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent event) {
				final Button b = (Button) event.getSource();
				OpenRequiredClassesDialog.this.openRequiredClassesData.setCloseOthers(b.getSelection());
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {

			}
		});

	}

	private void createPattern(final Composite parent) {
		final Composite composite = new Composite(parent, parent.getStyle());
		final GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setSize(300, 500);

		final GridData gridDataLabel = new GridData();
		final Label label = new Label(composite, SWT.NONE);
		label.setText("Pattern:                         ");
		label.setLayoutData(gridDataLabel);

		final GridData gridDataText = new GridData();
		gridDataText.grabExcessHorizontalSpace = true;

		this.patternText = new Text(composite, SWT.BORDER);
		this.patternText.setLayoutData(gridDataText);
		gridDataText.minimumWidth = 350;

		this.patternText.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				final Text text = (Text) e.widget;
				final String value = text.getText();
				//System.out.println(value.replace(ASTERISK, BACK_SLASH + ASTERISK));
				OpenRequiredClassesDialog.this.openRequiredClassesData.setPattern(value/*.replace(ASTERISK, BACK_SLASH + ASTERISK)*/);
			}
		});
	}

	/**
	 * @param shell
	 * @param createVariableData
	 */
	public OpenRequiredClassesDialog(final Shell shell, final OpenRequiredClassesData openRequiredClassesData) {
		super(shell);
		this.shell = shell;
		this.openRequiredClassesData = openRequiredClassesData;
	}

	@Override
	protected void configureShell(final Shell shell) {
		super.configureShell(shell);
		shell.setText("Open Required Classes");

	}

	private void createPackageSelectionPane(final Composite parent) {
		final Composite composite = new Composite(parent, parent.getStyle());
		final GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
		//composite.setSize(300, 500);

		final GridData gridDataLabel = new GridData();
		final Label label = new Label(composite, SWT.NONE);
		label.setText("Select Package:            ");
		label.setLayoutData(gridDataLabel);

		final GridData gridDataText = new GridData(GridData.FILL_HORIZONTAL);
		gridDataText.grabExcessHorizontalSpace = true;

		this.packageCombo = new Combo(composite, SWT.READ_ONLY | SWT.DROP_DOWN);// new
		// Text(composite,
		// SWT.BORDER);
		//this.packageCombo.setSize(50, 20);
		this.packageCombo.setLayoutData(gridDataText);
		gridDataText.minimumWidth = 450;
		final FastCodeCache fastCodeCache = FastCodeCache.getInstance();
		if (this.openRequiredClassesData.getCompUnit() != null) {
			this.currentPackage = this.openRequiredClassesData.getCompUnit().getPrimary().findPrimaryType().getPackageFragment();
			this.packageCombo.add(ENCLOSING_PACKAGE_STR + HYPHEN + getAlteredPackageName(this.currentPackage));
		}
		if (!fastCodeCache.getPackageSet().isEmpty()) {
			for (final IPackageFragment pkgFrgmt : fastCodeCache.getPackageSet()) {
				if (this.currentPackage != null && this.currentPackage.equals(pkgFrgmt)) {
					continue;
				}
				/*if (!isEmpty(project)) {
					if (!project.equals(pkgFrgmt.getJavaProject().getElementName())) {
						continue;
					}
				}*/

				boolean addItem = true;
				if (this.packageCombo.getItems() != null) {
					for (final String existingPkg : this.packageCombo.getItems()) {
						if (existingPkg.contains(ENCLOSING_PACKAGE_STR)) {
							continue;
						}
						if (existingPkg.equals(getAlteredPackageName(pkgFrgmt))) {
							addItem = false;
							break;
						}
					}
					if (addItem) {
						this.packageCombo.add(getAlteredPackageName(pkgFrgmt));
					}
				}
			}
		}
		this.packageCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent event) {
				final Combo pkgCombo = (Combo) event.widget;
				String selectedPkgName = pkgCombo.getText();
				if (selectedPkgName.contains(ENCLOSING_PACKAGE_STR)) {
					selectedPkgName = OpenRequiredClassesDialog.this.currentPackage.getElementName();
				}
				try {
					for (final IPackageFragment pkg : fastCodeCache.getPackageSet()) {
						if (getAlteredPackageName(pkg).equals(selectedPkgName)) {
							OpenRequiredClassesDialog.this.openRequiredClassesData.setFastCodePackage(new FastCodePackage(pkg));
						}
					}
				} catch (final Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {

			}

		});
		this.packageCombo.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(final FocusEvent e) {
				final Combo pkgCombo = (Combo) e.widget;
				String inputPkgName = pkgCombo.getText();
				if (!isEmpty(inputPkgName)) {
					if (inputPkgName.contains(ENCLOSING_PACKAGE_STR)) {
						inputPkgName = OpenRequiredClassesDialog.this.currentPackage.getElementName();
					}
					for (final IPackageFragment pkg : fastCodeCache.getPackageSet()) {
						if (pkg.getElementName().equals(inputPkgName) || getAlteredPackageName(pkg).equals(inputPkgName)) {
							return;
						}
					}
					if (inputPkgName.contains(CURRENT_PACKAGE)) {
						OpenRequiredClassesDialog.this.openRequiredClassesData.setFastCodePackage(new FastCodePackage(
								OpenRequiredClassesDialog.this.currentPackage));
						if (!fastCodeCache.getPackageSet().contains(OpenRequiredClassesDialog.this.currentPackage)) {
							fastCodeCache.getPackageSet().add(OpenRequiredClassesDialog.this.currentPackage);

						}
					}
				}

			}

			@Override
			public void focusGained(final FocusEvent arg0) {

			}
		});

		this.packageCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent e) {
				final Combo pkgCombo = (Combo) e.widget;
				if (isEmpty(pkgCombo.getText())) {
					setErrorMessage("Please choose a package");
				} else {
					setErrorMessage(OpenRequiredClassesDialog.this.defaultMessage);
				}
			}

		});

		final GridData gridDataButton = new GridData();

		this.packageBrowseButton = new Button(composite, SWT.PUSH);
		this.packageBrowseButton.setText("Browse");
		this.packageBrowseButton.setLayoutData(gridDataButton);
		final PackageSelectionDialog selectionDialog = null;

		this.packageBrowseButton.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent event) {
				final Button b = (Button) event.getSource();
				try {
					final String srcPath = getDefaultPathFromProject(
							getIJavaProjectFromName(OpenRequiredClassesDialog.this.openRequiredClassesData.getSelectedProject().getName()),
							"source", EMPTY_STR);
					final IPackageFragment allPackages[] = getPackagesInProject(
							getIJavaProjectFromName(OpenRequiredClassesDialog.this.openRequiredClassesData.getSelectedProject().getName()),
							srcPath, "source");
					if (allPackages == null) {
						return;
					}
					final PackageSelectionDialog selectionDialog = new PackageSelectionDialog(new Shell(), "Package ",
							"Choose a package from below", allPackages);
					IPackageFragment packageFragment = null;
					if (selectionDialog.open() != CANCEL) {
						packageFragment = (IPackageFragment) selectionDialog.getFirstResult();
						OpenRequiredClassesDialog.this.packageCombo.setText(getAlteredPackageName(packageFragment));
						boolean addItem = true;
						if (OpenRequiredClassesDialog.this.packageCombo.getItems() != null) {
							for (final String existingPkg : OpenRequiredClassesDialog.this.packageCombo.getItems()) {
								if (existingPkg.equals(getAlteredPackageName(packageFragment))) {
									if (!OpenRequiredClassesDialog.this.packageCombo.getText().equals(existingPkg)) {
										OpenRequiredClassesDialog.this.packageCombo.select(OpenRequiredClassesDialog.this.packageCombo
												.indexOf(existingPkg));

									}
									addItem = false;
									break;
								}
							}
						}
						if (addItem) {
							OpenRequiredClassesDialog.this.packageCombo.add(getAlteredPackageName(packageFragment));
							OpenRequiredClassesDialog.this.packageCombo.select(OpenRequiredClassesDialog.this.packageCombo.getItemCount() - 1);
						}
						if (!fastCodeCache.getPackageSet().contains(packageFragment)) {
							fastCodeCache.getPackageSet().add(packageFragment);
						}
						OpenRequiredClassesDialog.this.openRequiredClassesData.setFastCodePackage(new FastCodePackage(packageFragment));
					}

				} catch (final Exception ex) {
					ex.printStackTrace();
				}
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {

			}
		});
	}

	/**
	 * @param parent
	 */
	private void createProjectSelectionPane(final Composite parent) {
		final Composite composite = new Composite(parent, parent.getStyle());
		final GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);

		final GridData gridDataLabel = new GridData();
		this.projectLabel = new Label(composite, SWT.NONE);
		this.projectLabel.setText("Select Project:              ");
		this.projectLabel.setLayoutData(gridDataLabel);
		//this.projectLabel.setVisible(false);

		final GridData gridDataText = new GridData();
		gridDataText.grabExcessHorizontalSpace = true;

		this.projectCombo = new Combo(composite, SWT.READ_ONLY | SWT.DROP_DOWN);
		this.projectCombo.setSize(200, 20);
		this.projectCombo.setLayoutData(gridDataText);
		gridDataText.minimumWidth = 500;
		//this.projectCombo.setEnabled(false);
		//this.projectCombo.setVisible(false);
		final IProject projects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (final IProject prj : projects) {
			if (prj == null || !prj.exists() || !prj.isOpen()) {
				continue;
			}
			if (prj.getName().equals(FC_PLUGIN)) {
				continue;
			}
			this.projectCombo.add(prj.getName());
			this.prjMap.put(prj.getName(), prj);
		}


		if (this.openRequiredClassesData.getProject() != null) {
			this.projectCombo.select(this.projectCombo.indexOf(this.openRequiredClassesData.getProject().getName()));
			OpenRequiredClassesDialog.this.openRequiredClassesData.setSelectedProject(new FastCodeProject(
					OpenRequiredClassesDialog.this.prjMap.get(this.openRequiredClassesData.getProject().getName())));
		}

		this.projectCombo.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(final SelectionEvent event) {
				final String projectName = OpenRequiredClassesDialog.this.projectCombo.getText();
				if (!isEmpty(projectName)) {
					OpenRequiredClassesDialog.this.openRequiredClassesData.setSelectedProject(new FastCodeProject(
							OpenRequiredClassesDialog.this.prjMap.get(projectName)));
					clearErrorMessage(OpenRequiredClassesDialog.this.defaultMessage);
					isPrjInSync(OpenRequiredClassesDialog.this.prjMap.get(projectName));
				} else {
					setErrorMessage("Please select a project");
				}
				loadFolders(OpenRequiredClassesDialog.this.prjMap.get(projectName));
			}

			@Override
			public void widgetDefaultSelected(final SelectionEvent arg0) {

			}
		});

		/*this.projectCombo.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(final ModifyEvent arg0) {
				final String projectName = CreateSnippetDialog.this.projectCombo.getText();
				if (!isEmpty(projectName)) {
					isPrjInSync(CreateSnippetDialog.this.prjMap.get(projectName));
				}

			}
		});*/
	}

	protected void loadFolders(final IProject project) {
		try {
			if (project.isOpen() && JavaProject.hasJavaNature(project)) {
				final IJavaProject javaProject = JavaCore.create(project);

				IClasspathEntry[] classpathEntries = null;
				classpathEntries = javaProject.getResolvedClasspath(true);

				for (int i = 0; i < classpathEntries.length; i++) {
					final IClasspathEntry entry = classpathEntries[i];
					if (entry.getContentKind() == IPackageFragmentRoot.K_SOURCE) {
						final String relativePath = entry.getPath().toString();
						final IFolder folderFromPath = getFolderFromPath(project, relativePath);
						System.out.println(folderFromPath);
					}
				}

			}

		} catch (final Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	private String getCommonSuffix() {
		final GlobalSettings globalSettings = GlobalSettings.getInstance();
		final String commonSuffixes = globalSettings.getPropertyValue(COMMON_CLASS_SUFFIX.toUpperCase(), EMPTY_STR);
		final IJavaElement[] classesInPackage;
		final String suffix = null;
		/*try {
			classesInPackage = OpenRequiredClassesDialog.this.createVariableData.getPackageFragment().getChildren();
			final String[] commonSuffixArr = commonSuffixes.split(",");

			for (int k = 0; k < commonSuffixArr.length; k++) {
				for (int i = 0; i < classesInPackage.length; i++) {
					final String tmpClsName = classesInPackage[i].getElementName().substring(0,
							classesInPackage[i].getElementName().indexOf("."));
					if (!tmpClsName.toLowerCase().endsWith(commonSuffixArr[k].toLowerCase().trim())) {
						suffix = EMPTY_STR;
						break;
					}
					suffix = commonSuffixArr[k].trim();
				}
				if (suffix != null && !suffix.equals(EMPTY_STR)) {
					break;
				}
			}

		} catch (final JavaModelException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}*/
		if (suffix != null & suffix.trim().length() > 0) {
			final String[] choices = { "Yes", "No" };

			final String choice = getChoiceFromMultipleValues(this.shell, "Warning", "All the classes in this package end with " + suffix
					+ ". Do you want to add the same suffix to the class", choices);
			if (choice == null || choice.equals("No")) {
				return EMPTY_STR;
			}
		}
		return suffix == null ? EMPTY_STR : suffix;
	}

	/**
	 * @param parent
	 */
	protected void createErrorMessageText(final Composite parent) {

		final GridData errText = new GridData(590, 40);
		errText.grabExcessHorizontalSpace = true;

		this.errorMessageText = new Text(parent, SWT.READ_ONLY | SWT.WRAP);
		//this.errorMessageText.setForeground(new Color(null, 255, 0, 0));
		this.errorMessageText.setForeground(FastCodeColor.getErrorMsgColor());
		this.errorMessageText.setLayoutData(errText);
		setErrorMessage(this.defaultMessage);

	}

	/**
	 * @param errorMessage
	 */
	public void setMessage(final String errorMessage) {
		this.errorMessage = errorMessage;
		if (this.errorMessageText != null && !this.errorMessageText.isDisposed()) {
			this.errorMessageText.setText(errorMessage == null ? " \n " : errorMessage); //$NON-NLS-1$
			boolean hasError = false;
			if (errorMessage != null && !errorMessage.equals(this.defaultMessage)) {
				hasError = true;
			}
			this.errorMessageText.setEnabled(hasError);
			this.errorMessageText.setVisible(hasError);
			this.errorMessageText.getParent().update();
			final Control button = getButton(IDialogConstants.OK_ID);
			if (button != null) {
				button.setEnabled(errorMessage.equals(this.defaultMessage));
			}
		}
	}

	/**
	 * @param project
	 *
	 */
	private boolean isPrjInSync(final IProject project) {
		if (project != null && !project.isSynchronized(IResource.DEPTH_INFINITE)) {
			this.packageCombo.setEnabled(false);
			this.packageBrowseButton.setEnabled(false);
			setErrorMessage("Project " + project.getName() + " is not synchronised. Please synchronise and try again.");
			return false;
		} else {
			this.packageCombo.setEnabled(true);
			this.packageBrowseButton.setEnabled(true);
			clearErrorMessage(this.defaultMessage);
			return true;
		}
	}

	/**
	 * @param errorMessage
	 */
	public void setErrorMessage(final String errorMessage) {
		setMessage(errorMessage);
	}

	/**
	 * @param errorMessage
	 */
	public void clearErrorMessage(final String errorMessage) {
		setMessage(errorMessage);
	}

}
