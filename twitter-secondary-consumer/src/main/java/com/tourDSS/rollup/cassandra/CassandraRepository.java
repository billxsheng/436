package com.tourDSS.rollup.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.tourDSS.rollup.constants.Constants;

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
    }

    private void createKeyspace() {
        StringBuilder createKeyspaceSB = new StringBuilder("CREATE KEYSPACE IF NOT EXISTS ")
                .append(Constants.CASSANDRA_KEYSPACE_NAME)
                .append(" WITH replication = {'class':'SimpleStrategy', 'replication_factor' : 1};");
        final String createKeyspaceQuery = createKeyspaceSB.toString();
        session.execute(createKeyspaceQuery);
        Logger.getLogger("Cassandra Repository").log(Level.INFO, createKeyspaceQuery);
    }

    private void createTables() {
        StringBuilder tweetsTableSB = new StringBuilder("CREATE TABLE IF NOT EXISTS ")
                .append(Constants.CASSANDRA_KEYSPACE_NAME)
                .append(".")
                .append(Constants.CASSANDRA_CORE_LOCATION_TABLE)
                .append("(ls_name text, ls_count int, PRIMARY KEY (ls_name))");
        final String tweetsTableQuery = tweetsTableSB.toString();
        Logger.getLogger("Cassandra Repository").log(Level.INFO, tweetsTableQuery);
        session.execute(tweetsTableQuery);
    }

//    private Row getPrevRow(String key) {
//        StringBuilder tweetsTableSB = new StringBuilder("SELECT * FROM ")
//                .append(Constants.CASSANDRA_KEYSPACE_NAME)
//                .append(".")
//                .append(Constants.CASSANDRA_CORE_LOCATION_TABLE)
//                .append("WHERE location = ")
//                .append(key)
//                .append(";");
//        final String tweetsTableQuery = tweetsTableSB.toString();
//        Logger.getLogger("Cassandra Repository").log(Level.INFO, tweetsTableQuery);
//        return session.execute(tweetsTableQuery).one();
//    }

    public void close() {
        session.close();
        cluster.close();
    }
}
