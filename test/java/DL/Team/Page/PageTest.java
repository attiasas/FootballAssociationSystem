package DL.Team.Page;

import DL.Team.Team;
import DL.Users.Fan;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.util.Set;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * Description:  Test suite for Page class
 * ID:              X
 **/
public class PageTest
{

    @Test
    public void testAddFollower()
    {
        //init fans like pages
        Fan u1 = new Fan("Assaf","test@mail.com", DigestUtils.sha1Hex("abcde"));
        //Create team
        Team t1 = new Team("BestTeam",true,false);
        //Create Page
        TeamPage t1Page = new TeamPage("t1 Page!", t1);
        //connect team to page
        t1.setPage(t1Page);

        t1Page.addFollower(u1);

        assertTrue(isFanFollowerOfPage(u1, t1Page));
    }

    @Test
    public void testRemoveFollower()
    {
        //init fans like pages
        Fan u1 = new Fan("Assaf","test@mail.com", DigestUtils.sha1Hex("abcde"));
        //Create team
        Team t1 = new Team("BestTeam",true,false);
        //Create Page
        TeamPage t1Page = new TeamPage("t1 Page!", t1);
        //connect team to page
        t1.setPage(t1Page);

        t1Page.addFollower(u1);

        assertTrue(isFanFollowerOfPage(u1, t1Page));

        t1Page.removeFollower(u1);
        assertFalse(isFanFollowerOfPage(u1, t1Page));
    }

    @Test
    public void testIsFollower()
    {
        //init fans like pages
        Fan u1 = new Fan("Assaf","test@mail.com", DigestUtils.sha1Hex("abcde"));
        //Create team
        Team t1 = new Team("BestTeam",true,false);
        //Create Page
        TeamPage t1Page = new TeamPage("t1 Page!", t1);
        //connect team to page
        t1.setPage(t1Page);

        t1Page.addFollower(u1);

        assertTrue(t1Page.isFollower(u1));
    }


    private boolean isFanFollowerOfPage(Fan f, Page p)
    {
        Set<Fan> followers = p.getFollowers();
        for(Fan fan : followers)
        {
            if(fan.equals(f))
            {
                return true;
            }
        }
        return false;
    }





}