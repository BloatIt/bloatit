package com.bloatit;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoTeam.Right;
import com.bloatit.data.SessionManager;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.framework.exceptions.UnauthorizedOperationException;
import com.bloatit.framework.utils.DateUtils;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Comment;
import com.bloatit.model.Feature;
import com.bloatit.model.FeatureFactory;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.Offer;
import com.bloatit.model.Software;
import com.bloatit.model.Team;
import com.bloatit.model.feature.FeatureImplementation;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.model.right.AuthToken;
import com.bloatit.web.WebConfiguration;

public class BloatitExampleDB {

    private Software libreOffice;
    private final Member yoann;
    private final Member fred;
    private final Member thomas;
    private final Member admin;
    private final Member chogall;
    private final Member cerbere;
    private final Member hydre;
    private final Member celeste;
    private final Member elephantman;
    private final Member rataxes;
    private Software vlc;
    private Software perroquet;
    private Software mageia;

    public BloatitExampleDB() throws UnauthorizedOperationException, NotEnoughMoneyException {

        SessionManager.beginWorkUnit();

        fred = createMember("fred", "Frédéric Bertolus");
        thomas = createMember("thomas", "Thomas Guyard");
        yoann = createMember("yoann", "Yoann Plénet");
        admin = createMember("admin", "Administrator");
        admin.setRole(Role.ADMIN);

        chogall = createMember("chogall", "Cho'gall");
        cerbere = createMember("cerbere", "Cerbère");
        hydre = createMember("hydre", "Hydre");
        elephantman = createMember("elephantman", "ElephantMan");
        celeste = createMember("celeste", "Céleste");
        rataxes = createMember("rataxes", "Rataxès");

        // Add avatar
        chogall.setAvatar(getImage(chogall, "users/chogall.png"));
        cerbere.setAvatar(getImage(cerbere, "users/cerbere.png"));
        hydre.setAvatar(getImage(hydre, "users/hydre.png"));
        elephantman.setAvatar(getImage(elephantman, "users/elephantman.png"));
        celeste.setAvatar(getImage(celeste, "users/celeste.png"));
        rataxes.setAvatar(getImage(rataxes, "users/rataxes.png"));

        // Add money
        giveMoney(fred, 1000000);
        giveMoney(thomas, 2000000);
        giveMoney(yoann, 3000000);
        giveMoney(chogall, 2000);
        giveMoney(cerbere, 1000);
        giveMoney(hydre, 500);
        giveMoney(elephantman, 100000000);

        // Add teams
        final Team other = new Team("other", "plop@elveos.org", "An other team", Right.PROTECTED, yoann);
        other.setAvatar(getImage(yoann, "teams/other.png"));

        final Team b219 = new Team("b219", "b219@elveos.org", "The team for b219", Right.PROTECTED, fred);
        b219.setAvatar(getImage(fred, "teams/b219.png"));

        final Team ubuntuUsers = new Team("ubuntuUsers", "ubuntu.users@elveos.org", "The team for ubuntu users", Right.PUBLIC, thomas);
        ubuntuUsers.setAvatar(getImage(thomas, "teams/ubuntuUsers.png"));

        // Generate softwares
        generateVlcSoftware();
        generatePerroquetSoftware();
        generateLibreOfficeSoftware();
        generateMageiaSoftware();

        // Generate features

        final Feature twoSubtitlesInVlcFeature = generateVlcFeatureTwoSubtitles();
        final Feature addPerroquetInMageiaFeature = generateMageiaFeaturePerroquetPackage();
        final Feature colorPickerFeature = generateLibreOfficeFeatureColorPicker();
        Feature libreOfficeFeatureDefaultTemplate = generateLibreOfficeFeatureDefaultTemplate();
        Feature perroquetFeatureArabicSupport = generatePerroquetFeatureArabicSupport();
        Feature mageiaFeatureRemoveEmacs = generateMageiaFeatureRemoveEmacs();

        // Highlight features
        new HighlightFeature(twoSubtitlesInVlcFeature, 1, "Popular", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(colorPickerFeature, 2, "Recent", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(addPerroquetInMageiaFeature, 3, "In developement", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(libreOfficeFeatureDefaultTemplate, 4, "Need your help quicky", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(perroquetFeatureArabicSupport, 5, "Random", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(mageiaFeatureRemoveEmacs, 6, "Success", DateUtils.now(), DateUtils.flyingPigDate());

        SessionManager.endWorkUnitAndFlush();

    }

    public void generateMageiaSoftware() {
        // Mageia software

        final String mageiaTitle = "Mageia est un fork de Mandriva Linux, reposant sur une association de type 1901 composée de contributeurs reconnus et élus pour leur travail.";
        final String mageiaDescription = "http://mageia.org/fr/";
        mageia = new Software("Mageia", thomas, Locale.FRANCE, mageiaTitle, mageiaDescription);
        mageia.setImage(getImage(yoann, "mageia.png"));
    }

    public void generateLibreOfficeSoftware() {
        // LibreOffice software

        final String libreOfficeTitle = "LibreOffice (souvent abrégé en LibO) est une suite bureautique, dérivée directement de OpenOffice.org, créée par The Document Foundation. Cet embranchement a eu lieu le 28 septembre 2010, dans la continuité du rachat de Sun Microsystems par Oracle.";
        final String libreOfficeDescription = "LibreOffice is the free power-packed Open Source personal productivity suite for Windows, Macintosh and Linux, that gives you six feature-rich applications for all your document production and data processing needs: Writer, Calc, Impress, Draw, Math and Base. Support and documentation is free from our large, dedicated community of users, contributors and developers. You, too, can also get involved!"
                + "\n" + "http://www.libreoffice.org/";
        libreOffice = new Software("LibreOffice", thomas, Locale.FRANCE, libreOfficeTitle, libreOfficeDescription);
        libreOffice.setImage(getImage(fred, "libreoffice.png"));
    }

    public void generatePerroquetSoftware() {
        // Perroquet software

        final String perroquetTitle = "Perroquet est un programme éducatif dont le but est d'améliorer de manière divertissant votre niveau de compréhension orale des langues étrangères";
        final String perroquetDescription = "Le principe de Perroquet est d'utiliser une vidéo ou un fichier audio et les sous-titres associés pour vous faire écouter et comprendre les dialogues ou paroles. Après lui avoir indiqué les fichiers à utiliser, Perroquet va lire un morceau de la vidéo et puis la mettre en pause. Il vous indiquera alors le nombre de mot à trouver et vous devrez les taper pour pouvoir continuer la lecture. Il est possible de réécouter une séquence autant de fois que nécessaire. Si vous ne comprenez pas tout, Perroquet présente plusieurs moyen de vous aider. \n"
                + "http://perroquet.b219.org/";
        perroquet = new Software("Perroquet", thomas, Locale.FRANCE, perroquetTitle, perroquetDescription);
        perroquet.setImage(getImage(fred, "perroquet.png"));
    }

    public void generateVlcSoftware() {
        // VLC software

        final String vlcTitle = "VLC is a free and open source cross-platform multimedia player and framework that plays most multimedia files as well as DVD, Audio CD, VCD, and various streaming protocols. ";
        final String vlcDescription = "http://www.videolan.org/vlc/";
        vlc = new Software("VLC", thomas, Locale.FRANCE, vlcTitle, vlcDescription);
        vlc.setImage(getImage(thomas, "vlc.png"));
    }

    public Feature generateVlcFeatureTwoSubtitles() throws UnauthorizedOperationException, NotEnoughMoneyException {

        // Feature with offers selected, not validated and not founded

        final String twoSubtitlesInVlcFeatureDescription = "Offrir la possibilité d'afficher deux sous-titre à la fois dans VLC.\n"
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

        final String twoSubtitlesInVlcFeatureTitle = "Afficher en même temps un sous-titre en anglais et un sous-titre en néerlandais";

        final Feature twoSubtitlesInVlcFeature = FeatureFactory.createFeature(chogall,
                                                                          chogall.getLocale(),
                                                                          twoSubtitlesInVlcFeatureTitle,
                                                                          twoSubtitlesInVlcFeatureDescription,
                                                                          vlc);

        twoSubtitlesInVlcFeature.authenticate(new AuthToken(cerbere));
        final Comment comment1 = twoSubtitlesInVlcFeature.addComment("Super idée !\n"
                + "J'ai exactement le même besoin mais avec 3 langues. Du coup pour être un peu générique, je propose d'avoir la possibilité de sélectionner n langues. Je connais un ami qui apprend en-effet l'araméen, le latin, le grec, l'hébreu, le le haut-sindarin et l'égyptien et qui serait sans doute preneur aussi.");

        comment1.authenticate(new AuthToken(hydre));
        comment1.addComment("Je suis l'ami de Cerbère qui a posté ci-dessus et qui apprend des langues mortes. Je trouverais ça génial , mais il est indispensable de pouvoir réduire la taille du texte.\n"
                + "Je propose de forker cette demande pour inclure les demandes de changement (nombre de sous-titre non défini et taille des sous-titre définissable) ");

        comment1.authenticate(new AuthToken(chogall));
        comment1.addComment("OK pour moi, j'aurais dû y penser dès le début, j'ai merdé, j'avais mon cerveau gauche qui avait bu trop de vodka. ");

        twoSubtitlesInVlcFeature.authenticate(new AuthToken(elephantman));
        final Comment comment2 = twoSubtitlesInVlcFeature.addComment("Elle est naze votre idée, moi j'apprends une langue en 2.53 minutes (moyenne vérifiée sur un échantillon de 353 langues) du coup autant afficher un seul sous-titre à la fois");

        comment2.authenticate(new AuthToken(chogall));
        comment2.addComment("On ne peut pas vaincre un éléphant ! Abandonnons cette demande !");

        final String rataxesOfferDescription = "Je vais vous le faire vite et bien. Et tout ça pour vraiment pas cher !";
        twoSubtitlesInVlcFeature.authenticate(new AuthToken(rataxes));
        final Offer rataxesOffer = twoSubtitlesInVlcFeature.addOffer(rataxes,
                                                                    new BigDecimal(2300),
                                                                    rataxesOfferDescription,
                                                                    rataxes.getLocale(),
                                                                    DateUtils.tomorrow(),
                                                                    0);

        rataxesOffer.authenticate(new AuthToken(chogall));
        rataxesOffer.voteUp();
        rataxesOffer.authenticate(new AuthToken(hydre));
        rataxesOffer.voteUp();

        twoSubtitlesInVlcFeature.authenticate(new AuthToken(celeste));
        final String celesteMilestone1Description = "Oulala, ça à l'air compliqué tout ça... Je peux tout de même essayer mais je vais ramer. Je découpe le travail en 3 parties pour simplifier la tache.\n"
                + "Pour la première partie, je vais modifier le coeur du logiciel pour permettre d'afficher un nombre variable de sous-titre.";
        final Offer celesteOffer = twoSubtitlesInVlcFeature.addOffer(celeste,
                                                                    new BigDecimal(2500),
                                                                    celesteMilestone1Description,
                                                                    celeste.getLocale(),
                                                                    DateUtils.nowPlusSomeDays(2),
                                                                    0);

        final String celesteMilestone2Description = "Pour la 2ème partie, je vais faire les modifications d'IHM pour choisir les sous-titres et configurer leur disposition.";
        celesteOffer.addMilestone(new BigDecimal(1000), celesteMilestone2Description, celeste.getLocale(), DateUtils.nowPlusSomeDays(3), 0);

        final String celesteMilestone3Description = "Pour finir, je vais faire le packaging en tar.gz, deb, rpm et exe de la version patché pour une utilisatation immédiate. Je vais aussi proposer le patch upstream et créer un petit jeu de test fonctionnels.";
        celesteOffer.addMilestone(new BigDecimal(700), celesteMilestone3Description, celeste.getLocale(), DateUtils.nowPlusSomeDays(4), 0);

        celesteOffer.authenticate(new AuthToken(cerbere));
        celesteOffer.voteUp();

        // Contributions
        twoSubtitlesInVlcFeature.authenticate(new AuthToken(chogall));
        twoSubtitlesInVlcFeature.addContribution(new BigDecimal("800"), "On est prêts, non moi j'suis pas prêt !");

        twoSubtitlesInVlcFeature.authenticate(new AuthToken(cerbere));
        twoSubtitlesInVlcFeature.addContribution(new BigDecimal("500"), "Grrrrrr");

        twoSubtitlesInVlcFeature.authenticate(new AuthToken(hydre));
        twoSubtitlesInVlcFeature.addContribution(new BigDecimal("300"), "");

        return twoSubtitlesInVlcFeature;
    }

    public Feature generateMageiaFeaturePerroquetPackage() throws UnauthorizedOperationException, NotEnoughMoneyException {
        // Mageia feature

        // Feature in development

        final String addPerroquetInMageiaFeatureDescription = "Le logiciel perroquet (http://perroquet.b219.org) a des paquets pour Ubuntu et ArchLinux mais pas pour Mageia.\n"
                + "\n"
                + "Le but de cette demande est de créer un paquet pour perroquet et si possible l'intégrer dans les paquets officiels de Mageia.\n"
                + "Le paquet devra avoir le même niveau d'intégration que celui pour Ubuntu : icones, handle sur les fichiers .perroquet, ...";

        final String addPerroquetInMageiaFeaturetitle = "Make a packet for Mageia for the Perroquet software";

        final Feature addPerroquetInMageiaFeature = FeatureFactory.createFeature(fred,
                                                                             fred.getLocale(),
                                                                             addPerroquetInMageiaFeaturetitle,
                                                                             addPerroquetInMageiaFeatureDescription,
                                                                             mageia);

        final String hydrePerroquetOfferDescription = "Je le fais et j'ajoute le paquet pour la première release.";
        addPerroquetInMageiaFeature.authenticate(new AuthToken(hydre));
        final Offer hydrePerroquetOffer = addPerroquetInMageiaFeature.addOffer(hydre,
                                                                              new BigDecimal(200),
                                                                              hydrePerroquetOfferDescription,
                                                                              hydre.getLocale(),
                                                                              DateUtils.tomorrow(),
                                                                              0);
        // Contributions
        addPerroquetInMageiaFeature.authenticate(new AuthToken(hydre));
        addPerroquetInMageiaFeature.addContribution(new BigDecimal("10"), "");

        addPerroquetInMageiaFeature.authenticate(new AuthToken(fred));
        addPerroquetInMageiaFeature.addContribution(new BigDecimal("230"), "");

        // Add bugs
        setFeatureInDevelopmentState(addPerroquetInMageiaFeature);

        final Milestone firstMilestone = addPerroquetInMageiaFeature.getSelectedOffer().getMilestonees().iterator().next();
        firstMilestone.addBug(fred, "Ça marche pas!", "Rien ne se passe quand on click sur l'icone", fred.getLocale(), Level.FATAL);
        firstMilestone.addBug(elephantman,
                          "Faible qualité graphique pour les éléphants",
                          "L'icone est en vertoriel, c'est pas mal à 2 dimension mais je la trouve un peu pixélisé sur mon écran à 5 dimensions, c'est pas très très beau",
                          elephantman.getLocale(),
                          Level.MINOR);
        firstMilestone.addBug(yoann,
                          "Fichier de conf système manquant",
                          "Le fichier de conf /etc/perroquet système n'est pas placé. Il faudrait le corriger",
                          yoann.getLocale(),
                          Level.MAJOR);

        return addPerroquetInMageiaFeature;
    }

    public Feature generateLibreOfficeFeatureColorPicker() throws UnauthorizedOperationException {
        // LibreOffice feature

        // Feature without offer
        final String colorPickerFeatureDescription = "Actuellement dans LibreOffice, il y a un lot de couleur pré-tiré moche. Si l'on veut une jolie couleur, il faut passer dans tous les menus et on arrive enfin sur un outils anti-ergonomique.\n"
                + "Il faudrait donc ajouter un color picker à un endroit accessible, par exemple dans le selecteur de couleur des styles.";

        final String colorPickerFeatureTitle = "Permettre de choisir facilement n'importe quelle couleur";

        final Feature colorPickerFeature = FeatureFactory.createFeature(yoann,
                                                                    yoann.getLocale(),
                                                                    colorPickerFeatureTitle,
                                                                    colorPickerFeatureDescription,
                                                                    libreOffice);
        return colorPickerFeature;
    }

    public Feature generateLibreOfficeFeatureDefaultTemplate() throws UnauthorizedOperationException, NotEnoughMoneyException {
        // LibreOffice feature

        // Feature with offer validated but not funded
        final String featureDescription = "Actuellement dans LibreOffice, le template par défaut n'est pas très beau. Un jeu de template élégant inclus par défaut serait vraiment utile.";

        final String featureTitle = "Jolie template par défaut dans Libre Office ";

        final Feature feature = FeatureFactory.createFeature(yoann, yoann.getLocale(), featureTitle, featureDescription, libreOffice);

        final String offerDescription = "Je suis graphiste et j'ai justement commencé à travailler là dessus. Je propose de faire 10 templates variés";
        feature.authenticate(new AuthToken(celeste));
        final Offer offer = feature.addOffer(celeste, new BigDecimal(1000), offerDescription, celeste.getLocale(), DateUtils.tomorrow(), 0);

        FeatureImplementation featureImpl = (FeatureImplementation) feature;
        featureImpl.getDao().setValidationDate(DateUtils.now());

        // Contributions
        feature.authenticate(new AuthToken(chogall));
        feature.addContribution(new BigDecimal("10"), "");

        return feature;
    }

    public Feature generatePerroquetFeatureArabicSupport() throws UnauthorizedOperationException, NotEnoughMoneyException {
        // LibreOffice feature

        // Feature with offer not validated and funded
        final String featureDescription = "Il faut que perroquet soit capable de gérer les langue qui vont de droite à gauche (en particulier les langues arabes) et vérifier que toutes les caractères sont bien supportés.";

        final String featureTitle = "Support des langues arabe";

        final Feature feature = FeatureFactory.createFeature(yoann, yoann.getLocale(), featureTitle, featureDescription, perroquet);

        final String offerDescription = "Je suis graphiste et j'ai justement commencé à travailler là dessus. Je propose de faire 10 templates variés";
        feature.authenticate(new AuthToken(fred));
        final Offer offer = feature.addOffer(fred, new BigDecimal(750), offerDescription, fred.getLocale(), DateUtils.tomorrow(), 0);

        // Contributions
        feature.authenticate(new AuthToken(yoann));
        feature.addContribution(new BigDecimal("760"), "");

        return feature;
    }

    public Feature generateMageiaFeatureRemoveEmacs() throws UnauthorizedOperationException, NotEnoughMoneyException {
        // LibreOffice feature

        // Feature with offer not validated and not funded
        final String featureDescription = "Il faut absolument supprimer emacs des paquets disponible dans Mageia. En effet, le successeur d'emacs vim est maintenant mature et le logiciel emacs qui a bien servi est maintenant dépassé et encombre les paquets. Des sources indiquent aussi qu'emacs est dangereux pour la santé et qu'il peut engendrer un Syndrome du Canal Carpien. D'autre part emacs est peu accessible car il est difficilement utilisable par les personnes ne disposant que d'un seul doigt. ";

        final String featureTitle = "Suppression du paquet emacs déprécié";

        final Feature feature = FeatureFactory.createFeature(thomas, thomas.getLocale(), featureTitle, featureDescription, mageia);

        final String offerDescription = "Oui, vive vim !";
        feature.authenticate(new AuthToken(cerbere));
        final Offer offer = feature.addOffer(cerbere, new BigDecimal(300), offerDescription, cerbere.getLocale(), DateUtils.tomorrow(), 0);

        FeatureImplementation featureImpl = (FeatureImplementation) feature;
        featureImpl.getDao().setValidationDate(DateUtils.now());

        // Contributions
        feature.authenticate(new AuthToken(thomas));
        feature.addContribution(new BigDecimal("400"), "");

        setFeatureInFinishedState(feature);

        return feature;
    }

    /**
     * Work only if the money is available
     * @param feature
     */
    private void setFeatureInDevelopmentState(final Feature feature) {
        final FeatureImplementation featureImpl = (FeatureImplementation) feature;
        featureImpl.getDao().setValidationDate(DateUtils.now());
    }

    private void setFeatureInFinishedState(final Feature feature) {
        final FeatureImplementation featureImpl = (FeatureImplementation) feature;
        featureImpl.getDao().setFeatureState(FeatureState.FINISHED);
    }

    private void setFeatureInDiscardedState(final Feature feature) {
        final FeatureImplementation featureImpl = (FeatureImplementation) feature;
        featureImpl.getDao().setFeatureState(FeatureState.DISCARDED);
    }

    public void giveMoney(final Member member, final int amount) {
        final BankTransaction bankTransaction = new BankTransaction("money !!!",
                                                                    UUID.randomUUID().toString(),
                                                                    member,
                                                                    new BigDecimal(amount),
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
        final String path = WebConfiguration.getBloatitWwwDir() + "/resources/img/" + name;

        return FileMetadataManager.createFromLocalFile(author, path, name, "Projet's logo image");
    }

    public static void main(final String[] args) throws UnauthorizedOperationException, NotEnoughMoneyException {
        System.out.println("Begin database generation");
        new BloatitExampleDB();
        System.out.println("Database generation ended");
        System.exit(0);
    }

}
