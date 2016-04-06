package dao;

import com.fasterxml.jackson.databind.JsonNode;
import model.Education;
import model.Experience;
import model.User;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import play.libs.Json;
import utils.HBUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;


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
                u.setFirstname(Bytes.toString(r.getValue(Bytes.toBytes("personal"), Bytes.toBytes("firstname"))));
                u.setLastname(Bytes.toString(r.getValue(Bytes.toBytes("personal"), Bytes.toBytes("lastname"))));
                u.setNumber(Bytes.toString(r.getValue(Bytes.toBytes("personal"), Bytes.toBytes("number"))));
                u.setAddress(Bytes.toString(r.getValue(Bytes.toBytes("personal"), Bytes.toBytes("address"))));
                u.setDescription(Bytes.toString(r.getValue(Bytes.toBytes("personal"), Bytes.toBytes("description"))));
                u.setGithub(Bytes.toString(r.getValue(Bytes.toBytes("personal"), Bytes.toBytes("github"))));
                u.setLinkedin(Bytes.toString(r.getValue(Bytes.toBytes("personal"), Bytes.toBytes("linkedin"))));
                u.setDob(Bytes.toString(r.getValue(Bytes.toBytes("personal"), Bytes.toBytes("dob"))));
                //Education e = Json.fromJson(Bytes.toString(r.getValue(Bytes.toBytes("cv"), Bytes.toBytes("dob"))),Education.class);
                JsonNode educationJson = Json.parse(Bytes.toString(r.getValue(Bytes.toBytes("cv"), Bytes.toBytes("education"))));
                u.setEducation( Json.fromJson(educationJson, List.class ));
                JsonNode experiencesJson = Json.parse(Bytes.toString(r.getValue(Bytes.toBytes("cv"), Bytes.toBytes("experiences"))));
                u.setExperiences( Json.fromJson(experiencesJson, List.class ));
                JsonNode skillsJson = Json.parse(Bytes.toString(r.getValue(Bytes.toBytes("cv"), Bytes.toBytes("skills"))));
                u.setSkills( Json.fromJson(skillsJson, List.class ));
                return u;
            }
        } finally {
            if (table != null) table.close();
        }

    }


    public User getCredentials(String username) throws UnknownUsername, IOException {
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




    public List<HashMap> getList() throws UnknownUsername, IOException {
        System.out.println("getting list in DAO" );
        User u = new User();
        HBUtil hbUtil = new HBUtil();
        Connection connection = hbUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("users"));
        try {

            Scan scan = new Scan();
            scan.setCaching(20);
            scan.setBatch(20);
            scan.addFamily(Bytes.toBytes("credentials"));

            List<HashMap> liste=new ArrayList<HashMap>();
            ResultScanner resultScanner = table.getScanner(scan);
            Iterator<Result> iterator = resultScanner.iterator();
            while (iterator.hasNext())
            {
                Result next = iterator.next();

                for(Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> columnFamilyMap : next.getMap().entrySet())
                {
                    HashMap member=new HashMap();

                    for (Entry<byte[], NavigableMap<Long, byte[]>> entryVersion : columnFamilyMap.getValue().entrySet())
                    {



                        for (Entry<Long, byte[]> entry : entryVersion.getValue().entrySet())
                        {
                            String row = Bytes.toString(next.getRow());
                            String column = Bytes.toString(entryVersion.getKey());
                            byte[] value = entry.getValue();
                            long timesstamp = entry.getKey();

                           // System.out.println("adding map parameters");
                            //System.out.println("row:  "+row+"--- column: "+column+"--- value: "+Bytes.toString(value));

                            member.put(column,Bytes.toString(value));
                             }

                    }

                    //System.out.println("adding user in the list");

                    liste.add(member);
                }
            }

            return liste;
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
                    roleBuilder.append(s+"");
                }
                p.add(Bytes.toBytes("credentials"), Bytes.toBytes("roles"),
                        Bytes.toBytes( roleBuilder.toString() ));

                p.add(Bytes.toBytes("personal"), Bytes.toBytes("firstname"),
                        Bytes.toBytes( u.getFirstname() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("lastname"),
                        Bytes.toBytes( u.getLastname() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("number"),
                        Bytes.toBytes( u.getNumber() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("address"),
                        Bytes.toBytes( u.getAddress() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("dob"),
                        Bytes.toBytes( u.getDob() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("description"),
                        Bytes.toBytes( u.getDescription() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("linkedin"),
                        Bytes.toBytes( u.getLinkedin() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("github"),
                        Bytes.toBytes( u.getGithub() ));

                p.add(Bytes.toBytes("cv"), Bytes.toBytes("experiences"),
                        Bytes.toBytes( Json.stringify(Json.toJson(u.getExperiences()))));
                p.add(Bytes.toBytes("cv"), Bytes.toBytes("skills"),
                        Bytes.toBytes( Json.stringify(Json.toJson(u.getSkills()))));
                p.add(Bytes.toBytes("cv"), Bytes.toBytes("education"),
                        Bytes.toBytes( Json.stringify(Json.toJson(u.getEducation()))));


                table.put(p);
            } finally {
                if (table != null) table.close();
            }
        } finally {
            connection.close();
        }
    }



    public void addProfile(User u) throws IOException {
        HBUtil hbUtil = new HBUtil();
        Connection connection = hbUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("users"));
        try {

            try {
                Put p = new Put(Bytes.toBytes( u.getEmail() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("firstname"),
                        Bytes.toBytes( u.getFirstname() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("lastname"),
                        Bytes.toBytes( u.getLastname() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("number"),
                        Bytes.toBytes( u.getNumber() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("address"),
                        Bytes.toBytes( u.getAddress() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("description"),
                        Bytes.toBytes( u.getDescription() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("github"),
                        Bytes.toBytes( u.getGithub() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("linkedin"),
                        Bytes.toBytes( u.getLinkedin() ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("dob"),
                        Bytes.toBytes( u.getDob() ));
                p.add(Bytes.toBytes("cv"), Bytes.toBytes("experiences"),
                        Bytes.toBytes( Json.stringify(Json.toJson(u.getExperiences()))));
                p.add(Bytes.toBytes("cv"), Bytes.toBytes("skills"),
                        Bytes.toBytes( Json.stringify(Json.toJson(u.getSkills()))));
                p.add(Bytes.toBytes("cv"), Bytes.toBytes("education"),
                        Bytes.toBytes( Json.stringify(Json.toJson(u.getEducation()))));

                table.put(p);
            } finally {
                if (table != null) table.close();
            }
        } finally {
            if (table != null) table.close();
        }

    }

    public void addPicture(String picture,String username) throws IOException {
        HBUtil hbUtil = new HBUtil();
        Connection connection = hbUtil.getConnection();
        Table table = connection.getTable(TableName.valueOf("users"));
        try {

            try {
                Put p = new Put(Bytes.toBytes( username ));
                p.add(Bytes.toBytes("personal"), Bytes.toBytes("picture"),
                        Bytes.toBytes( picture ));

                table.put(p);
            } finally {
                if (table != null) table.close();
            }
        } finally {
            if (table != null) table.close();
        }
    }

    public String getPicture(String username) throws IOException, UnknownUsername {
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
                return Bytes.toString(r.getValue(Bytes.toBytes("personal"), Bytes.toBytes("picture")));

            }
        } finally {
            if (table != null) table.close();
        }

    }
}
