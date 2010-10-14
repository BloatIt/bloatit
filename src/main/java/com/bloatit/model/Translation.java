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

public class Translation implements Kudoable {
    private final Language language;
    private final String text;
    private Member author;
    private long karma;

    public Translation(Language language, String text, Member author, long karma){
        this.language = language;
        this.text = text;
        this.author = author;
        this.karma = karma;
    }

    public Language getLanguage(){
        return this.language;
    }

    public String getText(){
        return this.text;
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