package com.bloatit;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.data.DaoGroup.Right;
import com.bloatit.data.SessionManager;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.DateUtils;
import com.bloatit.model.AuthToken;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Comment;
import com.bloatit.model.Demand;
import com.bloatit.model.DemandFactory;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Group;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.Project;
import com.bloatit.model.managers.FileMetadataManager;

public class BloatitExampleDB {

    public BloatitExampleDB() throws UnauthorizedOperationException {

        SessionManager.beginWorkUnit();

        //Serious accounts
        Member fred = createMember("fred", "Frédéric Bertolus");
        Member thomas = createMember("thomas", "Thomas Guyard");
        Member yoann = createMember("yoann", "Yoann Plénet");
        Member admin = createMember("admin", "Administrator");

        //Very not serious accounts
        Member chogall = createMember("chogall", "Cho'gall");
        Member cerbere = createMember("cerbere", "Cerbère");
        Member hydre = createMember("hydre", "Hydre");
        Member elephantman = createMember("elephantman", "ElephantMan");
        Member celeste = createMember("celeste", "Céleste");
        Member rataxes = createMember("rataxes", "Rataxès");


        //Add money
        giveMoney(fred, 1000000);
        giveMoney(thomas, 2000000);
        giveMoney(yoann, 3000000);
        giveMoney(chogall, 2000);
        giveMoney(cerbere, 1000);
        giveMoney(hydre, 500);
        giveMoney(elephantman, 100000000);

        //Add groups
        Group other = new Group("other", "plop@elveos.org", Right.PROTECTED, yoann);
        Group b219 = new Group("b219", "b219@elveos.org", Right.PROTECTED, fred);
        Group ubuntuUsers = new Group("ubuntuUsers", "ubuntu.users@elveos.org", Right.PUBLIC, thomas);

        Project vlc = new Project("VLC", thomas, Locale.FRANCE, "VLC is a free and open source cross-platform multimedia player and framework that plays most multimedia files as well as DVD, Audio CD, VCD, and various streaming protocols. ", "http://www.videolan.org/vlc/",getImage(thomas, "vlc.png"));


        String twoSubtitlesInVlcDemandDescription = "Offrir la possibilité d'afficher deux sous-titre à la fois dans VLC.\n" +
        		"\n" +
        		"Afin de m'entrainer à parler anglais et néerlandais à la fois, je souhaite pouvoir afficher les sous-titre de ces deux langues en même temps dans VLC.\n" +
        		"Parce que je suis très gentil, si on peut afficher deux sous-titre de n'importe quelle langue ça m'ira aussi (si le néerlandais font bien sûr partis des langues supportées).\n" +
        		"\n" +
        		"Les fichiers de sous titre lus doivent être séparés. Je ne veux pas avoir à utiliser un logiciel quelconque qui combinera les sous titres. Je veux juste pouvoir clicker sur le bouton \"sous-titre\", cocher une case \"afficher deux sous-titre simultanément\" (wording à revoir) et voilà (ndt : en anglais dans le texte).\n" +
        		"\n" +
        		"Notes :\n" +
        		"- Les sous-titres pourront être dans des formats différents.\n" +
        		"- Les sous-titres pourront ne pas être synchronisés exactement de la même manière (un sous-titre pourra changer alors que le précédent est encore affiché)\n" +
        		"\n" +
        		"J'aimerais que ce soit implémenté dans la semaine, je suis en-effet en train de me préparer à un concours extrèmement complexe (le concours de la bicéphalie) qui aura lieu dans 3 semaines, et j'ai besoin d'au moins deux semaines pour maitriser parfaitement ces deux langues (j'ai pu apprendre le Chinois et l'Arabe en 3 jours auparavant, mais le néerlandais est quand même très complexe).";

        String twoSubtitlesInVlcDemandTitle = "Afficher en même temps un sous-titre en anglais et un sous-titre en néerlandais";

        Demand twoSubtitlesInVlcDemand = DemandFactory.createDemand(chogall, chogall.getLocale(), twoSubtitlesInVlcDemandTitle, twoSubtitlesInVlcDemandDescription, vlc);




        twoSubtitlesInVlcDemand.authenticate(new AuthToken(cerbere));
        Comment comment1 = twoSubtitlesInVlcDemand.addComment("Super idée !\n" +
        		"J'ai exactement le même besoin mais avec 3 langues. Du coup pour être un peu générique, je propose d'avoir la possibilité de sélectionner n langues. Je connais un ami qui apprend en-effet l'araméen, le latin, le grec, l'hébreu, le le haut-sindarin et l'égyptien et qui serait sans doute preneur aussi.");

        comment1.authenticate(new AuthToken(hydre));
        comment1.addChildComment("Je suis l'ami de Cerbère qui a posté ci-dessus et qui apprend des langues mortes. Je trouverais ça génial , mais il est indispensable de pouvoir réduire la taille du texte.\n" +
        		"Je propose de forker cette demande pour inclure les demandes de changement (nombre de sous-titre non défini et taille des sous-titre définissable) ");

        comment1.authenticate(new AuthToken(chogall));
        comment1.addChildComment("OK pour moi, j'aurais dû y penser dès le début, j'ai merdé, j'avais mon cerveau gauche qui avait bu trop de vodka. ");


        twoSubtitlesInVlcDemand.authenticate(new AuthToken(elephantman));
        Comment comment2 = twoSubtitlesInVlcDemand.addComment("Elle est naze votre idée, moi j'apprends une langue en 2.53 minutes (moyenne vérifiée sur un échantillon de 353 langues) du coup autant afficher un seul sous-titre à la fois");

        comment2.authenticate(new AuthToken(chogall));
        comment2.addChildComment("On ne peut pas vaincre un éléphant ! Abandonnons cette demande !");


        String rataxesOfferDescription = "Je vais vous le faire vite et bien. Et tout ça pour vraiment pas cher !";
        twoSubtitlesInVlcDemand.authenticate(new AuthToken(rataxes));
        Offer rataxesOffer = twoSubtitlesInVlcDemand.addOffer(rataxes, new BigDecimal(2300), rataxesOfferDescription, rataxes.getLocale(), DateUtils.tomorrow(), 0);



        rataxesOffer.authenticate(new AuthToken(chogall));
        rataxesOffer.voteUp();
        rataxesOffer.authenticate(new AuthToken(hydre));
        rataxesOffer.voteUp();



        twoSubtitlesInVlcDemand.authenticate(new AuthToken(celeste));
        Offer celesteOffer = twoSubtitlesInVlcDemand.addEmptyOffer(celeste);
        String celesteBatch1Description = "Oulala, ça à l'air compliqué tout ça... Je peux tout de même essayer mais je vais ramer. Je découpe le travail en 3 parties pour simplifier la tache.\n" +
        		"Pour la première partie, je vais modifier le coeur du logiciel pour permettre d'afficher un nombre variable de sous-titre.";
        celesteOffer.addBatch(new BigDecimal(2500), celesteBatch1Description, celeste.getLocale(), DateUtils.nowPlusSomeDays(2), 0);

        String celesteBatch2Description = "Pour la 2ème partie, je vais faire les modifications d'IHM pour choisir les sous-titres et configurer leur disposition.";
        celesteOffer.addBatch( new BigDecimal(1000), celesteBatch2Description, celeste.getLocale(), DateUtils.nowPlusSomeDays(3), 0);

        String celesteBatch3Description = "Pour finir, je vais faire le packaging en tar.gz, deb, rpm et exe de la version patché pour une utilisatation immédiate. Je vais aussi proposer le patch upstream et créer un petit jeu de test fonctionnels.";
        celesteOffer.addBatch( new BigDecimal(700), celesteBatch3Description, celeste.getLocale(), DateUtils.nowPlusSomeDays(4), 0);


        celesteOffer.authenticate(new AuthToken(cerbere));
        celesteOffer.voteUp();



        //Contributions

        try {
            twoSubtitlesInVlcDemand.authenticate(new AuthToken(chogall));
            twoSubtitlesInVlcDemand.addContribution(new BigDecimal("2000"), "");

            twoSubtitlesInVlcDemand.authenticate(new AuthToken(cerbere));
            twoSubtitlesInVlcDemand.addContribution(new BigDecimal("500"), "");


            twoSubtitlesInVlcDemand.authenticate(new AuthToken(hydre));
            twoSubtitlesInVlcDemand.addContribution(new BigDecimal("300"), "");

        } catch (NotEnoughMoneyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        SessionManager.endWorkUnitAndFlush();

    }

    public void giveMoney(Member member, int amount) {
        BankTransaction bankTransaction = new BankTransaction("money !!!", UUID.randomUUID().toString(), member, new BigDecimal(amount),  UUID.randomUUID().toString());
        bankTransaction.setAuthorized();
        bankTransaction.setValidated();
    }

    public Member createMember(String login, String name) throws UnauthorizedOperationException {
        Member member = new Member(login, "plop", login+"@elveos.org", Locale.FRANCE);
        member.authenticate(new AuthToken(member));
        member.setFullname(name);
        member.activate();
        return member;
    }

    private FileMetadata getImage(Member author, String name) {
        String path = ConfigurationManager.loadProperties("web.properties").getProperty("bloatit.www.dir")+"/resources/img/"+name;

        return FileMetadataManager.createFromLocalFile(author, path, name, "Beautiful image");
    }

    public static void main(final String[] args) throws UnauthorizedOperationException {
        System.out.println("Begin database generation");
        new BloatitExampleDB();
        System.out.println("Database generation ended");
        System.exit(0);
    }

}
