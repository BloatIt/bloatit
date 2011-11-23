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
package com.bloatit;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

import com.bloatit.common.ConfigurationManager;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.DaoFeature.FeatureState;
import com.bloatit.data.DaoMember.Role;
import com.bloatit.data.DaoMoneyWithdrawal.State;
import com.bloatit.data.DaoTeam.Right;
import com.bloatit.data.SessionManager;
import com.bloatit.data.exceptions.NotEnoughMoneyException;
import com.bloatit.data.exceptions.UniqueNameExpectedException;
import com.bloatit.framework.FrameworkConfiguration;
import com.bloatit.framework.exceptions.highlevel.ShallNotPassException;
import com.bloatit.framework.mailsender.MailServer;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.i18n.Language;
import com.bloatit.framework.webprocessor.context.User.ActivationState;
import com.bloatit.model.BankTransaction;
import com.bloatit.model.Comment;
import com.bloatit.model.Feature;
import com.bloatit.model.FeatureFactory;
import com.bloatit.model.FeatureImplementation;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.HighlightFeature;
import com.bloatit.model.Member;
import com.bloatit.model.Milestone;
import com.bloatit.model.MoneyWithdrawal;
import com.bloatit.model.Offer;
import com.bloatit.model.Software;
import com.bloatit.model.Team;
import com.bloatit.model.managers.FileMetadataManager;
import com.bloatit.model.right.AuthToken;
import com.bloatit.model.right.UnauthorizedOperationException;

public class BloatitExampleDB { // NO_UCD

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

