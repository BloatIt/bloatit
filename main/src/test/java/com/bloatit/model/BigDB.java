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
package com.bloatit.model;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoExternalAccount;
import com.bloatit.data.DaoExternalAccount.AccountType;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoFileMetadata.FileType;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.DaoTeam;
import com.bloatit.data.DaoTransaction;
import com.bloatit.data.SessionManager;
import com.bloatit.data.exceptions.NotEnoughMoneyException;

public class BigDB {

    private static final int nbUsers = 100;

    public BigDB() {

        SessionManager.beginWorkUnit();

        // Create some users
        final List<DaoMember> members = new ArrayList<DaoMember>();
        for (int i = 0; i < nbUsers; i++) {
            DaoMember member;
            member = DaoMember.createAndPersist("member " + i, Integer.toString(i), "salt", "mail" + i + "@nowhere.com", Locale.FRANCE);
            member.setFullname("User " + i + " Fullname");
            member.setExternalAccount(DaoExternalAccount.createAndPersist(member, AccountType.IBAN, "code"));
            members.add(member);

            try {
                DaoTransaction.createAndPersist(member.getInternalAccount(), member.getExternalAccount(), new BigDecimal("-1000"));
            } catch (final NotEnoughMoneyException e) {
                e.printStackTrace();
            }
            if (i % 500 == 0) {
                SessionManager.flush();
                SessionManager.clear();
            }
        }

        // Create Some Groups
        for (int i = 0; i < nbUsers / 4; i += 4) {
            final DaoTeam group = DaoTeam.createAndPersiste("group " + (i / 4), "plop" + i + "@plop.com", "description", DaoTeam.Right.PUBLIC);
            group.addMember(members.get(i), true);
            group.addMember(members.get(i + 1), false);
            group.addMember(members.get(i + 2), false);
            group.addMember(members.get(i + 3), false);
            if (i % 500 == 0) {
                SessionManager.flush();
                SessionManager.clear();
            }
        }

        DaoTeam.createAndPersiste("b2 ", "plop" + "@plop.com", "description", DaoTeam.Right.PUBLIC);

        final DaoSoftware project = DaoSoftware.createAndPersist("VLC", DaoDescription.createAndPersist(members.get(0),
                                                                                                        null,
                                                                                                        Locale.FRANCE,
                                                                                                        "title",
                                                                                                        "descrip"));
        project.setImage(DaoFileMetadata.createAndPersist(members.get(0), null, null, "/dev/", "null", FileType.JPG, 12));

        for (int i = 0; i < nbUsers; i++) {
            final DaoFeature feature = DaoFeature.createAndPersist(members.get(i),
                                                                   null,
                                                                   DaoDescription.createAndPersist(members.get(i),
                                                                                                   null,
                                                                                                   new Locale("fr"),
                                                                                                   fortune(140),
                                                                                                   fortune(1000) + fortune(1000) + fortune(1000)),
                                                                   project);

            final int commentCount = (int) (Math.random() * 5);

            for (int j = 0; j < commentCount; j++) {

                final DaoComment comment = DaoComment.createAndPersist(feature, null, members.get(i), fortune());
                createComment(comment);
                feature.addComment(comment);
            }

            final int nbContrib = pick(12);
            for (int j = 0; j < nbContrib; j++) {
                final DaoMember m = members.get(pick(nbUsers));
                try {
                    feature.addContribution(m, null, new BigDecimal((pick(10) + 1) * 10), fortune(144));
                } catch (final NotEnoughMoneyException e) {
                    e.printStackTrace();
                }
            }

            final DaoMember member = members.get(pick(nbUsers));
            if (pick(2) == 0) {
                feature.addOffer(new DaoOffer(member,
                                              null,
                                              feature,
                                              new BigDecimal((pick(50) + 10) * 10),
                                              DaoDescription.createAndPersist(member, null, new Locale("fr"), "Offre", fortune(254)),
                                              "GNU GPL",
                                              new Date(System.currentTimeMillis() + 200),
                                              0));
                if (pick(2) == 0) {
//                    for (final DaoContribution contrib : feature.getContributions()) {
//                        try {
//                            contrib.validate(feature.getOffers().iterator().next(), 100);
//                        } catch (final NotEnoughMoneyException e) {
//                            e.printStackTrace();
//                        }
//                    }
                }
            }

            if (i % 100 == 0) {
                SessionManager.flush();
                SessionManager.clear();
            }
        }

        SessionManager.endWorkUnitAndFlush();

    }

    private void createComment(final DaoComment comment) {
        final int nbComment = (int) (Math.random() * 15);
        for (int i = 0; i < nbComment; i++) {
            final int memberNum = (int) (Math.random() * nbUsers);
            final DaoComment other = DaoComment.createAndPersist(comment, null, DaoMember.getByLogin("member " + memberNum), fortune());
            comment.addChildComment(other);
            if ((int) (Math.random() * 15) % 15 == 0) {
                createComment(other);
            }
        }
    }

    public static String fortune() {
        String text = "";
        try {
            String line;
            final Process p = Runtime.getRuntime().exec("fortune");
            final BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                text += line;
            }
            input.close();
        } catch (final Exception err) {
            err.printStackTrace();
        }

        if (text.isEmpty()) {
            text = Long.toHexString(Double.doubleToLongBits(Math.random()));
        }

        return text;
    }

    private String fortune(final int i) {
        String text = fortune();
        if (text.length() > i) {
            text = text.substring(0, i);
        }
        return text;
    }

    private int pick(final int max) {
        return (int) (Math.random() * max);
    }

    public static void main(final String[] args) {
        new BigDB();
    }
}
