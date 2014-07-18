/**
 * @author : Gautam

 * Created : 09/02/2010

 */

package org.fastcode.preferences;

import static org.fastcode.preferences.PreferenceConstants.P_COMMON_TEMPLATE_PREFIX;

import org.eclipse.ui.IWorkbenchPreferencePage;

public class AdditionalTemplatePreferencePage extends AbstractTableTemplatePreferencePage implements IWorkbenchPreferencePage {

	/**
	 *
	 */
	public AdditionalTemplatePreferencePage() {
		super();
		this.templatePrefix = P_COMMON_TEMPLATE_PREFIX;
		setDescription("Additional Template preference");
	}

	@Override
	protected String getAllTemplatesPreferenceKey() {
		return P_COMMON_TEMPLATE_PREFIX;
	}

	@Override
	protected boolean isDetailedTemplate() {
		return false;
	}

	@Override
	protected boolean isShowAllowedFileExtension() {
		return false;
	}
}
