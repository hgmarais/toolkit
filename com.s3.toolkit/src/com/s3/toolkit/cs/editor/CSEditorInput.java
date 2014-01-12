package com.s3.toolkit.cs.editor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

public class CSEditorInput implements IEditorInput {

	private final int id;

  public CSEditorInput(int id) {
      this.id = id;
  }
  
  public int getId() {
      return id;
  }

  @Override
  public boolean exists() {
      return true;
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
      return null;
  }

  @Override
  public String getName() {
      return String.valueOf(id);
  }

  @Override
  public IPersistableElement getPersistable() {
      return null;
  }

  @Override
  public String getToolTipText() {
      return "Edits a CS_Config.xml";
  }

  @Override
  public Object getAdapter(Class adapter) {
      return null;
  }

  @Override
  public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + id;
      return result;
  }

  @Override
  public boolean equals(Object obj) {
      if (this == obj)
          return true;
      if (obj == null)
          return false;
      if (getClass() != obj.getClass())
          return false;
      CSEditorInput other = (CSEditorInput) obj;
      if (id != other.id)
          return false;
      return true;
  }

}
