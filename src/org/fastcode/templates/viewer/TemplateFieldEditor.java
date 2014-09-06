package org.fastcode.templates.viewer;

import static org.fastcode.common.FastCodeConstants.FAST_CODE_PLUGIN_ID;
import static org.fastcode.preferences.PreferenceConstants.P_DATABASE_TEMPLATE_PREFIX;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.fastcode.common.FastCodeConstants.FIELDS;
import org.fastcode.common.FastCodeConstants.FIRST_TEMPLATE;
import org.fastcode.common.FastCodeConstants.SECOND_TEMPLATE;
//import org.fastcode.templates.contentassist.FCTagAssistProcessor;
import org.fastcode.templates.contentassist.FCTagAssistProcessor;
import org.fastcode.templates.contentassist.ITemplateContentAssistant;
import org.fastcode.templates.contentassist.TemplateAssistProcessor;
import org.fastcode.templates.rules.FastCodeTemplateCodeScanner;
import org.fastcode.templates.rules.IRulesStrategy;
import org.fastcode.templates.rules.ParameterRulesContext;
import org.fastcode.templates.rules.FastCodeRulesContext;

public class TemplateFieldEditor extends FieldEditor {

	FastCodeRulesContext					ctx						= new FastCodeRulesContext();
	private final Composite					parent;
	private FastCodeTemplateViewer			fastCodeTemplateViewerField;
	public static final int					VALIDATE_ON_KEY_STROKE	= 0;

	public static final int					VALIDATE_ON_FOCUS_LOST	= 1;

	public static int						UNLIMITED				= -1;

	private final int						style;

	final IPreferenceStore					store					= new ScopedPreferenceStore(new InstanceScope(), FAST_CODE_PLUGIN_ID);

	int										validateStrategie		= VALIDATE_ON_KEY_STROKE;
	private String							oldValue;

	ITemplateContentAssistant[]				assistants				= null;
	ITemplateContentAssistant[]				fcTagAssistants				= null;
	IRulesStrategy[]						ruleStrategies			= null;
	IRulesStrategy[]						fcTagRuleStrategies			= null;
	ITextHover								textHover				= null;
	IAutoEditStrategy[]						autoEditStrategies		= null;
	TemplateAssistProcessor					templateAssistProcessor	= new TemplateAssistProcessor();
	FCTagAssistProcessor					fcTagAssistProcessor	= new FCTagAssistProcessor();
	Map<FIRST_TEMPLATE, SECOND_TEMPLATE>	templateItemsMap		= new HashMap<FIRST_TEMPLATE, SECOND_TEMPLATE>();

	public TemplateFieldEditor(final String templateName, final String labelText, final Composite parent, final String templateType,
			final FIELDS field, final int style) {

		init(templateName, labelText);
		this.parent = parent;
		this.style = style;
		// createLabel(labelText);
		createField(templateName, templateType, labelText, field);

	}

	/*
	 * returns the content of the template field
	 */
	public String getStringValue() {

		return this.fastCodeTemplateViewerField.getDocument().get();
	}

