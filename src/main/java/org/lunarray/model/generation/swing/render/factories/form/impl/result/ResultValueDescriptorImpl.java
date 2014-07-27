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
package org.lunarray.model.generation.swing.render.factories.form.impl.result;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JLabel;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.common.event.EventException;
import org.lunarray.common.event.Listener;
import org.lunarray.model.descriptor.converter.ConverterTool;
import org.lunarray.model.descriptor.converter.exceptions.ConverterException;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.extension.ExtensionRef;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.model.relation.RelationDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationResultDescriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.AccessBuffer;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.MutateBuffer;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;
import org.lunarray.model.generation.swing.render.factories.form.swing.events.OperationInvocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A result descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <R>
 *            The result value.
 */
public final class ResultValueDescriptorImpl<R>
		implements Descriptor<R>, AccessBuffer<R>, MutateBuffer<R>, Listener<OperationInvocationEvent<?, R>> {

	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ResultValueDescriptorImpl.class);
	/** Validation message. */
	private static final String MESSAGE_NULL = "Message may not be null.";
	/** The buffer value. */
	private R bufferValue;
	/** The coverter. */
	private ExtensionRef<ConverterTool> converterTool;
	/** The descriptor. */
	private OperationDescriptor<?> descriptor;
	/** The listeners. */
	private List<ValueChangeListener<R>> listeners;
	/** The presentation result. */
	private PresentationResultDescriptor<R> presentationResult;
	/** The relation descriptor. */
	private RelationDescriptor relationDescriptor;
	/** The result. */
	private ResultDescriptor<R> resultDescriptor;

	/**
	 * Default constructor.
	 * 
	 * @param resultDescriptor
	 *            The property.
	 * @param descriptor
	 *            The descriptor.
	 * @param model
	 *            The model.
	 */
	@SuppressWarnings("unchecked")
	public ResultValueDescriptorImpl(final ResultDescriptor<R> resultDescriptor, final OperationDescriptor<?> descriptor,
			final Model<?> model) {
		Validate.notNull(resultDescriptor, "Result may not be null.");
		Validate.notNull(descriptor, "Operation may not be null.");
		Validate.notNull(model, "Model may not be null.");
		this.resultDescriptor = resultDescriptor;
		this.presentationResult = resultDescriptor.adapt(PresentationResultDescriptor.class);
		this.relationDescriptor = resultDescriptor.adapt(RelationDescriptor.class);
		this.converterTool = model.getExtensionRef(ConverterTool.class);
		Validate.notNull(this.converterTool, "Model must have a converter tool extension.");
		this.listeners = new LinkedList<ValueChangeListener<R>>();
		this.descriptor = descriptor;
	}

	/** {@inheritDoc} */
	@Override
	public void addListener(final ValueChangeListener<R> listener) {
		this.listeners.add(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void apply() {
		// Read only.
	}

	/** {@inheritDoc} */
	@Override
	public AccessBuffer<R> getBufferAccessor() {
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public MutateBuffer<R> getBufferMutator() {
		return this;
	}

	/**
	 * Gets the value for the bufferValue field.
	 * 
	 * @return The value for the bufferValue field.
	 */
	public R getBufferValue() {
		return this.bufferValue;
	}

	/** {@inheritDoc} */
	@Override
	public <T> T getCoerceValue(final JLabel message, final Class<T> valueType) {
		Validate.notNull(message, ResultValueDescriptorImpl.MESSAGE_NULL);
		Validate.notNull(valueType, "Value type may not be null.");
		final ConverterTool tool = this.converterTool.get();
		T result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationResult)) {
			format = this.presentationResult.getFormat();
		}
		try {
			final String stringValue = tool.convertToString(this.resultDescriptor.getResultType(), this.bufferValue, format);
			result = tool.convertToInstance(valueType, stringValue, format);
			this.validate(message);
		} catch (final ConverterException e) {
			ResultValueDescriptorImpl.LOGGER.warn("Could not convert.", e);
			message.setText(e.getMessage());
		}
		return result;
	}

	/**
	 * Gets the value for the converterTool field.
	 * 
	 * @return The value for the converterTool field.
	 */
	public ExtensionRef<ConverterTool> getConverterTool() {
		return this.converterTool;
	}

	/**
	 * Gets the value for the descriptor field.
	 * 
	 * @return The value for the descriptor field.
	 */
	public OperationDescriptor<?> getDescriptor() {
		return this.descriptor;
	}

	/** {@inheritDoc} */
	@Override
	public String getLabel() {
		return "";
	}

	/**
	 * Gets the value for the listeners field.
	 * 
	 * @return The value for the listeners field.
	 */
	public List<ValueChangeListener<R>> getListeners() {
		return this.listeners;
	}

	/** {@inheritDoc} */
	@Override
	public String getName() {
		return this.descriptor.getName();
	}

	/**
	 * Gets the value for the presentationResult field.
	 * 
	 * @return The value for the presentationResult field.
	 */
	public PresentationResultDescriptor<R> getPresentationResult() {
		return this.presentationResult;
	}

	/** {@inheritDoc} */
	@Override
	public String getRelatedName() {
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
	public RelationDescriptor getRelationDescriptor() {
		return this.relationDescriptor;
	}

	/**
	 * Gets the value for the resultDescriptor field.
	 * 
	 * @return The value for the resultDescriptor field.
	 */
	public ResultDescriptor<R> getResultDescriptor() {
		return this.resultDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	public String getStringValue(final JLabel message) {
		Validate.notNull(message, ResultValueDescriptorImpl.MESSAGE_NULL);
		final ConverterTool tool = this.converterTool.get();
		String result = null;
		String format = null;
		if (!CheckUtil.isNull(this.presentationResult)) {
			format = this.presentationResult.getFormat();
		}
		try {
			result = tool.convertToString(this.resultDescriptor.getResultType(), this.bufferValue, format);
			this.validate(message);
		} catch (final ConverterException e) {
			message.setText(e.getMessage());
		}
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public R getValue(final JLabel message) {
		return this.bufferValue;
	}

	/** {@inheritDoc} */
	@Override
	public void handleEvent(final OperationInvocationEvent<?, R> event) throws EventException {
		this.bufferValue = event.getResult();
		for (final ValueChangeListener<R> listener : this.listeners) {
			listener.valueChanged(this.bufferValue);
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean isRelation() {
		return this.resultDescriptor.isRelation();
	}

	/** {@inheritDoc} */
	@Override
	public void removeListener(final ValueChangeListener<R> listener) {
		this.listeners.remove(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void revert() {
		// Real only.
	}

	/**
	 * Sets a new value for the bufferValue field.
	 * 
	 * @param bufferValue
	 *            The new value for the bufferValue field.
	 */
	public void setBufferValue(final R bufferValue) {
		this.bufferValue = bufferValue;
	}

	/** {@inheritDoc} */
	@Override
	public <T> void setCoerceValue(final JLabel message, final T value) {
		// Real only.
	}

	/**
	 * Sets a new value for the converterTool field.
	 * 
	 * @param converterTool
	 *            The new value for the converterTool field.
	 */
	public void setConverterTool(final ExtensionRef<ConverterTool> converterTool) {
		this.converterTool = converterTool;
	}

	/**
	 * Sets a new value for the descriptor field.
	 * 
	 * @param descriptor
	 *            The new value for the descriptor field.
	 */
	public void setDescriptor(final OperationDescriptor<?> descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Sets a new value for the listeners field.
	 * 
	 * @param listeners
	 *            The new value for the listeners field.
	 */
	public void setListeners(final List<ValueChangeListener<R>> listeners) {
		this.listeners = listeners;
	}

	/**
	 * Sets a new value for the presentationResult field.
	 * 
	 * @param presentationResult
	 *            The new value for the presentationResult field.
	 */
	public void setPresentationResult(final PresentationResultDescriptor<R> presentationResult) {
		this.presentationResult = presentationResult;
	}

	/**
	 * Sets a new value for the relationDescriptor field.
	 * 
	 * @param relationDescriptor
	 *            The new value for the relationDescriptor field.
	 */
	public void setRelationDescriptor(final RelationDescriptor relationDescriptor) {
		this.relationDescriptor = relationDescriptor;
	}

	/**
	 * Sets a new value for the resultDescriptor field.
	 * 
	 * @param resultDescriptor
	 *            The new value for the resultDescriptor field.
	 */
	public void setResultDescriptor(final ResultDescriptor<R> resultDescriptor) {
		this.resultDescriptor = resultDescriptor;
	}

	/** {@inheritDoc} */
	@Override
	public void setStringValue(final JLabel message, final String value) {
		// Real only.
	}

	/** {@inheritDoc} */
	@Override
	public void setValue(final JLabel message, final R value) {
		// Real only.
	}

	/** {@inheritDoc} */
	@Override
	public boolean valid() {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean validate(final JLabel messageLabel) {
		return true;
	}
}
