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
package com.bloatit.framework.meta;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

public class MetaFeedbackManager {

    public static boolean reportBug(final String bugReport) {
        final String dir = FrameworkConfiguration.getMetaBugsDirStorage();
        final File dirFile = new File(dir);
        dirFile.mkdirs();
        final File bugReportFile = new File(dir + "/" + UUID.randomUUID().toString());
        try {
            if (!bugReportFile.createNewFile()) {
                throw new BadProgrammerException("Couldn't create bug report file " + bugReportFile.getCanonicalPath());
            }
            final FileOutputStream fileStream = new FileOutputStream(bugReportFile);
            fileStream.write(bugReport.getBytes());
            fileStream.close();
        } catch (final IOException e) {
            Log.framework().error("Fail to write a bugreport", e);
            return false;
        }
        return true;
    }

    public static List<MetaFeedback> getOpenFeedbacks() {
        final String dir = FrameworkConfiguration.getMetaBugsDirStorage();
        final File dirFile = new File(dir);
        final File[] bugFiles = dirFile.listFiles();
        final List<File> orderedFiles = Arrays.asList(bugFiles);
        Collections.sort(orderedFiles, new Comparator<File>() {
            @Override
            public int compare(final File o1, final File o2) {
                return Long.valueOf(o1.lastModified()).compareTo(Long.valueOf(o2.lastModified()));
            }
        });

        final List<MetaFeedback> bugList = new ArrayList<MetaFeedback>();
        for (final File bugFile : orderedFiles) {
            try {
                if (!bugFile.getName().endsWith("-deleted")) {
                    bugList.add(new MetaFeedback(bugFile));
                }
            } catch (final IOException e) {
                Log.framework().error("Fail to read a bugreport", e);
            }
        }
        return bugList;
    }

    public static MetaFeedback getById(final String bugId) {
        final List<MetaFeedback> openBugs = getOpenFeedbacks();
        for (final MetaFeedback bug : openBugs) {
            if (bug.getId().equals(bugId)) {
                return bug;
            }
        }
        return null;
    }
}
