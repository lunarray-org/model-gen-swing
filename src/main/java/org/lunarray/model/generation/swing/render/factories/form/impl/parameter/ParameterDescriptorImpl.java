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
package org.lunarray.model.generation.swing.render.factories.form.impl.parameter;

import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.model.operation.parameters.ParameterDescriptor;
import org.lunarray.model.descriptor.util.OperationInvocationBuilder;

/**
 * A parameter descriptor.
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 * @param <P>
 *            The type.
 * @param <E>
 *            The entity.
 */
public final class ParameterDescriptorImpl<P, E>
		extends AbstractParameterDescriptorImpl<P, E> {

	/**
	 * Default constructor.
	 * 
	 * @param parameter
	 *            The parameter.
	 * @param builder
	 *            The builder.
	 * @param model
	 *            The model.
	 */
	public ParameterDescriptorImpl(final ParameterDescriptor<P> parameter, final OperationInvocationBuilder<E> builder,
			final Model<? super E> model) {
		super(parameter, builder, model);
	}
}
