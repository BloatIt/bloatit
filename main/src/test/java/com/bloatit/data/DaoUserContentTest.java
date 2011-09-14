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
package com.bloatit.data;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bloatit.data.DaoFileMetadata.FileType;
import com.bloatit.framework.utils.i18n.Language;

public class DaoUserContentTest {

    @Test
    public void testGetAuthor() {
        assertEquals(yo, feature.getMember());
    }

    private DaoMember yo;
    private DaoMember tom;
    private DaoMember fred;
    private DaoFeature feature;

    @Before
    public void setUp() throws Exception {
        SessionManager.generateTestSessionFactory();
        SessionManager.beginWorkUnit();
        {
            tom = DaoMember.createAndPersist("Thomas", "password", "salt", "tom@gmail.com", Locale.FRANCE);
            tom.setFullname("Thomas Guyard");
            SessionManager.flush();
        }
        {
            fred = DaoMember.createAndPersist("Fred", "other", "salt", "fred@gmail.com", Locale.FRANCE);
            fred.setFullname("Frédéric Bertolus");
            SessionManager.flush();
        }
        {
            yo = DaoMember.createAndPersist("Yoann", "plop", "salt", "yo@gmail.com", Locale.FRANCE);
            yo.setFullname("Yoann Plénet");
            SessionManager.flush();

            DaoTeam.createAndPersiste("Other", "plop@plop.com", "A group description", DaoTeam.Right.PUBLIC).addMember(yo, false);
            DaoTeam.createAndPersiste("myGroup", "plop1@plop.com", "A group description", DaoTeam.Right.PUBLIC).addMember(yo, false);
            (DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoTeam.Right.PROTECTED)).addMember(yo, true);
        }

        final DaoSoftware project = DaoSoftware.createAndPersist("VLC", DaoDescription.createAndPersist(fred, null, Language.FR, "title", "descrip"));
        project.setImage(DaoFileMetadata.createAndPersist(fred, null, null, "/dev/", "null", FileType.JPG, 12));

        feature = DaoFeature.createAndPersist(yo, null, DaoDescription.createAndPersist(yo, null,
                                                                                  Language.FR,
                                                                                  "Ma super demande !",
                                                                                  "Ceci est la descption de ma demande :) "), project);

        SessionManager.endWorkUnitAndFlush();
    }

    @After
    public void tearDown() throws Exception {
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }

}
