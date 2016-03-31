package dao;

import model.User;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import utils.HBUtil;

import java.io.IOException;
import java.util.Arrays;

public class UserDAO{
    public User getByUsername(String username) throws UnknownUsername, IOException {
        User u = new User();
        HBUtil hbUtil = new HBUtil();
        Connection connection = hbUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("users"));
        try {
            Get g = new Get(Bytes.toBytes(username));
            Result r = table.get(g);

            if(r.size() == 0){
                throw new UnknownUsername();
            }
            else {
                u.setEmail(Bytes.toString(r.getValue(Bytes.toBytes("credentials"), Bytes.toBytes("email"))));
                u.setPasswordHash(Bytes.toString(r.getValue(Bytes.toBytes("credentials"), Bytes.toBytes("password"))));
                String roles = (Bytes.toString(r.getValue(Bytes.toBytes("credentials"), Bytes.toBytes("roles"))));
                u.setRoles(Arrays.asList(roles.split("-")));
                return u;
            }
        } finally {
            if (table != null) table.close();
        }

    }

    public void createUser(User u) throws IOException {
        HBUtil hbUtil = new HBUtil();
        Connection connection = hbUtil.getConnection();
        try {
            Table table = connection.getTable(TableName.valueOf("users"));
            try {
                Put p = new Put(Bytes.toBytes( u.getEmail() ));
                p.add(Bytes.toBytes("credentials"), Bytes.toBytes("password"),
                        Bytes.toBytes( u.getPasswordHash() ));
                p.add(Bytes.toBytes("credentials"), Bytes.toBytes("email"),
                        Bytes.toBytes( u.getEmail() ));
                StringBuilder roleBuilder = new StringBuilder();
                for(String s : u.getRoles()) {
                    roleBuilder.append(s+"-");
                }
                p.add(Bytes.toBytes("credentials"), Bytes.toBytes("roles"),
                        Bytes.toBytes( roleBuilder.toString() ));
                table.put(p);
            } finally {
                if (table != null) table.close();
            }
        } finally {
            connection.close();
        }
    }
}
