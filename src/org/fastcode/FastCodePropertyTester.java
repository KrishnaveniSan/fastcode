package org.fastcode;

import org.eclipse.jface.preference.IPreferenceStore;

/*import org.eclipse.core.expressions.PropertyTester;*/

public class FastCodePropertyTester /*extends PropertyTester*/ {

	private IPreferenceStore		preferenceStore;

	/*public boolean test(final Object arg0, final String property, final Object[] arg2, final Object arg3) {
		this.preferenceStore = new ScopedPreferenceStore(new InstanceScope(), FAST_CODE_PLUGIN_ID);
		if ("versioncontrol".equals(property)) {
			System.out.println(this.preferenceStore.getBoolean(P_ENABLE_AUTO_CHECKIN));
			return this.preferenceStore.getBoolean(P_ENABLE_AUTO_CHECKIN);
			}
		return false;
	}*/

}
