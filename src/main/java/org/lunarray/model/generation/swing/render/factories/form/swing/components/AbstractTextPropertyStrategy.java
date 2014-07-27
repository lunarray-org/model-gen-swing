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

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.Descriptor;

/**
 * Constructs the text area.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The property type.
 * @param <J>
 *            The component type.
 */
public abstract class AbstractTextPropertyStrategy<P, J extends JTextComponent>
		extends AbstractFormPropertyRenderStrategy<P, J> {
	/** The text area. */
	private J textComponent;

	/**
	 * Constructs the strategy.
	 * 
	 * @param descriptor
	 *            The descriptor.
	 * @param context
	 *            The render context.
	 */
	public AbstractTextPropertyStrategy(final Descriptor<P> descriptor, final RenderContext<?> context) {
		super(descriptor, context);
	}

	/**
	 * Gets the value for the textComponent field.
	 * 
	 * @return The value for the textComponent field.
	 */
	public final J getTextComponent() {
		return this.textComponent;
	}

	/**
	 * Sets a new value for the textComponent field.
	 * 
	 * @param textComponent
	 *            The new value for the textComponent field.
	 */
	public final void setTextComponent(final J textComponent) {
		this.textComponent = textComponent;
	}

	/**
	 * Updated text event handling..
	 */
	public final void updatedText() {
		final String text = this.textComponent.getText();
		this.getDescriptor().getBufferMutator().setStringValue(this.getMessage(), text);
	}

	/** {@inheritDoc} */
	@Override
	public final void valueChanged(final P value) {
		this.textComponent.setText(this.getDescriptor().getBufferAccessor().getStringValue(this.getMessage()));
	}

	/** {@inheritDoc} */
	@Override
	protected final J createComponent() {
		this.textComponent = this.createTextComponent();
		this.textComponent.getDocument().addDocumentListener(new DocumentChangeListener());
		return this.textComponent;
	}

	/**
	 * Create a text component.
	 * 
	 * @return A text component.
	 */
	protected abstract J createTextComponent();

	/**
	 * The document change listener.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public final class DocumentChangeListener
			implements DocumentListener {

		/**
		 * Default constructor.
		 */
		public DocumentChangeListener() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public void changedUpdate(final DocumentEvent event) {
			AbstractTextPropertyStrategy.this.updatedText();
		}

		/** {@inheritDoc} */
		@Override
		public void insertUpdate(final DocumentEvent event) {
			AbstractTextPropertyStrategy.this.updatedText();
		}

		/** {@inheritDoc} */
		@Override
		public void removeUpdate(final DocumentEvent event) {
			AbstractTextPropertyStrategy.this.updatedText();
		}
	}
}
