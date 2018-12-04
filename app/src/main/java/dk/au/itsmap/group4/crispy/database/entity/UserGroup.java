package dk.au.itsmap.group4.crispy.database.entity;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.au.itsmap.group4.crispy.model.IUserGroup;

public class UserGroup extends Entity implements IUserGroup {

    private String name;
    private String owner;
    private List<String> userIds;
    private Map<String, String> users;

    public UserGroup() {}

    @Override
    @Exclude
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOwnerId() {
        return owner;
    }

    @Override
    public List<Map<String, String>> getAllUsers() {
        List<Map<String, String>> userNames = new ArrayList<>();
        for (String key : users.keySet()) {
            Map<String, String> userName = new HashMap<>();
            userName.put("name", users.get(key));
            userName.put("id", key);
            userNames.add(userName);
        }
        return userNames;
    }


    public Map<String, String> getUsers() {
        return users;
    }

    public List<String> getUserIds() {
        return userIds;
    }
}
