package com.bloatit.model.data;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.util.HibernateUtil;

@Entity
public class Draft extends UserContent {
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private Demand demand;
	@OneToOne(optional = false)
	private LocalizedText title; //  TODO goto demand
	@OneToOne(optional = false)
	private LocalizedText description; //  TODO goto demand
	@OneToOne(optional = false)
	private LocalizedText specification;
	@OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, mappedBy = "draft")
	private Set<Comment> comments = new HashSet<Comment>(0);

	protected Draft() {
		super();
	}

	public static Draft createAndPersist(Member member, Demand demand, Locale locale, String title, String description, String specification){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Draft draft = new Draft(member, demand, locale, title, description, specification);
		try {
			session.save(draft);
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return draft;
	}
	
	public Draft(Member member, Demand demand, Locale locale, String title, String description, String specification) {
		super(member);
		this.demand = demand;
		this.title = LocalizedText.createAndPersist(locale, title);
		this.description = LocalizedText.createAndPersist(locale, description);
		this.specification = LocalizedText.createAndPersist(locale, specification);
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
