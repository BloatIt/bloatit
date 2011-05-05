package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webprocessor.context.Context.tr;

import com.bloatit.common.Log;
import com.bloatit.data.DaoMilestone.MilestoneState;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.datetime.TimeRenderer;
import com.bloatit.framework.utils.datetime.TimeRenderer.TimeBase;
import com.bloatit.framework.utils.i18n.DateLocale.FormatStyle;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlList;
import com.bloatit.framework.webprocessor.components.HtmlListItem;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.javascript.JsShowHide;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.components.meta.XmlNode;
import com.bloatit.framework.webprocessor.components.meta.XmlText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Milestone;
import com.bloatit.model.Offer;
import com.bloatit.model.Release;
import com.bloatit.model.feature.FeatureImplementation;
import com.bloatit.web.HtmlTools;
import com.bloatit.web.components.HtmlProgressBar;
import com.bloatit.web.linkable.members.MembersTools;
import com.bloatit.web.pages.master.HtmlDefineParagraph;
import com.bloatit.web.url.AddReleasePageUrl;
import com.bloatit.web.url.MemberPageUrl;
import com.bloatit.web.url.PopularityVoteActionUrl;
import com.bloatit.web.url.ReleasePageUrl;
import com.bloatit.web.url.TeamPageUrl;

public final class OfferBlock extends HtmlDiv {

    private final Offer offer;

    public OfferBlock(final Offer offer, final boolean selected, final ElveosUserToken userToken) {
        super((selected ? "offer_selected_block" : "offer_unselected_block"));
        this.offer = offer;

        final HtmlDiv offerTopBlock = new HtmlDiv("offer_top_block");
        {
            final HtmlDiv offerLeftTopColumn = new HtmlDiv("offer_left_top_column");
            {
                offerLeftTopColumn.add(MembersTools.getMemberAvatar(offer.getAuthor()));
            }
            offerTopBlock.add(offerLeftTopColumn);

            final HtmlDiv offerRightTopColumn = new HtmlDiv("offer_right_top_column");
            {
                final HtmlDiv offerPriceBlock = new HtmlDiv("offer_price_block");
                {
                    final HtmlSpan priceLabel = new HtmlSpan("offer_block_label");
                    priceLabel.addText(tr("Total price: "));
                    offerPriceBlock.add(priceLabel);

                    final HtmlSpan price = new HtmlSpan("offer_block_price");
                    price.addText(Context.getLocalizator().getCurrency(offer.getAmount()).getSimpleEuroString());
                    offerPriceBlock.add(price);
                }
                offerRightTopColumn.add(offerPriceBlock);

                final HtmlParagraph authorPara = new HtmlParagraph();
                authorPara.setCssClass("offer_block_para");
                {
                    final HtmlSpan authorLabel = new HtmlSpan("offer_block_label");
                    authorLabel.addText(tr("Author: "));
                    authorPara.add(authorLabel);

                    if (offer.getAsTeam() != null) {
                        HtmlLink author = null;
                        author = new TeamPageUrl(offer.getAsTeam()).getHtmlLink(offer.getAsTeam().getDisplayName());
                        author.setCssClass("offer_block_author");
                        authorPara.add(author);

                        authorPara.addText(tr(" by "));
                        authorPara.add(new MemberPageUrl(offer.getMember()).getHtmlLink(offer.getMember().getDisplayName()));
                    } else {
                        HtmlLink author = null;
                        author = new MemberPageUrl(offer.getMember()).getHtmlLink(offer.getMember().getDisplayName());
                        author.setCssClass("offer_block_author");
                        authorPara.add(author);
                    }

                }
                offerRightTopColumn.add(authorPara);

                final HtmlDiv progressPara = new HtmlDiv("offer_block_para");
                {
                    final HtmlSpan progressLabel = new HtmlSpan("offer_block_label");
                    progressLabel.addText(tr("Funding: "));
                    progressPara.add(progressLabel);

                    final int progression = (int) Math.floor(offer.getProgression());
                    final HtmlSpan progress = new HtmlSpan("offer_block_progress");
                    progress.addText(tr("{0} %", String.valueOf(progression)));
                    progressPara.add(progress);

                    int cappedProgressValue = progression;
                    if (cappedProgressValue > FeatureImplementation.PROGRESSION_PERCENT) {
                        cappedProgressValue = FeatureImplementation.PROGRESSION_PERCENT;
                    }

                    final HtmlProgressBar progressBar = new HtmlProgressBar(cappedProgressValue);
                    progressPara.add(progressBar);

                }
                offerRightTopColumn.add(progressPara);
            }
            offerTopBlock.add(offerRightTopColumn);
        }
        add(offerTopBlock);

        final HtmlDiv offerBottomBlock = new HtmlDiv("offer_bottom_block");
        {
            final HtmlDiv offerLeftBottomColumn = new HtmlDiv("offer_left_bottom_column");
            {
                offerLeftBottomColumn.add(generatePopularityBlock());
            }
            offerBottomBlock.add(offerLeftBottomColumn);

            final HtmlDiv offerRightBottomColumn = new HtmlDiv("offer_right_bottom_column");
            {
                // Lots
                final PageIterable<Milestone> lots = offer.getMilestones();
                if (lots.size() == 1) {
                    final Milestone lot = lots.iterator().next();

                    final HtmlParagraph datePara = new HtmlParagraph();
                    datePara.setCssClass("offer_block_para");
                    {
                        final HtmlSpan dateLabel = new HtmlSpan("offer_block_label");
                        dateLabel.addText(tr("Delivery Date: "));
                        datePara.add(dateLabel);

                        final HtmlSpan date = new HtmlSpan("offer_block_date");
                        date.addText(Context.getLocalizator().getDate(lot.getExpirationDate()).toString(FormatStyle.MEDIUM));
                        datePara.add(date);
                    }
                    offerRightBottomColumn.add(datePara);

                    final HtmlParagraph description = new HtmlParagraph();
                    {
                        final HtmlSpan descriptionLabel = new HtmlSpan("offer_block_label");
                        descriptionLabel.addText(tr("Offer's description: "));
                        description.add(descriptionLabel);
                        description.add(new XmlText("<br />"));
                        description.addText(lot.getDescription());
                    }
                    offerRightBottomColumn.add(description);

                    generateAddReleaseLink(lot, offerRightBottomColumn, userToken);

                    generateReleaseList(lot, offerRightBottomColumn);

                    // Validation details
                    generateValidationDetails(lot, offerRightBottomColumn);

                } else {
                    int i = 0;
                    for (final Milestone lot : lots) {
                        i++;
                        final HtmlDiv lotBlock = new HtmlDiv("offer_lot_block");
                        {
                            final HtmlDiv offerLotPriceBlock = new HtmlDiv("offer_price_block");
                            {
                                final HtmlSpan priceLabel = new HtmlSpan("offer_block_label");
                                priceLabel.addText(tr("Price: "));
                                offerLotPriceBlock.add(priceLabel);

                                final HtmlSpan price = new HtmlSpan("offer_block_price_lot");
                                price.addText(Context.getLocalizator().getCurrency(lot.getAmount()).getSimpleEuroString());
                                offerLotPriceBlock.add(price);
                            }
                            lotBlock.add(offerLotPriceBlock);

                            final HtmlTitle lotTitle = new HtmlTitle(tr("Lot {0} - ", i) + getLotState(lot), 2);
                            lotBlock.add(lotTitle);

                            final HtmlParagraph datePara = new HtmlParagraph();
                            datePara.setCssClass("offer_block_para");
                            {
                                final HtmlSpan dateLabel = new HtmlSpan("offer_block_label");
                                dateLabel.addText(tr("Delivery Date: "));
                                datePara.add(dateLabel);

                                final HtmlSpan date = new HtmlSpan("offer_block_date");
                                date.addText(Context.getLocalizator().getDate(lot.getExpirationDate()).toString(FormatStyle.MEDIUM));
                                datePara.add(date);
                            }
                            lotBlock.add(datePara);

                            final HtmlParagraph description = new HtmlParagraph();
                            description.addText(lot.getDescription());
                            lotBlock.add(description);

                            generateAddReleaseLink(lot, lotBlock, userToken);

                            generateReleaseList(lot, lotBlock);

                            // Validation details
                            generateValidationDetails(lot, lotBlock);
                        }
                        offerRightBottomColumn.add(lotBlock);
                    }
                }

            }
            offerBottomBlock.add(offerRightBottomColumn);
        }
        add(offerBottomBlock);

    }

