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
package com.bloatit.framework;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.bloatit.framework.exceptions.highlevel.ExternalErrorException;

/*
 * Find the last version of a file
 */
public class ResourceFinder {

    private final String baseUrl;
    private final Map<String, String> map = new HashMap<String, String>();

    public ResourceFinder(final String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String find(final String resourceUrl) {
        if (!map.containsKey(resourceUrl)) {
            map.put(resourceUrl, findForCache(resourceUrl));
        }

        return map.get(resourceUrl);
    }

    public String findForCache(final String resourceUrl) {

        final String dirName = resourceUrl.split("/[^/]+$")[0];
        final String fileName = resourceUrl.substring(dirName.length() + 1);

        final String fileNameBase = fileName.split(".[^.]+$")[0];
        final String fileNameExtension = fileName.substring(fileNameBase.length());

        final File dir = new File(baseUrl + dirName);
        if (!dir.isDirectory()) {
            throw new ExternalErrorException(dirName + " is not a valid directory for " + resourceUrl);
        }

        final String[] versionList = dir.list(new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                if (name.startsWith(fileNameBase + "-") && name.endsWith(fileNameExtension)) {
                    return true;
                }
                return false;
            }
        });

        final List<VersionFile> versionFileList = new ArrayList<VersionFile>();

        for (final String versionFile : versionList) {
            final int begin = Math.min(fileNameBase.length() + 1, versionFile.length() - 1);
            final int end = Math.max(begin, versionFile.length() - fileNameExtension.length());
            final String version = versionFile.substring(begin, end);

            versionFileList.add(new VersionFile(versionFile, version));
        }

        final VersionFile last = findMax(versionFileList);

        if (last == null) {
            throw new ExternalErrorException(resourceUrl + " resource not found");
        }

        return dirName + '/' + last.fileName;
    }

    private <T extends Comparable<T>> T findMax(final List<T> list) {
        T max = null;

        for (final T item : list) {
            if (max == null) {
                max = item;
            } else {
                if (max.compareTo(item) < 0) {
                    max = item;
                }
            }
        }
        return max;
    }

    public static class VersionFile implements Comparable<VersionFile> {

        private final String fileName;
        private final List<Integer> versionParts = new ArrayList<Integer>();

        public VersionFile(final String fileName, final String version) {
            this.fileName = fileName;

            final String[] split = version.split("[^0-9]");

            for (final String s : split) {
                if (s.length() == 0) {
                    versionParts.add(-1);
                } else {
                    versionParts.add(Integer.valueOf(s));
                }
            }

        }

        @Override
        public int compareTo(final VersionFile o) {
            for (int i = 0; i < versionParts.size(); i++) {
                final int compareTo = getPart(i).compareTo(o.getPart(i));
                if (compareTo != 0) {
                    return compareTo;
                }

            }

            return -1;
        }

        private Integer getPart(final int index) {
            if (index < versionParts.size()) {
                return versionParts.get(index);
            }
            return -1;
        }

    }
}
