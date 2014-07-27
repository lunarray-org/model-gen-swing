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

import javax.swing.JTextArea;

import org.apache.commons.lang.Validate;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;

/**
 * Constructs the text area.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 */
public final class TextAreaPropertyStrategy<P>
		extends AbstractTextPropertyStrategy<P, JTextArea> {
	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The property descriptor. May not be null.
	 * @param context
	 *            The render context. May not be null.
	 */
	protected TextAreaPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
		super(descriptor, context);
	}

	/** {@inheritDoc} */
	@Override
	protected JTextArea createTextComponent() {
		return new JTextArea();
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
			return new TextAreaPropertyStrategy<P>(descriptor, context);
		}
	}
}
