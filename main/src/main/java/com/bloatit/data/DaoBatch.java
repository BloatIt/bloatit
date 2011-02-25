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

import java.math.BigDecimal;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.Query;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.search.annotations.DateBridge;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Resolution;
import org.hibernate.search.annotations.Store;

import com.bloatit.data.DaoBug.BugState;
import com.bloatit.data.DaoBug.Level;
import com.bloatit.data.queries.QueryCollection;
import com.bloatit.framework.exceptions.FatalErrorException;
import com.bloatit.framework.exceptions.NonOptionalParameterException;
import com.bloatit.framework.utils.PageIterable;

/**
 * A DaoBatch is a part of a DaoOffer.
 * 
 * @author Thomas Guyard
 */
@Entity
public final class DaoBatch extends DaoIdentifiable {

    public enum BatchState {
        PENDING, DEVELOPING, UAT, VALIDATED, CANCELED
    }

    /**
     * After this date, the Batch should be done.
     */
    @Basic(optional = false)
    @Field(index = Index.UN_TOKENIZED, store = Store.YES)
    @DateBridge(resolution = Resolution.DAY)
    @Column(updatable = false)
    private Date expirationDate;

    @Basic(optional = false)
    @Column(updatable = false)
    private int secondBeforeValidation;

    @Basic(optional = false)
    @Column(updatable = false)
    private int fatalBugsPercent;

    @Basic(optional = false)
    @Column(updatable = false)
    private int majorBugsPercent;

    /**
     * The amount represents the money the member want to have to make his
     * offer.
     */
    @Basic(optional = false)
    @Column(updatable = false)
    // @Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
    private BigDecimal amount;

    // nullable.
    @Enumerated
    private Level levelToValidate;

    @Basic(optional = false)
    private BatchState batchState;

    /**
     * Remember a description is a title with some content. (Translatable)
     */
    @ManyToOne
    @Cascade(value = { CascadeType.ALL })
    @IndexedEmbedded
    private DaoDescription description;

    @OneToMany(mappedBy = "batch")
    @Cascade(value = { CascadeType.ALL })
    private final Set<DaoBug> bugs = new HashSet<DaoBug>();

    @OneToMany(mappedBy = "batch")
    @Cascade(value = { CascadeType.ALL })
    private final Set<DaoRelease> releases = new HashSet<DaoRelease>();

    @ManyToOne(optional = false)
    private DaoOffer offer;

    // ======================================================================
    // Construction.
    // ======================================================================

