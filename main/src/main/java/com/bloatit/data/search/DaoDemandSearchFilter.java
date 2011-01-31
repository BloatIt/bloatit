package com.bloatit.data.search;

import java.io.IOException;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

public class DaoDemandSearchFilter extends Filter {

    private static final long serialVersionUID = 2532131753889492412L;

    @Override
    public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
        System.err.println("getDocIdSet");
        OpenBitSet bitSet = new OpenBitSet( reader.maxDoc() );

        {
            TermDocs termDocs = reader.termDocs(new Term( "demandState", "preparing" ));

            while ( termDocs.next() ) {
                System.err.println("termDocs.next()");
                bitSet.set( termDocs.doc() );
                bitSet.set( termDocs.doc() );
            }
        }

        {
            TermDocs termDocs = reader.termDocs(new Term( "demandState", "pending" ));

            while ( termDocs.next() ) {
                System.err.println("termDocs.next()");
                bitSet.set( termDocs.doc() );
                bitSet.set( termDocs.doc() );
            }
        }

        return bitSet;
    }


}

