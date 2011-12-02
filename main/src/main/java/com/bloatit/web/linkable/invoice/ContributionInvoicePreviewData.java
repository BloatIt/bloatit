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

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.Optional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.masters.Data;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.Actor;
import com.bloatit.model.ContributionInvoice;
import com.bloatit.model.ContributionInvoice.PreviewSettings;
import com.bloatit.model.MilestoneContributionAmount;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.model.right.UnauthorizedPublicReadOnlyAccessException;
import com.bloatit.web.url.ContributionInvoicePreviewDataUrl;

/**
 * A invoice file
 *
 * @author fred
 */
@ParamContainer("contribution_invoice_preview")
public final class ContributionInvoicePreviewData extends Data {


    @RequestParam(name = "contribution")
    @NonOptional(@tr("The id of the contribution is incorrect or missing"))
    private final MilestoneContributionAmount contribution;

    @RequestParam(name = "actor")
    @NonOptional(@tr("The id of the actor is incorrect or missing"))
    private final Actor<?> actor;

    @RequestParam(name = "invoiceNumber")
    @NonOptional(@tr("The invoice number is incorrect or missing"))
    private final BigDecimal invoiceNumber;

    @RequestParam(name = "applyVAT")
    @Optional
    private final Boolean applyVAT;

    private byte[] bytes;

    private String filename;



    public ContributionInvoicePreviewData(final ContributionInvoicePreviewDataUrl url) throws PageNotFoundException {
        super(url);
        contribution = url.getContribution();
        invoiceNumber = url.getInvoiceNumber();
        applyVAT = url.getApplyVAT();
        actor = url.getActor();

    }

    @Override
    protected Url checkRightsAndEverything() {
        return NO_ERROR;
    }

    @Override
    protected void doProcess() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ContributionInvoice.generateInvoice(actor, contribution.getContribution().getAuthor(), "Contribution", "Contribution", contribution.getAmount(), contribution.getMilestone(), contribution.getContribution(), applyVAT,  new PreviewSettings(output, invoiceNumber));

        bytes = output.toByteArray();
        filename = actor.getContact().getInvoiceId(invoiceNumber)+ ".pdf";

        } catch (UnauthorizedPrivateAccessException e) {
            throw new ShallNotPassException("Showing this preview is not allowed",e);
        } catch (UnauthorizedPublicReadOnlyAccessException e) {
            throw new ShallNotPassException("Showing this preview is not allowed",e);
        }
    }

    @Override
    public long getFileSize() {
        return bytes.length;
    }

    @Override
    public String getFileName() {
        return filename;
    }

    @Override
    public byte[] getFileData() {
        return bytes;
    }



}
