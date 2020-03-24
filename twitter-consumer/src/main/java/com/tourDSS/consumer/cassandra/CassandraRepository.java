package com.tourDSS.consumer.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.tourDSS.consumer.constants.Constants;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CassandraRepository {

    /*
        A cluster is a collection of nodes.
        A name is assigned to a cluster that will be used by participating nodes.
    */
    private Cluster cluster;

    private Session session;

    public void initialize(String node, Integer port) {
        Cluster.Builder b = Cluster.builder().addContactPoint(node);
        if (port != null) {
            b.withPort(port);
        }
        cluster = b.build();
        session = cluster.connect();
        this.createKeyspace();
        this.createTables();
        this.close();
    }

    private void createKeyspace() {
        StringBuilder createKeyspaceSB = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS " )
                .append(Constants.CASSANDRA_KEYSPACE_NAME)
                .append(" WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};");
        final String createKeyspaceQuery = createKeyspaceSB.toString();
        session.execute(createKeyspaceQuery);
        Logger.getLogger("Cassandra Repository").log(Level.INFO, createKeyspaceQuery);
    }

    private void createTables() {
        StringBuilder tweetsTableSB = new StringBuilder("CREATE TABLE IF NOT EXISTS " )
                .append(Constants.CASSANDRA_KEYSPACE_NAME).append(".")
                .append(Constants.CASSANDRA_CORE_TWEETS_TABLE)
                .append("(id timeuuid, tweet_sentiment text, tweet_location text, tweet_username text, tweet_text text, PRIMARY KEY (id));");
        final String tweetsTableQuery = tweetsTableSB.toString();
        Logger.getLogger("Cassandra Repository").log(Level.INFO, tweetsTableQuery);
        session.execute(tweetsTableQuery);
    }

    public void close() {
        session.close();
        cluster.close();
    }
}
