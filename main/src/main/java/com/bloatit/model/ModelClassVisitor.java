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


public interface ModelClassVisitor<ReturnType> {

    ReturnType visit(ExternalAccount model);

    ReturnType visit(InternalAccount model);

    ReturnType visit(Member model);

    ReturnType visit(BankTransaction model);

    ReturnType visit(Batch model);

    ReturnType visit(Description model);

    ReturnType visit(Group model);

    ReturnType visit(HighlightDemand model);

    ReturnType visit(JoinGroupInvitation model);

    ReturnType visit(Project model);

    ReturnType visit(Transaction model);

    ReturnType visit(Bug model);

    ReturnType visit(Contribution model);

    ReturnType visit(FileMetadata model);

    ReturnType visit(Kudos model);

    ReturnType visit(Comment model);

    ReturnType visit(Demand model);

    ReturnType visit(Offer model);

    ReturnType visit(Translation model);

    ReturnType visit(Release model);
}
