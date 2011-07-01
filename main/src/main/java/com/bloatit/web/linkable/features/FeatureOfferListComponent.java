/*
 * Copyright (C) 2010 BloatIt. This file is part of BloatIt. BloatIt is free
 * software: you can redistribute it and/or modify it under the terms of the GNU
 * Affero General Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * BloatIt is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details. You should have received a copy of the GNU Affero General Public
 * License along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */
package com.bloatit.web.linkable.features;

import static com.bloatit.framework.webprocessor.context.Context.tr;
import static com.bloatit.framework.webprocessor.context.Context.trn;

import java.math.BigDecimal;

import com.bloatit.data.queries.EmptyPageIterable;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.datetime.DateUtils;
import com.bloatit.framework.utils.datetime.TimeRenderer;
import com.bloatit.framework.utils.i18n.CurrencyLocale;
import com.bloatit.framework.webprocessor.components.HtmlDiv;
import com.bloatit.framework.webprocessor.components.HtmlLink;
import com.bloatit.framework.webprocessor.components.HtmlParagraph;
import com.bloatit.framework.webprocessor.components.HtmlSpan;
import com.bloatit.framework.webprocessor.components.HtmlTitle;
import com.bloatit.framework.webprocessor.components.PlaceHolderElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlElement;
import com.bloatit.framework.webprocessor.components.meta.HtmlMixedText;
import com.bloatit.framework.webprocessor.context.Context;
import com.bloatit.model.ElveosUserToken;
import com.bloatit.model.Feature;
import com.bloatit.model.Offer;
import com.bloatit.web.url.MakeOfferPageUrl;

public class FeatureOfferListComponent extends HtmlDiv {
    protected FeatureOfferListComponent(final Feature feature, ElveosUserToken userToken) {
        super();
        PageIterable<Offer> offers = new EmptyPageIterable<Offer>();
        offers = feature.getOffers();
        int nbUnselected = offers.size();

        final Offer selectedOffer = feature.getSelectedOffer();
        if (selectedOffer != null) {
            nbUnselected--;
        }

        final HtmlDiv offersBlock = new HtmlDiv("offers_block");

        switch (feature.getFeatureState()) {
            case PENDING: {
                if (nbUnselected > 0) {
                    offersBlock.add(new HtmlTitle(tr("No selected offer"), 1));
                } else {
                    offersBlock.add(new HtmlTitle(tr("No offer"), 1));
                }

                final BicolumnOfferBlock block = new BicolumnOfferBlock(true);
                offersBlock.add(block);
                block.addInLeftColumn(new HtmlParagraph(tr("There is not yet offer to develop this feature. The first offer will be selected by default.")));

                final HtmlLink link = new MakeOfferPageUrl(feature).getHtmlLink(tr("Make an offer"));
                link.setCssClass("button");

                final HtmlDiv noOffer = new HtmlDiv("no_offer_block");
                {
                    noOffer.add(link);
                }

                if (nbUnselected > 0) {
                    generateUnselectedOfferList(feature, userToken, offers, nbUnselected, selectedOffer, offersBlock);
                }

                block.addInRightColumn(noOffer);
            }

                break;
            case PREPARING: {
                offersBlock.add(new HtmlTitle(tr("Selected offer"), 1));

                // Selected
                final BicolumnOfferBlock block = new BicolumnOfferBlock(true);
                offersBlock.add(block);

                // Generating the left column
                block.addInLeftColumn(new HtmlParagraph(tr("The selected offer is the one with the more popularity.")));
                if (selectedOffer != null) {
                    if (feature.getValidationDate() != null && DateUtils.isInTheFuture(feature.getValidationDate())) {
                        final TimeRenderer renderer = new TimeRenderer(DateUtils.elapsed(DateUtils.now(), feature.getValidationDate()));

                        final BigDecimal amountLeft = selectedOffer.getAmount().subtract(feature.getContribution());

                        if (amountLeft.compareTo(BigDecimal.ZERO) > 0) {
                            final CurrencyLocale currency = Context.getLocalizator().getCurrency(amountLeft);
                            final HtmlSpan timeSpan = new HtmlSpan("bold");
                            timeSpan.addText(renderer.getTimeString());
                            final HtmlMixedText timeToValid = new HtmlMixedText(tr("This offer will be validated in about <0::>. After this time, the offer will go into development as soon as the requested amount is available ({0} left).",
                                                                                   currency.getSimpleEuroString()),
                                                                                timeSpan);
                            final HtmlParagraph element = new HtmlParagraph(timeToValid);
                            block.addInLeftColumn(element);
                        } else {
                            final HtmlSpan timeSpan = new HtmlSpan("bold");
                            timeSpan.addText(renderer.getTimeString());
                            final HtmlMixedText timeToValid = new HtmlMixedText(Context.tr("This offer will go into development in about <0::>."),
                                                                                timeSpan);
                            block.addInLeftColumn(timeToValid);
                        }
                    } else {
                        final BigDecimal amountLeft = feature.getSelectedOffer().getAmount().subtract(feature.getContribution());
                        final CurrencyLocale currency = Context.getLocalizator().getCurrency(amountLeft);
                        block.addInLeftColumn(new HtmlParagraph(tr("This offer is validated and will go into development as soon as the requested amount is available ({0} left).",
                                                                   currency.toString())));
                    }
                    // Generating the right column
                    block.addInRightColumn(new OfferBlock(selectedOffer, true, userToken));
                }

                generateUnselectedOfferList(feature, userToken, offers, nbUnselected, selectedOffer, offersBlock);
                break;
            }
            case DEVELOPPING:
                final BicolumnOfferBlock block;
                offersBlock.add(new HtmlTitle(tr("Offer in development"), 1));
                offersBlock.add(block = new BicolumnOfferBlock(true));
                block.addInLeftColumn(new HtmlParagraph(tr("This offer is in development. You can discuss about it in the comments.")));
                if (selectedOffer != null && selectedOffer.hasRelease()) {
                    block.addInLeftColumn(new HtmlParagraph(tr("Test the last release and report bugs.")));
                }
                block.addInRightColumn(new OfferBlock(selectedOffer, true, userToken));

                generateOldOffersList(offers, nbUnselected, selectedOffer, offersBlock, userToken);

                break;
            case FINISHED:
                offersBlock.add(new HtmlTitle(tr("Finished offer"), 1));
                offersBlock.add(block = new BicolumnOfferBlock(true));
                block.addInLeftColumn(new HtmlParagraph(tr("This offer is finished.")));
                block.addInRightColumn(new OfferBlock(selectedOffer, true, userToken));

                generateOldOffersList(offers, nbUnselected, selectedOffer, offersBlock, userToken);
                break;
            case DISCARDED:
                offersBlock.add(new HtmlTitle(tr("Feature discarded ..."), 1));
                break;
            default:
                break;
        }
        add(offersBlock);
    }

