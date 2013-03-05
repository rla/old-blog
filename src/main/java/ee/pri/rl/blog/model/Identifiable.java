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
package ee.pri.rl.blog.model;

import java.io.Serializable;

/**
 * Part of smart EntityModel implementation. Read more from
 * http://wicketinaction.com/2008/09/building-a-smart-entitymodel
 * 
 * @author Raivo Laanemets
 */
public interface Identifiable<T extends Serializable> {
	
	/**
	 * Returns the primary key of the entity.
	 */
	T getId();
}