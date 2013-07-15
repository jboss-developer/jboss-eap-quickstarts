/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010 Red Hat Inc. and/or its affiliates and other
 * contributors as indicated by the @author tags. All rights reserved.
 * See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.as.quickstarts.hibernate_search.infinispan;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.util.Version;
import org.infinispan.Cache;
import org.infinispan.lucene.InfinispanDirectory;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.remoting.transport.Address;
import org.infinispan.util.InfinispanCollections;
import org.jboss.as.quickstarts.hibernate_search.model.data.FeedEntry;

/**
 * FeedActions does some basic operations on the Lucene index,
 * to be used by InfinispanFeedService to show base operations on Lucene.
 *
 * @author Sanne Grinovero
 * @since 4.0
 */
public class FeedActions {

    /**
     * The MAIN_FIELD
     */
    private static final String MAIN_FIELD = "description";

    /**
     * The Analyzer used in all methods *
     */
    private static final Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_35);

    private InfinispanDirectory index;

    private final Cache<?, ?> cache;

    public FeedActions(InfinispanDirectory index, Cache<?, ?> cache) {
        this.index = index;
        this.cache = cache;
    }

    /**
     * Runs a Query and returns the stored field for each matching document
     *
     * @throws java.io.IOException
     */
    public List<String> listStoredValuesMatchingQuery(Query query) {
        try {
            IndexSearcher searcher = new IndexSearcher(index, true);
            TopDocs topDocs = searcher.search(query, null, 100);// demo limited to 100 documents!
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            List<String> list = new ArrayList<String>();
            for (ScoreDoc sd : scoreDocs) {
                Document doc = searcher.doc(sd.doc);
                list.add(doc.get(MAIN_FIELD));
            }
            return list;
        } catch (IOException ioe) {
            // not recommended: in the simple demo this likely means that the index was not yet
            // initialized, so returning empty list.
            return InfinispanCollections.emptyList();
        }
    }

    /**
     * Returns a list of the values of all stored fields
     *
     * @throws java.io.IOException
     */
    public List<String> listAllDocuments() {
        MatchAllDocsQuery q = new MatchAllDocsQuery();
        return listStoredValuesMatchingQuery(q);
    }

    /**
     * Creates a new document having just one field containing a string
     *
     * @param feedEntry The Feed Entry snippet to add
     * @throws java.io.IOException
     */
    public void addNewDocument(FeedEntry feedEntry) {
        IndexWriter iw = null;
        try {
            iw = new IndexWriter(index, analyzer, MaxFieldLength.UNLIMITED);
            Document doc = new Document();
            Field fieldTitle = new Field("title", feedEntry.getTitle(), Store.YES, Index.ANALYZED);
            Field fieldDescription = new Field("description", feedEntry.getDescription(), Store.YES, Index.ANALYZED);
            Field fieldAuthor = new Field("author", feedEntry.getAuthor(), Store.YES, Index.ANALYZED);
            doc.add(fieldTitle);
            doc.add(fieldDescription);
            doc.add(fieldAuthor);
            iw.addDocument(doc);
            iw.commit();
        } catch (LockObtainFailedException e) {
            e.printStackTrace();
        } catch (CorruptIndexException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                iw.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /**
     * Parses a query using the single field as default
     *
     * @throws org.apache.lucene.queryParser.ParseException
     *
     */
    public Query parseQuery(String queryLine) throws ParseException {
        QueryParser parser = new QueryParser(Version.LUCENE_35, MAIN_FIELD, analyzer);
        return parser.parse(queryLine);
    }

    /**
     * Returns a list of Addresses of all members in the cluster
     */
    public List<Address> listAllMembers() {
        EmbeddedCacheManager cacheManager = cache.getCacheManager();
        return cacheManager.getMembers();
    }

}
