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

import javax.swing.JTextField;

import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;

/**
 * Constructs the text output.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public final class TextOutputPropertyStrategy<P>
		extends AbstractFormPropertyRenderStrategy<P, JTextField> {

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The property descriptor.
	 * @param context
	 *            The render context.
	 */
	protected TextOutputPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
		super(descriptor, context);
	}

	/** {@inheritDoc} */
	@Override
	public void valueChanged(final P value) {
		// Value changed.
	}

	/** {@inheritDoc} */
	@Override
	protected JTextField createComponent() {
		final JTextField field = new JTextField(this.getDescriptor().getBufferAccessor().getStringValue(this.getMessage()));
		field.setEditable(false);
		return field;
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
			return new TextOutputPropertyStrategy<P>(descriptor, context);
		}
	}
}
