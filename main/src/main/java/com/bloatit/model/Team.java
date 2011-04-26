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
import com.bloatit.framework.exceptions.lowlevel.MemberNotInTeamException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException;
import com.bloatit.framework.exceptions.lowlevel.UnauthorizedOperationException.SpecialCode;
import com.bloatit.framework.utils.Image;
import com.bloatit.framework.utils.PageIterable;
import com.bloatit.framework.utils.StringUtils;
import com.bloatit.model.lists.ListBinder;
import com.bloatit.model.lists.MemberList;
import com.bloatit.model.right.Action;
import com.bloatit.model.right.RgtTeam;

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

    /**
     * Sets the type of team: either <code>PROTECTED</code> or
     * <code>PUBLIC</code>
     */
    public void setRight(final Right right) {
        getDao().setRight(right);
    }

    public void setDisplayName(final String displayName) throws UnauthorizedOperationException {
        tryAccess(new RgtTeam.DisplayName(), Action.WRITE);
        getDao().setDisplayName(displayName);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Accessors
    // /////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return the list of members that are part of this team
     */
    public PageIterable<Member> getMembers() {
        return new MemberList(getDao().getMembers());
    }

    /**
     * @return the type of team: either <code>PROTECTED</code> or
     *         <code>PUBLIC</code>
     */
    public Right getRight() {
        return getDao().getRight();
    }

    /**
     * Indicates wheter the team is public or not
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
    public String getDescription() {
        return getDao().getDescription();
    }

    @Override
    public String getDisplayName() {
        final String displayName = getDao().getDisplayName();
        if (StringUtils.isEmpty(displayName)) {
            return getLogin();
        }
        return displayName;
    }

    public EnumSet<UserTeamRight> getUserTeamRight(final Member member) {
        return getDao().getUserTeamRight(member.getDao());
    }

    public boolean canAccessContact(final Action action) {
        return canAccess(new RgtTeam.Contact(), action);
    }

    public String getContact() throws UnauthorizedOperationException {
        tryAccess(new RgtTeam.Contact(), Action.READ);
        return getDao().getContact();
    }

    public void setContact(final String contact) throws UnauthorizedOperationException {
        tryAccess(new RgtTeam.Contact(), Action.WRITE);
        getDao().setContact(contact);
    }

    public void setDescription(final String description) throws UnauthorizedOperationException {
        if (!getAuthToken().getVisitor().hasModifyTeamRight(this)) {
            throw new UnauthorizedOperationException(SpecialCode.TEAM_MISSING_MODIFY_RIGHT);
        }
        getDao().setDescription(description);
    }

    @Override
    public Image getAvatar() {
        return new Image(FileMetadata.create(getDao().getAvatar()));
    }

    public void setAvatar(final FileMetadata fileImage) {
        // TODO: right management
        if (fileImage == null) {
            getDao().setAvatar(null);
        } else {
            getDao().setAvatar(fileImage.getDao());
        }
    }

    @Override
    public PageIterable<Contribution> doGetContributions() {
        return new ListBinder<Contribution, DaoContribution>(getDao().getContributions());
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Visitor
    // /////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public <ReturnType> ReturnType accept(final ModelClassVisitor<ReturnType> visitor) {
        return visitor.visit(this);
    }

    // /////////////////////////////////////////////////////////////////////////////////////////
    // Rights
    // /////////////////////////////////////////////////////////////////////////////////////////
    
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
}
