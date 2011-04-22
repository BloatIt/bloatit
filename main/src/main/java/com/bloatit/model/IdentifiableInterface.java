//
// Copyright (c) 2011 Linkeos.
//
// This file is part of Elveos.org.
// Elveos.org is free software: you can redistribute it and/or modify it
// under the terms of the GNU General Public License as published by the
// Free Software Foundation, either version 3 of the License, or (at your
// option) any later version.
//
// Elveos.org is distributed in the hope that it will be useful, but WITHOUT
// ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
// more details.
// You should have received a copy of the GNU General Public License along
// with Elveos.org. If not, see http://www.gnu.org/licenses/.
//
package com.bloatit.model;

/**
 * @author Thomas Guyard Every identifiable should inherite from the
 *         identifiable interface.
 */
public interface IdentifiableInterface {

    /**
     * return the unique id of this identifiable.
     * 
     * @return the unique id of this identifiable.
     */
    Integer getId();

    /**
     * Implement the visitor pattern.
     * 
     * @param <ReturnType> the return type of the accept method
     * @param visitor a visitor that can visit every model layer class.
     * @return visitor.visit()
     */
    <ReturnType> ReturnType accept(ModelClassVisitor<ReturnType> visitor);

}
