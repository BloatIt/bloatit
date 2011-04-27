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
package com.bloatit.data.search;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

import com.bloatit.data.search.Search.Pair;

public class DaoFeatureSearchFilter extends Filter {
    private static final long serialVersionUID = -1252700875329385254L;

    private List<Pair<String, String>> filteredTerms = null;

    public void setFilteredTerms(final List<Pair<String, String>> filteredTerms) {
        this.filteredTerms = filteredTerms;
    }

    @Override
    public DocIdSet getDocIdSet(final IndexReader reader) throws IOException {
        final OpenBitSet bitSet = new OpenBitSet(reader.maxDoc());
        bitSet.set(0, reader.maxDoc()); // Set all document ok

        if (filteredTerms != null) {
            for (final Pair<String, String> pair : filteredTerms) {

                final TermDocs termDocs = reader.termDocs(new Term(pair.key, pair.value.toLowerCase()));

                while (termDocs.next()) {
                    bitSet.clear(termDocs.doc());
                }
            }
        }

        return bitSet;
    }

}
