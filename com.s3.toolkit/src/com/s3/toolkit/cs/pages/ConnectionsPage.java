package com.s3.toolkit.cs.pages;

import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

public class ConnectionsPage extends FormPage {

	public static final String ID = "connections";

	public ConnectionsPage(FormEditor editor) {
		super(editor, ID, "Connections");
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
	}

}
