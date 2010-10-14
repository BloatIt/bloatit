/*
 * Copyright (C) 2010 BloatIt.
 *
 * This file is part of BloatIt.
 *
 * BloatIt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BloatIt is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with BloatIt. If not, see <http://www.gnu.org/licenses/>.
 */

package com.bloatit.model;

import com.bloatit.web.server.Language;

/**
 * The version of a text for a given language
 */
public class Translation implements Kudoable {
    private final Language language;
    private Member author;
    private long karma;

    private String entry;

    /**
     * Creates a new translation
     * @param entry The element translated into the new language
     * @param language The language of the translation
     * @param author The author of the translation
     * @param karma The karma of the translation
     */
    public Translation(String entry, Language language, Member author, long karma){
        this.entry = entry;
        this.language = language;
        this.author = author;
        this.karma = karma;
    }

    public Language getLanguage(){
        return this.language;
    }

    /**
     * @return the text for this given language
     */
    public String getEntry(){
        return this.entry;
    }

    @Override
    public void kudo(Member kudoer){
        this.karma += (long)(Math.log(kudoer.getKarma())/Math.log(2));
        this.author.receiveKudo(kudoer);
        kudoer.issueKudo();
    }

    @Override
    public void report(Member reporter){
        this.karma -= (long)(Math.log(reporter.getKarma())/Math.log(2));
        this.author.receiveReport(reporter);
        reporter.issueReport();
    }

    @Override
    public long getReputation(){
        return this.karma;
    }

    @Override
    public Member getAuthor(){
        return this.author;
    }

}