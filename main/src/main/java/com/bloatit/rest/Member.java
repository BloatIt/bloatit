package com.bloatit.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.bloatit.framework.rest.RestServer.RequestMethod;
import com.bloatit.framework.rest.annotations.REST;

@XmlRootElement
public class Member {
    private String name;
    private Date age;
    private int id;

    private final List<String> messages = new ArrayList<String>() {
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

    public Member(int id, String name, Date age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    @XmlAttribute
    public String getName() {
        return name;
    }

    @XmlAttribute
    public Date getAge() {
        return age;
    }

    @XmlAttribute
    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Date age) {
        this.age = age;
    }

    public void setId(int id) {
        this.id = id;
    }

    @REST(name = "messages", method = RequestMethod.GET, params = "plop")
    public List<String> getMessages(String plop) {
        return messages;
    }

    @REST(name = "messages", method = RequestMethod.GET)
    @XmlElement
    public List<String> getMessages() {
        return messages;
    }

    @SuppressWarnings("deprecation")
    private static Members members = new Members() {
        private static final long serialVersionUID = 1L;
        {
            add(new Member(1, "Yoann", new Date(1984, 12, 19)));
            add(new Member(2, "Plop", new Date(1983, 1, 7)));
            add(new Member(3, "Fred", new Date(1980, 7, 13)));
            add(new Member(4, "Tom", new Date(1983, 11, 5)));
        }
    };

    @REST(name = "members", method = RequestMethod.GET)
    public static Members getMembers() {
        return members;
    }

    @REST(name = "members", method = RequestMethod.GET)
    public static Member getMember(int id) {
        for (Member m : members) {
            if (m.id == id) {
                return m;
            }
        }
        return null;
    }

    @REST(name = "members", method = RequestMethod.GET, params = { "name" })
    public static Member getMemberByName(String name) {
        for (Member m : members) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    @REST(name = "members", method = RequestMethod.GET, params = { "name", "plop" })
    public static Member getMemberByName(String name, String plop) {
        for (Member m : members) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

}

@XmlRootElement
@XmlSeeAlso({ Member.class })
class Members extends ArrayList<Member> {
    private static final long serialVersionUID = -6854206394315558823L;

    public Members() {
        super();
    }

    @XmlElement
    public List<Member> getMembers() {
        return this;
    }
}
