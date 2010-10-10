/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.model;

public class Demand {
    private String title;
    private String description;
    private String specification;
    private int karma;
    private int id;
    private Member author;

    public Demand(int id, String title, String description, String specification, int karma, Member author) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.specification = specification;
        this.karma = karma;
        this.author = author;
    }

    public Member getAuthor() {
        return author;
    }

    public String getDescription() {
        return description;
    }

    public int getId() {
        return id;
    }

    public int getKarma() {
        return karma;
    }

    public String getSpecification() {
        return specification;
    }

    public String getTitle() {
        return title;
    }
}