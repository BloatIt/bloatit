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

import java.math.BigDecimal;
import java.util.Locale;

import com.bloatit.data.DaoBankTransaction;
import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoFileMetadata.FileType;
import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.DaoTeam;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.DaoTransaction;
import com.bloatit.data.SessionManager;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.data.exceptions.UniqueNameExpectedException;
import com.bloatit.framework.exceptions.highlevel.BadProgrammerException;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.context.User.ActivationState;

public class SimpleTestDB {

    private final DaoMember tom;
    private final DaoMember fred;
    private final DaoMember yo;
    private final DaoMember loser;
    private final DaoTeam publicGroup;
    private final DaoTeam privateGroup;
    private final DaoFeature feature;
    private final DaoSoftware project;
    private DaoBankTransaction yoBankTransaction;
    private DaoBankTransaction publicGroupBankTransaction;

    public SimpleTestDB() {

        SessionManager.beginWorkUnit();

        loser = new Member("loser", "loser", "loser@gmail.com", Locale.FRANCE).getDao();
        loser.setFullname("loser loser");
        loser.setActivationState(ActivationState.ACTIVE);

        tom = new Member("Thomas", "password", "tom@gmail.com", Locale.FRANCE).getDao();
        tom.setFullname("Thomas Guyard");
        tom.setActivationState(ActivationState.ACTIVE);

        fred = new Member("Fred", "other", "fred@gmail.com", Locale.FRANCE).getDao();
        fred.setFullname("Frédéric Bertolus");
        fred.setActivationState(ActivationState.ACTIVE);

        yo = new Member("Yoann", "plop", "yo@gmail.com", Locale.FRANCE).getDao();
        yo.setFullname("Yoann Plénet");
        yo.setActivationState(ActivationState.ACTIVE);
        yo.getContact().setName("Yoann Plénet");
        yo.getContact().setCountry("Earth");

        final DaoMember admin = new Member("admin", "admin", "admin@gmail.com", Locale.FRANCE).getDao();
        admin.setFullname("Administrator");
        admin.setActivationState(ActivationState.ACTIVE);
        admin.setRole(Role.ADMIN);

        publicGroup = DaoTeam.createAndPersiste("publicGroup", "plop@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        publicGroup.getContact().setName("publicGroup");
        publicGroup.getContact().setCountry("Mars");

        privateGroup = DaoTeam.createAndPersiste("privateGroup", "plop2@plop.com", "A group description", DaoTeam.Right.PROTECTED);

        publicGroup.addMember(tom, true);
        tom.addTeamRight(publicGroup, UserTeamRight.CONSULT);
        tom.addTeamRight(publicGroup, UserTeamRight.MODIFY);
        tom.addTeamRight(publicGroup, UserTeamRight.INVITE);
        tom.addTeamRight(publicGroup, UserTeamRight.BANK);
        tom.addTeamRight(publicGroup, UserTeamRight.PROMOTE);
        tom.addTeamRight(publicGroup, UserTeamRight.TALK);
        publicGroup.addMember(fred, true);
        fred.addTeamRight(publicGroup, UserTeamRight.CONSULT);
        fred.addTeamRight(publicGroup, UserTeamRight.TALK);
        publicGroup.addMember(yo, false);
        yo.addTeamRight(publicGroup, UserTeamRight.CONSULT);
        yo.addTeamRight(publicGroup, UserTeamRight.TALK);
        yo.addTeamRight(publicGroup, UserTeamRight.BANK);
        publicGroup.addMember(loser, false);

        privateGroup.addMember(tom, true);
        tom.addTeamRight(privateGroup, UserTeamRight.CONSULT);
        tom.addTeamRight(privateGroup, UserTeamRight.MODIFY);
        tom.addTeamRight(privateGroup, UserTeamRight.INVITE);
        tom.addTeamRight(privateGroup, UserTeamRight.BANK);
        tom.addTeamRight(privateGroup, UserTeamRight.PROMOTE);
        tom.addTeamRight(privateGroup, UserTeamRight.TALK);
        privateGroup.addMember(fred, true);
        fred.addTeamRight(privateGroup, UserTeamRight.CONSULT);
        fred.addTeamRight(privateGroup, UserTeamRight.TALK);
        privateGroup.addMember(yo, false);
        yo.addTeamRight(privateGroup, UserTeamRight.CONSULT);
        yo.addTeamRight(privateGroup, UserTeamRight.TALK);
        yo.addTeamRight(privateGroup, UserTeamRight.BANK);
        privateGroup.addMember(loser, false);

        try {
            yoBankTransaction = DaoBankTransaction.createAndPersist("test", "token1", yo, new BigDecimal("1000"), new BigDecimal("1100"), "order1");
            getYoBankTransaction().setAuthorized();
            getYoBankTransaction().setValidated();

            publicGroupBankTransaction = DaoBankTransaction.createAndPersist("test",
                                                                             "token2",
                                                                             publicGroup,
                                                                             new BigDecimal("1000"),
                                                                             new BigDecimal("1100"),
                                                                             "order2");
            publicGroupBankTransaction.setAuthorized();
            publicGroupBankTransaction.setValidated();

            DaoTransaction.createAndPersist(tom.getInternalAccount(), privateGroup.getExternalAccount(), new BigDecimal("-1000"));
            DaoTransaction.createAndPersist(fred.getInternalAccount(), privateGroup.getExternalAccount(), new BigDecimal("-1000"));
        } catch (final NotEnoughMoneyException e) {
            e.printStackTrace();
        }

        try {
            project = DaoSoftware.createAndPersist("VLC", DaoDescription.createAndPersist(tom, null,Language.FR, "title", "descrip"));
        } catch (UniqueNameExpectedException e) {
            throw new BadProgrammerException(e);
        }

        feature = DaoFeature.createAndPersist(yo,
                                              null,
                                              DaoDescription.createAndPersist(yo, null, Language.FR, "Mon titre", "Ceci est une description"),
                                              project);

        project.setImage(DaoFileMetadata.createAndPersist(tom, null, feature, "/dev/", "null", FileType.JPG, 12));
        final DaoComment c1 = DaoComment.createAndPersist(feature, null, tom, "Pas tres constructif hein !");
        final DaoComment c2 = DaoComment.createAndPersist(feature, null, fred, "Plop");
        final DaoComment c21 = DaoComment.createAndPersist(c2, null, tom, "plup");
        final DaoComment c22 = DaoComment.createAndPersist(c2, null, tom, "CCC-Combo Breaker ;) ");
        final DaoComment c23 = DaoComment.createAndPersist(c2, null, fred, "Plip");
        feature.addComment(c1);
        feature.addComment(c2);
        c1.addChildComment(DaoComment.createAndPersist(c1, null, yo, "Je sais c'est just un test"));
        c2.addChildComment(c21);
        c2.addChildComment(c22);
        c2.addChildComment(c23);

        c22.addKudos(yo, null, 12);
        c22.addKudos(fred, null, 22);
        c2.addKudos(tom, null, 42);
        c1.addKudos(tom, null, -12);
        c21.addKudos(fred, null, -1);

        try {
            feature.addContribution(yo, null, new BigDecimal("120"), "I'm so generous too");
            feature.addContribution(tom, null, new BigDecimal("121"), "I'm so generous too");

            feature.addOffer(new DaoOffer(fred,
                                          null,
                                          feature,
                                          new BigDecimal("200"),
                                          DaoDescription.createAndPersist(fred, null, Language.FR, "Mon Offre", "Voici la description"),
                                          "GNU GPL",
                                          DateUtils.tomorrow(),
                                          0));

            feature.getOffers().iterator().next().setState(PopularityState.VALIDATED);

            final DaoFeature feature1 = DaoFeature.createAndPersist(fred, null, DaoDescription.createAndPersist(fred,
                                                                                                                null,
                                                                                                                Language.EN,
                                                                                                                "I try it in English",
                                                                                                                "Hello world"), project);
            feature1.getDescription().addTranslation(tom,
                                                                        null,
                                                                        Language.FR,
                                                                        "J'essaie en anglais",
                                                                        "Salut le monde");
            feature1.addContribution(yo, null, new BigDecimal("12"), "I'm so generous too");
            feature1.addContribution(fred, null, new BigDecimal("11"), "I'm so generous too");
        } catch (final NotEnoughMoneyException e1) {
            e1.printStackTrace();
        }

        SessionManager.endWorkUnitAndFlush();

    }

    public DaoMember getTom() {
        return tom;
    }

    public DaoMember getFred() {
        return fred;
    }

    public DaoMember getYo() {
        return yo;
    }

    public final DaoMember getLoser() {
        return loser;
    }

    public final DaoTeam getPublicGroup() {
        return publicGroup;
    }

    public final DaoTeam getPrivateGroup() {
        return privateGroup;
    }

    public final DaoSoftware getProject() {
        return project;
    }

    public DaoFeature getFeature() {
        return feature;
    }

    public DaoBankTransaction getYoBankTransaction() {
        return yoBankTransaction;
    }

    public DaoBankTransaction getPublicGroupBankTransaction() {
        return publicGroupBankTransaction;
    }
}
