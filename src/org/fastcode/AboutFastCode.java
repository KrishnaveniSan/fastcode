package org.fastcode;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.fastcode.dialog.AboutFastCodeDialog;

public class AboutFastCode implements IEditorActionDelegate, IActionDelegate, IWorkbenchWindowActionDelegate {

	public void dispose() {
		// TODO Auto-generated method stub

	}

	public void init(IWorkbenchWindow arg0) {
		// TODO Auto-generated method stub

	}

	public void run(IAction arg0) {
		AboutFastCodeDialog aboutFastCodeDialog = new AboutFastCodeDialog(new Shell());
		aboutFastCodeDialog.open();

	}

	public void selectionChanged(IAction arg0, ISelection arg1) {
		// TODO Auto-generated method stub

	}

	public void setActiveEditor(IAction arg0, IEditorPart arg1) {
		// TODO Auto-generated method stub

	}
}
