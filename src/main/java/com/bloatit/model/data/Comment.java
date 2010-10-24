package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

@Entity
public class Comment extends Kudosable {

    @Basic(optional = false)
    private String text;
    @OneToMany(mappedBy = "comment", cascade = { CascadeType.ALL })
    @OrderBy(value = "creationDate")
    private Set<Comment> children = new HashSet<Comment>(0);

    public Comment(Member member, String text) {
        super(member);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public Set<Comment> getChildren() {
        return children;
    }

    public void addChildComment(Comment comment) {
        children.add(comment);
    }

    protected Comment() {
        super();
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    @ManyToOne
    private Comment comment;

    protected void setText(String text) {
        this.text = text;
    }

    protected void setChildren(Set<Comment> children) {
        this.children = children;
    }

    protected void setComment(Comment comment) {
        this.comment = comment;
    }

    protected Comment getComment() {
        return comment;
    }
}