    private void generateUnselectedOfferList(final Feature feature,
                                             ElveosUserToken userToken,
                                             PageIterable<Offer> offers,
                                             int nbUnselected,
                                             final Offer selectedOffer,
                                             final HtmlDiv offersBlock) {
        // UnSelected
        offersBlock.add(new HtmlTitle(trn("Unselected offer ({0})", "Unselected offers ({0})", nbUnselected, nbUnselected), 1));
        final BicolumnOfferBlock unselectedBlock = new BicolumnOfferBlock(true);
        offersBlock.add(unselectedBlock);
        unselectedBlock.addInLeftColumn(new MakeOfferPageUrl(feature).getHtmlLink(tr("Make a concurrent offer")));
        unselectedBlock.addInLeftColumn(new HtmlParagraph(tr("Vote on a competing offer to select it.")));

        for (final Offer offer : offers) {
            if (offer != selectedOffer) {
                unselectedBlock.addInRightColumn(new OfferBlock(offer, false, userToken));
            }
        }
    }

    private void generateOldOffersList(final PageIterable<Offer> offers,
                                       final int nbUnselected,
                                       final Offer selectedOffer,
                                       final HtmlDiv offersBlock,
                                       ElveosUserToken userToken) {
        // UnSelected
        offersBlock.add(new HtmlTitle(trn("Old offer ({0})", "Old offers ({0})", nbUnselected, nbUnselected), 1));
        final BicolumnOfferBlock unselectedBlock = new BicolumnOfferBlock(true);
        offersBlock.add(unselectedBlock);
        unselectedBlock.addInLeftColumn(new HtmlParagraph(tr("These offers have not been selected and will never be developed.")));

        for (final Offer offer : offers) {
            if (offer != selectedOffer) {
                unselectedBlock.addInRightColumn(new OfferBlock(offer, false, userToken));
            }
        }
    }

    private static class BicolumnOfferBlock extends HtmlDiv {

        private final PlaceHolderElement leftColumn = new PlaceHolderElement();
        private final PlaceHolderElement rightColumn = new PlaceHolderElement();

        public BicolumnOfferBlock(final boolean isSelected) {
            super("offer_type_block");
            HtmlDiv divSelected;
            if (isSelected) {
                divSelected = new HtmlDiv("offer_selected_description");
            } else {
                divSelected = new HtmlDiv("offer_unselected_description");
            }
            add(new HtmlDiv("offer_type_left_column").add(divSelected.add(leftColumn)));
            add(new HtmlDiv("offer_type_right_column").add(rightColumn));
        }

        public void addInLeftColumn(final HtmlElement elment) {
            leftColumn.add(elment);
        }

        public void addInRightColumn(final HtmlElement elment) {
            rightColumn.add(elment);
        }
    }
}
