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

import javax.swing.JLabel;

import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.property.CollectionPropertyDescriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.CollectionAccessBuffer;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.CollectionDescriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.CollectionMutateBuffer;

/**
 * A collection descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <C>
 *            The collection type.
 * @param <P>
 *            The type.
 * @param <E>
 *            The entity.
 */
public final class CollectionPropertyDescriptorImpl<C, P extends Collection<C>, E>
		extends AbstractPropertyDescriptorImpl<P, E>
		implements CollectionDescriptor<C, P>, CollectionAccessBuffer<C, P>, CollectionMutateBuffer<C, P> {

	/** The collection property. */
	private CollectionPropertyDescriptor<C, P, E> collectionProperty;

	/**
	 * Default constructor.
	 * 
	 * @param property
	 *            The property.
	 * @param model
	 *            The model.
	 */
	public CollectionPropertyDescriptorImpl(final CollectionPropertyDescriptor<C, P, E> property, final Model<? super E> model) {
		super(property, model);
		this.collectionProperty = property;
	}

	/** {@inheritDoc} */
	@Override
	public CollectionAccessBuffer<C, P> getCollectionBufferAccessor() {
		return this;
	}

	/** {@inheritDoc} */
	@Override
	public CollectionMutateBuffer<C, P> getCollectionBufferMutator() {
		return this;
	}

	/**
	 * Gets the value for the collectionProperty field.
	 * 
	 * @return The value for the collectionProperty field.
	 */
	public CollectionPropertyDescriptor<C, P, E> getCollectionProperty() {
		return this.collectionProperty;
	}

	/** {@inheritDoc} */
	@Override
	public Collection<C> getValues(final JLabel message) {
		return this.getValue(message);
	}

	/**
	 * Sets a new value for the collectionProperty field.
	 * 
	 * @param collectionProperty
	 *            The new value for the collectionProperty field.
	 */
	public void setCollectionProperty(final CollectionPropertyDescriptor<C, P, E> collectionProperty) {
		this.collectionProperty = collectionProperty;
	}

	/** {@inheritDoc} */
	@Override
	public void setValues(final JLabel message, final Collection<C> values) {
		this.getValue(message).clear();
		this.getValue(message).addAll(values);
	}
}
