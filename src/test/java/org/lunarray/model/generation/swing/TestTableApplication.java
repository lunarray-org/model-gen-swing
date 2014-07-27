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
package org.lunarray.model.generation.swing;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.lunarray.model.descriptor.builder.annotation.presentation.builder.PresQualBuilder;
import org.lunarray.model.descriptor.converter.def.DefaultConverterTool;
import org.lunarray.model.descriptor.dictionary.composite.EntityDictionary;
import org.lunarray.model.descriptor.dictionary.composite.simple.CompositeDictionary;
import org.lunarray.model.descriptor.dictionary.enumeration.EnumDictionary;
import org.lunarray.model.descriptor.model.Model;
import org.lunarray.model.descriptor.resource.Resource;
import org.lunarray.model.descriptor.resource.simpleresource.SimpleClazzResource;
import org.lunarray.model.descriptor.validator.beanvalidation.BeanValidationValidator;
import org.lunarray.model.generation.swing.components.impl.TableComponentBuilder;
import org.lunarray.model.generation.swing.model.Sample01;
import org.lunarray.model.generation.swing.model.Sample02;
import org.lunarray.model.generation.swing.model.SampleEnum;

/**
 * 
 * @author Pal Hargitai (pal@lunarray.org)
 */
public class TestTableApplication {

	private Model<Object> model;

	public TestTableApplication() throws Exception {
		this.init();
	}

	public static void main(final String[] args) throws Exception {
		new TestTableApplication();
	}

	public void init() throws Exception {
		@SuppressWarnings("unchecked")
		final Resource<Class<? extends Object>> resource = new SimpleClazzResource<Object>(Sample01.class, Sample02.class, SampleEnum.class);
		final List<EntityDictionary<?, ?>> dictionaries = new LinkedList<EntityDictionary<?, ?>>();
		dictionaries.add(new Sample02Dictionary());
		final BeanValidationValidator validator = new BeanValidationValidator();
		this.model = PresQualBuilder.createBuilder()
				.extensions(new EnumDictionary(new CompositeDictionary(dictionaries)), validator, new DefaultConverterTool())
				.resources(resource).build();
		final JFrame frame = new JFrame("Test application");
		final TableComponentBuilder<Object, Sample01> builder = TableComponentBuilder.createBuilder();
		builder.model(this.model).entityKey("Sample01").entity(Sample01.DATA);
		frame.getContentPane().add(builder.build().getComponent());
		frame.setSize(200, 200);
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
}
