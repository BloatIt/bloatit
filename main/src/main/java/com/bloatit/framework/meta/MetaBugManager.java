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

public class MetaBugManager {

    public static boolean reportBug(String bugReport) {
        String dir = FrameworkConfiguration.getMetaBugsDirStorage();

        File dirFile = new File(dir);
        dirFile.mkdirs();

        File bugReportFile = new File(dir + "/" + UUID.randomUUID().toString());

        try {
            bugReportFile.createNewFile();

            FileOutputStream fileStream = new FileOutputStream(bugReportFile);
            fileStream.write(bugReport.getBytes());
            fileStream.close();

        } catch (IOException e) {
            Log.framework().error("Fail to write a bugreport", e);
            return false;
        }

        return true;

    }

    public static List<MetaBug> getOpenBugs() {
        String dir = FrameworkConfiguration.getMetaBugsDirStorage();

        File dirFile = new File(dir);

        File[] bugFiles = dirFile.listFiles();

        List<File> orderedFiles = Arrays.asList(bugFiles);

        Collections.sort(orderedFiles, new Comparator<File>() {

            @Override
            public int compare(File o1, File o2) {
                return Long.valueOf(o1.lastModified()).compareTo(Long.valueOf(o2.lastModified()));
            }

        });

        List<MetaBug> bugList = new ArrayList<MetaBug>();

        for (File bugFile : orderedFiles) {
            try {
                if(!bugFile.getName().endsWith("-deleted")) {
                    bugList.add(new MetaBug(bugFile));
                }
            } catch (IOException e) {
                Log.framework().error("Fail to read a bugreport", e);
            }
        }

        return bugList;
    }

    public static MetaBug getById(String bugId) {
        List<MetaBug> openBugs = getOpenBugs();

        for(MetaBug bug: openBugs) {
            if(bug.getId().equals(bugId)) {
                return bug;
            }
        }

        return null;
    }

}
