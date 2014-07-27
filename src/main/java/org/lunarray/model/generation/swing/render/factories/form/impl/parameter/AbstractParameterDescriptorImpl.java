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
package org.lunarray.model.generation.swing.render.factories.form.impl.parameter;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.exceptions.ConverterException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.operation.parameters.ParameterDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationParameterDescriptor;
import org.lunarray.model.descriptor.util.OperationInvocationBuilder;
import org.lunarray.model.descriptor.validator.ValueValidator;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.AccessBuffer;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.MutateBuffer;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;
import org.lunarray.model.generation.swing.util.MessageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * An abstract parameter descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The type.
 * @param <E>
 *            The entity.
 */
public abstract class AbstractParameterDescriptorImpl<P, E>
		implements Descriptor<P>, AccessBuffer<P>, MutateBuffer<P> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractParameterDescriptorImpl.class);
	/** Validation message. */
	private static final String MESSAGE_NULL = "Message may not be null.";
	/** The required message. */
	private static final String REQUIRED_MESSAGE = "validation.value.required";
	/** The buffer value. */
	private P bufferValue;
	/** The builder. */
	private OperationInvocationBuilder<E> builder;
	/** The converter. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The listeners. */
	private List<ValueChangeListener<P>> listeners;
	/** The parameter. */
	private ParameterDescriptor<P> parameter;
	/** The presentation parameter. */
	private PresentationParameterDescriptor<P> presentationParameter;
	/** The relation descriptor. */
	private RelationDescriptor relationDescriptor;
	/** Tests if it's valid. */
	private boolean validValue;
	/** The validator. */
	private ExtensionRef<ValueValidator> valueValidator;

	/**
	 * Default constructor.
	 * 
	 * @param parameter
	 *            The parameter.
	 * @param builder
	 *            The builder.
	 * @param model
	 *            The model.
	 */
	@SuppressWarnings("unchecked")
	public AbstractParameterDescriptorImpl(final ParameterDescriptor<P> parameter, final OperationInvocationBuilder<E> builder,
			final Model<? super E> model) {
		Validate.notNull(parameter, "Parameter may not be null.");
		Validate.notNull(builder, "Operation builder may not be null.");
		Validate.notNull(model, "Model may not be null.");
		this.parameter = parameter;
		this.builder = builder;
		this.presentationParameter = parameter.adapt(PresentationParameterDescriptor.class);
		this.relationDescriptor = parameter.adapt(RelationDescriptor.class);
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
		this.builder.parameter(this.parameter, this.bufferValue);
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

	/**
	 * Gets the value for the builder field.
	 * 
	 * @return The value for the builder field.
	 */
	public final OperationInvocationBuilder<E> getBuilder() {
		return this.builder;
	}

	/** {@inheritDoc} */
	@Override
	public final <T> T getCoerceValue(final JLabel message, final Class<T> valueType) {
		Validate.notNull(message, AbstractParameterDescriptorImpl.MESSAGE_NULL);
		Validate.notNull(valueType, "Value type may not be null.");
		final ConverterTool tool = this.getConverterTool().get();
		T result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationParameter)) {
			format = this.presentationParameter.getFormat();
		}
		try {
			final String stringValue = tool.convertToString(this.parameter.getType(), this.bufferValue, format);
			result = tool.convertToInstance(valueType, stringValue, format);
			this.validate(message);
		} catch (final ConverterException e) {
			AbstractParameterDescriptorImpl.LOGGER.warn("Could not convert.", e);
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

	/** {@inheritDoc} */
	@Override
	public final String getLabel() {
		String labelText;
		if (CheckUtil.isNull(this.presentationParameter)) {
			labelText = Integer.toString(this.parameter.getIndex());
		} else {
			labelText = this.presentationParameter.getDescription();
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
		return Integer.toString(this.parameter.getIndex());
	}

	/**
	 * Gets the value for the parameter field.
	 * 
	 * @return The value for the parameter field.
	 */
	public final ParameterDescriptor<P> getParameter() {
		return this.parameter;
	}

	/**
	 * Gets the value for the presentationParameter field.
	 * 
	 * @return The value for the presentationParameter field.
	 */
	public final PresentationParameterDescriptor<P> getPresentationParameter() {
		return this.presentationParameter;
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
		Validate.notNull(message, AbstractParameterDescriptorImpl.MESSAGE_NULL);
		final ConverterTool tool = this.getConverterTool().get();
		String result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationParameter)) {
			format = this.presentationParameter.getFormat();
		}
		try {
			result = tool.convertToString(this.parameter.getType(), this.bufferValue, format);
			this.validate(message);
		} catch (final ConverterException e) {
			AbstractParameterDescriptorImpl.LOGGER.warn("Could not convert in get string.", e);
			message.setText(e.getMessage());
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public final P getValue(final JLabel message) {
		Validate.notNull(message, AbstractParameterDescriptorImpl.MESSAGE_NULL);
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
		this.bufferValue = null;
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

	/**
	 * Sets a new value for the builder field.
	 * 
	 * @param builder
	 *            The new value for the builder field.
	 */
	public final void setBuilder(final OperationInvocationBuilder<E> builder) {
		this.builder = builder;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public final <T> void setCoerceValue(final JLabel message, final T value) {
		Validate.notNull(message, AbstractParameterDescriptorImpl.MESSAGE_NULL);
		final ConverterTool tool = this.getConverterTool().get();
		String format = null;
		if (!CheckUtil.isNull(this.presentationParameter)) {
			format = this.presentationParameter.getFormat();
		}
		try {
			if (this.parameter.isAssignable(value)) {
				this.bufferValue = (P) value;
			} else {
				final String stringValue = tool.convertToString((Class<T>) value.getClass(), value, format);
				this.bufferValue = tool.convertToInstance(this.parameter.getType(), stringValue, format);
			}
			if (this.validate(message)) {
				this.builder.parameter(this.parameter, this.bufferValue);
				message.setText("");
			}
		} catch (final ConverterException e) {
			AbstractParameterDescriptorImpl.LOGGER.warn("Could not to set coerce.", e);
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
	 * Sets a new value for the listeners field.
	 * 
	 * @param listeners
	 *            The new value for the listeners field.
	 */
	public final void setListeners(final List<ValueChangeListener<P>> listeners) {
		this.listeners = listeners;
	}

	/**
	 * Sets a new value for the parameter field.
	 * 
	 * @param parameter
	 *            The new value for the parameter field.
	 */
	public final void setParameter(final ParameterDescriptor<P> parameter) {
		this.parameter = parameter;
	}

	/**
	 * Sets a new value for the presentationParameter field.
	 * 
	 * @param presentationParameter
	 *            The new value for the presentationParameter field.
	 */
	public final void setPresentationParameter(final PresentationParameterDescriptor<P> presentationParameter) {
		this.presentationParameter = presentationParameter;
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
		Validate.notNull(message, AbstractParameterDescriptorImpl.MESSAGE_NULL);
		final ConverterTool tool = this.getConverterTool().get();
		String format = null;
		if (!CheckUtil.isNull(this.presentationParameter)) {
			format = this.presentationParameter.getFormat();
		}
		try {
			if (this.parameter.isAssignable(stringValue)) {
				this.bufferValue = (P) stringValue;
			} else {
				this.bufferValue = tool.convertToInstance(this.parameter.getType(), stringValue, format);
			}
			if (this.validate(message)) {
				this.builder.parameter(this.parameter, this.bufferValue);
				message.setText("");
			}
		} catch (final ConverterException e) {
			AbstractParameterDescriptorImpl.LOGGER.warn("Could not convert to set to string.", e);
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
		Validate.notNull(message, AbstractParameterDescriptorImpl.MESSAGE_NULL);
		this.bufferValue = value;
		if (this.validate(message)) {
			this.builder.parameter(this.parameter, this.bufferValue);
		} else {
			message.setText("");
		}
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
		Validate.notNull(messageLabel, AbstractParameterDescriptorImpl.MESSAGE_NULL);
		this.validValue = true;
		if (CheckUtil.isNull(this.presentationParameter) || !this.presentationParameter.isRequiredIndication()) {
			messageLabel.setText("");
		} else {
			this.validValue ^= CheckUtil.isNull(this.bufferValue);
			messageLabel.setText(MessageUtil.getMessage(AbstractParameterDescriptorImpl.REQUIRED_MESSAGE));
		}
		return this.validValue;
	}
}