    private void generateReleaseList(final Milestone lot, final HtmlDiv lotBlock) {
        final PageIterable<Release> releases = lot.getReleases();
        if (releases.size() > 0) {
            lotBlock.add(new HtmlParagraph(tr("Releases:")));
            final HtmlList list = new HtmlList();
            for (final Release release : releases) {
                final String date = Context.getLocalizator().getDate(release.getCreationDate()).toString(FormatStyle.MEDIUM);
                final HtmlLink releaseLink = new ReleasePageUrl(release).getHtmlLink(release.getVersion());
                list.add(new HtmlListItem(new HtmlMixedText("<0::> (<1::>)", releaseLink, new HtmlSpan().addText(date))));
            }
            lotBlock.add(list);
        }
    }

    private void generateValidationDetails(final Milestone lot, final HtmlDiv lotBlock) {

        final JsShowHide showHideValidationDetails = new JsShowHide(false);
        showHideValidationDetails.setHasFallback(false);

        final HtmlParagraph showHideLink = new HtmlParagraph(tr("show validation details"));
        showHideLink.setCssClass("fake_link");
        showHideValidationDetails.addActuator(showHideLink);
        lotBlock.add(showHideLink);

        final HtmlDiv validationDetailsDiv = new HtmlDiv();

        final HtmlDefineParagraph timeBeforeValidationPara = new HtmlDefineParagraph(tr("Minimun time before validation: "),
                                                                                     new TimeRenderer(lot.getSecondBeforeValidation()
                                                                                             * DateUtils.MILLISECOND_PER_SECOND).renderRange(TimeBase.DAY,
                                                                                                                                             FormatStyle.MEDIUM));
        validationDetailsDiv.add(timeBeforeValidationPara);

        final HtmlDefineParagraph fatalBugPourcentPara = new HtmlDefineParagraph(tr("Payment when no fatal bug: "),
                                                                                 String.valueOf(lot.getFatalBugsPercent()) + "%");
        validationDetailsDiv.add(fatalBugPourcentPara);

        final HtmlDefineParagraph majorBugPourcentPara = new HtmlDefineParagraph(tr("Payment when no fatal bug: "),
                                                                                 String.valueOf(lot.getMajorBugsPercent()) + "%");
        validationDetailsDiv.add(majorBugPourcentPara);

        final HtmlDefineParagraph minorBugPourcentPara = new HtmlDefineParagraph(tr("Payment when no minor bug: "),
                                                                                 String.valueOf(lot.getMinorBugsPercent()) + "%");
        validationDetailsDiv.add(minorBugPourcentPara);

        lotBlock.add(validationDetailsDiv);

        showHideValidationDetails.addListener(validationDetailsDiv);
        showHideValidationDetails.apply();
    }

