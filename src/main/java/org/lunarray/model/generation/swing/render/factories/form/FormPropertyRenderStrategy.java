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
package org.lunarray.model.generation.swing.render.factories.form;

import java.util.Collection;

import javax.swing.JComponent;
import javax.swing.JLabel;

import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.CollectionDescriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueVisitor;

/**
 * The render strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public interface FormPropertyRenderStrategy<P> {
	/**
	 * Gets the component.
	 * 
	 * @return The component.
	 */
	JComponent getComponent();

	/**
	 * Gets the label.
	 * 
	 * @return The label.
	 */
	JLabel getLabel();

	/**
	 * Gets the message.
	 * 
	 * @return The message.
	 */
	JLabel getMessage();

	/**
	 * Gets the property name.
	 * 
	 * @return The property name.
	 */
	String getName();

	/**
	 * Gets the value visitor.
	 * 
	 * @return The visitor.
	 */
	ValueVisitor getVisitor();

	/**
	 * Collection factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	interface CollectionDescriptorFactory {
		/**
		 * Create a strategy.
		 * 
		 * @param context
		 *            The context. May not be null.
		 * @param descriptor
		 *            The descriptor. May not be null.
		 * @return The strategy.
		 * @param <E>
		 *            The entity type.
		 * @param <C>
		 *            The collection type.
		 * @param <P>
		 *            The type.
		 */
		<E, C, P extends Collection<C>> FormPropertyRenderStrategy<P> createStrategy(final RenderContext<E> context,
				final CollectionDescriptor<C, P> descriptor);
	}

	/**
	 * Factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	interface DescriptorFactory {
		/**
		 * Create a strategy.
		 * 
		 * @param context
		 *            The context. May not be null.
		 * @param descriptor
		 *            The descriptor. May not be null.
		 * @return The strategy.
		 * @param <E>
		 *            The entity type.
		 * @param <P>
		 *            The type.
		 */
		<E, P> FormPropertyRenderStrategy<P> createStrategy(final RenderContext<E> context, final Descriptor<P> descriptor);
	}
}