	/*
	 * @param template name creates an new template field
	 */
	private void createField(final String templateName, final String templateType, final String labelText, final FIELDS field) {

		getLabelControl(this.parent);
		this.fastCodeTemplateViewerField = this.style == SWT.SINGLE ? new FastCodeTemplateViewer(this.parent, SWT.BORDER | SWT.RESIZE)
				: new FastCodeTemplateViewer(this.parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.RESIZE);
		final GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		this.fastCodeTemplateViewerField.getControl().setLayoutData(gd);

		configureFastCodeTemplateViewer(templateType, labelText, field);

		switch (this.validateStrategie) {
		case VALIDATE_ON_KEY_STROKE:
			this.fastCodeTemplateViewerField.getControl().addKeyListener(new KeyAdapter() {

				/* (non-Javadoc)
				 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
				 */
				@Override
				public void keyReleased(final KeyEvent e) {
					valueChanged();
				}
			});
			this.fastCodeTemplateViewerField.getControl().addFocusListener(new FocusAdapter() {
				// Ensure that the value is checked on focus loss in case we
				// missed a keyRelease or user hasn't released key.
				// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=214716
				@Override
				public void focusLost(final FocusEvent e) {
					valueChanged();
				}
			});

			break;
		case VALIDATE_ON_FOCUS_LOST:
			this.fastCodeTemplateViewerField.getControl().addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(final KeyEvent e) {
					clearErrorMessage();
				}
			});
			this.fastCodeTemplateViewerField.getControl().addFocusListener(new FocusAdapter() {
				@Override
				public void focusGained(final FocusEvent e) {
					refreshValidState();
				}

				@Override
				public void focusLost(final FocusEvent e) {
					valueChanged();
					clearErrorMessage();
				}
			});
			break;
		default:
			Assert.isTrue(false, "Unknown validate strategy");//$NON-NLS-1$
		}

	}

	protected void valueChanged() {
		fireValueChanged(VALUE, this.oldValue, this.fastCodeTemplateViewerField.getDocument().get());

	}

	public Control getControl() {
		return this.fastCodeTemplateViewerField.getControl();
	}

	public void setText(final String txt) {

		this.fastCodeTemplateViewerField.setDocument(new Document(txt));
	}

	public void setEmptyStringAllowed(final boolean emptyStringAllowed) {
	}

	public void setEditable(final boolean edit) {
		this.fastCodeTemplateViewerField.setEditable(edit);
	}

	@Override
	public void setEnabled(final boolean value, final Composite parent) {

		super.setEnabled(value, parent);
		this.fastCodeTemplateViewerField.getControl().setEnabled(value);
	}

	public void setLayout(final GridData gd) {
		this.fastCodeTemplateViewerField.getControl().setLayoutData(gd);
	}

	/*
	 * apply content assist and coloring rules for the template field
	 */
	public void configureFastCodeTemplateViewer(final String templateType, final String labelText, final FIELDS field) {

		if (field.equals(FIELDS.ADDITIONAL_PARAMETER)) {

			final ParameterRulesContext pc = new ParameterRulesContext();
			this.assistants = pc.getTemplateContentAssistants(null);
			this.autoEditStrategies = pc.getTemplateAutoEditStrategies();
			this.textHover = pc.getTemplateTextHover(null);
			this.ruleStrategies = pc.getParameterRuleStrategies();
		} else {

			this.assistants = this.ctx.getTemplateContentAssistants(null);
			this.fcTagAssistants = this.ctx.getFCTagContentAssistants(null);
			this.ruleStrategies = this.ctx.getTemplateRuleStrategies();
			this.fcTagRuleStrategies = this.ctx.getFCTagRuleStrategies();
			if (templateType.equals(P_DATABASE_TEMPLATE_PREFIX)) {
				this.assistants = this.ctx.getDBTemplateContentAssistants(null);
			}
			/*if (templateType.equals(P_FILE_TEMPLATE_PREFIX)) {//commented as Sudha told not required
				this.assistants = this.ctx.getFileTemplateContentAssistants(null);
			}*/

			this.textHover = this.ctx.getTemplateTextHover(null);
			this.autoEditStrategies = this.ctx.getTemplateAutoEditStrategies();
		}

		this.templateAssistProcessor.setAssistants(this.assistants);
		//this.templateAssistProcessor.setFcMethodassistants(this.fcTagAssistants);
		this.fcTagAssistProcessor.setAssistants(this.fcTagAssistants);

		final SourceViewerConfiguration configuration = new FastCodeTemplateViewerConfiguration(new FastCodeTemplateCodeScanner(
				this.ruleStrategies), new FastCodeTemplateCodeScanner(
						this.fcTagRuleStrategies), this.textHover, this.autoEditStrategies, this.templateAssistProcessor, this.fcTagAssistProcessor);
		this.fastCodeTemplateViewerField.configure(configuration);

	}

	public void updateSourceConfigurations(final FIRST_TEMPLATE firstTemplate, final SECOND_TEMPLATE secondTemplate) {

		this.templateItemsMap.clear();
		this.templateItemsMap.put(firstTemplate, secondTemplate);
		this.assistants = this.ctx.getTemplateContentAssistants(this.templateItemsMap);
		this.fcTagAssistants = this.ctx.getFCTagContentAssistants(this.templateItemsMap);
		this.templateAssistProcessor.setAssistants(this.assistants);
		//this.templateAssistProcessor.setFcMethodassistants(this.fcTagAssistants);
		this.fcTagAssistProcessor.setAssistants(this.fcTagAssistants);
		final SourceViewerConfiguration configuration = new FastCodeTemplateViewerConfiguration(new FastCodeTemplateCodeScanner(
				this.ruleStrategies), new FastCodeTemplateCodeScanner(
						this.fcTagRuleStrategies), this.textHover, this.autoEditStrategies, this.templateAssistProcessor, this.fcTagAssistProcessor);
		this.fastCodeTemplateViewerField.configure(configuration);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.FieldEditor#adjustForNumColumns(int)
	 */
	@Override
	protected void adjustForNumColumns(final int numColumns) {
		final GridData gd = (GridData) this.fastCodeTemplateViewerField.getControl().getLayoutData();
		gd.horizontalSpan = numColumns - 1;
		// We only grab excess space if we have to
		// If another field editor has more columns then
		// we assume it is setting the width.
		// gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
		gd.grabExcessVerticalSpace = true;
		gd.heightHint = 100;

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.preference.FieldEditor#doFillIntoGrid(org.eclipse.swt
	 * .widgets.Composite, int)
	 */
	@Override
	protected void doFillIntoGrid(final Composite parent, final int numColumns) {
		// super.doFillIntoGrid(parent, numColumns);
		adjustForNumColumns(numColumns);

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.FieldEditor#doLoad()
	 */
	@Override
	protected void doLoad() {
		if (this.fastCodeTemplateViewerField != null) {
			getPreferenceName();
			final String value = getPreferenceStore().getString(getPreferenceName());
			this.fastCodeTemplateViewerField.setDocument(new Document(value));
			this.oldValue = value;
		}

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.FieldEditor#doLoadDefault()
	 */
	@Override
	protected void doLoadDefault() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.preference.FieldEditor#doStore()
	 */
	@Override
	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), this.fastCodeTemplateViewerField.getDocument().get());

	}

	@Override
	public int getNumberOfControls() {
		// TODO Auto-generated method stub
		return 2;
	}
}
