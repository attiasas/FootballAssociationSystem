package DL.Game.MatchEvents;

import java.util.Date;

/**
 * Description:     X
 * ID:              X
 **/
public abstract class Event {

    private int gameTime;
    private Date timeStamp;
    private String description;

    public Event(int gameTime, Date timeStamp, String description) {
        if (gameTime >= 0 && timeStamp != null && description != null) {
            this.gameTime = gameTime;
            this.timeStamp = timeStamp;
            this.description = description;
        }
    }
}
