package com.bloatit.model.data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.bloatit.model.data.util.SessionManger;

/**
 * For now the state is managed as a simple enum. But it will be necessary to
 * associate data with each state (for example the date of change from a state
 * to an other). We will have to find a way to do this.
 * 
 * @author tom
 * 
 */
// One draft is the current Demand
// The other drafts are the 
// 1 kudos for each draft.
// 1 kudos for the specif.
// 1 kudos for the Validate button
// TODO a process class. -> Different State = different bases.
@Entity
public class Demand extends UserContent {
	public enum State {
		CONSTRUCTING, VALIDATED, DEVELOPING, DEVELOPED, ACCEPTED, REJECTED;
	}

	@Basic(optional = false)
	@Enumerated
	private State state;
	@OneToOne(mappedBy = "demand", optional = false)
	private Draft currentDraft;

	// TODO sort me.
	@OneToMany(mappedBy = "demand", cascade = { CascadeType.ALL })
	private Set<Draft> drafts = new HashSet<Draft>(0);
	@OneToMany(mappedBy = "demand", cascade = { CascadeType.ALL })
	private Set<Offer> offers = new HashSet<Offer>(0);
	@OneToMany(mappedBy = "demand", cascade = { CascadeType.ALL })
	private Set<Transaction> contributions = new HashSet<Transaction>(0);

	public static Demand createAndPersist(Member author, Locale locale, String title, String description, String specification) {
		Session session = SessionManger.getSessionFactory().getCurrentSession();
		Demand demand = new Demand(author, locale, title, description, specification);
		try {
			session.save(demand);
		} catch (HibernateException e) {
			session.getTransaction().rollback();
			throw e;
		}
		return demand;
	}

	protected Demand() {
		super();
	}

	public Demand(Member author, Locale locale, String title, String description, String specification) {
		super(author);
		state = State.CONSTRUCTING;
		currentDraft = new Draft(author, this, locale, title, description, specification);
		drafts.add(currentDraft);
	}

	// TODO find or create a type to throw when the state is wrong.
	public void addDraft(Member author, Locale locale, String title, String description, String specification) throws Throwable {
		if (state != State.CONSTRUCTING) {
			throw (new Throwable("Demand is no longuer in construction mode."));
		}
		drafts.add(new Draft(author, this, locale, title, description, specification));
	}

	public void addOffer() {
		// TODO
	}

	public void addContribution(Member member, BigDecimal amount) throws Throwable {
		if (amount.compareTo(new BigDecimal("0")) <= 0) {
			throw (new Throwable("Amount must be non null and positive."));
		}
		contributions.add(new Transaction(member, amount));
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getState() {
		return state;
	}

	public Draft getCurrentDraft() {
		return currentDraft;
	}

	public Set<Draft> getDrafts() {
		return drafts;
	}

	public Set<Offer> getOffers() {
		return offers;
	}

	public Set<Transaction> getContributions() {
		return contributions;
	}

	// ======================================================================
	// For hibernate mapping
	// ======================================================================

	protected void setCurrentDraft(Draft currentDraft) {
		this.currentDraft = currentDraft;
	}

	protected void setDrafts(Set<Draft> drafts) {
		this.drafts = drafts;
	}

	protected void setOffers(Set<Offer> offers) {
		this.offers = offers;
	}

	protected void setContributions(Set<Transaction> contributions) {
		this.contributions = contributions;
	}
}
