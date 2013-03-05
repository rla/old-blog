/**
 * Copyright (C) 2009 Raivo Laanemets <rl@starline.ee>
 *
 * This file is part of rl-blog.
 *
 * Foobar is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Foobar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with rl-blog.  If not, see <http://www.gnu.org/licenses/>.
 */
package ee.pri.rl.blog.web.model;

import java.io.Serializable;

import org.apache.wicket.model.LoadableDetachableModel;

import ee.pri.rl.blog.model.Identifiable;
import ee.pri.rl.blog.service.BlogService;
import ee.pri.rl.blog.web.BlogServiceHolder;

/**
 * Smart EntityModel.
 * http://wicketinaction.com/2008/09/building-a-smart-entitymodel/
 * 
 * @see Identifiable
 * @see BlogService#loadIdentifiable(Class, Serializable)
 * 
 * @author Raivo Laanemets
 */
public class EntityModel<T extends Identifiable<?>> extends LoadableDetachableModel<T> {
	private static final long serialVersionUID = 1L;

	private Class<?> clazz;
	private Serializable id;
	private transient T entity;

	public EntityModel(T entity) {
		this.clazz = entity.getClass();
		this.id = entity.getId();
		this.entity = entity;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T load() {
		if (entity == null) {
			entity = BlogServiceHolder.get().loadIdentifiable((Class<T>) clazz, id);
		}
		
		return entity;
	}

}