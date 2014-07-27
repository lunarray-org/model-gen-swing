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

import org.apache.commons.lang.Validate;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.table.GeneratedColumn;
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
public final class TextOutputPropertyStrategy<P, E>
		extends AbstractOutputPropertyStrategy<P, E> {

	/**
	 * Constructs the strategy.
	 * 
	 * @param propertyDescriptor
	 *            The property descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 */
	protected TextOutputPropertyStrategy(final PropertyDescriptor<P, E> propertyDescriptor, final RenderContext<E> context) {
		super(propertyDescriptor, context);
	}

	/** {@inheritDoc} */
	@Override
	public GeneratedColumn<E, ?> getGenerator() {
		return new TextColumnGenerator<P, E>(this);
	}

	/**
	 * The factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class StrategyFactory
			implements Factory {

		/**
		 * Default constructor.
		 */
		public StrategyFactory() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public <P, E> TablePropertyRenderStrategy<P, E> createStrategy(final RenderContext<E> context,
				final PropertyDescriptor<P, E> descriptor) {
			Validate.notNull(context, "Context may not be null.");
			Validate.notNull(descriptor, "Descriptor may not be null.");
			return new TextOutputPropertyStrategy<P, E>(descriptor, context);
		}
	}
}
