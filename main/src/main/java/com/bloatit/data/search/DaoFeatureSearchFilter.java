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

    private static long serialVersionUID = 2532131753889492412L;

    private List<Pair<String, String>> filteredTerms = null;

    public void setFilteredTerms(List<Pair<String, String>> filteredTerms) {
        this.filteredTerms = filteredTerms;
    }

    @Override
    public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
        OpenBitSet bitSet = new OpenBitSet(reader.maxDoc());
        bitSet.set(0, reader.maxDoc()); // Set all document ok

        if (filteredTerms != null) {
            for (Pair<String, String> pair : filteredTerms) {

                TermDocs termDocs = reader.termDocs(new Term(pair.key, pair.value.toLowerCase()));

                while (termDocs.next()) {
                    bitSet.clear(termDocs.doc());
                }
            }
        }

        return bitSet;
    }

}
