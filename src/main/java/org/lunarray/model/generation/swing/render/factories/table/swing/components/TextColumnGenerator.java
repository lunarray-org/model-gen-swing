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
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.exceptions.ConverterException;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A column generator.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <E>
 *            The entity type.
 */
public final class TextColumnGenerator<P, E>
		extends AbstractGeneratedColumn<P, E, String> {
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(TextColumnGenerator.class);

	/**
	 * Default constructor.
	 * 
	 * @param textOutputPropertyStrategy
	 *            The output strategy. May not be null.
	 */
	public TextColumnGenerator(final TextOutputPropertyStrategy<P, E> textOutputPropertyStrategy) {
		super(textOutputPropertyStrategy);
	}

	/** {@inheritDoc} */
	@Override
	public Class<String> getRenderType() {
		return String.class;
	}

	/** {@inheritDoc} */
	@Override
	public String getValue(final E entity) {
		Validate.notNull(entity, "Entity may not be null.");
		String result = null;
		try {
			final OutputPropertyStrategy<P, E> strategy = this.getOutputPropertyStrategy();
			final PropertyDescriptor<P, E> property = strategy.getProperty();
			final P value = property.getValue(entity);
			final ConverterTool tool = strategy.getConverterTool().get();
			if (value instanceof String) {
				result = String.class.cast(value);
			} else {
				String format = null;
				final PresentationPropertyDescriptor<P, E> presentationProperty = strategy.getPresentationProperty();
				if (!CheckUtil.isNull(presentationProperty)) {
					format = presentationProperty.getFormat();
				}
				result = this.processRelated(entity, value, property, tool, format);
			}
		} catch (final ValueAccessException e) {
			TextColumnGenerator.LOGGER.warn("Could not access.", e);
		}
		return result;
	}

	/**
	 * Process as a related property.
	 * 
	 * @param entity
	 *            The entity type.
	 * @param value
	 *            The property value.
	 * @param property
	 *            The property.
	 * @param tool
	 *            The converter tool.
	 * @param format
	 *            The message format.
	 * @return The result.
	 * @throws ValueAccessException
	 *             Thrown if the value could not be accessed.
	 */
	private String processRelated(final E entity, final P value, final PropertyDescriptor<P, E> property, final ConverterTool tool,
			final String format) throws ValueAccessException {
		String result = null;
		try {
			if (property.isRelation()) {
				@SuppressWarnings("unchecked")
				final PropertyDescriptor<Object, P> displayProperty = (PropertyDescriptor<Object, P>) this.resolveDisplayProperty();
				final Object innerValue = displayProperty.getValue(property.getValue(entity));
				if (value instanceof String) {
					result = String.class.cast(innerValue);
				} else {
					result = tool.convertToString(displayProperty.getPropertyType(), innerValue, format);
				}
			} else {
				result = tool.convertToString(property.getPropertyType(), value, format);
			}
		} catch (final ConverterException e) {
			TextColumnGenerator.LOGGER.warn("Could not convert.", e);
		}
		return result;
	}
}