    public BloatitExampleDB() throws UnauthorizedOperationException, NotEnoughMoneyException, UniqueNameExpectedException {
        System.setProperty("log4J.path", ConfigurationManager.SHARE_DIR + "/log");
        MailServer.getInstance().initialize();

        SessionManager.beginWorkUnit();

        fred = createMember("fred", "Frédéric Bertolus", Locale.FRANCE);
        thomas = createMember("thomas", "Thomas Guyard", Locale.FRANCE);
        yoann = createMember("yoann", "Yoann Plénet", Locale.US);
        admin = createMember("admin", "Administrator", Locale.FRANCE);
        admin.getDao().setRole(Role.ADMIN);

        chogall = createMember("chogall", "Cho'gall", Locale.UK);
        cerbere = createMember("cerbere", "Cerbère", Locale.FRANCE);
        hydre = createMember("hydre", "Hydre", Locale.US);
        elephantman = createMember("elephantman", "ElephantMan", Locale.CANADA);
        celeste = createMember("celeste", "Céleste", Locale.UK);
        rataxes = createMember("rataxes", "Rataxès", Locale.FRANCE);

        AuthToken.authenticate(fred);
        fred.getContact().setName("Frederic Bertolus");
        fred.getContact().setStreet("Le superbe appartement à gauche");
        AuthToken.authenticate(thomas);
        thomas.getContact().setName("Thomas Guyard");
        thomas.getContact().setStreet("Rue Michel Ange");
        thomas.getContact().setCity("Antony");
        thomas.getContact().setPostalCode("92160");
        thomas.getContact().setCountry("France");
        AuthToken.authenticate(yoann);
        yoann.getContact().setName("Yoann Plénet");
        yoann.getContact().setStreet("Le superbe appartement à gauche");
        yoann.getContact().setCity("Antony");
        yoann.getContact().setPostalCode("92160");
        yoann.getContact().setCountry("Allemagne");
        yoann.getContact().setIsCompany(true);
        AuthToken.authenticate(cerbere);
        cerbere.getContact().setName("Cerbère Le Chien");
        cerbere.getContact().setStreet("666, quai du Styx");
        cerbere.getContact().setCity("Tartare");
        cerbere.getContact().setPostalCode("42666");
        cerbere.getContact().setCountry("L'Enfer");
        cerbere.getContact().setIsCompany(true);
        cerbere.getContact().setLegalId("Hades & Fils");
        cerbere.getContact().setInvoiceIdNumber(new BigDecimal(5));
        cerbere.getContact().setInvoiceIdTemplate("HADES-{YEAR|2}{MONTH}-{ID|6}");
        cerbere.getContact().setTaxIdentification("RSC123456");
        cerbere.getContact().setTaxRate(new BigDecimal("0.196"));
        
        // Add avatar
        AuthToken.authenticate(chogall);
        chogall.setAvatar(getImage(chogall, "users/chogall.png"));
        AuthToken.authenticate(cerbere);
        cerbere.setAvatar(getImage(cerbere, "users/cerbere.png"));
        AuthToken.authenticate(hydre);
        hydre.setAvatar(getImage(hydre, "users/hydre.png"));
        AuthToken.authenticate(elephantman);
        elephantman.setAvatar(getImage(elephantman, "users/elephantman.png"));
        AuthToken.authenticate(celeste);
        celeste.setAvatar(getImage(celeste, "users/celeste.png"));
        AuthToken.authenticate(rataxes);
        rataxes.setAvatar(getImage(rataxes, "users/rataxes.png"));

        // Add money
        giveMoney(fred, 1000000);
        giveMoney(thomas, 2000000);
        giveMoney(yoann, 3000000);
        giveMoney(chogall, 2000);
        giveMoney(cerbere, 1000);
        giveMoney(hydre, 500);
        giveMoney(elephantman, 100000000);

        // Add withdrawal
        AuthToken.authenticate(fred);
        withdrawMoney(fred, 1000, State.REQUESTED);
        withdrawMoney(fred, 2000, State.TREATED);
        withdrawMoney(fred, 3000, State.REFUSED);
        withdrawMoney(fred, 4000, State.CANCELED);
        withdrawMoney(fred, 5000, State.COMPLETE);

        AuthToken.authenticate(yoann);
        withdrawMoney(yoann, 1000, State.REQUESTED);
        withdrawMoney(yoann, 2000, State.TREATED);
        withdrawMoney(yoann, 3000, State.REFUSED);
        withdrawMoney(yoann, 4000, State.CANCELED);
        withdrawMoney(yoann, 5000, State.COMPLETE);

        AuthToken.authenticate(thomas);
        withdrawMoney(thomas, 1000, State.REQUESTED);
        withdrawMoney(thomas, 2000, State.TREATED);
        withdrawMoney(thomas, 3000, State.REFUSED);
        withdrawMoney(thomas, 4000, State.CANCELED);
        withdrawMoney(thomas, 5000, State.COMPLETE);

        // Add teams
        final Team other = new Team("other", "plop@elveos.org", "An other team", Right.PROTECTED, yoann);
        AuthToken.authenticate(yoann);
        other.setAvatar(getImage(yoann, "teams/other.png"));

        final Team b219 = new Team("b219", "b219@elveos.org", "The team for b219", Right.PROTECTED, fred);
        AuthToken.authenticate(fred);
        b219.setAvatarUnprotected(getImage(fred, "teams/b219.png"));

        final Team ubuntuUsers = new Team("ubuntuUsers", "ubuntu.users@elveos.org", "The team for ubuntu users", Right.PUBLIC, thomas);
        AuthToken.authenticate(thomas);
        ubuntuUsers.setAvatarUnprotected(getImage(thomas, "teams/ubuntuUsers.png"));

        // Generate softwares
        generateVlcSoftware();
        generatePerroquetSoftware();
        generateLibreOfficeSoftware();
        generateMageiaSoftware();

        // Generate features

        final Feature twoSubtitlesInVlcFeature = generateVlcFeatureTwoSubtitles();
        final Feature addPerroquetInMageiaFeature = generateMageiaFeaturePerroquetPackage();
        final Feature colorPickerFeature = generateLibreOfficeFeatureColorPicker();
        final Feature libreOfficeFeatureDefaultTemplate = generateLibreOfficeFeatureDefaultTemplate();
        final Feature perroquetFeatureArabicSupport = generatePerroquetFeatureArabicSupport();
        final Feature mageiaFeatureRemoveEmacs = generateMageiaFeatureRemoveEmacs();

        // Highlight features
        new HighlightFeature(twoSubtitlesInVlcFeature, 1, "Popular", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(colorPickerFeature, 2, "Recent", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(addPerroquetInMageiaFeature, 3, "In developement", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(libreOfficeFeatureDefaultTemplate, 4, "Need your help quicky", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(perroquetFeatureArabicSupport, 5, "Random", DateUtils.now(), DateUtils.flyingPigDate());
        new HighlightFeature(mageiaFeatureRemoveEmacs, 6, "Success", DateUtils.now(), DateUtils.flyingPigDate());

        SessionManager.endWorkUnitAndFlush();

    }

    public void generateMageiaSoftware() throws UniqueNameExpectedException {
        // Mageia software

        final String mageiaTitle = "Mageia est un fork de Mandriva Linux, reposant sur une association de type 1901 composée de contributeurs reconnus et élus pour leur travail. ";
        final String mageiaDescription = "http://mageia.org/fr/";
        mageia = new Software("Mageia", thomas, Locale.FRANCE, mageiaTitle + mageiaDescription);
        mageia.setImage(getImage(yoann, "mageia.png"));
    }

    public void generateLibreOfficeSoftware() throws UniqueNameExpectedException {
        // LibreOffice software

        final String libreOfficeTitle = "LibreOffice (souvent abrégé en LibO) est une suite bureautique, dérivée directement de OpenOffice.org, créée par The Document Foundation. Cet embranchement a eu lieu le 28 septembre 2010, dans la continuité du rachat de Sun Microsystems par Oracle. ";
        final String libreOfficeDescription = "LibreOffice is the free power-packed Open Source personal productivity suite for Windows, Macintosh and Linux, that gives you six feature-rich applications for all your document production and data processing needs: Writer, Calc, Impress, Draw, Math and Base. Support and documentation is free from our large, dedicated community of users, contributors and developers. You, too, can also get involved!"
                + "\n" + "http://www.libreoffice.org/";
        libreOffice = new Software("LibreOffice", thomas, Locale.FRANCE, libreOfficeTitle + libreOfficeDescription);
        libreOffice.setImage(getImage(fred, "libreoffice.png"));
    }

    public void generatePerroquetSoftware() throws UniqueNameExpectedException {
        // Perroquet software

        final String perroquetTitle = "Perroquet est un programme éducatif dont le but est d'améliorer de manière divertissant votre niveau de compréhension orale des langues étrangères ";
        final String perroquetDescription = "Le principe de Perroquet est d'utiliser une vidéo ou un fichier audio et les sous-titres associés pour vous faire écouter et comprendre les dialogues ou paroles. Après lui avoir indiqué les fichiers à utiliser, Perroquet va lire un morceau de la vidéo et puis la mettre en pause. Il vous indiquera alors le nombre de mot à trouver et vous devrez les taper pour pouvoir continuer la lecture. Il est possible de réécouter une séquence autant de fois que nécessaire. Si vous ne comprenez pas tout, Perroquet présente plusieurs moyen de vous aider. \n"
                + "http://perroquet.b219.org/";
        perroquet = new Software("Perroquet", thomas, Locale.FRANCE, perroquetTitle + perroquetDescription);
        perroquet.setImage(getImage(fred, "perroquet.png"));
    }

    public void generateVlcSoftware() throws UniqueNameExpectedException {
        // VLC software

        final String vlcTitle = "VLC is a free and open source cross-platform multimedia player and framework that plays most multimedia files as well as DVD, Audio CD, VCD, and various streaming protocols. ";
        final String vlcDescription = "http://www.videolan.org/vlc/";
        vlc = new Software("VLC", thomas, Locale.FRANCE, vlcTitle + vlcDescription);
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
                                                                              null,
                                                                              Language.fromLocale(chogall.getLocale()),
                                                                              twoSubtitlesInVlcFeatureTitle,
                                                                              twoSubtitlesInVlcFeatureDescription,
                                                                              vlc);

        AuthToken.authenticate(cerbere);
        final Comment comment1 = twoSubtitlesInVlcFeature.addComment("Super idée !\n"
                + "J'ai exactement le même besoin mais avec 3 langues. Du coup pour être un peu générique, je propose d'avoir la possibilité de sélectionner n langues. Je connais un ami qui apprend en-effet l'araméen, le latin, le grec, l'hébreu, le le haut-sindarin et l'égyptien et qui serait sans doute preneur aussi.");

        AuthToken.authenticate(hydre);
        comment1.addComment("Je suis l'ami de Cerbère qui a posté ci-dessus et qui apprend des langues mortes. Je trouverais ça génial , mais il est indispensable de pouvoir réduire la taille du texte.\n"
                + "Je propose de forker cette demande pour inclure les demandes de changement (nombre de sous-titre non défini et taille des sous-titre définissable) ");

        AuthToken.authenticate(chogall);
        comment1.addComment("OK pour moi, j'aurais dû y penser dès le début, j'ai merdé, j'avais mon cerveau gauche qui avait bu trop de vodka. ");

        AuthToken.authenticate(elephantman);
        final Comment comment2 = twoSubtitlesInVlcFeature.addComment("Elle est naze votre idée, moi j'apprends une langue en 2.53 minutes (moyenne vérifiée sur un échantillon de 353 langues) du coup autant afficher un seul sous-titre à la fois");

        AuthToken.authenticate(chogall);
        comment2.addComment("On ne peut pas vaincre un éléphant ! Abandonnons cette demande !");

        final String rataxesOfferDescription = "Je vais vous le faire vite et bien. Et tout ça pour vraiment pas cher !";
        AuthToken.authenticate(rataxes);
        final Offer rataxesOffer = twoSubtitlesInVlcFeature.addOffer(new BigDecimal("123"),
                                                                     rataxesOfferDescription,
                                                                     "GNU GPL V3",
                                                                     Language.fromLocale(rataxes.getLocale()),
                                                                     DateUtils.tomorrow(),
                                                                     0);

        AuthToken.authenticate(chogall);
        rataxesOffer.voteUp();
        AuthToken.authenticate(hydre);
        rataxesOffer.voteUp();

        AuthToken.authenticate(celeste);
        final String celesteMilestone1Description = "Oulala, ça à l'air compliqué tout ça... Je peux tout de même essayer mais je vais ramer. Je découpe le travail en 3 parties pour simplifier la tache.\n"
                + "Pour la première partie, je vais modifier le coeur du logiciel pour permettre d'afficher un nombre variable de sous-titre.";
        final Offer celesteOffer = twoSubtitlesInVlcFeature.addOffer(new BigDecimal("123"),
                                                                     celesteMilestone1Description,
                                                                     "GNU GPL V3",
                                                                     Language.FR,
                                                                     DateUtils.nowPlusSomeDays(2),
                                                                     0);

        AuthToken.authenticate(celeste);
        final String celesteMilestone2Description = "Pour la 2ème partie, je vais faire les modifications d'IHM pour choisir les sous-titres et configurer leur disposition.";
        celesteOffer.addMilestone(new BigDecimal(1000), celesteMilestone2Description, Language.FR, DateUtils.nowPlusSomeDays(3), 0);

        final String celesteMilestone3Description = "Pour finir, je vais faire le packaging en tar.gz, deb, rpm et exe de la version patché pour une utilisatation immédiate. Je vais aussi proposer le patch upstream et créer un petit jeu de test fonctionnels.";
        celesteOffer.addMilestone(new BigDecimal(700), celesteMilestone3Description, Language.FR, DateUtils.nowPlusSomeDays(4), 0);

        AuthToken.authenticate(cerbere);
        celesteOffer.voteUp();

        // Contributions
        AuthToken.authenticate(chogall);
        twoSubtitlesInVlcFeature.addContribution(new BigDecimal("800"), "On est prêts, non moi j'suis pas prêt !");

        AuthToken.authenticate(cerbere);
        twoSubtitlesInVlcFeature.addContribution(new BigDecimal("500"), "Grrrrrr");

        AuthToken.authenticate(hydre);
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
                                                                                 null,
                                                                                 Language.fromLocale(fred.getLocale()),
                                                                                 addPerroquetInMageiaFeaturetitle,
                                                                                 addPerroquetInMageiaFeatureDescription,
                                                                                 mageia);

        final String hydrePerroquetOfferDescription = "Je le fais et j'ajoute le paquet pour la première release.";
        AuthToken.authenticate(hydre);
        addPerroquetInMageiaFeature.addOffer(new BigDecimal(200),
                                             hydrePerroquetOfferDescription,
                                             "GNU GPL V3",
                                             Language.FR,
                                             DateUtils.tomorrow(),
                                             0);
        // Contributions
        AuthToken.authenticate(hydre);
        addPerroquetInMageiaFeature.addContribution(new BigDecimal("10"), "");

        AuthToken.authenticate(fred);
        addPerroquetInMageiaFeature.addContribution(new BigDecimal("230"), "");

        // Add bugs
        setFeatureInDevelopmentState(addPerroquetInMageiaFeature);

        final Milestone firstMilestone = addPerroquetInMageiaFeature.getSelectedOffer().getMilestones().iterator().next();
        AuthToken.authenticate(fred);
        firstMilestone.addBug("Ça marche pas!", "Rien ne se passe quand on click sur l'icone", Language.fromLocale(fred.getLocale()), Level.FATAL);
        AuthToken.authenticate(elephantman);
        firstMilestone.addBug("Faible qualité graphique pour les éléphants",
                              "L'icone est en vertoriel, c'est pas mal à 2 dimension mais je la trouve un peu pixélisé sur mon écran à 5 dimensions, c'est pas très très beau",
                              Language.FR,
                              Level.MINOR);

        AuthToken.authenticate(yoann);
        firstMilestone.addBug("Fichier de conf système manquant",
                              "Le fichier de conf /etc/perroquet système n'est pas placé. Il faudrait le corriger",
                              Language.FR,
                              Level.MAJOR);

        return addPerroquetInMageiaFeature;
    }

    public Feature generateLibreOfficeFeatureColorPicker() {
        // LibreOffice feature

        // Feature without offer
        final String colorPickerFeatureDescription = "Actuellement dans LibreOffice, il y a un lot de couleur pré-tiré moche. Si l'on veut une jolie couleur, il faut passer dans tous les menus et on arrive enfin sur un outils anti-ergonomique.\n"
                + "Il faudrait donc ajouter un color picker à un endroit accessible, par exemple dans le selecteur de couleur des styles.";

        final String colorPickerFeatureTitle = "Permettre de choisir facilement n'importe quelle couleur";

        final Feature colorPickerFeature = FeatureFactory.createFeature(yoann,
                                                                        null,
                                                                        Language.FR,
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

        final Feature feature = FeatureFactory.createFeature(yoann, null, Language.FR, featureTitle, featureDescription, libreOffice);

        final String offerDescription = "Je suis graphiste et j'ai justement commencé à travailler là dessus. Je propose de faire 10 templates variés";
        AuthToken.authenticate(celeste);
        feature.addOffer(new BigDecimal(1000), offerDescription, "GNU GPL V3", Language.FR, DateUtils.tomorrow(), 0);

        final FeatureImplementation featureImpl = (FeatureImplementation) feature;
        featureImpl.getDao().setValidationDate(DateUtils.now());

        // Contributions
        AuthToken.authenticate(chogall);
        feature.addContribution(new BigDecimal("10"), "");

        return feature;
    }

    public Feature generatePerroquetFeatureArabicSupport() throws UnauthorizedOperationException, NotEnoughMoneyException {
        // LibreOffice feature

        // Feature with offer not validated and funded
        final String featureDescription = "Il faut que perroquet soit capable de gérer les langue qui vont de droite à gauche (en particulier les langues arabes) et vérifier que toutes les caractères sont bien supportés.";

        final String featureTitle = "Support des langues arabe";

        final Feature feature = FeatureFactory.createFeature(yoann, null, Language.FR, featureTitle, featureDescription, perroquet);

        final String offerDescription = "Je suis graphiste et j'ai justement commencé à travailler là dessus. Je propose de faire 10 templates variés";
        AuthToken.authenticate(fred);
        feature.addOffer(new BigDecimal(750), offerDescription, "GNU GPL V3", Language.FR, DateUtils.tomorrow(), 0);

        // Contributions
        AuthToken.authenticate(yoann);
        feature.addContribution(new BigDecimal("760"), "");

        return feature;
    }

    public Feature generateMageiaFeatureRemoveEmacs() throws UnauthorizedOperationException, NotEnoughMoneyException {
        // LibreOffice feature

        // Feature with offer not validated and not funded
        final String featureDescription = "Il faut absolument supprimer emacs des paquets disponible dans Mageia. En effet, le successeur d'emacs vim est maintenant mature et le logiciel emacs qui a bien servi est maintenant dépassé et encombre les paquets. Des sources indiquent aussi qu'emacs est dangereux pour la santé et qu'il peut engendrer un Syndrome du Canal Carpien. D'autre part emacs est peu accessible car il est difficilement utilisable par les personnes ne disposant que d'un seul doigt. ";

        final String featureTitle = "Suppression du paquet emacs déprécié";

        final Feature feature = FeatureFactory.createFeature(thomas, null, Language.FR, featureTitle, featureDescription, mageia);

        final String offerDescription = "Oui, vive vim !";
        AuthToken.authenticate(cerbere);
        feature.addOffer(new BigDecimal(300), offerDescription, "GNU GPL V3", Language.FR, DateUtils.tomorrow(), 0);

        final FeatureImplementation featureImpl = (FeatureImplementation) feature;
        featureImpl.getDao().setValidationDate(DateUtils.now());

        // Contributions
        AuthToken.authenticate(thomas);
        feature.addContribution(new BigDecimal("400"), "");
        
        AuthToken.authenticate(yoann);
        feature.addContribution(new BigDecimal("300"), "");

        setFeatureInFinishedState(feature);

        return feature;
    }

    /**
     * Work only if the money is available
     * 
     * @param feature
     */
    private void setFeatureInDevelopmentState(final Feature feature) {
        final FeatureImplementation featureImpl = (FeatureImplementation) feature;
        featureImpl.getDao().setValidationDate(DateUtils.now());
    }

    private void setFeatureInFinishedState(final Feature feature) {
        final FeatureImplementation featureImpl = (FeatureImplementation) feature;
        AuthToken.authenticate(admin);
        try {
            feature.setFeatureState(FeatureState.DEVELOPPING);
            feature.getSelectedOffer().getCurrentMilestone().forceValidate();
        } catch (UnauthorizedOperationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void withdrawMoney(final Member m, final int amount, final State completion) {
        final MoneyWithdrawal mw = new MoneyWithdrawal(m, "GB87 BARC 2065 8244 9716 55", new BigDecimal(amount));
        AuthToken.authenticate(admin);

        try {
            switch (completion) {
                case REQUESTED:
                    break;
                case TREATED:
                    break;
                case COMPLETE:
                    mw.setTreated();
                    mw.setComplete();
                    break;
                case CANCELED:
                    mw.setCanceled();
                    break;
                case REFUSED:
                    mw.setRefused();
                    break;
            }
        } catch (final UnauthorizedOperationException e) {
            throw new ShallNotPassException("Right error in creating money withdrawal", e);
        }
    }

    public void giveMoney(final Member member, final int amount) throws UnauthorizedOperationException {

        final BankTransaction bankTransaction = new BankTransaction("money !!!",
                                                                    UUID.randomUUID().toString(),
                                                                    member,
                                                                    new BigDecimal(amount),
                                                                    new BigDecimal(amount),
                                                                    UUID.randomUUID().toString());
        bankTransaction.getDao().setAuthorized();
        bankTransaction.getDao().setValidated();
    }

    public Member createMember(final String login, final String name, final Locale locale) throws UnauthorizedOperationException {
        final Member member = new Member(login, "plop", login + "@elveos.org", locale);
        AuthToken.authenticate(member);
        member.setFullname(name);
        member.activate(member.getActivationKey());
        return member;
    }

    private FileMetadata getImage(final Member author, final String name) {
        final String path = FrameworkConfiguration.getWwwDir() + FrameworkConfiguration.getCommonsDir() + "/img/" + name;
        return FileMetadataManager.createFromLocalFile(author, null, path, name, "Projet's logo image");
    }

    public static void main(final String[] args) throws UnauthorizedOperationException, NotEnoughMoneyException, UniqueNameExpectedException {
        System.out.println("Begining database generation");
        new BloatitExampleDB();
        System.out.println("Database generation ended");
        System.exit(0);
    }
}
