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

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.CollectionDescriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;

/**
 * Constructs the list selector.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <C>
 *            The collection type.
 * @param <P>
 *            The property type.
 */
public final class MultipleListSelectPropertyStrategy<C, P extends Collection<C>>
		extends AbstractSelectFieldPropertyStrategy<P, C, JList>
		implements ValueChangeListener<P> {

	/** The collection descriptor. */
	private CollectionDescriptor<C, P> collectionDescriptor;
	/** The list. */
	private JList list;
	/** The items. */
	private List<Item<C>> selectItems;

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 */
	protected MultipleListSelectPropertyStrategy(final CollectionDescriptor<C, P> descriptor, final RenderContext<?> context) {
		super(descriptor, context);
		this.collectionDescriptor = descriptor;
	}

	/**
	 * Gets the value for the collectionDescriptor field.
	 * 
	 * @return The value for the collectionDescriptor field.
	 */
	public CollectionDescriptor<C, P> getCollectionDescriptor() {
		return this.collectionDescriptor;
	}

	/**
	 * Gets the value for the list field.
	 * 
	 * @return The value for the list field.
	 */
	public JList getList() {
		return this.list;
	}

	/**
	 * Gets the value for the selectItems field.
	 * 
	 * @return The value for the selectItems field.
	 */
	public List<Item<C>> getSelectItems() {
		return this.selectItems;
	}

	/**
	 * Sets a new value for the collectionDescriptor field.
	 * 
	 * @param collectionDescriptor
	 *            The new value for the collectionDescriptor field.
	 */
	public void setCollectionDescriptor(final CollectionDescriptor<C, P> collectionDescriptor) {
		this.collectionDescriptor = collectionDescriptor;
	}

	/**
	 * Sets a new value for the list field.
	 * 
	 * @param list
	 *            The new value for the list field.
	 */
	public void setList(final JList list) {
		this.list = list;
	}

	/**
	 * Sets a new value for the selectItems field.
	 * 
	 * @param selectItems
	 *            The new value for the selectItems field.
	 */
	public void setSelectItems(final List<Item<C>> selectItems) {
		this.selectItems = selectItems;
	}

	/** {@inheritDoc} */
	@Override
	public void valueChanged(final P value) {
		int index = 0;
		final List<Integer> selected = new LinkedList<Integer>();
		final Collection<C> collection = this.collectionDescriptor.getCollectionBufferAccessor().getValues(this.getMessage());
		if (!CheckUtil.isNull(collection)) {
			for (final Item<C> item : this.selectItems) {
				if (collection.contains(item.getEntity())) {
					selected.add(Integer.valueOf(index));
				}
				index = index + 1;
			}
			final int[] selectedArray = new int[selected.size()];
			int copyIndex = 0;
			for (final Integer select : selected) {
				selectedArray[copyIndex] = select.intValue();
				copyIndex = copyIndex + 1;
			}
			this.list.setSelectedIndices(selectedArray);
		}
	}

	/** {@inheritDoc} */
	@Override
	protected JList createSelectComponent() {
		this.list = new JList();
		this.selectItems = this.getItems();
		final String[] labels = new String[this.selectItems.size()];
		int index = 0;
		for (final Item<C> item : this.selectItems) {
			labels[index] = item.getLabel();
			index = index + 1;
		}
		this.list.addListSelectionListener(new ListListener());
		this.list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		return this.list;
	}

	/**
	 * The list selection listener.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public final class ListListener
			implements ListSelectionListener {

		/**
		 * Default constructor.
		 */
		public ListListener() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public void valueChanged(final ListSelectionEvent event) {
			@SuppressWarnings("unchecked")
			final Item<C>[] selectedItems = (Item<C>[]) MultipleListSelectPropertyStrategy.this.list.getSelectedValues();
			final Collection<C> collection = new LinkedList<C>();
			for (final Item<C> item : selectedItems) {
				collection.add(item.getEntity());
			}
			MultipleListSelectPropertyStrategy.this.collectionDescriptor.getCollectionBufferMutator().setValues(
					MultipleListSelectPropertyStrategy.this.getMessage(), collection);
		}
	}

	/**
	 * The factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class StrategyFactory
			implements FormPropertyRenderStrategy.CollectionDescriptorFactory {

		/**
		 * Default constructor.
		 */
		public StrategyFactory() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public <E, C, P extends Collection<C>> FormPropertyRenderStrategy<P> createStrategy(final RenderContext<E> context,
				final CollectionDescriptor<C, P> descriptor) {
			Validate.notNull(descriptor, "Descriptor may not be null.");
			Validate.notNull(context, "Context may not be null.");
			return new MultipleListSelectPropertyStrategy<C, P>(descriptor, context);
		}
	}
}
