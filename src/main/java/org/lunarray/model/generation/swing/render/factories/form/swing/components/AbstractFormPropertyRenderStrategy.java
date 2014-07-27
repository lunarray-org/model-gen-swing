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
package org.lunarray.model.generation.swing.render.factories.form.swing.components;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueVisitor;

/**
 * The abstract form strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <J>
 *            The component type.
 */
public abstract class AbstractFormPropertyRenderStrategy<P, J extends JComponent>
		implements FormPropertyRenderStrategy<P>, ValueChangeListener<P> {
	/** The component. */
	private J component;
	/** The descriptor. */
	private Descriptor<P> descriptor;
	/** The label. */
	private JLabel label;
	/** The message. */
	private JLabel message;
	/** The model. */
	private Model<?> model;
	/** The property name. */
	private String name;

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 */
	public AbstractFormPropertyRenderStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
		Validate.notNull(descriptor, "Descriptor may not be null.");
		Validate.notNull(context, "Context may not be null.");
		this.model = context.getModel();
		this.name = context.getName(descriptor.getName());
		this.descriptor = descriptor;
		this.message = new JLabel("");
		this.component = this.createComponent();
		this.label = new JLabel(this.descriptor.getLabel());
		descriptor.revert();
		descriptor.addListener(this);
	}

	/** {@inheritDoc} */
	@Override
	public final J getComponent() {
		return this.component;
	}

	/**
	 * Gets the value for the descriptor field.
	 * 
	 * @return The value for the descriptor field.
	 */
	public final Descriptor<P> getDescriptor() {
		return this.descriptor;
	}

	/**
	 * Gets the value for the label field.
	 * 
	 * @return The value for the label field.
	 */
	@Override
	public final JLabel getLabel() {
		return this.label;
	}

	/**
	 * Gets the value for the message field.
	 * 
	 * @return The value for the message field.
	 */
	@Override
	public final JLabel getMessage() {
		return this.message;
	}

	/**
	 * Gets the value for the model field.
	 * 
	 * @return The value for the model field.
	 */
	public final Model<?> getModel() {
		return this.model;
	}

	/** {@inheritDoc} */
	@Override
	public final String getName() {
		return this.name;
	}

	/** {@inheritDoc} */
	@Override
	public final ValueVisitor getVisitor() {
		return this.descriptor;
	}

	/**
	 * Sets a new value for the component field.
	 * 
	 * @param component
	 *            The new value for the component field.
	 */
	public final void setComponent(final J component) {
		this.component = component;
	}

	/**
	 * Sets a new value for the descriptor field.
	 * 
	 * @param descriptor
	 *            The new value for the descriptor field.
	 */
	public final void setDescriptor(final Descriptor<P> descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Sets a new value for the label field.
	 * 
	 * @param label
	 *            The new value for the label field.
	 */
	public final void setLabel(final JLabel label) {
		this.label = label;
	}

	/**
	 * Sets a new value for the message field.
	 * 
	 * @param message
	 *            The new value for the message field.
	 */
	public final void setMessage(final JLabel message) {
		this.message = message;
	}

	/**
	 * Sets a new value for the model field.
	 * 
	 * @param model
	 *            The new value for the model field.
	 */
	public final void setModel(final Model<?> model) {
		this.model = model;
	}

	/**
	 * Sets a new value for the name field.
	 * 
	 * @param name
	 *            The new value for the name field.
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Sets a new value for the propertyName field.
	 * 
	 * @param propertyName
	 *            The new value for the propertyName field.
	 */
	public final void setPropertyName(final String propertyName) {
		this.name = propertyName;
	}

	/**
	 * Creates a component.
	 * 
	 * @return The component.
	 */
	protected abstract J createComponent();
}
