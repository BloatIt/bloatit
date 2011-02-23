package com.bloatit.rest.resources;

import com.bloatit.model.BankTransaction;
import com.bloatit.model.Batch;
import com.bloatit.model.Bug;
import com.bloatit.model.Comment;
import com.bloatit.model.Contribution;
import com.bloatit.model.Demand;
import com.bloatit.model.Description;
import com.bloatit.model.ExternalAccount;
import com.bloatit.model.FileMetadata;
import com.bloatit.model.Group;
import com.bloatit.model.HighlightDemand;
import com.bloatit.model.InternalAccount;
import com.bloatit.model.JoinGroupInvitation;
import com.bloatit.model.Kudos;
import com.bloatit.model.Member;
import com.bloatit.model.Offer;
import com.bloatit.model.Project;
import com.bloatit.model.Release;
import com.bloatit.model.Transaction;
import com.bloatit.model.Translation;

public interface ModelClassVisitor <ReturnType> {

    ReturnType visit(ExternalAccount model);

    ReturnType visit(InternalAccount model);

    ReturnType visit(Member model);

    ReturnType visit(BankTransaction model);

    ReturnType visit(Batch model);

    ReturnType visit(Description model);

    ReturnType visit(Group model);

    ReturnType visit(HighlightDemand model);

    ReturnType visit(JoinGroupInvitation model);

    ReturnType visit(Project model);

    ReturnType visit(Transaction model);

    ReturnType visit(Bug model);

    ReturnType visit(Contribution model);

    ReturnType visit(FileMetadata model);

    ReturnType visit(Kudos model);

    ReturnType visit(Comment model);

    ReturnType visit(Demand model);

    ReturnType visit(Offer model);

    ReturnType visit(Translation model);
    
    ReturnType visit(Release model);
}
