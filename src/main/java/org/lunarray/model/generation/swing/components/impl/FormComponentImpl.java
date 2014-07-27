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
package org.lunarray.model.generation.swing.components.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.apache.commons.lang.Validate;
import org.lunarray.common.check.CheckUtil;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.validator.EntityValidator;
import org.lunarray.model.descriptor.validator.PropertyViolation;
import org.lunarray.model.generation.swing.components.FormComponent;
import org.lunarray.model.generation.swing.render.RenderContext;
import org.lunarray.model.generation.swing.render.factories.form.FormPropertyRenderStrategy;
import org.lunarray.model.generation.swing.render.factories.form.descriptor.ValueVisitor;
import org.lunarray.model.generation.swing.render.factories.form.swing.FormPropertyRenderStrategyFactoryImpl;
import org.lunarray.model.generation.swing.render.factories.form.swing.components.OperationOutputStrategy;
import org.lunarray.model.generation.swing.util.CellLocation;
import org.lunarray.model.generation.swing.util.ComponentLocation;
import org.lunarray.model.generation.swing.util.ComponentLocation.ComponentLocationBuilder;
import org.lunarray.model.generation.swing.util.MessageUtil;
import org.lunarray.model.generation.util.Composer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Swing form.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <S>
 *            The super type.
 * @param <E>
 *            The entity type.
 */
