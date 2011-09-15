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

import java.util.Locale;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.bloatit.data.DaoFileMetadata.FileType;
import com.bloatit.data.exceptions.UniqueNameExpectedException;
import com.bloatit.framework.utils.i18n.Language;

public class DataTestUnit {

    protected static DaoMember tom;
    protected static DaoMember fred;
    protected static DaoMember yo;
    protected static DaoTeam other;
    protected static DaoTeam myGroup;
    protected static DaoTeam b219;
    protected static DaoSoftware project;
    
    @BeforeClass
    public static void createDB() throws UniqueNameExpectedException {
        SessionManager.generateTestSessionFactory();
        SessionManager.beginWorkUnit();
        tom = DaoMember.createAndPersist("Thomas", "password", "salt", "tom@gmail.com", Locale.FRANCE);
        tom.setFullname("Thomas Guyard");
        fred = DaoMember.createAndPersist("Fred", "other", "salt", "fred@gmail.com", Locale.FRANCE);
        fred.setFullname("Frédéric Bertolus");
        yo = DaoMember.createAndPersist("Yoann", "plop", "salt", "yo@gmail.com", Locale.FRANCE);
        yo.setFullname("Yoann Plénet");

        other = DaoTeam.createAndPersiste("Other", "plop@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        other.addMember(yo, false);
        myGroup = DaoTeam.createAndPersiste("myGroup", "plop1@plop.com", "A group description", DaoTeam.Right.PUBLIC);
        myGroup.addMember(yo, false);
        b219 = DaoTeam.createAndPersiste("b219", "plop2@plop.com", "A group description", DaoTeam.Right.PROTECTED);
        b219.addMember(yo, true);

        project = DaoSoftware.createAndPersist("VLC", DaoDescription.createAndPersist(fred, null, Language.FR, "title", "descrip"));
        project.setImage(DaoFileMetadata.createAndPersist(fred, null, null, "/dev/", "null", FileType.JPG, 12));
        SessionManager.endWorkUnitAndFlush();
    }
    
    @AfterClass
    public static void closeDB() {
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.endWorkUnitAndFlush();
        }
        SessionManager.getSessionFactory().close();
    }
    
    @Before
    public void setUp() throws Exception {
        SessionManager.beginWorkUnit();
    }

    @After
    public void tearDown() throws Exception {
        if (SessionManager.getSessionFactory().getCurrentSession().getTransaction().isActive()) {
            SessionManager.rollback();
        }
    }
}
