package com.bloatit.model;

import java.math.BigDecimal;
import java.util.Locale;

import com.bloatit.data.DaoComment;
import com.bloatit.data.DaoContribution;
import com.bloatit.data.DaoDescription;
import com.bloatit.data.DaoExternalAccount;
import com.bloatit.data.DaoExternalAccount.AccountType;
import com.bloatit.data.DaoFeature;
import com.bloatit.data.DaoFileMetadata;
import com.bloatit.data.DaoFileMetadata.FileType;
import com.bloatit.data.DaoKudosable.PopularityState;
import com.bloatit.data.DaoMember;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoOffer;
import com.bloatit.data.DaoSoftware;
import com.bloatit.data.DaoTeam;
import com.bloatit.data.DaoTransaction;
import com.bloatit.data.DaoTranslation;
import com.bloatit.data.SessionManager;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.webprocessor.context.User.ActivationState;

public class SimpleTestDB {

    private final DaoMember tom;
    private final DaoMember fred;
    private final DaoMember yo;
    private final DaoTeam other;
    private final DaoTeam b219;
    private final DaoTeam ubuntuUsers;
    private final DaoFeature feature;
    private final DaoSoftware project;

    public SimpleTestDB() {

        SessionManager.beginWorkUnit();

        tom = new Member("Thomas", "password", "tom@gmail.com", Locale.FRANCE).getDao();
        tom.setFullname("Thomas Guyard");
        tom.setActivationState(ActivationState.ACTIVE);
        fred = new Member("Fred", "other", "fred@gmail.com", Locale.FRANCE).getDao();
        fred.setFullname("Frédéric Bertolus");
        fred.setActivationState(ActivationState.ACTIVE);
        yo = new Member("Yoann", "plop", "yo@gmail.com", Locale.FRANCE).getDao();
        yo.setFullname("Yoann Plénet");
        yo.setActivationState(ActivationState.ACTIVE);

        final DaoMember admin = new Member("admin", "admin",  "admin@gmail.com", Locale.FRANCE).getDao();
        admin.setFullname("Administrator");
        admin.setActivationState(ActivationState.ACTIVE);
        admin.setRole(Role.ADMIN);

        other = DaoTeam.createAndPersiste("other", "plop@plop.com", "A group description", DaoTeam.Right.PROTECTED);
        b219 = DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoTeam.Right.PROTECTED);
        ubuntuUsers = DaoTeam.createAndPersiste("ubuntuUsers", "plop3@plop.com", "A group description", DaoTeam.Right.PUBLIC);

        other.addMember(yo, true);
        b219.addMember(yo, false);
        b219.addMember(tom, true);
        b219.addMember(fred, false);
        ubuntuUsers.addMember(fred, true);

        yo.setExternalAccount(DaoExternalAccount.createAndPersist(yo, AccountType.IBAN, "code"));
        tom.setExternalAccount(DaoExternalAccount.createAndPersist(tom, AccountType.IBAN, "code"));
        fred.setExternalAccount(DaoExternalAccount.createAndPersist(fred, AccountType.IBAN, "code"));
        b219.setExternalAccount(DaoExternalAccount.createAndPersist(b219, AccountType.IBAN, "code"));
        ubuntuUsers.setExternalAccount(DaoExternalAccount.createAndPersist(ubuntuUsers, AccountType.IBAN, "code"));

        try {
            DaoTransaction.createAndPersist(yo.getInternalAccount(), b219.getExternalAccount(), new BigDecimal("-1000"));
            DaoTransaction.createAndPersist(tom.getInternalAccount(), b219.getExternalAccount(), new BigDecimal("-1000"));
            DaoTransaction.createAndPersist(fred.getInternalAccount(), b219.getExternalAccount(), new BigDecimal("-1000"));
        } catch (final NotEnoughMoneyException e) {
            e.printStackTrace();
        }

        project = DaoSoftware.createAndPersist("VLC", DaoDescription.createAndPersist(tom, null, Locale.FRANCE, "title", "descrip"));

        feature = DaoFeature.createAndPersist(yo,
                                              null,
                                              DaoDescription.createAndPersist(yo, null, new Locale("fr"), "Mon titre", "Ceci est une description"),
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
                                          DaoDescription.createAndPersist(fred, null, new Locale("fr"), "Mon Offre", "Voici la description"),
                                          DateUtils.tomorrow(),
                                          0));

            feature.getOffers().iterator().next().setState(PopularityState.VALIDATED);

            for (final DaoContribution contribution : feature.getContributions()) {
                try {
                    contribution.validate(feature.getOffers().iterator().next(), 100);
                } catch (final NotEnoughMoneyException e) {
                    e.printStackTrace();
                }
            }

            final DaoFeature feature1 = DaoFeature.createAndPersist(fred, null, DaoDescription.createAndPersist(fred,
                                                                                                                null,
                                                                                                                new Locale("en"),
                                                                                                                "I try it in English",
                                                                                                                "Hello world"), project);
            feature1.getDescription().addTranslation(new DaoTranslation(tom,
                                                                        null,
                                                                        feature1.getDescription(),
                                                                        new Locale("fr"),
                                                                        "J'essaie en anglais",
                                                                        "Salut le monde"));
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

    public DaoTeam getOther() {
        return other;
    }

    public DaoTeam getB219() {
        return b219;
    }

    public DaoTeam getUbuntuUsers() {
        return ubuntuUsers;
    }

    public DaoFeature getFeature() {
        return feature;
    }

    public static void main(final String[] args) {
        new SimpleTestDB();
    }

}
