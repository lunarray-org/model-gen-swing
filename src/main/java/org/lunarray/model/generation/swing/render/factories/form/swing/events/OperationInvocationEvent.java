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
package org.lunarray.model.generation.swing.render.factories.form.swing.events;

import org.lunarray.model.descriptor.model.operation.OperationDescriptor;

/**
 * A operation execute event.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <E>
 *            The entity type.
 * @param <R>
 *            The result type.
 */
public final class OperationInvocationEvent<E, R> {

	/** The operation. */
	private OperationDescriptor<E> descriptor;
	/** The result. */
	private R result;

	/**
	 * Default constructor.
	 * 
	 * @param descriptor
	 *            The operation.
	 * @param result
	 *            The result.
	 */
	public OperationInvocationEvent(final OperationDescriptor<E> descriptor, final R result) {
		super();
		this.descriptor = descriptor;
		this.result = result;
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
	 * Gets the value for the result field.
	 * 
	 * @return The value for the result field.
	 */
	public R getResult() {
		return this.result;
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
	 * Sets a new value for the result field.
	 * 
	 * @param result
	 *            The new value for the result field.
	 */
	public void setResult(final R result) {
		this.result = result;
	}
}
