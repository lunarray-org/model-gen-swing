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

/**
 * Output strategy for properties in a table.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public interface OutputPropertyStrategy<P, E> {

	/**
	 * Gets the value for the converterTool field.
	 * 
	 * @return The value for the converterTool field.
	 */
	ExtensionRef<ConverterTool> getConverterTool();

	/**
	 * Gets the model.
	 * 
	 * @return The model.
	 */
	Model<?> getModel();

	/**
	 * Gets the presentation property descriptor.
	 * 
	 * @return The property descriptor.
	 */
	PresentationPropertyDescriptor<P, E> getPresentationProperty();

	/**
	 * Gets the property descriptor.
	 * 
	 * @return The property descriptor.
	 */
	PropertyDescriptor<P, E> getProperty();

	/**
	 * Gets the property label.
	 * 
	 * @return The property label.
	 */
	String getPropertyLabel();
}
