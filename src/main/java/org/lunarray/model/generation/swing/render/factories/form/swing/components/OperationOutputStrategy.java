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
package org.lunarray.model.generation.swing.render.factories.form.swing.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JLabel;

import org.lunarray.common.check.CheckUtil;
import org.lunarray.common.event.Bus;
import org.lunarray.common.event.EventException;
import org.lunarray.model.descriptor.accessor.exceptions.ValueAccessException;
import org.lunarray.model.descriptor.model.operation.OperationDescriptor;
import org.lunarray.model.descriptor.model.operation.result.ResultDescriptor;
import org.lunarray.model.descriptor.presentation.PresentationOperationDescriptor;
import org.lunarray.model.descriptor.util.OperationInvocationBuilder;
import org.lunarray.model.generation.swing.render.factories.form.swing.events.OperationInvocationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A operation output strategy.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 */
public final class OperationOutputStrategy<E>
		implements ActionListener {

	/** A logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(OperationOutputStrategy.class);
	/** A builder. */
	private OperationInvocationBuilder<E> builder;
	/** An event bus. */
	private Bus bus;
	/** A button. */
	private JButton button;
	/** The descriptor. */
	private OperationDescriptor<E> descriptor;
	/** The label. */
	private JLabel label;
	/** The message. */
	private JLabel message;
	/** The presentation descriptor. */
	private PresentationOperationDescriptor<E> presentationDescriptor;

	/**
	 * Default constructor.
	 * 
	 * @param descriptor
	 *            The descriptor. May not be null.
	 * @param builder
	 *            The builder. May not be null.
	 * @param bus
	 *            The bus. May not be null.
	 */
	@SuppressWarnings("unchecked")
	public OperationOutputStrategy(final OperationDescriptor<E> descriptor, final OperationInvocationBuilder<E> builder, final Bus bus) {
		this.descriptor = descriptor;
		this.presentationDescriptor = descriptor.adapt(PresentationOperationDescriptor.class);
		this.builder = builder;
		String text;
		String buttonText;
		if (CheckUtil.isNull(this.presentationDescriptor)) {
			text = descriptor.getName();
			buttonText = descriptor.getName();
		} else {
			text = this.presentationDescriptor.getDescription();
			buttonText = this.presentationDescriptor.getButton();
		}
		this.label = new JLabel(text);
		this.button = new JButton(buttonText);
		this.button.addActionListener(this);
		this.bus = bus;
		this.message = new JLabel("");
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(final ActionEvent event) {
		// Button pressed.
		this.execute(this.descriptor.getResultDescriptor());
	}

	/**
	 * Gets the value for the builder field.
	 * 
	 * @return The value for the builder field.
	 */
	public OperationInvocationBuilder<E> getBuilder() {
		return this.builder;
	}

	/**
	 * Gets the value for the bus field.
	 * 
	 * @return The value for the bus field.
	 */
	public Bus getBus() {
		return this.bus;
	}

	/**
	 * Gets the value for the button field.
	 * 
	 * @return The value for the button field.
	 */
	public JButton getButton() {
		return this.button;
	}

	/**
	 * Gets the value for the descriptor field.
	 * 
	 * @return The value for the descriptor field.
	 */
	public OperationDescriptor<E> getDescriptor() {
		return this.descriptor;
	}

	/**
	 * Gets the label.
	 * 
	 * @return The label.
	 */
	public JLabel getLabel() {
		return this.label;
	}

	/**
	 * Gets the value for the message field.
	 * 
	 * @return The value for the message field.
	 */
	public JLabel getMessage() {
		return this.message;
	}

	/**
	 * Gets the value for the presentationDescriptor field.
	 * 
	 * @return The value for the presentationDescriptor field.
	 */
	public PresentationOperationDescriptor<E> getPresentationDescriptor() {
		return this.presentationDescriptor;
	}

	/**
	 * Sets a new value for the builder field.
	 * 
	 * @param builder
	 *            The new value for the builder field.
	 */
	public void setBuilder(final OperationInvocationBuilder<E> builder) {
		this.builder = builder;
	}

	/**
	 * Sets a new value for the bus field.
	 * 
	 * @param bus
	 *            The new value for the bus field.
	 */
	public void setBus(final Bus bus) {
		this.bus = bus;
	}

	/**
	 * Sets a new value for the button field.
	 * 
	 * @param button
	 *            The new value for the button field.
	 */
	public void setButton(final JButton button) {
		this.button = button;
	}

	/**
	 * Sets a new value for the descriptor field.
	 * 
	 * @param descriptor
	 *            The new value for the descriptor field.
	 */
	public void setDescriptor(final OperationDescriptor<E> descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * Sets a new value for the label field.
	 * 
	 * @param label
	 *            The new value for the label field.
	 */
	public void setLabel(final JLabel label) {
		this.label = label;
	}

	/**
	 * Sets a new value for the message field.
	 * 
	 * @param message
	 *            The new value for the message field.
	 */
	public void setMessage(final JLabel message) {
		this.message = message;
	}

	/**
	 * Sets a new value for the presentationDescriptor field.
	 * 
	 * @param presentationDescriptor
	 *            The new value for the presentationDescriptor field.
	 */
	public void setPresentationDescriptor(final PresentationOperationDescriptor<E> presentationDescriptor) {
		this.presentationDescriptor = presentationDescriptor;
	}

	/**
	 * Execute operation.
	 * 
	 * @param descriptor
	 *            The result descriptor.
	 * @param <R>
	 *            The result type.
	 */
	private <R> void execute(final ResultDescriptor<R> descriptor) {
		try {
			final R result = this.builder.execute(descriptor.getResultType());
			final OperationInvocationEvent<E, R> event = new OperationInvocationEvent<E, R>(this.descriptor, result);
			this.bus.handleEvent(event, this.builder);
		} catch (final ValueAccessException e) {
			OperationOutputStrategy.LOGGER.warn("Could not access value.", e);
			if (e.getCause() instanceof InvocationTargetException) {
				final InvocationTargetException ite = (InvocationTargetException) e.getCause();
				final Throwable origin = ite.getCause();
				this.getMessage().setText(origin.getMessage());
			} else {
				this.getMessage().setText(e.getMessage());
			}
		} catch (final EventException e) {
			OperationOutputStrategy.LOGGER.warn("Could not process events.", e);
		}
	}
}
