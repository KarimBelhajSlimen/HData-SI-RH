package dao;

import com.fasterxml.jackson.databind.JsonNode;
import model.Experience;
import model.User;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import play.libs.Json;
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
                u.setEmail(username);
                u.setPasswordHash(Bytes.toString(r.getValue(Bytes.toBytes("credentials"), Bytes.toBytes("password"))));
                String roles = (Bytes.toString(r.getValue(Bytes.toBytes("credentials"), Bytes.toBytes("roles"))));
                u.setRoles(Arrays.asList(roles.split("-")));
                return u;
            }
        } finally {
            if (table != null) table.close();
        }

    }

    public void createUser(User u) throws UserAlreadyExistsException, IOException {
        HBUtil hbUtil = new HBUtil();
        Connection connection = hbUtil.getConnection();
        try {
            Table table = connection.getTable(TableName.valueOf("users"));

            //check if user exists
            Get g = new Get(Bytes.toBytes(u.getEmail()));
            Result r = table.get(g);
            if(r.size() != 0){
                throw new UserAlreadyExistsException();
            }

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

    public void addProfile(User u){
        /*Experience e = new Experience();
        e.setBegin_year(2011);
        e.setEnd_year(2012);
        e.setCompany("ok");
        e.setDescription("desc");
        e.setPosition("eee");
        u.setExperience(Arrays.asList( new Experience[]{
            e
        }
        ));

        JsonNode json = Json.toJson(u);
        System.out.println("11111"+Json.stringify(Json.toJson(u.getExperience())));
        System.out.println("#####"+Json.stringify(json));
        User u2 = Json.fromJson(json,User.class);
        System.out.println(u2.toString());*/

    }
}
