package com.bloatit;

import java.util.Locale;

import com.bloatit.data.SessionManager;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.Member;

public class BloatitExampleDB {

    public BloatitExampleDB() throws UnauthorizedOperationException {

        SessionManager.beginWorkUnit();


        Member fred = new Member("fredb219", "plop", "fred.bertolus@gmail.com", Locale.FRANCE);
        fred.setFullname("Frédéric Bertolus");
        fred.activate();


        /*
        tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com", Locale.FRANCE);
        tom.setFullname("Thomas Guyard");
        tom.setActivationState(ActivationState.ACTIVE);
        fred = DaoMember.createAndPersist("fredb219", "plop", "fred.bertolus@gmail.com", Locale.FRANCE);
        fred.setFullname("Frédéric Bertolus");
        fred.setActivationState(ActivationState.ACTIVE);
        yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com", Locale.FRANCE);
        yo.setFullname("Yoann Plénet");
        yo.setActivationState(ActivationState.ACTIVE);

        DaoMember admin = DaoMember.createAndPersist("admin", "admin", "admin@gmail.com", Locale.FRANCE);
        admin.setFullname("Administrator");
        admin.setActivationState(ActivationState.ACTIVE);
        admin.setRole(Role.ADMIN);

        other = DaoGroup.createAndPersiste("other", "plop@plop.com", DaoGroup.Right.PROTECTED);
        b219 = DaoGroup.createAndPersiste("b219", "plop2@plop.com", DaoGroup.Right.PROTECTED);
        ubuntuUsers = DaoGroup.createAndPersiste("ubuntuUsers", "plop3@plop.com", DaoGroup.Right.PUBLIC);

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

        project = DaoProject.createAndPersist("VLC",
                                              DaoDescription.createAndPersist(tom, Locale.FRANCE, "title", "descrip"),
                                              DaoFileMetadata.createAndPersist(tom, null, "/dev/", "null", FileType.JPG, 12));

        demand = DaoDemand.createAndPersist(yo,
                                            DaoDescription.createAndPersist(yo, new Locale("fr"), "Mon titre", "Ceci est une description"),
                                            project);
        final DaoComment c1 = DaoComment.createAndPersist(tom, "Pas tres constructif hein !");
        final DaoComment c2 = DaoComment.createAndPersist(fred, "Plop");
        final DaoComment c21 = DaoComment.createAndPersist(tom, "plup");
        final DaoComment c22 = DaoComment.createAndPersist(tom, "CCC-Combo Breaker ;) ");
        final DaoComment c23 = DaoComment.createAndPersist(fred, "Plip");
        demand.addComment(c1);
        demand.addComment(c2);
        c1.addChildComment(DaoComment.createAndPersist(yo, "Je sais c'est just un test"));
        c2.addChildComment(c21);
        c2.addChildComment(c22);
        c2.addChildComment(c23);

        c22.addKudos(yo, 12);
        c22.addKudos(fred, 22);
        c2.addKudos(tom, 42);
        c1.addKudos(tom, -12);
        c21.addKudos(fred, -1);

        try {
            demand.addContribution(yo, new BigDecimal("120"), "I'm so generous too");
            demand.addContribution(tom, new BigDecimal("121"), "I'm so generous too");

            demand.addOffer(DaoOffer.createAndPersist(fred,
                                                      demand,
                                                      new BigDecimal("200"),
                                                      DaoDescription.createAndPersist(fred, new Locale("fr"), "Mon Offre", "Voici la description"),
                                                      DateUtils.tomorrow()));

            demand.getOffers().iterator().next().setState(PopularityState.VALIDATED);

            for (final DaoContribution contribution : demand.getContributionsFromQuery()) {
                try {
                    contribution.validate(demand.getOffers().iterator().next(), 100);
                } catch (final NotEnoughMoneyException e) {
                    e.printStackTrace();
                }
            }

            final DaoDemand demand1 = DaoDemand.createAndPersist(fred, DaoDescription.createAndPersist(fred,
                                                                                                       new Locale("en"),
                                                                                                       "I try it in English",
                                                                                                       "Hello world"), project);
            demand1.getDescription().addTranslation(new DaoTranslation(tom, demand1.getDescription(), new Locale("fr"), "J'essaie en anglais",
                    "Salut le monde"));
            demand1.addContribution(yo, new BigDecimal("12"), "I'm so generous too");
            demand1.addContribution(fred, new BigDecimal("11"), "I'm so generous too");
        } catch (final NotEnoughMoneyException e1) {
            e1.printStackTrace();
        }*/

        SessionManager.endWorkUnitAndFlush();

    }

    public static void main(final String[] args) throws UnauthorizedOperationException {
        new BloatitExampleDB();
    }

}
