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
	@OneToMany(mappedBy="comment")
	private Set<Comment> children = new HashSet<Comment>(0);
	
	// For hibernate mapping 
	@ManyToOne
	private Comment comment;
	@ManyToOne
	private Draft draft;

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
}
