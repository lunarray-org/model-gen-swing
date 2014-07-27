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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueChangeListener;

/**
 * Constructs the radio selector.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public final class RadioSelectPropertyStrategy<P>
		extends AbstractSelectFieldPropertyStrategy<P, P, JPanel>
		implements ValueChangeListener<P> {
	/** The radio buttons. */
	private List<JRadioButton> buttons;
	/** The final value. */
	private P selectedValue;
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
	protected RadioSelectPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
		super(descriptor, context);
	}

	/**
	 * Gets the value for the buttons field.
	 * 
	 * @return The value for the buttons field.
	 */
	public List<JRadioButton> getButtons() {
		return this.buttons;
	}

	/**
	 * Gets the value for the selectedValue field.
	 * 
	 * @return The value for the selectedValue field.
	 */
	public P getSelectedValue() {
		return this.selectedValue;
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
	 * Sets a new value for the buttons field.
	 * 
	 * @param buttons
	 *            The new value for the buttons field.
	 */
	public void setButtons(final List<JRadioButton> buttons) {
		this.buttons = buttons;
	}

	/**
	 * Sets a new value for the selectedValue field.
	 * 
	 * @param selectedValue
	 *            The new value for the selectedValue field.
	 */
	public void setSelectedValue(final P selectedValue) {
		this.selectedValue = selectedValue;
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
		final Iterator<Item<P>> itemIt = this.selectItems.iterator();
		final Iterator<JRadioButton> buttonIt = this.buttons.iterator();
		while (itemIt.hasNext() && buttonIt.hasNext()) {
			final Item<P> item = itemIt.next();
			final JRadioButton button = buttonIt.next();
			if (item.getEntity().equals(value)) {
				button.setSelected(true);
			} else {
				button.setSelected(false);
			}
		}
	}

	/**
	 * Process an item.
	 * 
	 * @param panel
	 *            The panel to place it in.
	 * @param group
	 *            The panel group.
	 * @param item
	 *            The item.
	 */
	private void processItem(final JPanel panel, final ButtonGroup group, final Item<P> item) {
		final JRadioButton radio = new JRadioButton(item.getLabel());
		this.buttons.add(radio);
		radio.addActionListener(new ValueListener(item.getEntity()));
		group.add(radio);
		panel.add(radio);
	}

	/** {@inheritDoc} */
	@Override
	protected JPanel createSelectComponent() {
		final JPanel panel = new JPanel(new GridLayout(0, 1));
		final ButtonGroup group = new ButtonGroup();
		this.selectItems = this.getItems();
		this.buttons = new LinkedList<JRadioButton>();
		for (final Item<P> item : this.selectItems) {
			this.processItem(panel, group, item);
		}
		return panel;
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
			return new RadioSelectPropertyStrategy<P>(descriptor, context);
		}
	}

	/**
	 * The value change listener.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public final class ValueListener
			implements ActionListener {
		/** The value. */
		private P value;

		/**
		 * The value listener.
		 * 
		 * @param value
		 *            The value.
		 */
		public ValueListener(final P value) {
			this.value = value;
		}

		/** {@inheritDoc} */
		@Override
		public void actionPerformed(final ActionEvent event) {
			RadioSelectPropertyStrategy.this.getDescriptor().getBufferMutator()
					.setValue(RadioSelectPropertyStrategy.this.getMessage(), this.value);
			RadioSelectPropertyStrategy.this.selectedValue = this.value;
		}

		/**
		 * Gets the value for the value field.
		 * 
		 * @return The value for the value field.
		 */
		public P getValue() {
			return this.value;
		}

		/**
		 * Sets a new value for the value field.
		 * 
		 * @param value
		 *            The new value for the value field.
		 */
		public void setValue(final P value) {
			this.value = value;
		}
	}
}
