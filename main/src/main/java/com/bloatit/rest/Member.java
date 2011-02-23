package com.bloatit.rest;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class Member {
    private String name;
    private Calendar birth;
    private int id;

    private final MarshableList<String> messages = new MarshableList<String>() {
        private static final long serialVersionUID = -2913090876724284523L;
        {
            add("message 1");
            add("message 2");
            add("message 3");
            add("message 4");
        }
    };

    protected Member() {
        // Default constructor for XML generation
    }

    public Member(final int id, final String name, final Calendar birth) {
        this.id = id;
        this.name = name;
        this.birth = birth;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    @XmlAttribute
    public Calendar getBirth() {
        return birth;
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setBirth(final Calendar birth) {
        this.birth = birth;
    }

    public void setId(final int id) {
        this.id = id;
    }

    @XmlElementWrapper(name = "messages")
    @REST(name = "messages", method = RequestMethod.GET)
    @XmlElement(name = "message")
    public MarshableList<String> getMessages() {
        return messages;
    }

    private static Members members = new Members() {
        private static final long serialVersionUID = 1L;
        {
            add(new Member(1, "Yoann", new GregorianCalendar(1984, 12, 19)));
            add(new Member(2, "Plop", new GregorianCalendar(1983, 1, 7)));
            add(new Member(3, "Fred", new GregorianCalendar(1980, 7, 13)));
            add(new Member(4, "Tom", new GregorianCalendar(1983, 11, 5)));
        }
    };

    @REST(name = "members", method = RequestMethod.GET)
    public static Members getMembers() {
        return members;
    }

    @REST(name = "members", method = RequestMethod.GET)
    public static Member getMember(final int id) {
        for (final Member m : members) {
            if (m.id == id) {
                return m;
            }
        }
        return null;
    }

    @REST(name = "members", method = RequestMethod.GET, params = { "name" })
    public static Member getMemberByName(final String name) {
        for (final Member m : members) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    @REST(name = "members", method = RequestMethod.GET, params = { "name", "plop" })
    public static Member getMemberByName(final String name, final String plop) {
        for (final Member m : members) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

}
