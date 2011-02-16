package com.bloatit;

import java.util.Locale;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.data.DaoGroup.Right;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.SessionManager;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.model.AuthToken;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.model.Project;
import com.bloatit.model.managers.FileMetadataManager;

public class BloatitExampleDB {

    public BloatitExampleDB() throws UnauthorizedOperationException {

        SessionManager.beginWorkUnit();


        Member fred = new Member("fredb219", "plop", "fred@elveos.org", Locale.FRANCE);
        fred.authenticate(new AuthToken(fred));
        fred.setFullname("Frédéric Bertolus");
        fred.activate();


        Member tom = new Member("Thomas", "plop", "tom@elveos.org", Locale.FRANCE);
        tom.authenticate(new AuthToken(tom));
        tom.setFullname("Thomas Guyard");
        tom.activate();

        Member yo = new Member("Yo", "plop", "yo@elveos.org", Locale.FRANCE);
        yo.authenticate(new AuthToken(yo));
        yo.setFullname("Yoann Plénet");
        yo.activate();

        Member admin = new Member("admin", "admin", "admin@elveos.org", Locale.FRANCE);
        admin.authenticate(new AuthToken(admin));
        admin.setFullname("Administrator");
        admin.activate();
        admin.setRole(Role.ADMIN);

        Group other = new Group("other", "plop@elveos.org", Right.PROTECTED, yo);
        Group b219 = new Group("b219", "b219@elveos.org", Right.PROTECTED, fred);
        Group ubuntuUsers = new Group("ubuntuUsers", "ubuntu.users@elveos.org", Right.PUBLIC, tom);


        /*other.addMember(yo, true);
        b219.addMember(yo, false);
        b219.addMember(tom, true);
        b219.addMember(fred, false);
        ubuntuUsers.addMember(fred, true);*/


        /*try {

            DaoTransaction.createAndPersist(yo.getInternalAccount(), b219.getExternalAccount(), new BigDecimal("-1000"));
            DaoTransaction.createAndPersist(tom.getInternalAccount(), b219.getExternalAccount(), new BigDecimal("-1000"));
            DaoTransaction.createAndPersist(fred.getInternalAccount(), b219.getExternalAccount(), new BigDecimal("-1000"));
        } catch (final NotEnoughMoneyException e) {
            e.printStackTrace();
        }*/

        Project vlc = new Project("VLC", tom, Locale.FRANCE, "VLC is a free and open source cross-platform multimedia player and framework that plays most multimedia files as well as DVD, Audio CD, VCD, and various streaming protocols. ", "http://www.videolan.org/vlc/",getImage(tom, "vlc.png"));


        /*
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

    private FileMetadata getImage(Member author, String name) {
        String path = ConfigurationManager.loadProperties("web.properties").getProperty("bloatit.www.dir")+"/resources/img/"+name;

        return FileMetadataManager.createFromLocalFile(author, path, name, "Beautiful image");
    }

    public static void main(final String[] args) throws UnauthorizedOperationException {
        new BloatitExampleDB();
    }

}
