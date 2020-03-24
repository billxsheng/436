package com.tourDSS.rollup;

import com.tourDSS.rollup.cassandra.CassandraRepository;
import com.tourDSS.rollup.spark.SparkLocationRollup;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.getLogger("org").setLevel(Level.OFF);
        Logger.getLogger("akka").setLevel(Level.OFF);

        CassandraRepository cr = new CassandraRepository();
        cr.initialize("127.0.0.1", null);
        SparkLocationRollup slr = new SparkLocationRollup();
        slr.runJob();
        cr.close();
    }
}