public final class FormComponentImpl<S, E extends S>
		extends AbstractComponent<S, E>
		implements FormComponent<E> {

	/** The bundle button cancel key. */
	private static final String BUTTON_CANCEL = "form.button.cancel";
	/** The bundle button submit key. */
	private static final String BUTTON_SUBMIT = "form.button.submit";
	/** The cancel action. */
	private static final String CANCEL_ACTION = "cancel";
	/** The commit action. */
	private static final String COMMIT_ACTION = "commit";
	/** Horizontal weight for the component. */
	private static final double COMPONENT_HOR_WEIGHT = 0.7;
	/** Component padding. */
	private static final int COMPONENT_PADDING = 2;
	/** Vertical weight. */
	private static final double COMPONENT_VERT_WEIGHT = 0.0;
	/** Component width. */
	private static final int COMPONENT_WIDTH = 2;
	/** Height. */
	private static final int DEFAULT_HEIGHT = 1;
	/** Default padding. */
	private static final int DEFAULT_PAD = 0;
	/** Horizontal weight. */
	private static final double HOR_WEIGHT = 0.3;
	/** Horizontal weight for the label. */
	private static final double LABEL_HOR_WEIGHT = 0.3;
	/** Label index. */
	private static final int LABEL_INDEX = 0;
	/** Label width. */
	private static final int LABEL_WIDTH = 1;
	/** Large width. */
	private static final int LARGE_WIDTH = 5;
	/** Left column index. */
	private static final int LEFT_COLUMN = 1;
	/** The logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(FormComponentImpl.class);
	/** Right column index. */
	private static final int RIGHT_COLUMN = 3;
	/** Row duplication factor. */
	private static final int ROW_DUPLICATION = 2;
	/** Serial id. */
	private static final long serialVersionUID = 4222966867630504854L;
	/** Small width. */
	private static final int SMALL_WIDTH = 3;
	/** Validation message. */
	private static final String STRATEGY_NULL = "Strategy may not be null.";
	/** Vertical weight. */
	private static final double VERT_WEIGHT = 1.0;
	/** The cancel button. */
	private JButton cancelButton;
	/** The panel. */
	private JPanel componentPanel;
	/** The entity. */
	private E entity;
	/** An extra row counter. */
	private int extraRowCounter;
	/** The form. */
	private JPanel form;
	/** Message elements. */
	private Map<String, JLabel> labels;
	/** The message position. */
	private MessagePosition position;
	/** A row counter. */
	private int rows;
	/** The submit button. */
	private JButton submitButton;
	/** The value visitors. */
	private List<ValueVisitor> visitors;

	/**
	 * Constructs the form component.
	 * 
	 * @param model
	 *            The model. May not be null.
	 * @param entityKey
	 *            The entity key. Must be a non-null known key.
	 * @param entity
	 *            The entity. May not be null.
	 * @param locale
	 *            The locale.
	 * @param position
	 *            The Message position. May not be null.
	 */
	protected FormComponentImpl(final Model<S> model, final String entityKey, final E entity, final Locale locale,
			final MessagePosition position) {
		super(model, entityKey, locale);
		Validate.notNull(entity, "Entity may not be null.");
		Validate.notNull(position, "Position has to be set.");
		this.setLayout(new BorderLayout());
		this.labels = new HashMap<String, JLabel>();
		this.entity = entity;
		this.visitors = new LinkedList<ValueVisitor>();
		this.position = position;
		this.init();
		this.rows = 0;
		this.extraRowCounter = 0;
	}

	/** {@inheritDoc} */
	@Override
	public void addActionListener(final ActionListener listener) {
		this.cancelButton.addActionListener(listener);
		this.submitButton.addActionListener(listener);
	}

	/** {@inheritDoc} */
	@Override
	public JButton getCancelButton() {
		return this.cancelButton;
	}

	/** {@inheritDoc} */
	@Override
	public JComponent getComponent() {
		return this;
	}

	/**
	 * Gets the value for the componentPanel field.
	 * 
	 * @return The value for the componentPanel field.
	 */
	public JPanel getComponentPanel() {
		return this.componentPanel;
	}

	/**
	 * Gets the value for the entity field.
	 * 
	 * @return The value for the entity field.
	 */
	@Override
	public E getEntity() {
		return this.entity;
	}

	/**
	 * Gets the value for the extraRowCounter field.
	 * 
	 * @return The value for the extraRowCounter field.
	 */
	public int getExtraRowCounter() {
		return this.extraRowCounter;
	}

	/** {@inheritDoc} */
	@Override
	public JPanel getForm() {
		return this.form;
	}

	/**
	 * Gets the value for the labels field.
	 * 
	 * @return The value for the labels field.
	 */
	public Map<String, JLabel> getLabels() {
		return this.labels;
	}

	/**
	 * Gets the value for the messagePosition field.
	 * 
	 * @return The value for the messagePosition field.
	 */
	public MessagePosition getMessagePosition() {
		return this.position;
	}

	/**
	 * Gets the value for the position field.
	 * 
	 * @return The value for the position field.
	 */
	public MessagePosition getPosition() {
		return this.position;
	}

	/**
	 * Gets the value for the rows field.
	 * 
	 * @return The value for the rows field.
	 */
	public int getRows() {
		return this.rows;
	}

	/** {@inheritDoc} */
	@Override
	public JButton getSubmitButton() {
		return this.submitButton;
	}

	/**
	 * Gets the value for the visitors field.
	 * 
	 * @return The value for the visitors field.
	 */
	public List<ValueVisitor> getVisitors() {
		return this.visitors;
	}

	/** {@inheritDoc} */
	@Override
	public void processBeginStrategy(final OperationOutputStrategy<E> strategy) {
		Validate.notNull(strategy, FormComponentImpl.STRATEGY_NULL);
		FormComponentImpl.LOGGER.debug("Processing operation begin strategy: {}", strategy);
		final int realRow = this.calculateRealRow();
		final JComponent label = strategy.getLabel();
		this.componentPanel.add(label, new GridBagConstraints(FormComponentImpl.LABEL_INDEX, realRow, FormComponentImpl.LABEL_WIDTH,
				FormComponentImpl.DEFAULT_HEIGHT, FormComponentImpl.LABEL_HOR_WEIGHT, FormComponentImpl.COMPONENT_VERT_WEIGHT,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(FormComponentImpl.DEFAULT_PAD,
						FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD),
				FormComponentImpl.COMPONENT_PADDING, FormComponentImpl.COMPONENT_PADDING));
		this.extraRowCounter = this.extraRowCounter + 1;

	}

	/** {@inheritDoc} */
	@Override
	public void processEndStrategy(final OperationOutputStrategy<E> strategy) {
		Validate.notNull(strategy, FormComponentImpl.STRATEGY_NULL);
		FormComponentImpl.LOGGER.debug("Processing operation end strategy: {}", strategy);
		final int realRow = this.calculateRealRow();
		final JLabel label = strategy.getLabel();
		final ComponentLocation location = this.calculateLocation(realRow);
		final CellLocation componentLocation = location.getComponent();
		final CellLocation messageLocation = location.getMessage();
		final JComponent button = strategy.getButton();
		final JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		buttonPanel.add(button);
		this.componentPanel.add(buttonPanel, new GridBagConstraints(componentLocation.getColumn(), componentLocation.getRow(),
				FormComponentImpl.LABEL_WIDTH, FormComponentImpl.DEFAULT_HEIGHT, FormComponentImpl.LABEL_HOR_WEIGHT,
				FormComponentImpl.COMPONENT_VERT_WEIGHT, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(
						FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD,
						FormComponentImpl.DEFAULT_PAD), FormComponentImpl.COMPONENT_PADDING, FormComponentImpl.COMPONENT_PADDING));
		final JLabel message = strategy.getMessage();
		message.setSize(label.getWidth(), label.getHeight());
		message.setForeground(Color.RED);
		this.componentPanel.add(message, new GridBagConstraints(messageLocation.getColumn(), messageLocation.getRow(),
				FormComponentImpl.COMPONENT_WIDTH, FormComponentImpl.DEFAULT_HEIGHT, FormComponentImpl.COMPONENT_HOR_WEIGHT,
				FormComponentImpl.COMPONENT_VERT_WEIGHT, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.HORIZONTAL, new Insets(
						FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD,
						FormComponentImpl.DEFAULT_PAD), FormComponentImpl.COMPONENT_PADDING, FormComponentImpl.COMPONENT_PADDING));
		this.rows = this.rows + 1;
	}

	/**
	 * Processes a strategy.
	 * 
	 * @param strategy
	 *            The strategy.
	 * @param <P>
	 *            The property type.
	 */
	@Override
	public <P> void processStrategy(final FormPropertyRenderStrategy<P> strategy) {
		Validate.notNull(strategy, FormComponentImpl.STRATEGY_NULL);
		FormComponentImpl.LOGGER.debug("Processing property strategy: {}", strategy);
		final int realRow = this.calculateRealRow();
		final ComponentLocation location = this.calculateLocation(realRow);
		final CellLocation componentLocation = location.getComponent();
		final CellLocation messageLocation = location.getMessage();
		final JComponent label = strategy.getLabel();
		this.componentPanel.add(label, new GridBagConstraints(FormComponentImpl.LABEL_INDEX, realRow, FormComponentImpl.LABEL_WIDTH,
				FormComponentImpl.DEFAULT_HEIGHT, FormComponentImpl.LABEL_HOR_WEIGHT, FormComponentImpl.COMPONENT_VERT_WEIGHT,
				GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(FormComponentImpl.DEFAULT_PAD,
						FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD),
				FormComponentImpl.COMPONENT_PADDING, FormComponentImpl.COMPONENT_PADDING));
		final JComponent component = strategy.getComponent();
		this.componentPanel.add(component, new GridBagConstraints(componentLocation.getColumn(), componentLocation.getRow(),
				FormComponentImpl.COMPONENT_WIDTH, FormComponentImpl.DEFAULT_HEIGHT, FormComponentImpl.COMPONENT_HOR_WEIGHT,
				FormComponentImpl.COMPONENT_VERT_WEIGHT, GridBagConstraints.PAGE_START, GridBagConstraints.HORIZONTAL, new Insets(
						FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD,
						FormComponentImpl.DEFAULT_PAD), FormComponentImpl.COMPONENT_PADDING, FormComponentImpl.COMPONENT_PADDING));
		final JLabel message = strategy.getMessage();
		message.setSize(label.getWidth(), label.getHeight());
		message.setForeground(Color.RED);
		this.labels.put(strategy.getName(), message);
		this.componentPanel.add(message, new GridBagConstraints(messageLocation.getColumn(), messageLocation.getRow(),
				FormComponentImpl.COMPONENT_WIDTH, FormComponentImpl.DEFAULT_HEIGHT, FormComponentImpl.COMPONENT_HOR_WEIGHT,
				FormComponentImpl.COMPONENT_VERT_WEIGHT, GridBagConstraints.FIRST_LINE_END, GridBagConstraints.HORIZONTAL, new Insets(
						FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD,
						FormComponentImpl.DEFAULT_PAD), FormComponentImpl.COMPONENT_PADDING, FormComponentImpl.COMPONENT_PADDING));
		this.visitors.add(strategy.getVisitor());
		this.rows = this.rows + 1;
	}

	/** {@inheritDoc} */
	@Override
	public void removeActionListener(final ActionListener listener) {
		this.cancelButton.removeActionListener(listener);
		this.submitButton.removeActionListener(listener);
	}

	/**
	 * Sets a new value for the cancelButton field.
	 * 
	 * @param cancelButton
	 *            if (this.component instanceof Property.Viewer) { final
	 *            Property.Viewer viewer = (Property.Viewer) this.component;
	 *            viewer.setPropertyDataSource(new ModelProperty
	 *            <P, E>
	 *            (this.converterTool, this.property, this.entity)); } The new
	 *            value for the cancelButton field.
	 */
	public void setCancelButton(final JButton cancelButton) {
		this.cancelButton = cancelButton;
	}

	/**
	 * Sets a new value for the componentPanel field.
	 * 
	 * @param componentPanel
	 *            The new value for the componentPanel field.
	 */
	public void setComponentPanel(final JPanel componentPanel) {
		this.componentPanel = componentPanel;
	}

	/**
	 * Sets a new value for the entity field.
	 * 
	 * @param entity
	 *            The new value for the entity field.
	 */
	public void setEntity(final E entity) {
		this.entity = entity;
		this.init();
	}

	/**
	 * Sets a new value for the extraRowCounter field.
	 * 
	 * @param extraRowCounter
	 *            The new value for the extraRowCounter field.
	 */
	public void setExtraRowCounter(final int extraRowCounter) {
		this.extraRowCounter = extraRowCounter;
	}

	/**
	 * Sets a new value for the form field.
	 * 
	 * @param form
	 *            The new value for the form field.
	 */
	public void setForm(final JPanel form) {
		this.form = form;
	}

	/**
	 * Sets a new value for the labels field.
	 * 
	 * @param labels
	 *            The new value for the labels field.
	 */
	public void setLabels(final Map<String, JLabel> labels) {
		this.labels = labels;
	}

	/**
	 * Sets a new value for the messagePosition field.
	 * 
	 * @param messagePosition
	 *            The new value for the messagePosition field.
	 */
	public void setMessagePosition(final MessagePosition messagePosition) {
		this.position = messagePosition;
	}

	/**
	 * Sets a new value for the position field.
	 * 
	 * @param position
	 *            The new value for the position field.
	 */
	public void setPosition(final MessagePosition position) {
		this.position = position;
	}

	/**
	 * Sets a new value for the rows field.
	 * 
	 * @param rows
	 *            The new value for the rows field.
	 */
	public void setRows(final int rows) {
		this.rows = rows;
	}

	/**
	 * Sets a new value for the submitButton field.
	 * 
	 * @param submitButton
	 *            The new value for the submitButton field.
	 */
	public void setSubmitButton(final JButton submitButton) {
		this.submitButton = submitButton;
	}

	/**
	 * Sets a new value for the visitors field.
	 * 
	 * @param visitors
	 *            The new value for the visitors field.
	 */
	public void setVisitors(final List<ValueVisitor> visitors) {
		this.visitors = visitors;
	}

	/**
	 * Calculates the location.
	 * 
	 * @param realRow
	 *            The current row.
	 * @return The location.
	 */
	private ComponentLocation calculateLocation(final int realRow) {
		final ComponentLocationBuilder builder = ComponentLocation.createBuilder();
		if (MessagePosition.TOP == this.position) {
			builder.message().row(realRow);
			builder.component().row(realRow + FormComponentImpl.LEFT_COLUMN);
		} else if (MessagePosition.BOTTOM == this.position) {
			builder.message().row(realRow + FormComponentImpl.LEFT_COLUMN);
			builder.component().row(realRow);
		} else {
			builder.message().row(realRow);
			builder.component().row(realRow);
		}
		if (MessagePosition.LEFT == this.position) {
			builder.message().column(FormComponentImpl.LEFT_COLUMN);
			builder.component().column(FormComponentImpl.RIGHT_COLUMN);
		} else if (MessagePosition.RIGHT == this.position) {
			builder.message().column(FormComponentImpl.RIGHT_COLUMN);
			builder.component().column(FormComponentImpl.LEFT_COLUMN);
		} else {
			builder.message().column(FormComponentImpl.LEFT_COLUMN);
			builder.component().column(FormComponentImpl.LEFT_COLUMN);
		}
		return builder.build();
	}

	/**
	 * Calculate the row.
	 * 
	 * @return The row.
	 */
	private int calculateRealRow() {
		int realRow;
		if ((MessagePosition.BOTTOM == this.position) || (MessagePosition.TOP == this.position)) {
			realRow = this.rows * 2;
		} else {
			realRow = this.rows;
		}
		realRow = realRow + this.extraRowCounter;
		return realRow;
	}

	/** Initializes the form. */
	private void init() {
		this.removeAll();
		this.form = new JPanel(new BorderLayout());
		final Composer<RenderContext<E>, S, E> composer = new Composer<RenderContext<E>, S, E>();
		final Model<S> model = this.getModel();
		composer.setContext(new RenderContext<E>(model));
		composer.setPropertyRenderStrategyFactory(new FormPropertyRenderStrategyFactoryImpl<E>(this));
		composer.setVariableResolver(new ComponentVariableResolver());
		this.form.add(new JLabel(composer.getLabel()), BorderLayout.NORTH);
		this.componentPanel = new JPanel(new GridBagLayout());
		this.form.add(this.componentPanel, BorderLayout.CENTER);
		composer.compose(true);
		if ((MessagePosition.BOTTOM == this.position) || (MessagePosition.TOP == this.position)) {
			this.componentPanel.add(new JPanel(), new GridBagConstraints(FormComponentImpl.DEFAULT_PAD, FormComponentImpl.ROW_DUPLICATION
					* this.rows, FormComponentImpl.SMALL_WIDTH, FormComponentImpl.DEFAULT_HEIGHT, FormComponentImpl.HOR_WEIGHT,
					FormComponentImpl.VERT_WEIGHT, GridBagConstraints.PAGE_END, GridBagConstraints.BOTH, new Insets(
							FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD,
							FormComponentImpl.DEFAULT_PAD), FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD));
		} else {
			this.componentPanel.add(new JPanel(), new GridBagConstraints(FormComponentImpl.DEFAULT_PAD, this.rows,
					FormComponentImpl.LARGE_WIDTH, FormComponentImpl.DEFAULT_HEIGHT, FormComponentImpl.HOR_WEIGHT,
					FormComponentImpl.VERT_WEIGHT, GridBagConstraints.PAGE_END, GridBagConstraints.BOTH, new Insets(
							FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD,
							FormComponentImpl.DEFAULT_PAD), FormComponentImpl.DEFAULT_PAD, FormComponentImpl.DEFAULT_PAD));
		}
		this.form.setBorder(new BevelBorder(BevelBorder.LOWERED));
		final JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		this.submitButton = new JButton(MessageUtil.getMessage(FormComponentImpl.BUTTON_SUBMIT));
		this.submitButton.setActionCommand(FormComponentImpl.COMMIT_ACTION);
		this.submitButton.addActionListener(new ButtonListener());
		bottomPanel.add(this.submitButton);
		this.cancelButton = new JButton(MessageUtil.getMessage(FormComponentImpl.BUTTON_CANCEL));
		this.cancelButton.setActionCommand(FormComponentImpl.CANCEL_ACTION);
		this.cancelButton.addActionListener(new ButtonListener());
		bottomPanel.add(this.cancelButton);
		this.form.add(bottomPanel, BorderLayout.SOUTH);
		this.add(this.form);
	}

	/**
	 * The button listener.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public final class ButtonListener
			implements ActionListener {

		/**
		 * Default constructor.
		 */
		public ButtonListener() {
			// Default constructor.
		}

		/** {@inheritDoc} */
		@Override
		public void actionPerformed(final ActionEvent event) {
			if (FormComponentImpl.COMMIT_ACTION.equals(event.getActionCommand())) {
				for (final ValueVisitor visitor : FormComponentImpl.this.visitors) {
					visitor.apply();
				}
				final EntityValidator validator = FormComponentImpl.this.getModel().getExtension(EntityValidator.class);
				if (!CheckUtil.isNull(validator)) {
					final Set<String> unviolated = new HashSet<String>(FormComponentImpl.this.labels.keySet());
					final Collection<PropertyViolation<E, ?>> violations = validator.validate(FormComponentImpl.this.getEntityDescriptor(),
							FormComponentImpl.this.getEntity());
					for (final PropertyViolation<E, ?> violation : violations) {
						final String name = violation.getProperty().getName();
						FormComponentImpl.this.labels.get(name).setText(violation.getMessage());
						unviolated.remove(name);
					}
					for (final String unviolate : unviolated) {
						FormComponentImpl.this.labels.get(unviolate).setText("");
					}
				}
			} else if (FormComponentImpl.CANCEL_ACTION.equals(event.getActionCommand())) {
				for (final ValueVisitor visitor : FormComponentImpl.this.visitors) {
					visitor.revert();
				}
				for (final JLabel label : FormComponentImpl.this.labels.values()) {
					label.setText("");
				}
			}
		}
	}

	/**
	 * The message location.
	 * 
	 * @author Pal Hargitai (pal@lunarray.org)
	 */
	public enum MessagePosition {
		/** Beneath input component. */
		BOTTOM,
		/** Left the input component. */
		LEFT,
		/** Right the input component. */
		RIGHT,
		/** Above the input component. */
		TOP;
	}
}
