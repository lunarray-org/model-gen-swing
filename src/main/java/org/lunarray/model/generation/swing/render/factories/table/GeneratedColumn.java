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

/**
 * A generated column.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 * @param <R>
 *            The render type.
 */
public interface GeneratedColumn<E, R> {

	/**
	 * Gets the label.
	 * 
	 * @return The label.
	 */
	String getLabel();

	/**
	 * Gets the render type.
	 * 
	 * @return The render type.
	 */
	Class<R> getRenderType();

	/**
	 * Gets the value.
	 * 
	 * @param entity
	 *            The entity. May not be null.
	 * @return The value.
	 */
	R getValue(E entity);
}
