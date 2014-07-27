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
package org.lunarray.model.generation.swing.render.factories.table.swing.components;

import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.table.TablePropertyRenderStrategy;

/**
 * Constructs the text output.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public abstract class AbstractOutputPropertyStrategy<P, E>
		implements TablePropertyRenderStrategy<P, E>, OutputPropertyStrategy<P, E> {

	/** The converter tool. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The model. */
	private Model<?> model;
	/** The property. */
	private PropertyDescriptor<P, E> property;

	/**
	 * Constructs the strategy.
	 * 
	 * @param propertyDescriptor
	 *            The property descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 */
	public AbstractOutputPropertyStrategy(final PropertyDescriptor<P, E> propertyDescriptor, final RenderContext<E> context) {
		this.property = propertyDescriptor;
		this.model = context.getModel();
		this.converterTool = this.model.getExtensionRef(ConverterTool.class);
	}

	/** {@inheritDoc} */
	@Override
	public final ExtensionRef<ConverterTool> getConverterTool() {
		return this.converterTool;
	}

	/** {@inheritDoc} */
	@Override
	public final Model<?> getModel() {
		return this.model;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public final PresentationPropertyDescriptor<P, E> getPresentationProperty() {
		return this.property.adapt(PresentationPropertyDescriptor.class);
	}

	/** {@inheritDoc} */
	@Override
	public final PropertyDescriptor<P, E> getProperty() {
		return this.property;
	}

	/** {@inheritDoc} */
	@Override
	public final String getPropertyLabel() {
		String result;
		if (this.hasPresentationProperty()) {
			result = this.getPresentationProperty().getDescription();
		} else {
			result = this.getProperty().getName();
		}
		return result;
	}

	/**
	 * Tests if the property is a presentation property.
	 * 
	 * @return The property.
	 */
	public final boolean hasPresentationProperty() {
		return this.property.adapt(PresentationPropertyDescriptor.class) != null;
	}

	/**
	 * Sets a new value for the converterTool field.
	 * 
	 * @param converterTool
	 *            The new value for the converterTool field.
	 */
	public final void setConverterTool(final ExtensionRef<ConverterTool> converterTool) {
		this.converterTool = converterTool;
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
	 * Sets a new value for the property field.
	 * 
	 * @param property
	 *            The new value for the property field.
	 */
	public final void setProperty(final PropertyDescriptor<P, E> property) {
		this.property = property;
	}
}
