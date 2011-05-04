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
package com.bloatit.model;

import java.util.EnumSet;

import com.bloatit.data.DaoContribution;
import com.bloatit.data.DaoTeam;
import com.bloatit.data.DaoTeam.Right;
import com.bloatit.data.DaoTeamRight.UserTeamRight;
import com.bloatit.data.DaoUserContent;
import com.bloatit.framework.exceptions.lowlevel.MemberNotInTeamException;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.model.lists.ListBinder;
import com.bloatit.model.lists.MemberList;
import com.bloatit.model.lists.MoneyWithdrawalList;
import com.bloatit.model.lists.UserContentList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtTeam;
import com.bloatit.model.right.UnauthorizedOperationException;
import com.bloatit.model.right.UnauthorizedPublicAccessException;
import com.bloatit.model.right.UnauthorizedOperationException.SpecialCode;

/**
 * This is a team ... There are member in it.
 *
 * @see DaoTeam
 */
public final class Team extends Actor<DaoTeam> {

    // /////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTION
    // /////////////////////////////////////////////////////////////////////////////////////////

    private static final class MyCreator extends Creator<DaoTeam, Team> {
        @SuppressWarnings("synthetic-access")
        @Override
        public Team doCreate(final DaoTeam dao) {
            return new Team(dao);
        }
    }

    @SuppressWarnings("synthetic-access")
    public static Team create(final DaoTeam dao) {
        return new MyCreator().create(dao);
    }

    /**
     * <p>
     * Creates a new team
     * </p>
     *
     * @param login the displayed name of the team
     * @param contact a string with various means to contact the team
     * @param description a textual description of the team
     * @param right <ether the team is <code>PUBLIC</code> or
     *            <code>PROTECTED</code>
     * @param author the creator of the team
     */
    public Team(final String login, final String contact, final String description, final Right right, final Member author) {
        super(DaoTeam.createAndPersiste(login, contact, description, right));
        author.addToTeamUnprotected(this);

        // Give all rights
        changeRightUnprotected(author, UserTeamRight.CONSULT, true);
        changeRightUnprotected(author, UserTeamRight.BANK, true);
        changeRightUnprotected(author, UserTeamRight.INVITE, true);
        changeRightUnprotected(author, UserTeamRight.MODIFY, true);
        changeRightUnprotected(author, UserTeamRight.PROMOTE, true);
        changeRightUnprotected(author, UserTeamRight.TALK, true);
    }

    private Team(final DaoTeam dao) {
        super(dao);
    }

    public void setContact(final String contact) throws UnauthorizedPublicAccessException {
        tryAccess(new RgtTeam.Contact(), Action.WRITE);
        getDao().setContact(contact);
    }

    public void setDescription(final String description) throws UnauthorizedPublicAccessException {
        tryAccess(new RgtTeam.Description(), Action.WRITE);
        getDao().setDescription(description);
    }

    public void setAvatar(final FileMetadata fileImage) throws UnauthorizedPublicAccessException {
        tryAccess(new RgtTeam.Avatar(), Action.WRITE);
        setAvatarUnprotected(fileImage);
    }

    public void setAvatarUnprotected(final FileMetadata fileImage) {
        if (fileImage == null) {
            getDao().setAvatar(null);
        } else {
            getDao().setAvatar(fileImage.getDao());
        }
    }

