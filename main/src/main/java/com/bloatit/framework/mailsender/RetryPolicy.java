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
package com.bloatit.framework.mailsender;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;

/**
 * A class that handles retry policies
 * <p>
 * This class is thread safe.
 * </p>
 * <p>
 * Example:
 * 
 * <pre>
 * String retries = &quot;[1ms, 1s, 1min, 1h, 1d, ...]&quot;;
 * RetryPolicy r = new RetryPolicy(retries);
 * 
 * while (r.hasNext()) {
 *     long w = r.getNext();
 *     // Do something with w
 * }
 * </pre>
 * 
 * </p>
 */
public class RetryPolicy {
    private final static long MILLISECOND = 1;
    private final static long SECOND = 1000 * MILLISECOND;
    private final static long MINUTE = 60 * SECOND;
    private final static long HOUR = 60 * MINUTE;
    private final static long DAY = 24 * HOUR;

    private long[] policy;

    private int index = 0;

    /**
     * Create a retry policy based on a String representing the policy
     */
    public RetryPolicy(String policy) {
        this.policy = parsePolicy(policy);
    }

    /**
     * Creates a retry policy based on an array of longs.
     * <p>
     * Contact: Values in the array must be positive values or -1 (-1 means the
     * previous policy will be retried indefinitely)
     * </p>
     * 
     * @param policy
     */
    public RetryPolicy(long[] policy) {
        this.policy = Arrays.copyOf(policy, policy.length);
    }

    private long[] parsePolicy(String policy) {
        policy = policy.substring(policy.indexOf('[') + 1, policy.lastIndexOf(']'));
        policy = policy.replace(" ", "");
        String[] split = policy.split(",");
        Pattern p = Pattern.compile("^([1-9][0-9]*)([a-zA-Z]*)$");

        long[] retries = new long[split.length];
        int j = 0;
        for (String s : split) {
            Matcher m = p.matcher(s);
            if (!m.matches() || m.groupCount() != 2) {
                if (s.equals("...")) {
                    if (j == 0) {
                        throw new BadProgrammerException("Retry policy cannot start with '...'. Please respect format [1ms, 1s, 1min, 1h, 1d, ...]");
                    }
                    if (j < split.length - 1) {
                        throw new BadProgrammerException("'...' must be the last argument of the policy. Please respect format [1ms, 1s, 1min, 1h, 1d, ...]");
                    }
                    retries[j] = 0;
                } else {
                    throw new BadProgrammerException("Couldn't parse retry policy string. Unexpected string '" + s
                            + "'. Please respect format [1ms, 1s, 1min, 1h, 1d, ...]");
                }
            } else {
                int i = Integer.parseInt(m.group(1));
                String d = m.group(2);
                if (d.equals("ms")) {
                    retries[j] = i * MILLISECOND;
                } else if (d.equals("s")) {
                    retries[j] = i * SECOND;
                } else if (d.equals("min")) {
                    retries[j] = i * MINUTE;
                } else if (d.equals("h")) {
                    retries[j] = i * HOUR;
                } else if (d.equals("d")) {
                    retries[j] = i * DAY;
                } else {
                    throw new BadProgrammerException("Couldn't parse retry policy string. Unexpected string '" + s
                            + "'. Please respect format [1ms, 1s, 1min, 1h, 1d, ...]");
                }
            }
            j++;
        }
        return retries;
    }

    protected synchronized void reinit() {
        index = 0;
    }

    /**
     * @return <i>true</i> if there is another time to wait, <i>false</i>
     *         otherwise
     */
    public synchronized boolean hasNext() {
        return index != policy.length;
    }

    /**
     * @return the next duration to wait in milliseconds
     */
    public synchronized long getNext() {
        long next = policy[index];
        if (next == 0) {
            return policy[index - 1];
        }
        if (index > policy.length) {
            return -1;
        }

        index++;
        return next;
    }

    public synchronized String toString() {
        StringBuilder sb = new StringBuilder();
        for (long p : policy) {
            sb.append(p + ",");
        }
        return sb.toString();
    }
}
