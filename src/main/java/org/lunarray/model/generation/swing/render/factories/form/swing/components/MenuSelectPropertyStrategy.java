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
import java.util.List;

import javax.swing.JComboBox;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;

/**
 * Constructs the menu selector.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public final class MenuSelectPropertyStrategy<P>
		extends AbstractSelectFieldPropertyStrategy<P, P, JComboBox>
		implements ValueChangeListener<P>, ActionListener {

	/** The box. */
	private JComboBox comboBox;
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
	protected MenuSelectPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
		super(descriptor, context);
	}

	/** {@inheritDoc} */
	@Override
	public void actionPerformed(final ActionEvent event) {
		final Object selectedItem = MenuSelectPropertyStrategy.this.comboBox.getSelectedItem();
		this.getDescriptor().getBufferMutator().setCoerceValue(this.getMessage(), selectedItem);
	}

	/**
	 * Gets the value for the comboBox field.
	 * 
	 * @return The value for the comboBox field.
	 */
	public JComboBox getComboBox() {
		return this.comboBox;
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
	 * Sets a new value for the comboBox field.
	 * 
	 * @param comboBox
	 *            The new value for the comboBox field.
	 */
	public void setComboBox(final JComboBox comboBox) {
		this.comboBox = comboBox;
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
				this.comboBox.setSelectedIndex(index);
			}
			index = index + 1;
		}
	}

	/** {@inheritDoc} */
	@Override
	protected JComboBox createSelectComponent() {
		this.selectItems = this.getItems();
		this.comboBox = new JComboBox(this.selectItems.toArray());
		this.comboBox.addActionListener(this);
		return this.comboBox;
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
			return new MenuSelectPropertyStrategy<P>(descriptor, context);
		}
	}
}
