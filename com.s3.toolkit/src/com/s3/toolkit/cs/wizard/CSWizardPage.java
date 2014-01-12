package com.s3.toolkit.cs.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class CSWizardPage extends WizardPage {

	private Composite container;
	
	private Text nameText;

	public CSWizardPage() {
		super("Connectivity Server");
    setTitle("Connectivity Server");
    setDescription("Create a new Connectivity Server project.");
	}
	
	public String getProjectName() {
		return nameText.getText().trim();
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NONE);
    GridLayout layout = new GridLayout();
    container.setLayout(layout);
    layout.numColumns = 2;
    Label nameLabel = new Label(container, SWT.NONE);
    nameLabel.setText("Project name:");

    nameText = new Text(container, SWT.BORDER | SWT.SINGLE);
    nameText.setText("");
    nameText.addKeyListener(new KeyListener() {

      @Override
      public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

      }

      @Override
      public void keyReleased(KeyEvent e) {
        if (!nameText.getText().isEmpty()) {
          setPageComplete(true);
        }
      }

    });
    GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    nameText.setLayoutData(gd);
    Label labelCheck = new Label(container, SWT.NONE);
    labelCheck.setText("This is a check");
    Button check = new Button(container, SWT.CHECK);
    check.setSelection(true);
    // Required to avoid an error in the system
    setControl(container);
    setPageComplete(false);

	}

}
