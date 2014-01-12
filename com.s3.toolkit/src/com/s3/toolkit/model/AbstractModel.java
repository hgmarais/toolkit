package com.s3.toolkit.model;

import java.util.LinkedList;
import java.util.List;

public class AbstractModel implements Model {
	
	private AbstractModel parent;
	
	private List<ModelListener> listeners;

	public AbstractModel() {
		this(null);
	}
	
	public AbstractModel(AbstractModel parent) {
		this.parent = parent;
	}
	
	/* (non-Javadoc)
	 * @see com.s3.toolkit.model.Model#getParent()
	 */
	@Override
	public AbstractModel getParent() {
		return parent;
	}
	
	/* (non-Javadoc)
	 * @see com.s3.toolkit.model.Model#addListener(com.s3.toolkit.model.ModelListener)
	 */
	@Override
	public void addListener(ModelListener listener) {
		if (listeners == null) {
			listeners = new LinkedList<ModelListener>();
		}
		
		listeners.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see com.s3.toolkit.model.Model#removeListener(com.s3.toolkit.model.ModelListener)
	 */
	@Override
	public void removeListener(ModelListener listener) {
		if (listeners == null) {
			return;
		}
		
		listeners.remove(listener);
		
		if (listeners.isEmpty()) {
			listeners = null;
		}
	}
	
	protected void fireChildAdded(Model child) {
		fireEvent(new ChildAddedEvent(this, child));
	}
	
	protected void fireChildRemoved(Model child) {
		fireEvent(new ChildRemovedEvent(this, child));
	}
	
	protected void firePropertyChange(String propertyName, Object value) {
		fireEvent(new PropertyChangeEvent(this, propertyName, value));
	}
	
	protected void fireEvent(ModelEvent event) {
		if (listeners != null) {
			for (ModelListener listener : listeners) {
				listener.handleEvent(event);
			}
		}
		
		if (parent != null) {
			parent.fireEvent(event);
		}
	}
	
}
