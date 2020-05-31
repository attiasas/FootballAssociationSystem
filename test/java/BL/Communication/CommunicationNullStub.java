package BL.Communication;


import DL.Users.Notifiable;
import DL.Users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommunicationNullStub extends ClientServerCommunication {

    private List<User> users;

    public CommunicationNullStub() {
    }

    public CommunicationNullStub(List<User> users){
        this.users = users;
    }

    @Override
    public List login(String username, String password) {
        List<User> user = new ArrayList<>();
        for (User u : users){
            if (u.getUsername().equals(username) && u.getHashedPassword().equals(password)){
                user.add(u);
            }
        }
        return user;
    }

    @Override
    public List query(String queryName, Map<String, Object> parameters) {
        return null;
    }

    @Override
    public boolean update(String queryName, Map<String, Object> parameters) {
        return false;
    }

    @Override
    public boolean insert(Object toInsert) {
        return false;
    }

    @Override
    public boolean delete(Object toDelete) {
        return false;
    }

    @Override
    public boolean transaction(List<SystemRequest> requests) {
        return false;
    }

    @Override
    public boolean merge(Object toMerge) {
        return false;
    }

    @Override
    public boolean startNotificationListener() {
        return true;
    }

    @Override
    public void notificationDemon() {
    }

    @Override
    public void stopListener() {
    }

    @Override
    public boolean notify(Notifiable notifiable) {
        return true;
    }

    @Override
    public String toString() {
        return "null";
    }
}


