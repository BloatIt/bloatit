package com.bloatit.framework.social;

import java.util.ArrayList;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

/**
 * Class used to send messages to various micro blogging services.
 */
public class MicroBlogManager {
    public static ArrayList<MicroBlog> microblogs;

    public MicroBlogManager(final String[] microBlogs, final String password) {
        microblogs = new ArrayList<MicroBlog>();
        final String[] rawMbList = microBlogs;

        for (String mb : rawMbList) {
            mb = mb.trim();
            if (!mb.startsWith("[") || !mb.endsWith("]")) {
                throw new BadProgrammerException("The property 'micro.blogs' must be a list of list: [[realm1& login1& url1],[realm2& login2& url2]]");
            }
            mb = mb.substring(1, mb.length() - 1);
            final String[] split = mb.split("&");
            if (split.length != 3) {
                throw new BadProgrammerException("Each element in the 'micro.blogs' property should have 3 items : [[realm1& login1& url1],[realm2& login2& url2]]");
            }
            microblogs.add(new MicroBlog(split[2].trim(), split[1].trim(), password, split[0].trim()));
        }
    }

    public void post(final String message) {
        for (final MicroBlog mb : microblogs) {
            mb.post(message);
        }
    }

    public static String[] getMicroBlogNames() {
        final String[] result = new String[microblogs.size()];
        int i = 0;
        for (final MicroBlog mb : microblogs) {
            result[i++] = mb.getRealm();
        }
        return result;
    }
}
