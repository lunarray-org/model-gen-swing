/* 
 * Model Tools.
 * Copyright (C) 2013 Pal Hargitai (pal@lunarray.org)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.lunarray.model.generation.swing.components;

import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.OperationOutputStrategy;

/**
 * Describes a form component.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 */
public interface FormComponent<E> {

	/**
	 * Adds an action listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void addActionListener(ActionListener listener);

	/**
	 * Gets the value for the cancelButton field.
	 * 
	 * @return The value for the cancelButton field.
	 */
	JButton getCancelButton();

	/**
	 * Gets the component.
	 * 
	 * @return The component.
	 */
	JComponent getComponent();

	/**
	 * Gets the value for the entity field.
	 * 
	 * @return The value for the entity field.
	 */
	E getEntity();

	/**
	 * Gets the value for the form field.
	 * 
	 * @return The value for the form field.
	 */
	JPanel getForm();

	/**
	 * Gets the value for the submitButton field.
	 * 
	 * @return The value for the submitButton field.
	 */
	JButton getSubmitButton();

	/**
	 * Begin operation.
	 * 
	 * @param strategy
	 *            The strategy.
	 */
	void processBeginStrategy(OperationOutputStrategy<E> strategy);

	/**
	 * End operation.
	 * 
	 * @param strategy
	 *            The strategy.
	 */
	void processEndStrategy(OperationOutputStrategy<E> strategy);

	/**
	 * Process a strategy.
	 * 
	 * @param strategy
	 *            The strategy.
	 * @param <P>
	 *            The type.
	 */
	<P> void processStrategy(final FormPropertyRenderStrategy<P> strategy);

	/**
	 * Removes an action listener.
	 * 
	 * @param listener
	 *            The listener.
	 */
	void removeActionListener(ActionListener listener);
}