    /**
     * Create a DaoBatch.
     * 
     * @param amount is the amount of the offer. Must be non null, and > 0.
     * @param text is the description of the demand. Must be non null.
     * @param dateExpire is the date when this offer should be finish. Must be
     *            non null, and in the future.
     * @param secondBeforeValidation TODO
     * @throws NonOptionalParameterException if a parameter is null.
     * @throws FatalErrorException if the amount is < 0 or if the Date is in the
     *             future.
     */
    public DaoBatch(final Date dateExpire,
                    final BigDecimal amount,
                    final DaoDescription description,
                    final DaoOffer offer,
                    final int secondBeforeValidation) {
        super();
        if (dateExpire == null || amount == null || description == null || offer == null) {
            throw new NonOptionalParameterException();
        }
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new FatalErrorException("Amount must be > 0");
        }
        if (dateExpire.before(new Date())) {
            throw new FatalErrorException("Make sure the date is in the future.");
        }
        this.expirationDate = (Date) dateExpire.clone();
        this.amount = amount;
        this.description = description;
        this.offer = offer;
        this.secondBeforeValidation = secondBeforeValidation;
        this.levelToValidate = Level.FATAL;
        this.fatalBugsPercent = 100;
        this.majorBugsPercent = 0;
        this.batchState = BatchState.PENDING;
    }

    /**
     * Set the percent of money the developer will received when all bugs of one
     * level are closed. This method take parameters for the Fatal and Major
     * level. The Minor level is calculated from it (see
     * {@link #getMinorBugsPercent()}).
     * 
     * @param fatalPercent is the percent of the money the developer will get
     *            when all the {@link Level#FATAL} bugs are closed. It must be
     *            >= 0 and <= 100.
     * @param majorPercent is the percent of the money the developer will get
     *            when all the {@link Level#MAJOR} bugs are closed. It must be
     *            >= 0 and <= 100.
     */
    public void updateMajorFatalPercent(final int fatalPercent, final int majorPercent) {
        if (fatalPercent < 0 || majorPercent < 0) {
            throw new FatalErrorException("The parameters must be percents !");
        }
        if ((fatalPercent + majorPercent) > 100) {
            throw new FatalErrorException("The sum of the two percent parameters is > 100 !");
        }
        this.fatalBugsPercent = fatalPercent;
        this.majorBugsPercent = majorPercent;
    }

    public void setDeveloping() {
        batchState = BatchState.DEVELOPING;
    }

    public void addRelease(final DaoRelease release) {
        releases.add(release);
        if (batchState == BatchState.DEVELOPING) {
            batchState = BatchState.UAT;
        }
        getOffer().batchHasARelease(this);
    }

    public void addBug(final DaoBug bug) {
        bugs.add(bug);
    }

    /**
     * Tells that the Income state of this batch is finished, and everything is
     * OK. The validation can be partial (when some major or minor bugs are
     * open). The validate method may also validate nothing if some FATAL bugs
     * are open, or if the validation period is not open. You can change this
     * behavior using the <code>force</code> parameter. The force parameter
     * allows to validate the batch without taking into account these previous
     * restrictions.
     * 
     * @param force force the validation of this batch. Do not take care of the
     *            bugs and the timeOuts.
     * @return true if all parts of this batch is validated.
     */
    public boolean validate(final boolean force) {
        //
        // Calculate the real percent (= percent of this batch * percent of this
        // level).
        final int batchPercent = offer.getBatchPercent(this);
        final int fatalPercent = (batchPercent * fatalBugsPercent) / 100;
        final int majorPercent = (batchPercent * majorBugsPercent) / 100;
        final int minorPercent = batchPercent - majorPercent - fatalPercent;

        //
        // Do the validation
        //
        if (levelToValidate == Level.FATAL && (force || shouldValidatePart(Level.FATAL))) {
            levelToValidate = Level.MAJOR;
            offer.getDemand().validateContributions(fatalPercent);
        }
        // if fatalBugPercent == 100, there is nothing left to validate so it is
        // automatically validated.
        if (levelToValidate == Level.MAJOR && (force || shouldValidatePart(Level.MAJOR) || fatalBugsPercent == 100)) {
            levelToValidate = Level.MINOR;
            offer.getDemand().validateContributions(majorPercent);
        }
        // when minorBugPercent == 0, there is nothing left to validate so it is
        // automatically validated.
        if (levelToValidate == Level.MINOR && (force || shouldValidatePart(Level.MINOR) || getMinorBugsPercent() == 0)) {
            levelToValidate = null;
            offer.getDemand().validateContributions(minorPercent);
        }
        if (levelToValidate == null) {
            batchState = BatchState.VALIDATED;
            offer.passToNextBatch();
            return true;
        }
        return false;
    }

    /**
     * You can validate a batch after its release and when the bugs requirement
     * are done.
     * 
     * @return true if an admin should validate this Batch part. False
     *         otherwise.
     */
    public boolean shouldValidatePart(final Level level) {
        if (validationPeriodFinished() && getNonResolvedBugs(level).size() == 0) {
            return true;
        }
        return false;
    }

    public boolean partIsValidated(final Level level) {
        return this.levelToValidate == null || !EnumSet.range(levelToValidate, Level.MINOR).contains(level);
    }

    private boolean validationPeriodFinished() {
        final Date releasedDate = getReleasedDate();
        if (releasedDate == null) {
            return false;
        }
        return new Date(releasedDate.getTime() + ((long) secondBeforeValidation) * 1000).before(new Date());
    }

    public void cancelBatch() {
        batchState = BatchState.CANCELED;
    }

    // ======================================================================
    // Getters.
    // ======================================================================

    public PageIterable<DaoBug> getNonResolvedBugs(final Level level) {
        final org.hibernate.classic.Session currentSession = SessionManager.getSessionFactory().getCurrentSession();
        final Query filteredBugs = currentSession.createFilter(bugs, "where errorLevel = :level and state!=:state")
                                                 .setParameter("level", level)
                                                 .setParameter("state", BugState.RESOLVED);
        final Query filteredBugsSize = currentSession.createFilter(bugs, "select count (*) where errorLevel = :level and state!=:state")
                                                     .setParameter("level", level)
                                                     .setParameter("state", BugState.RESOLVED);
        return new QueryCollection<DaoBug>(filteredBugs, filteredBugsSize);
    }

    public PageIterable<DaoBug> getBugs(final Level level) {
        final Query filteredBugs = SessionManager.createFilter(bugs, "where errorLevel = :level").setParameter("level", level);
        final Query filteredBugsSize = SessionManager.createFilter(bugs, "select count (*) where errorLevel = :level").setParameter("level", level);
        return new QueryCollection<DaoBug>(filteredBugs, filteredBugsSize);
    }

    public PageIterable<DaoBug> getBugs(final BugState state) {
        final Query filteredBugs = SessionManager.createFilter(bugs, "where state = :state").setParameter("state", state);
        final Query filteredBugsSize = SessionManager.createFilter(bugs, "select count (*) where state = :state").setParameter("state", state);
        return new QueryCollection<DaoBug>(filteredBugs, filteredBugsSize);
    }

    public PageIterable<DaoBug> getBugs(final Level level, final BugState state) {
        final Query filteredBugs = SessionManager.createFilter(bugs, "where errorLevel = :level and state = :state");
        final Query filteredBugsSize = SessionManager.createFilter(bugs, "select count (*) where errorLevel = :level and state = :state");
        return new QueryCollection<DaoBug>(filteredBugs, filteredBugsSize).setParameter("level", level).setParameter("state", state);
    }

    public PageIterable<DaoRelease> getReleases() {
        final Query filteredBugs = SessionManager.createFilter(releases, "order by creationDate DESC");
        final Query filteredBugsSize = SessionManager.createFilter(releases, "select count (*)");
        return new QueryCollection<DaoRelease>(filteredBugs, filteredBugsSize);
    }

    public Date getExpirationDate() {
        return (Date) expirationDate.clone();
    }

    public BatchState getBatchState() {
        return batchState;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public DaoDescription getDescription() {
        return description;
    }

    public DaoOffer getOffer() {
        return offer;
    }

    /**
     * @return the releaseDate
     */
    public Date getReleasedDate() {
        final Query query = SessionManager.createFilter(releases, "select max(creationDate)");
        return (Date) query.uniqueResult();
    }

    /**
     * @return the fatalBugsPercent
     */
    public int getFatalBugsPercent() {
        return fatalBugsPercent;
    }

    /**
     * @return the majorBugsPercent
     */
    public int getMajorBugsPercent() {
        return majorBugsPercent;
    }

    /**
     * @return the getMinorBugsPercent (= 100 - (majorBugsPercent +
     *         fatalBugsPercent)).
     */
    public int getMinorBugsPercent() {
        return 100 - (majorBugsPercent + fatalBugsPercent);
    }

    // ======================================================================
    // Visitor.
    // ======================================================================

    @Override
    public <ReturnType> ReturnType accept(final DataClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // ======================================================================
    // For hibernate mapping
    // ======================================================================

    protected DaoBatch() {
        super();
    }

    // ======================================================================
    // equals and hashCode.
    // ======================================================================

    /*
     * (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((amount == null) ? 0 : amount.hashCode());
        result = prime * result + ((description == null) ? 0 : description.hashCode());
        result = prime * result + ((expirationDate == null) ? 0 : expirationDate.hashCode());
        result = prime * result + fatalBugsPercent;
        result = prime * result + majorBugsPercent;
        result = prime * result + ((offer == null) ? 0 : offer.hashCode());
        result = prime * result + secondBeforeValidation;
        return result;
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DaoBatch other = (DaoBatch) obj;
        if (amount == null) {
            if (other.amount != null) {
                return false;
            }
        } else if (!amount.equals(other.amount)) {
            return false;
        }
        if (description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!description.equals(other.description)) {
            return false;
        }
        if (expirationDate == null) {
            if (other.expirationDate != null) {
                return false;
            }
        } else if (!expirationDate.equals(other.expirationDate)) {
            return false;
        }
        if (fatalBugsPercent != other.fatalBugsPercent) {
            return false;
        }
        if (majorBugsPercent != other.majorBugsPercent) {
            return false;
        }
        if (offer == null) {
            if (other.offer != null) {
                return false;
            }
        } else if (!offer.equals(other.offer)) {
            return false;
        }
        if (secondBeforeValidation != other.secondBeforeValidation) {
            return false;
        }
        return true;
    }

}
