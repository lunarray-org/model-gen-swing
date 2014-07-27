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

import java.util.List;

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;

/**
 * Constructs the list selector.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public final class SingleListSelectPropertyStrategy<P>
		extends AbstractSelectFieldPropertyStrategy<P, P, JList>
		implements ValueChangeListener<P> {

	/** The list. */
	private JList list;
	/** The items. */
	private List<Item<P>> selectItems;

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 */
	protected SingleListSelectPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
		super(descriptor, context);
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
	public List<Item<P>> getSelectItems() {
		return this.selectItems;
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
	public void setSelectItems(final List<Item<P>> selectItems) {
		this.selectItems = selectItems;
	}

	/** {@inheritDoc} */
	@Override
	public void valueChanged(final P value) {
		int index = 0;
		for (final Item<P> item : this.selectItems) {
			if (item.getEntity().equals(value)) {
				this.list.setSelectedIndex(index);
			}
			index = index + 1;
		}
	}

	/** {@inheritDoc} */
	@Override
	protected JList createSelectComponent() {
		this.list = new JList();
		this.selectItems = this.getItems();
		final String[] labels = new String[this.selectItems.size()];
		int index = 0;
		for (final Item<P> item : this.selectItems) {
			labels[index] = item.getLabel();
			index = index + 1;
		}
		this.list.addListSelectionListener(new ListListener());
		this.list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		return this.list;
	}

	/**
	 * A list listener for selections.
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
			final Item<P> item = (Item<P>) SingleListSelectPropertyStrategy.this.list.getSelectedValue();
			SingleListSelectPropertyStrategy.this.getDescriptor().getBufferMutator()
					.setValue(SingleListSelectPropertyStrategy.this.getMessage(), item.getEntity());
		}
	}

	/**
	 * The factory.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public static final class StrategyFactory
			implements FormPropertyRenderStrategy.DescriptorFactory {

		/**
		 * Default constructor.
		 */
		public StrategyFactory() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public <E, P> FormPropertyRenderStrategy<P> createStrategy(final RenderContext<E> context, final Descriptor<P> descriptor) {
			Validate.notNull(descriptor, "Descriptor may not be null.");
			Validate.notNull(context, "Context may not be null.");
			return new SingleListSelectPropertyStrategy<P>(descriptor, context);
		}
	}
}
