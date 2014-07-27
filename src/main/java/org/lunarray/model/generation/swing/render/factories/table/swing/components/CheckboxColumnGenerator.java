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
public final class CheckboxColumnGenerator<P, E>
		extends AbstractGeneratedColumn<P, E, Boolean> {
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(CheckboxColumnGenerator.class);

	/**
	 * Default constructor.
	 * 
	 * @param strategy
	 *            The output strategy. May not be null.
	 */
	public CheckboxColumnGenerator(final CheckboxOutputPropertyStrategy<P, E> strategy) {
		super(strategy);
	}

	/** {@inheritDoc} */
	@Override
	public Class<Boolean> getRenderType() {
		return Boolean.class;
	}

	/** {@inheritDoc} */
	@Override
	public Boolean getValue(final E entity) {
		Validate.notNull(entity, "Entity may not be null.");
		Boolean result = null;
		try {
			final OutputPropertyStrategy<P, E> strategy = this.getOutputPropertyStrategy();
			final PropertyDescriptor<P, E> property = strategy.getProperty();
			final P value = property.getValue(entity);
			final ConverterTool tool = strategy.getConverterTool().get();
			if (value instanceof Boolean) {
				result = Boolean.class.cast(value);
			} else {
				String format = null;
				final PresentationPropertyDescriptor<P, E> presentationProperty = strategy.getPresentationProperty();
				if (!CheckUtil.isNull(presentationProperty)) {
					format = presentationProperty.getFormat();
				}
				final String stringValue = tool.convertToString(property.getPropertyType(), value, format);
				result = tool.convertToInstance(Boolean.class, stringValue, format);
			}
		} catch (final ValueAccessException e) {
			CheckboxColumnGenerator.LOGGER.warn("Could not access.", e);
		} catch (final ConverterException e) {
			CheckboxColumnGenerator.LOGGER.warn("Could not convert.", e);
		}
		return result;
	}
}
