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
package org.lunarray.model.generation.swing.render.factories.form.impl.property;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.exceptions.ConverterException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.property.PropertyDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationPropertyDescriptor;
import org.lunarray.model.descriptor.validator.PropertyViolation;
import org.lunarray.model.descriptor.validator.ValueValidator;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.AccessBuffer;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.MutateBuffer;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract property descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The type.
 * @param <E>
 *            The entity.
 */
public abstract class AbstractPropertyDescriptorImpl<P, E>
		implements Descriptor<P>, AccessBuffer<P>, MutateBuffer<P> {

	/** Access label. */
	private static final String ACCESS_MESSAGE = "Could not access.";
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPropertyDescriptorImpl.class);
	/** Validation message. */
	private static final String MESSAGE_NULL = "Message may not be null.";
	/** The buffer value. */
	private P bufferValue;
	/** The converter. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The entity. */
	private E entity;
	/** The listeners. */
	private List<ValueChangeListener<P>> listeners;
	/** The presentation property. */
	private PresentationPropertyDescriptor<P, E> presentationProperty;
	/** The property. */
	private PropertyDescriptor<P, E> property;
	/** The relation descriptor. */
	private RelationDescriptor relationDescriptor;
	/** Indication if it's valid. */
	private boolean validValue;
	/** The validator. */
	private ExtensionRef<ValueValidator> valueValidator;

	/**
	 * Default constructor.
	 * 
	 * @param property
	 *            The property.
	 * @param model
	 *            The model.
	 */
	@SuppressWarnings("unchecked")
	public AbstractPropertyDescriptorImpl(final PropertyDescriptor<P, E> property, final Model<? super E> model) {
		Validate.notNull(property, "Property may not be null.");
		Validate.notNull(model, "Model may not be null.");
		this.property = property;
		this.presentationProperty = property.adapt(PresentationPropertyDescriptor.class);
		this.relationDescriptor = property.adapt(RelationDescriptor.class);
		this.converterTool = model.getExtensionRef(ConverterTool.class);
		Validate.notNull(this.converterTool, "Model must have a converter tool extension.");
		this.valueValidator = model.getExtensionRef(ValueValidator.class);
		this.listeners = new LinkedList<ValueChangeListener<P>>();
	}

	/** {@inheritDoc} */
	@Override
	public final void addListener(final ValueChangeListener<P> listener) {
		this.listeners.add(listener);
	}

	/** {@inheritDoc} */
	@Override
	public final void apply() {
		try {
			this.getProperty().setValue(this.entity, this.bufferValue);
		} catch (final ValueAccessException e) {
			AbstractPropertyDescriptorImpl.LOGGER.warn(AbstractPropertyDescriptorImpl.ACCESS_MESSAGE, e);
		}
		for (final ValueChangeListener<P> listener : this.listeners) {
			listener.valueChanged(this.bufferValue);
		}
	}

	/** {@inheritDoc} */
	@Override
	public final AccessBuffer<P> getBufferAccessor() {
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public final MutateBuffer<P> getBufferMutator() {
		return this;
	}

	/**
	 * Gets the value for the bufferValue field.
	 * 
	 * @return The value for the bufferValue field.
	 */
	public final P getBufferValue() {
		return this.bufferValue;
	}

	/** {@inheritDoc} */
	@Override
	public final <T> T getCoerceValue(final JLabel message, final Class<T> valueType) {
		Validate.notNull(message, AbstractPropertyDescriptorImpl.MESSAGE_NULL);
		Validate.notNull(valueType, "Value type may not be null.");
		final ConverterTool tool = this.getConverterTool().get();
		T result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationProperty)) {
			format = this.presentationProperty.getFormat();
		}
		try {
			final String stringValue = tool.convertToString(this.property.getPropertyType(), this.bufferValue, format);
			result = tool.convertToInstance(valueType, stringValue, format);
			this.validate(message);
		} catch (final ConverterException e) {
			AbstractPropertyDescriptorImpl.LOGGER.warn("Could not convert to get coerce.", e);
			message.setText(e.getMessage());
		}
		return result;
	}

	/**
	 * Gets the value for the converterTool field.
	 * 
	 * @return The value for the converterTool field.
	 */
	public final ExtensionRef<ConverterTool> getConverterTool() {
		return this.converterTool;
	}

	/**
	 * Gets the value for the entity field.
	 * 
	 * @return The value for the entity field.
	 */
	public final E getEntity() {
		return this.entity;
	}

	/** {@inheritDoc} */
	@Override
	public final String getLabel() {
		String labelText;
		if (CheckUtil.isNull(this.presentationProperty)) {
			labelText = this.property.getName();
		} else {
			labelText = this.presentationProperty.getDescription();
		}
		return labelText;
	}

	/**
	 * Gets the value for the listeners field.
	 * 
	 * @return The value for the listeners field.
	 */
	public final List<ValueChangeListener<P>> getListeners() {
		return this.listeners;
	}

	/** {@inheritDoc} */
	@Override
	public final String getName() {
		return this.getProperty().getName();
	}

	/**
	 * Gets the value for the presentationProperty field.
	 * 
	 * @return The value for the presentationProperty field.
	 */
	public final PresentationPropertyDescriptor<P, E> getPresentationProperty() {
		return this.presentationProperty;
	}

	/**
	 * Gets the value for the property field.
	 * 
	 * @return The value for the property field.
	 */
	public final PropertyDescriptor<P, E> getProperty() {
		return this.property;
	}

	/** {@inheritDoc} */
	@Override
	public final String getRelatedName() {
		String result = null;
		if (this.isRelation()) {
			result = this.relationDescriptor.getRelatedName();
		}
		return result;
	}

	/**
	 * Gets the value for the relationDescriptor field.
	 * 
	 * @return The value for the relationDescriptor field.
	 */
	public final RelationDescriptor getRelationDescriptor() {
		return this.relationDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	public final String getStringValue(final JLabel message) {
		Validate.notNull(message, AbstractPropertyDescriptorImpl.MESSAGE_NULL);
		final ConverterTool tool = this.getConverterTool().get();
		String result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationProperty)) {
			format = this.presentationProperty.getFormat();
		}
		try {
			result = tool.convertToString(this.property.getPropertyType(), this.bufferValue, format);
			this.validate(message);
		} catch (final ConverterException e) {
			AbstractPropertyDescriptorImpl.LOGGER.warn("Could not convert to get string.", e);
			message.setText(e.getMessage());
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final P getValue(final JLabel message) {
		Validate.notNull(message, AbstractPropertyDescriptorImpl.MESSAGE_NULL);
		this.validate(message);
		return this.bufferValue;
	}

	/**
	 * Gets the value for the valueValidator field.
	 * 
	 * @return The value for the valueValidator field.
	 */
	public final ExtensionRef<ValueValidator> getValueValidator() {
		return this.valueValidator;
	}

	/** {@inheritDoc} */
	@Override
	public final boolean isRelation() {
		return !CheckUtil.isNull(this.relationDescriptor);
	}

	/**
	 * Gets the value for the valid field.
	 * 
	 * @return The value for the valid field.
	 */
	public final boolean isValid() {
		return this.validValue;
	}

	/**
	 * Gets the value for the validValue field.
	 * 
	 * @return The value for the validValue field.
	 */
	public final boolean isValidValue() {
		return this.validValue;
	}

	@Override
	public final void removeListener(final ValueChangeListener<P> listener) {
		this.listeners.remove(listener);
	}

	/** {@inheritDoc} */
	@Override
	public final void revert() {
		try {
			this.bufferValue = this.getProperty().getValue(this.entity);
		} catch (final ValueAccessException e) {
			AbstractPropertyDescriptorImpl.LOGGER.warn(AbstractPropertyDescriptorImpl.ACCESS_MESSAGE, e);
		}
		for (final ValueChangeListener<P> listener : this.listeners) {
			listener.valueChanged(this.bufferValue);
		}
	}

	/**
	 * Sets a new value for the bufferValue field.
	 * 
	 * @param bufferValue
	 *            The new value for the bufferValue field.
	 */
	public final void setBufferValue(final P bufferValue) {
		this.bufferValue = bufferValue;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public final <T> void setCoerceValue(final JLabel message, final T value) {
		Validate.notNull(message, AbstractPropertyDescriptorImpl.MESSAGE_NULL);
		final ConverterTool tool = this.getConverterTool().get();
		String format = null;
		if (!CheckUtil.isNull(this.presentationProperty)) {
			format = this.presentationProperty.getFormat();
		}
		try {
			if (this.property.isAssignable(value)) {
				this.bufferValue = (P) value;
			} else {
				final String stringValue = tool.convertToString((Class<T>) value.getClass(), value, format);
				this.bufferValue = tool.convertToInstance(this.property.getPropertyType(), stringValue, format);
			}
			this.validate(message);
		} catch (final ConverterException e) {
			AbstractPropertyDescriptorImpl.LOGGER.warn("Could not convert to set to coerce.", e);
			message.setText(e.getMessage());
		}
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
	 * Sets a new value for the entity field.
	 * 
	 * @param entity
	 *            The new value for the entity field.
	 */
	public final void setEntity(final E entity) {
		this.entity = entity;
		this.revert();
	}

	/**
	 * Sets a new value for the listeners field.
	 * 
	 * @param listeners
	 *            The new value for the listeners field.
	 */
	public final void setListeners(final List<ValueChangeListener<P>> listeners) {
		this.listeners = listeners;
	}

	/**
	 * Sets a new value for the presentationProperty field.
	 * 
	 * @param presentationProperty
	 *            The new value for the presentationProperty field.
	 */
	public final void setPresentationProperty(final PresentationPropertyDescriptor<P, E> presentationProperty) {
		this.presentationProperty = presentationProperty;
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

	/**
	 * Sets a new value for the relationDescriptor field.
	 * 
	 * @param relationDescriptor
	 *            The new value for the relationDescriptor field.
	 */
	public final void setRelationDescriptor(final RelationDescriptor relationDescriptor) {
		this.relationDescriptor = relationDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	@SuppressWarnings("unchecked")
	public final void setStringValue(final JLabel message, final String stringValue) {
		Validate.notNull(message, AbstractPropertyDescriptorImpl.MESSAGE_NULL);
		final ConverterTool tool = this.getConverterTool().get();
		String format = null;
		if (!CheckUtil.isNull(this.presentationProperty)) {
			format = this.presentationProperty.getFormat();
		}
		try {
			if (this.property.isAssignable(stringValue)) {
				this.bufferValue = (P) stringValue;
			} else {
				this.bufferValue = tool.convertToInstance(this.property.getPropertyType(), stringValue, format);
			}
			this.validate(message);
		} catch (final ConverterException e) {
			AbstractPropertyDescriptorImpl.LOGGER.warn("Could not convert to set to string.", e);
			message.setText(e.getMessage());
		}
	}

	/**
	 * Sets a new value for the valid field.
	 * 
	 * @param valid
	 *            The new value for the valid field.
	 */
	public final void setValid(final boolean valid) {
		this.validValue = valid;
	}

	/**
	 * Sets a new value for the validValue field.
	 * 
	 * @param validValue
	 *            The new value for the validValue field.
	 */
	public final void setValidValue(final boolean validValue) {
		this.validValue = validValue;
	}

	/** {@inheritDoc} */
	@Override
	public final void setValue(final JLabel message, final P value) {
		this.bufferValue = value;
		this.validate(message);
	}

	/**
	 * Sets a new value for the valueValidator field.
	 * 
	 * @param valueValidator
	 *            The new value for the valueValidator field.
	 */
	public final void setValueValidator(final ExtensionRef<ValueValidator> valueValidator) {
		this.valueValidator = valueValidator;
	}

	/** {@inheritDoc} */
	@Override
	public final boolean valid() {
		return this.validValue;
	}

	/** {@inheritDoc} */
	@Override
	public final boolean validate(final JLabel messageLabel) {
		Validate.notNull(messageLabel, AbstractPropertyDescriptorImpl.MESSAGE_NULL);
		final ValueValidator validator = this.valueValidator.get();
		if (!CheckUtil.isNull(validator)) {
			final Collection<PropertyViolation<E, P>> violations = validator.validateValue(this.property, this.bufferValue);
			if (violations.isEmpty()) {
				this.validValue = true;
				messageLabel.setText("");
			} else {
				this.validValue = false;
				for (final PropertyViolation<E, P> violation : violations) {
					messageLabel.setText(violation.getMessage());
				}
			}
		}
		return this.validValue;
	}
}
