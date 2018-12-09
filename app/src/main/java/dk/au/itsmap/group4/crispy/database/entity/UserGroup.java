package dk.au.itsmap.group4.crispy.database.entity;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.au.itsmap.group4.crispy.model.IUserGroup;

public class UserGroup extends Entity implements IUserGroup {

    private String name;
    private String ownerId;
    private List<String> userIds;
    private Map<String, Map<String, String>> users;

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
        return ownerId;
    }

    @Override
    @Exclude
    public List<Map<String, String>> getAllUsers() {
        List<Map<String, String>> userNames = new ArrayList<>();
        for (String key : users.keySet()) {
            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("name", users.get(key).get("name"));
            userInfo.put("photo_url", users.get(key).get("photo_url"));
            userInfo.put("id", key);
            userNames.add(userInfo);
        }
        return userNames;
    }

    @Override
    public void deleteUser(String userId) {
        users.remove(userId);
        userIds.remove(userId);
    }

    public Map<String, Map<String, String>> getUsers() {
        return users;
    }

    public List<String> getUserIds() {
        return userIds;
    }
}
