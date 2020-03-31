package BL.Server.utils;

import org.junit.jupiter.api.Test;

import static BL.Server.utils.Settings.getPropertyValue;
import static org.junit.jupiter.api.Assertions.*;

class SettingsTest {

    @Test
    public void testGetSingleProperty(){
        assertEquals("3306", getPropertyValue("db.port"));
    }

    @Test
    public void testGetDEV_DBConnection(){
        String dbName = getPropertyValue("db.database");
        assertEquals(String.format("jdbc:mysql://localhost:3306/%s",dbName), Settings.getDEV_DBConnection());
    }

    @Test
    public void testGetTEST_DBConnection(){
        String dbName = getPropertyValue("dbtest.database");
        assertEquals(String.format("jdbc:mysql://localhost:3306/%s",dbName), Settings.getTEST_DBConnection());
    }

    @Test
    public void testPropertyNotExists(){
        assertNull( getPropertyValue("notexist"));
    }
}