    private void generateAddReleaseLink(final Milestone lot, final HtmlDiv lotBlock, final ElveosUserToken userToken) {
        if (isDeveloper(userToken) && (lot.getMilestoneState() == MilestoneState.DEVELOPING || lot.getMilestoneState() == MilestoneState.UAT)) {
            final HtmlLink link = new HtmlLink(new AddReleasePageUrl(lot).urlString(), tr("Add a release"));
            link.setCssClass("button");
            lotBlock.add(link);
        }
    }

    private boolean isDeveloper(final ElveosUserToken userToken) {
        if (!userToken.isAuthenticated()) {
            return false;
        }
        final Offer selectedOffer = offer.getFeature().getSelectedOffer();
        if (selectedOffer == null) {
            return false;
        }
        if (selectedOffer.getAsTeam() != null) {
            return userToken.getMember().hasModifyTeamRight(selectedOffer.getAsTeam());
        }
        if (selectedOffer.getMember().equals(userToken.getMember())) {
            return true;
        }
        return false;
    }

    private String getLotState(final Milestone lot) {
        switch (lot.getMilestoneState()) {
            case PENDING:
                return "";
            case DEVELOPING:
                return tr("Developing");
            case UAT:
                return tr("Released");
            case VALIDATED:
                return tr("Validated");
            case CANCELED:
                return tr("Canceled");
            default:
                break;
        }
        Log.web().fatal("Lot not found in getLotState ! this is an implementation bug");
        return "";
    }

    private XmlNode generatePopularityBlock() {
        final HtmlDiv offerSummaryPopularity = new HtmlDiv("offer_popularity");
        {
            final HtmlParagraph popularityText = new HtmlParagraph(tr("Popularity"), "offer_popularity_text");
            final HtmlParagraph popularityScore = new HtmlParagraph(HtmlTools.compressKarma(offer.getPopularity()), "offer_popularity_score");

            offerSummaryPopularity.add(popularityText);
            offerSummaryPopularity.add(popularityScore);

            if (!offer.getRights().isOwner()) {
                final int vote = offer.getUserVoteValue();
                if (vote == 0) {
                    final HtmlDiv offerPopularityJudge = new HtmlDiv("offer_popularity_judge");
                    {
                        // Useful
                        final PopularityVoteActionUrl usefulUrl = new PopularityVoteActionUrl(offer, true);
                        final HtmlLink usefulLink = usefulUrl.getHtmlLink("+");
                        usefulLink.setCssClass("useful");

                        // Useless
                        final PopularityVoteActionUrl uselessUrl = new PopularityVoteActionUrl(offer, false);
                        final HtmlLink uselessLink = uselessUrl.getHtmlLink("−");
                        uselessLink.setCssClass("useless");

                        offerPopularityJudge.add(usefulLink);
                        offerPopularityJudge.add(uselessLink);
                    }
                    offerSummaryPopularity.add(offerPopularityJudge);
                } else {
                    // Already voted
                    final HtmlDiv offerPopularityJudged = new HtmlDiv("offer_popularity_judged");
                    {
                        if (vote > 0) {
                            offerPopularityJudged.add(new HtmlParagraph("+" + vote, "Useful"));
                        } else {
                            offerPopularityJudged.add(new HtmlParagraph("−" + Math.abs(vote), "useless"));
                        }
                    }
                    offerSummaryPopularity.add(offerPopularityJudged);
                }
            } else {
                final HtmlDiv offerPopularityNone = new HtmlDiv("offer_popularity_none");

                offerSummaryPopularity.add(offerPopularityNone);
            }
        }
        return offerSummaryPopularity;
    }
}
