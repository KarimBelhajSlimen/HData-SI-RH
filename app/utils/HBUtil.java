package utils;

import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;
import java.math.BigInteger;

/**
 * Created by root on 30/03/16.
 */
public class HBUtil {

    public Connection getConnection() throws IOException {
        PlayUtil playUtil = new PlayUtil();

        org.apache.hadoop.conf.Configuration config = HBaseConfiguration.create();
        config.set("hbase.zookeeper.property.clientPort", playUtil.getProperty("hbase.zookeeper.property.clientPort"));
        config.set("hbase.zookeeper.quorum", playUtil.getProperty("hbase.zookeeper.quorum"));

        Connection connection = ConnectionFactory.createConnection(config);
        return connection;
    }
}
