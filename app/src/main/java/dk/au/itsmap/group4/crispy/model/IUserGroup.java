package dk.au.itsmap.group4.crispy.model;

import java.util.List;
import java.util.Map;

public interface IUserGroup {

    String getId();

    String getName();

    String getOwnerId();

    List<Map<String, String>> getAllUsers();

    void deleteUser(String userId);
}
