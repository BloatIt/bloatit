package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@MappedSuperclass
public class Comment extends Kudosable {

	@ManyToOne(optional = false)
	private Draft commentedDraft;
	@OneToOne(optional = false)
	private LocalizedText text;
	@OneToMany(mappedBy = "comment")
	private Set<Comment> children = new HashSet<Comment>(0);

	public Comment(Draft commentedDraft, Member member, LocalizedText text) {
		super(member);
		this.commentedDraft = commentedDraft;
		this.text = text;
	}

	public Draft getCommentedDraft() {
		return commentedDraft;
	}

	public LocalizedText getText() {
		return text;
	}

	public Set<Comment> getChildren() {
		return children;
	}

	public void addChildComment(Member member, LocalizedText text) {
		children.add(new Comment(null, member, text));
	}

	protected Comment() {
		super();
	}
	
	// ======================================================================
	// For hibernate mapping
	// ======================================================================
	
	@ManyToOne
	private Comment comment;
	@ManyToOne
	private Draft draft;
	protected Comment getComment() {
    	return comment;
    }

	protected void setComment(Comment comment) {
    	this.comment = comment;
    }

	protected Draft getDraft() {
    	return draft;
    }

	protected void setDraft(Draft draft) {
    	this.draft = draft;
    }

	protected void setCommentedDraft(Draft commentedDraft) {
    	this.commentedDraft = commentedDraft;
    }

	protected void setText(LocalizedText text) {
    	this.text = text;
    }

	protected void setChildren(Set<Comment> children) {
    	this.children = children;
    }
}