    /**
     * Sets the type of team: either <code>PROTECTED</code> or
     * <code>PUBLIC</code>
     *
     * @throws UnauthorizedPublicAccessException
     */
    public void setRight(final Right right) throws UnauthorizedPublicAccessException {
        tryAccess(new RgtTeam.Right(), Action.WRITE);
        getDao().setRight(right);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Accessors
    // /////////////////////////////////////////////////////////////////////////////////////////

    public void changeRight(final Member admin, final Member target, final UserTeamRight right, final boolean give)
            throws UnauthorizedOperationException, MemberNotInTeamException {
        if (!canChangeRight(admin, target, right, give)) {
            throw new UnauthorizedOperationException(SpecialCode.TEAM_PROMOTE_RIGHT_MISSING);
        }
        if (!target.isInTeam(this)) {
            throw new MemberNotInTeamException();
        }
        changeRightUnprotected(target, right, give);
    }

    private void changeRightUnprotected(final Member target, final UserTeamRight right, final boolean give) {
        if (give) {
            target.getDao().addTeamRight(this.getDao(), right);
        } else {
            target.getDao().removeTeamRight(this.getDao(), right);
        }
    }

    // no right management: this is public data
    public String getContact() {
        return getDao().getContact();
    }

    // no right management: this is public data
    @Override
    public Image getAvatar() {
        return new Image(FileMetadata.create(getDao().getAvatar()));
    }

    /**
     * @return the list of members that are part of this team
     */
    // no right management: this is public data
    public PageIterable<Member> getMembers() {
        return new MemberList(getDao().getMembers());
    }

    /**
     * Indicates whether the team is public or not
     *
     * @return <code>true</code> if the team is public, <code>false</code>
     *         otherwise
     */
    public boolean isPublic() {
        return (getDao().getRight() == Right.PUBLIC);
    }

    /**
     * @return the textual representation of this team
     */
    // no right management: this is public data
    public String getDescription() {
        return getDao().getDescription();
    }

    // no right management: this is public data
    @Override
    public String getDisplayName() {
        return getLogin();
    }

    public EnumSet<UserTeamRight> getUserTeamRight(final Member member) {
        return getDao().getUserTeamRight(member.getDao());
    }

    @Override
    public PageIterable<Contribution> doGetContributions() {
        return new ListBinder<Contribution, DaoContribution>(getDao().getContributions());
    }

    @Override
    public PageIterable<MoneyWithdrawal> doGetMoneyWithdrawals() throws UnauthorizedOperationException {
        return new MoneyWithdrawalList(getDao().getMoneyWithdrawals());
    }

    public PageIterable<UserContent<? extends DaoUserContent>> getActivity() {
        // TODO set rights
        return new UserContentList(getDao().getActivity());
    }

    public long getRecentActivityCount() {
        // TODO set rights
        return getDao().getRecentActivityCount(ModelConfiguration.getRecentActivityDays());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Can ...
    // /////////////////////////////////////////////////////////////////////////////////////////

    public boolean canAccessContact(final Action action) {
        return canAccess(new RgtTeam.Contact(), action);
    }

    /**
     * Tells if the authenticated user can access the <i>BankTransaction</i>
     * property.
     *
     * @param action the type of access you want to do on the
     *            <i>BankTransaction</i> property.
     * @return true if you can access the <i>BankTransaction</i> property.
     */
    public final boolean canAccessBankTransaction(final Action action) {
        return canAccess(new RgtTeam.BankTransaction(), action);
    }

    /**
     * Tells if the authenticated user can access the <i>DisplayName</i>
     * property.
     *
     * @param action the type of access you want to do on the <i>DisplayName</i>
     *            property.
     * @return true if you can access the <i>DisplayName</i> property.
     */
    public final boolean canAccessDisplayName(final Action action) {
        return canAccess(new RgtTeam.DisplayName(), action);
    }

    /**
     * Tells if the authenticated user can access the <i>Avatar</i> property.
     *
     * @param action the type of access you want to do on the <i>Avatar</i>
     *            property.
     * @return true if you can access the <i>Avatar</i> property.
     */
    public final boolean canAccessAvatar(final Action action) {
        return canAccess(new RgtTeam.Avatar(), action);
    }

    /**
     * Tells if the authenticated user can access the <i>Right</i> property.
     *
     * @param action the type of access you want to do on the <i>Right</i>
     *            property.
     * @return true if you can access the <i>Right</i> property.
     */
    public final boolean canAccessRight(final Action action) {
        return canAccess(new RgtTeam.Right(), action);
    }

    /**
     * Tells if the authenticated user can access the <i>Description</i>
     * property.
     *
     * @param action the type of access you want to do on the <i>Description</i>
     *            property.
     * @return true if you can access the <i>Description</i> property.
     */
    public final boolean canAccessDescription(final Action action) {
        return canAccess(new RgtTeam.Description(), action);
    }

    public boolean canChangeRight(final Member admin, final Member target, final UserTeamRight right, final boolean give) {
        if (admin == null) {
            return false;
        }
        if (target.equals(admin) && admin.hasPromoteTeamRight(this)) {
            return false;
        }
        if (!target.isInTeam(this)) {
            return false;
        }
        if (!admin.hasPromoteTeamRight(this)) {
            return false;
        }
        if (!(target.hasTeamRight(this, right) ^ give)) {
            return false;
        }
        return true;
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

}
