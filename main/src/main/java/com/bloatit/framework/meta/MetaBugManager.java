package com.bloatit.framework.meta;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.bloatit.common.Log;
import com.bloatit.framework.FrameworkConfiguration;


public class MetaBugManager {

    public static boolean reportBug(String bugReport) {
        String dir = FrameworkConfiguration.getMetaBugsDirStorage();

        File dirFile = new File(dir);
        dirFile.mkdirs();

        File bugReportFile = new File(dir+"/"+UUID.randomUUID().toString());

        try {
            bugReportFile.createNewFile();

            FileOutputStream fileStream = new FileOutputStream(bugReportFile);
            fileStream.write(bugReport.getBytes());
            fileStream.close();

        } catch (IOException e) {
            Log.framework().error("Fail to write a bugreport",e);
            return false;
        }

        return true;

    }

    public static List<String> getOpenBugs() {
        String dir = FrameworkConfiguration.getMetaBugsDirStorage();

        File dirFile = new File(dir);

        File[] bugFiles = dirFile.listFiles();

        List<String> bugList = new ArrayList<String>();

        for(File bugFile: bugFiles) {
            try {
                byte[] buffer = new byte[(int) bugFile.length()];
                BufferedInputStream f = new BufferedInputStream(new FileInputStream(bugFile));
                f.read(buffer);
                bugList.add(new String(buffer));

            } catch (IOException e) {
                Log.framework().error("Fail to read a bugreport",e);
            }
        }

        return bugList;
    }



}
