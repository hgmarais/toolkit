package com.s3.toolkit.gui.editor;

import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.text.ParseException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.synth.SynthLookAndFeel;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

import com.s3.toolkit.synth.SynthResources;

public class PageEditor extends EditorPart {
	
	private int lafIndex = 0;
	
	private Frame frame;

	private SynthLookAndFeel laf;

	public PageEditor() {
		System.out.println("PageEditor");
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
	}

	@Override
	public void doSaveAs() {
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		setSite(site);
		setInput(input);
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite parent) {
		laf = new SynthLookAndFeel();

		try {
			laf.load(SynthResources.class.getResourceAsStream("laf.xml"), SynthResources.class);
			UIManager.setLookAndFeel(laf);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND);
    frame = SWT_AWT.new_Frame(composite);
    frame.setLayout(new FlowLayout());
    
		JLabel label = new JLabel("Label");
    JButton button = new JButton("Change");
    
    button.addActionListener(new ActionListener() {
    	public void actionPerformed(java.awt.event.ActionEvent arg0) {
    		lafIndex++;
    		
    		if (lafIndex == 2) {
    			lafIndex = 0;
    		}
    		
    		reloadLookAndFeel();
    	}
    });
    
    frame.add(label);
    frame.add(button);
	}

	protected void reloadLookAndFeel() {
		String name = "laf"+lafIndex+".xml";
		System.out.println("toggle : "+name);
		
		try {
			laf.load(SynthResources.class.getResourceAsStream(name), SynthResources.class);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		SynthLookAndFeel.updateStyles(frame);
	}

	@Override
	public void setFocus() {
	}

}
