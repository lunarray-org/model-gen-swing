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
package org.lunarray.model.generation.swing.render.factories.form.descriptor;

import javax.swing.JLabel;

/**
 * An access buffer that accesses buffered values.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The type.
 */
public interface AccessBuffer<P> {

	/**
	 * Coerce the value.
	 * 
	 * @param message
	 *            The message. May not be null.
	 * @param valueType
	 *            The value type. May not be null.
	 * @return The value.
	 * @param <T>
	 *            The type.
	 */
	<T> T getCoerceValue(JLabel message, Class<T> valueType);

	/**
	 * Gets the value.
	 * 
	 * @param message
	 *            The message. May not be null.
	 * @return The value.
	 */
	String getStringValue(JLabel message);

	/**
	 * Gets the value.
	 * 
	 * @param message
	 *            The message. May not be null.
	 * @return The value.
	 */
	P getValue(JLabel message);
}
