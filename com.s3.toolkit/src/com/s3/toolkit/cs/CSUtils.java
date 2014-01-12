package com.s3.toolkit.cs;

import java.util.Arrays;
import java.util.EventObject;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.FocusCellOwnerDrawHighlighter;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
import org.eclipse.jface.viewers.TableViewerFocusCellManager;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Table;

import com.s3.toolkit.cs.model.ParameterModel;

public class CSUtils {
	
	public static void addParameterColumns(final TableViewer tableViewer) {
		/* Enable edit on double click. */
		TableViewerFocusCellManager focusCellManager = new TableViewerFocusCellManager(tableViewer, new FocusCellOwnerDrawHighlighter(tableViewer));

		ColumnViewerEditorActivationStrategy activationSupport = new ColumnViewerEditorActivationStrategy(tableViewer) {
		    protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
		        // Enable editor only with mouse double click
		        if (event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION) {
		            EventObject source = event.sourceEvent;
		            if (source instanceof MouseEvent && ((MouseEvent)source).button == 3)
		                return false;

		            return true;
		        }

		        return false;
		    }
		};

		TableViewerEditor.create(tableViewer, focusCellManager, activationSupport, ColumnViewerEditor.TABBING_HORIZONTAL | 
		    ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | 
		    ColumnViewerEditor.TABBING_VERTICAL |
		    ColumnViewerEditor.KEYBOARD_ACTIVATION);
		
		/* Register */
		TableViewerColumn registerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		registerColumn.getColumn().setText("Register");
		registerColumn.getColumn().setWidth(50);
		registerColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ParameterModel)element).getRegister();
			}
		});
		
		/* Tag Name */
		TableViewerColumn tagNameColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		tagNameColumn.getColumn().setText("Tag Name");
		tagNameColumn.getColumn().setWidth(150);
		tagNameColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ParameterModel)element).getTagName();
			}
		});
		
		/* Prefix */
		TableViewerColumn prefixColumn = new TableViewerColumn(tableViewer, SWT.NONE);
		prefixColumn.getColumn().setText("Prefix");
		prefixColumn.getColumn().setWidth(50);
		prefixColumn.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ParameterModel)element).getPrefix();
			}
		});
		
		Table table = tableViewer.getTable();
		
		String[] columnProperties = new String[]{ParameterModel.REGISTER_ATTRIBUTE, ParameterModel.TAG_NAME_ATTRIBUTE, ParameterModel.PREFIX_ATTRIBUTE};
		CellEditor[] cellEditors = new CellEditor[columnProperties.length];
		TextCellEditor cellEditor = new TextCellEditor(table);
		Arrays.fill(cellEditors, cellEditor);
		
		tableViewer.setColumnProperties(columnProperties);
		tableViewer.setCellEditors(cellEditors);
		tableViewer.setCellModifier(new ICellModifier() {
			
			@Override
			public void modify(Object element, String property, Object value) {
				ParameterModel parameter = null;
				
				if (element instanceof Item) {
					parameter = (ParameterModel) ((Item) element).getData();
				} else {
					parameter = (ParameterModel) element;
				}
				
				if (ParameterModel.REGISTER_ATTRIBUTE.equals(property)) {
					parameter.setRegister((String)value);
				} else if (ParameterModel.TAG_NAME_ATTRIBUTE.equals(property)) {
					parameter.setTagName((String)value);
				} else if (ParameterModel.PREFIX_ATTRIBUTE.equals(property)) {
					parameter.setPrefix((String)value);
				}
				
				tableViewer.update(parameter, null);
			}
			
			@Override
			public Object getValue(Object element, String property) {
				ParameterModel parameter = (ParameterModel) element;
				
				if (ParameterModel.REGISTER_ATTRIBUTE.equals(property)) {
					return parameter.getRegister();
				} else if (ParameterModel.TAG_NAME_ATTRIBUTE.equals(property)) {
					return parameter.getTagName();
				} else if (ParameterModel.PREFIX_ATTRIBUTE.equals(property)) {
					return parameter.getPrefix();
				}
				
				return null;
			}
			
			@Override
			public boolean canModify(Object element, String property) {
				return true;
			}
			
		});
	}

}
