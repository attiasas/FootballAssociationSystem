package BL.Communication;


import java.util.List;
import java.util.Map;

public class CommunicationNullStub extends ClientServerCommunication {

    public CommunicationNullStub() {
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

}


