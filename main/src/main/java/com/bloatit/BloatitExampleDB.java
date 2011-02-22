package com.bloatit;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoDemand.DemandState;
import com.bloatit.data.DaoGroup.Right;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.SessionManager;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.DateUtils;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Batch;
import com.bloatit.model.Comment;
import com.bloatit.model.Demand;
import com.bloatit.model.DemandFactory;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Group;
import com.bloatit.model.HighlightDemand;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.Project;
import com.bloatit.model.demand.DemandImplementation;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.model.right.AuthToken;

public class BloatitExampleDB {

    public BloatitExampleDB() throws UnauthorizedOperationException, NotEnoughMoneyException {

        SessionManager.beginWorkUnit();

        // Serious accounts
        final Member fred = createMember("fred", "Frédéric Bertolus");
        final Member thomas = createMember("thomas", "Thomas Guyard");
        final Member yoann = createMember("yoann", "Yoann Plénet");
        final Member admin = createMember("admin", "Administrator");
        admin.setRole(Role.ADMIN);

        // Very not serious accounts
        final Member chogall = createMember("chogall", "Cho'gall");
        final Member cerbere = createMember("cerbere", "Cerbère");
        final Member hydre = createMember("hydre", "Hydre");
        final Member elephantman = createMember("elephantman", "ElephantMan");
        final Member celeste = createMember("celeste", "Céleste");
        final Member rataxes = createMember("rataxes", "Rataxès");

        // Add money
        giveMoney(fred, 1000000);
        giveMoney(thomas, 2000000);
        giveMoney(yoann, 3000000);
        giveMoney(chogall, 2000);
        giveMoney(cerbere, 1000);
        giveMoney(hydre, 500);
        giveMoney(elephantman, 100000000);

        // Add groups
        final Group other = new Group("other", "plop@elveos.org", "An other group", Right.PROTECTED, yoann);
        final Group b219 = new Group("b219", "b219@elveos.org", "The group for b219", Right.PROTECTED, fred);
        final Group ubuntuUsers = new Group("ubuntuUsers", "ubuntu.users@elveos.org", "The group for ubuntu users", Right.PUBLIC, thomas);

        // VLC project

        final String vlcTitle = "VLC is a free and open source cross-platform multimedia player and framework that plays most multimedia files as well as DVD, Audio CD, VCD, and various streaming protocols. ";
        final String vlcDescription = "http://www.videolan.org/vlc/";
        final Project vlc = new Project("VLC", thomas, Locale.FRANCE, vlcTitle, vlcDescription, getImage(thomas, "vlc.png"));

        // Perroquet project

        final String perroquetTitle = "Perroquet est un programme éducatif dont le but est d'améliorer de manière divertissant votre niveau de compréhension orale des langues étrangères";
        final String perroquetDescription = "Le principe de Perroquet est d'utiliser une vidéo ou un fichier audio et les sous-titres associés pour vous faire écouter et comprendre les dialogues ou paroles. Après lui avoir indiqué les fichiers à utiliser, Perroquet va lire un morceau de la vidéo et puis la mettre en pause. Il vous indiquera alors le nombre de mot à trouver et vous devrez les taper pour pouvoir continuer la lecture. Il est possible de réécouter une séquence autant de fois que nécessaire. Si vous ne comprenez pas tout, Perroquet présente plusieurs moyen de vous aider. \n"
                + "http://perroquet.b219.org/";
        final Project perroquet = new Project("Perroquet", thomas, Locale.FRANCE, perroquetTitle, perroquetDescription, getImage(fred,
                                                                                                                                 "perroquet.png"));

        // LibreOffice project

        final String libreOfficeTitle = "LibreOffice (souvent abrégé en LibO) est une suite bureautique, dérivée directement de OpenOffice.org, créée par The Document Foundation. Cet embranchement a eu lieu le 28 septembre 2010, dans la continuité du rachat de Sun Microsystems par Oracle.";
        final String libreOfficeDescription = "LibreOffice is the free power-packed Open Source personal productivity suite for Windows, Macintosh and Linux, that gives you six feature-rich applications for all your document production and data processing needs: Writer, Calc, Impress, Draw, Math and Base. Support and documentation is free from our large, dedicated community of users, contributors and developers. You, too, can also get involved!"
                + "\n" + "http://www.libreoffice.org/";
        final Project libreOffice = new Project("LibreOffice",
                                                thomas,
                                                Locale.FRANCE,
                                                libreOfficeTitle,
                                                libreOfficeDescription,
                                                getImage(fred, "libreoffice.png"));

        // Mageia project

        final String mageiaTitle = "Mageia est un fork de Mandriva Linux, reposant sur une association de type 1901 composée de contributeurs reconnus et élus pour leur travail.";
        final String mageiaDescription = "http://mageia.org/fr/";
        final Project mageia = new Project("Mageia", thomas, Locale.FRANCE, mageiaTitle, mageiaDescription, getImage(yoann, "mageia.png"));

        final String twoSubtitlesInVlcDemandDescription = "Offrir la possibilité d'afficher deux sous-titre à la fois dans VLC.\n"
                + "\n"
                + "Afin de m'entrainer à parler anglais et néerlandais à la fois, je souhaite pouvoir afficher les sous-titre de ces deux langues en même temps dans VLC.\n"
                + "Parce que je suis très gentil, si on peut afficher deux sous-titre de n'importe quelle langue ça m'ira aussi (si le néerlandais font bien sûr partis des langues supportées).\n"
                + "\n"
                + "Les fichiers de sous titre lus doivent être séparés. Je ne veux pas avoir à utiliser un logiciel quelconque qui combinera les sous titres. Je veux juste pouvoir clicker sur le bouton \"sous-titre\", cocher une case \"afficher deux sous-titre simultanément\" (wording à revoir) et voilà (ndt : en anglais dans le texte).\n"
                + "\n"
                + "Notes :\n"
                + "- Les sous-titres pourront être dans des formats différents.\n"
                + "- Les sous-titres pourront ne pas être synchronisés exactement de la même manière (un sous-titre pourra changer alors que le précédent est encore affiché)\n"
                + "\n"
                + "J'aimerais que ce soit implémenté dans la semaine, je suis en-effet en train de me préparer à un concours extrèmement complexe (le concours de la bicéphalie) qui aura lieu dans 3 semaines, et j'ai besoin d'au moins deux semaines pour maitriser parfaitement ces deux langues (j'ai pu apprendre le Chinois et l'Arabe en 3 jours auparavant, mais le néerlandais est quand même très complexe).";

        final String twoSubtitlesInVlcDemandTitle = "Afficher en même temps un sous-titre en anglais et un sous-titre en néerlandais";

        final Demand twoSubtitlesInVlcDemand = DemandFactory.createDemand(chogall,
                                                                          chogall.getLocale(),
                                                                          twoSubtitlesInVlcDemandTitle,
                                                                          twoSubtitlesInVlcDemandDescription,
                                                                          vlc);

        twoSubtitlesInVlcDemand.authenticate(new AuthToken(cerbere));
        final Comment comment1 = twoSubtitlesInVlcDemand.addComment("Super idée !\n"
                + "J'ai exactement le même besoin mais avec 3 langues. Du coup pour être un peu générique, je propose d'avoir la possibilité de sélectionner n langues. Je connais un ami qui apprend en-effet l'araméen, le latin, le grec, l'hébreu, le le haut-sindarin et l'égyptien et qui serait sans doute preneur aussi.");

        comment1.authenticate(new AuthToken(hydre));
        comment1.addComment("Je suis l'ami de Cerbère qui a posté ci-dessus et qui apprend des langues mortes. Je trouverais ça génial , mais il est indispensable de pouvoir réduire la taille du texte.\n"
                + "Je propose de forker cette demande pour inclure les demandes de changement (nombre de sous-titre non défini et taille des sous-titre définissable) ");

        comment1.authenticate(new AuthToken(chogall));
        comment1.addComment("OK pour moi, j'aurais dû y penser dès le début, j'ai merdé, j'avais mon cerveau gauche qui avait bu trop de vodka. ");

        twoSubtitlesInVlcDemand.authenticate(new AuthToken(elephantman));
        final Comment comment2 = twoSubtitlesInVlcDemand.addComment("Elle est naze votre idée, moi j'apprends une langue en 2.53 minutes (moyenne vérifiée sur un échantillon de 353 langues) du coup autant afficher un seul sous-titre à la fois");

        comment2.authenticate(new AuthToken(chogall));
        comment2.addComment("On ne peut pas vaincre un éléphant ! Abandonnons cette demande !");

        final String rataxesOfferDescription = "Je vais vous le faire vite et bien. Et tout ça pour vraiment pas cher !";
        twoSubtitlesInVlcDemand.authenticate(new AuthToken(rataxes));
        final Offer rataxesOffer = twoSubtitlesInVlcDemand.addOffer(rataxes,
                                                                    new BigDecimal(2300),
                                                                    rataxesOfferDescription,
                                                                    rataxes.getLocale(),
                                                                    DateUtils.tomorrow(),
                                                                    0);

        rataxesOffer.authenticate(new AuthToken(chogall));
        rataxesOffer.voteUp();
        rataxesOffer.authenticate(new AuthToken(hydre));
        rataxesOffer.voteUp();

        twoSubtitlesInVlcDemand.authenticate(new AuthToken(celeste));
        final Offer celesteOffer = twoSubtitlesInVlcDemand.addEmptyOffer(celeste);
        final String celesteBatch1Description = "Oulala, ça à l'air compliqué tout ça... Je peux tout de même essayer mais je vais ramer. Je découpe le travail en 3 parties pour simplifier la tache.\n"
                + "Pour la première partie, je vais modifier le coeur du logiciel pour permettre d'afficher un nombre variable de sous-titre.";
        celesteOffer.addBatch(new BigDecimal(2500), celesteBatch1Description, celeste.getLocale(), DateUtils.nowPlusSomeDays(2), 0);

        final String celesteBatch2Description = "Pour la 2ème partie, je vais faire les modifications d'IHM pour choisir les sous-titres et configurer leur disposition.";
        celesteOffer.addBatch(new BigDecimal(1000), celesteBatch2Description, celeste.getLocale(), DateUtils.nowPlusSomeDays(3), 0);

        final String celesteBatch3Description = "Pour finir, je vais faire le packaging en tar.gz, deb, rpm et exe de la version patché pour une utilisatation immédiate. Je vais aussi proposer le patch upstream et créer un petit jeu de test fonctionnels.";
        celesteOffer.addBatch(new BigDecimal(700), celesteBatch3Description, celeste.getLocale(), DateUtils.nowPlusSomeDays(4), 0);

        celesteOffer.authenticate(new AuthToken(cerbere));
        celesteOffer.voteUp();

        // Mageia demand
        final String addPerroquetInMageiaDemandDescription = "Le logiciel perroquet (http://perroquet.b219.org) a des paquets pour Ubuntu et ArchLinux mais pas pour Mageia.\n"
                + "\n"
                + "Le but de cette demande est de créer un paquet pour perroquet et si possible l'intégrer dans les paquets officiels de Mageia.\n"
                + "Le paquet devra avoir le même niveau d'intégration que celui pour Ubuntu : icones, handle sur les fichiers .perroquet, ...";

        final String addPerroquetInMageiaDemandtitle = "Make a packet for Mageia for the Perroquet software";

        final Demand addPerroquetInMageiaDemand = DemandFactory.createDemand(fred,
                                                                             fred.getLocale(),
                                                                             addPerroquetInMageiaDemandtitle,
                                                                             addPerroquetInMageiaDemandDescription,
                                                                             mageia);

        final String hydrePerroquetOfferDescription = "Je le fais et j'ajoute le paquet pour la première release.";
        addPerroquetInMageiaDemand.authenticate(new AuthToken(hydre));
        final Offer hydrePerroquetOffer = addPerroquetInMageiaDemand.addOffer(hydre,
                                                                              new BigDecimal(200),
                                                                              hydrePerroquetOfferDescription,
                                                                              hydre.getLocale(),
                                                                              DateUtils.tomorrow(),
                                                                              0);

        // LibreOffice demand
        final String colorPickerDemandDescription = "Actuellement dans LibreOffice, il y a un lot de couleur pré-tiré moche. Si l'on veut une jolie couleur, il faut passer dans tous les menus et on arrive enfin sur un outils anti-ergonomique.\n"
                + "Il faudrait donc ajouter un color picker à un endroit accessible, par exemple dans le selecteur de couleur des styles.";

        final String colorPickerDemandTitle = "Permettre de choisir facilement n'importe quelle couleur";

        final Demand colorPickerDemand = DemandFactory.createDemand(yoann,
                                                                    yoann.getLocale(),
                                                                    colorPickerDemandTitle,
                                                                    colorPickerDemandDescription,
                                                                    libreOffice);

        // Contributions

        twoSubtitlesInVlcDemand.authenticate(new AuthToken(chogall));
        twoSubtitlesInVlcDemand.addContribution(new BigDecimal("800"), "On est prêts, non moi j'suis pas prêt !");

        twoSubtitlesInVlcDemand.authenticate(new AuthToken(cerbere));
        twoSubtitlesInVlcDemand.addContribution(new BigDecimal("500"), "Grrrrrr");

        twoSubtitlesInVlcDemand.authenticate(new AuthToken(hydre));
        twoSubtitlesInVlcDemand.addContribution(new BigDecimal("300"), "");

        addPerroquetInMageiaDemand.authenticate(new AuthToken(hydre));
        addPerroquetInMageiaDemand.addContribution(new BigDecimal("10"), "");

        addPerroquetInMageiaDemand.authenticate(new AuthToken(fred));
        addPerroquetInMageiaDemand.addContribution(new BigDecimal("230"), "");

        // Highlight demands
        new HighlightDemand(twoSubtitlesInVlcDemand, 1, "This is the only one", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightDemand(colorPickerDemand, 2, "This is the only one", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightDemand(addPerroquetInMageiaDemand, 3, "This is the only one", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightDemand(twoSubtitlesInVlcDemand, 4, "This is the only one", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightDemand(twoSubtitlesInVlcDemand, 5, "This is the only one", DateUtils.now(), DateUtils.flyingPigDate());
        // new HighlightDemand(twoSubtitlesInVlcDemand, 6,
        // "This is the only one", DateUtils.now(), DateUtils.flyingPigDate());

        // Add bugs
        setDemandInDevelopmentState(addPerroquetInMageiaDemand);

        final Batch firstBatch = addPerroquetInMageiaDemand.getSelectedOffer().getBatches().iterator().next();
        firstBatch.addBug(fred, "Ça marche pas!", "Rien ne se passe quand on click sur l'icone", fred.getLocale(), Level.FATAL);
        firstBatch.addBug(elephantman,
                          "Faible qualité graphique pour les éléphants",
                          "L'icone est en vertoriel, c'est pas mal à 2 dimension mais je la trouve un peu pixélisé sur mon écran à 5 dimensions, c'est pas très très beau",
                          elephantman.getLocale(),
                          Level.MINOR);
        firstBatch.addBug(yoann,
                          "Fichier de conf système manquant",
                          "Le fichier de conf /etc/perroquet système n'est pas placé. Il faudrait le corriger",
                          yoann.getLocale(),
                          Level.MAJOR);

        SessionManager.endWorkUnitAndFlush();

    }

    private void setDemandInDevelopmentState(final Demand demand) {
        final DemandImplementation demandImpl = (DemandImplementation) demand;
        demandImpl.getDao().setDemandState(DemandState.DEVELOPPING);
    }

    public void giveMoney(final Member member, final int amount) {
        final BankTransaction bankTransaction = new BankTransaction("money !!!",
                                                                    UUID.randomUUID().toString(),
                                                                    member,
                                                                    new BigDecimal(amount),
                                                                    UUID.randomUUID().toString());
        bankTransaction.setAuthorized();
        bankTransaction.setValidated();
    }

    public Member createMember(final String login, final String name) throws UnauthorizedOperationException {
        final Member member = new Member(login, "plop", login + "@elveos.org", Locale.FRANCE);
        member.authenticate(new AuthToken(member));
        member.setFullname(name);
        member.activate();
        return member;
    }

    private FileMetadata getImage(final Member author, final String name) {
        final String path = ConfigurationManager.loadProperties("web.properties").getProperty("bloatit.www.dir") + "/resources/img/" + name;

        return FileMetadataManager.createFromLocalFile(author, path, name, "Beautiful image");
    }

    public static void main(final String[] args) throws UnauthorizedOperationException, NotEnoughMoneyException {
        System.out.println("Begin database generation");
        new BloatitExampleDB();
        System.out.println("Database generation ended");
        System.exit(0);
    }

}
