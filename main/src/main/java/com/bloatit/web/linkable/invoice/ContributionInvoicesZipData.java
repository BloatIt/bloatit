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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.webprocessor.PageNotFoundException;
import com.bloatit.framework.webprocessor.annotations.NonOptional;
import com.bloatit.framework.webprocessor.annotations.ParamContainer;
import com.bloatit.framework.webprocessor.annotations.RequestParam;
import com.bloatit.framework.webprocessor.annotations.tr;
import com.bloatit.framework.webprocessor.masters.Data;
import com.bloatit.framework.webprocessor.url.Url;
import com.bloatit.model.ContributionInvoice;
import com.bloatit.model.Milestone;
import com.bloatit.model.MilestoneContributionAmount;
import com.bloatit.model.right.UnauthorizedPrivateAccessException;
import com.bloatit.web.url.ContributionInvoicesZipDataUrl;

/**
 * A invoice file
 * 
 * @author fred
 */
@ParamContainer("contribution_invoices_zip")
public final class ContributionInvoicesZipData extends Data {

    
    @RequestParam(name = "milestone")
    @NonOptional(@tr("The id of the contribution is incorrect or missing"))
    private final Milestone milestone;

    private byte[] bytes;

    private String filename;

    public ContributionInvoicesZipData(final ContributionInvoicesZipDataUrl url) throws PageNotFoundException {
        super(url);
        milestone = url.getMilestone();
        
    }

    @Override
    protected Url checkRightsAndEverything() {
        
        return NO_ERROR;
    }

    @Override
    protected void doProcess() {
        
        filename = "invoices-milestone-"+milestone.getId()+".zip";
        
        int targetPosition = milestone.getPosition();
        List<ContributionInvoice> invoices = new ArrayList<ContributionInvoice>();
        for(MilestoneContributionAmount contribution: milestone.getContributionAmounts()) {
            int position = 1;
            for(ContributionInvoice invoice: contribution.getContribution().getInvoices()) {
                if(position == targetPosition) {
                    invoices.add(invoice);
                }
                position++;
            }
        }
        
     
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        
        try {
            // Create the ZIP file
            ZipOutputStream out = new ZipOutputStream(output);

            // Compress the files
            for (ContributionInvoice invoice: invoices) {
                FileInputStream in = new FileInputStream(invoice.getFile());

                // Add ZIP entry to output stream.
                out.putNextEntry(new ZipEntry(invoice.getInvoiceNumber()+".pdf"));

                // Transfer bytes from the file to the ZIP file
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                // Complete the entry
                out.closeEntry();
                in.close();
            }

            // Complete the ZIP file
            out.close();
        } catch (IOException e) {
            throw new BadProgrammerException(e);
        } catch (UnauthorizedPrivateAccessException e) {
            throw new BadProgrammerException(e);
        }
     
        bytes = output.toByteArray();
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
