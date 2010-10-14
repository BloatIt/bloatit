package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
@MappedSuperclass
public class Draft extends UserContent {
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private Demand demand;
	@OneToOne(optional = false)
	private LocalizedText title;
	@OneToOne(optional = false)
	private LocalizedText description;
	@OneToOne(optional = false)
	private LocalizedText specification;
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "draft")
	private Set<Comment> comments = new HashSet<Comment>(0);

	protected Draft() {
		super();
	}

	public Draft(Member member, Demand demand, LocalizedText title, LocalizedText description, LocalizedText specification) {
		super(member);
		this.demand = demand;
		this.title = title;
		this.description = description;
		this.specification = specification;
	}

	public Demand getDemand() {
		return demand;
	}

	public LocalizedText getTitle() {
		return title;
	}

	public LocalizedText getDescription() {
		return description;
	}

	public LocalizedText getSpecification() {
		return specification;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void addComment(Member member, LocalizedText text) {
		comments.add(new Comment(this, member, text));
	}

	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected void setDemand(Demand demand) {
		this.demand = demand;
	}

	protected void setTitle(LocalizedText title) {
		this.title = title;
	}

	protected void setDescription(LocalizedText description) {
		this.description = description;
	}

	protected void setSpecification(LocalizedText specification) {
		this.specification = specification;
	}

	protected void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

}
