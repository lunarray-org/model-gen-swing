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
package org.lunarray.model.generation.swing.render.factories.table;

import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.generation.swing.render.RenderContext;

/**
 * The render strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public interface TablePropertyRenderStrategy<P, E> {

	/**
	 * Gets a column generator.
	 * 
	 * @return The column generator.
	 */
	GeneratedColumn<E, ?> getGenerator();

	/**
	 * The factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	interface Factory {
		/**
		 * Creates a strategy.
		 * 
		 * @param context
		 *            The context. May not be null.
		 * @param descriptor
		 *            The descriptor. May not be null.
		 * @return The strategy.
		 * @param <P>
		 *            The type.
		 * @param <E>
		 *            The entity.
		 */
		<P, E> TablePropertyRenderStrategy<P, E> createStrategy(final RenderContext<E> context, final PropertyDescriptor<P, E> descriptor);
	}
}
