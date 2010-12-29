package com.bloatit.framework;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.bloatit.model.data.DaoComment;
import com.bloatit.model.data.DaoContribution;
import com.bloatit.model.data.DaoDemand;
import com.bloatit.model.data.DaoDescription;
import com.bloatit.model.data.DaoExternalAccount;
import com.bloatit.model.data.DaoExternalAccount.AccountType;
import com.bloatit.model.data.DaoGroup;
import com.bloatit.model.data.DaoKudosable.State;
import com.bloatit.model.data.DaoMember;
import com.bloatit.model.data.DaoTransaction;
import com.bloatit.model.data.DaoTranslation;
import com.bloatit.model.data.util.SessionManager;
import com.bloatit.model.exceptions.NotEnoughMoneyException;

public class TestDB {

    private final DaoMember tom;
    private final DaoMember fred;
    private final DaoMember yo;
    private final DaoGroup other;
    private final DaoGroup b219;
    private final DaoGroup ubuntuUsers;
    private final DaoDemand demand;

    public TestDB() {

        SessionManager.beginWorkUnit();

        tom = DaoMember.createAndPersist("Thomas", "password", "tom@gmail.com");
        tom.setFullname("Thomas Guyard");
        fred = DaoMember.createAndPersist("Fred", "other", "fred@gmail.com");
        fred.setFullname("Frédéric Bertolus");
        yo = DaoMember.createAndPersist("Yo", "plop", "yo@gmail.com");
        yo.setFullname("Yoann Plénet");

        other = DaoGroup.createAndPersiste("other", "plop@plop.com", DaoGroup.Right.PROTECTED);
        b219 = DaoGroup.createAndPersiste("b219", "plop@plop.com", DaoGroup.Right.PROTECTED);
        ubuntuUsers = DaoGroup.createAndPersiste("ubuntuUsers", "plop@plop.com", DaoGroup.Right.PUBLIC);

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

        demand = DaoDemand.createAndPersist(yo, DaoDescription.createAndPersist(yo, new Locale("fr"), "Mon titre", "Ceci est une description"));
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

        demand.createSpecification(fred, "Tiens voila une spécif vraiment précise");
        try {
            demand.addContribution(yo, new BigDecimal("120"), "I'm so generous too");
            demand.addContribution(tom, new BigDecimal("121"), "I'm so generous too");

            demand.addOffer(fred,
                    new BigDecimal("200"),
                    DaoDescription.createAndPersist(fred, new Locale("fr"), "Mon Offre", "Voici la description"),
                    new Date());

            demand.getOffers().iterator().next().setState(State.VALIDATED);

            for (final DaoContribution contribution : demand.getContributionsFromQuery()) {
                try {
                    contribution.accept(demand.getOffers().iterator().next());
                } catch (final NotEnoughMoneyException e) {
                    e.printStackTrace();
                }
            }

            final DaoDemand demand1 = DaoDemand.createAndPersist(fred,
                    DaoDescription.createAndPersist(fred, new Locale("en"), "I try it in English", "Hello world"));
            demand1.getDescription().addTranslation(new DaoTranslation(tom, demand1.getDescription(), new Locale("fr"), "J'essaie en anglais",
                    "Salut le monde"));
            demand1.addContribution(yo, new BigDecimal("12"), "I'm so generous too");
            demand1.addContribution(fred, new BigDecimal("11"), "I'm so generous too");
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

    public DaoGroup getOther() {
        return other;
    }

    public DaoGroup getB219() {
        return b219;
    }

    public DaoGroup getUbuntuUsers() {
        return ubuntuUsers;
    }

    public DaoDemand getDemand() {
        return demand;
    }

    public static void main(final String[] args) {
        new TestDB();
    }

}
