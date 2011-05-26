/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.invoice;

import java.io.File;

import com.bloatit.framework.webprocessor.annotations.ParamConstraint;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.masters.Resource;
import com.bloatit.framework.webprocessor.url.PageForbiddenUrl;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Invoice;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.url.InvoiceResourceUrl;

/**
 * A invoice file
 *
 * @author fred
 */
@ParamContainer("invoice")
public final class InvoiceResource extends Resource {

    private static final String FILE_FIELD_NAME = "id";

    @ParamConstraint(optionalErrorMsg = @tr("The id of the resource is incorrect or missing"))
    @RequestParam(name = FILE_FIELD_NAME)
    private final Invoice invoice;

    private File file;
    private String invoiceNumber;

    public InvoiceResource(final InvoiceResourceUrl url) {
        this.invoice = url.getInvoice();

    }

    @Override
    protected Url checkRightsAndEverything() {
        try {
            file = new File(invoice.getFile());
            invoiceNumber = "invoice-" + invoice.getInvoiceNumber()+".pdf";
        } catch (UnauthorizedPrivateAccessException e) {
            return new PageForbiddenUrl();
        }

        return NO_ERROR;
    }


    @Override
    public String getFileUrl() {
        return file.getAbsolutePath();
    }

    @Override
    public long getFileSize() {
        return file.length();
    }

    @Override
    public String getFileName() {
            return invoiceNumber;
    }


}
