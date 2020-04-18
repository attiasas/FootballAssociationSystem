package DL.Users;

import DL.Team.Page.Page;
import DL.Team.Page.TeamPage;
import DL.Team.Team;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Description:     Test suite for fan class
 * ID:              7
 **/
public class FanTest
{

    @Test
    public void testFollowPage()
    {
        //Create team
        Team t1 = new Team("BestTeam",true,false);
        //Create Page
        TeamPage t1Page = new TeamPage("t1 Page!", t1);
        //connect team to page
        t1.setPage(t1Page);

        Fan fan = new Fan("Amir", "Amir@mail.com", "abcde");

        fan.followPage(t1Page);

        assertTrue(t1Page.isFollower(fan));
        assertTrue(isFanFollowing(fan, t1Page));
    }

    @Test
    public void testUnfollowPage()
    {
        //Create team
        Team t1 = new Team("BestTeam",true,false);
        //Create Page
        TeamPage t1Page = new TeamPage("t1 Page!", t1);
        //connect team to page
        t1.setPage(t1Page);

        Fan fan = new Fan("Amir", "Amir@mail.com", "abcde");
        fan.followPage(t1Page);

        fan.unfollowPage(t1Page);

        assertFalse(t1Page.isFollower(fan));
        assertFalse(isFanFollowing(fan, t1Page));
    }

    @Test
    public void testUnfollowAllPages()
    {
        //Create team
        Team t1 = new Team("BestTeam",true,false);
        //Create Page
        TeamPage t1Page = new TeamPage("t1 Page!", t1);
        //connect team to page
        t1.setPage(t1Page);

        //Create team
        Team t2 = new Team("WorstTeam",true,false);
        //Create Page
        TeamPage t2Page = new TeamPage("t2 Page!", t2);
        //connect team to page
        t2.setPage(t2Page);

        Fan fan = new Fan("Amir", "Amir@mail.com", "abcde");
        fan.followPage(t1Page);
        fan.followPage(t2Page);

        fan.unfollowAllPages();

        //check the first page is unfollowed
        assertFalse(t1Page.isFollower(fan));
        assertFalse(isFanFollowing(fan, t1Page));
    }


    private boolean isFanFollowing (Fan fan, Page page)
    {
        Set<Page> following = fan.getFollowing();

        Iterator<Page> it = following.iterator();
        while(it.hasNext())
        {
            Page p = it.next();
            if(page.equals(p))
            {
                return true;
            }
        }
        return false;
    }

}