package DL.Game.MatchEvents;

/**
 * Description:     X
 * ID:              X
 **/
public abstract class Event {

    private int gameTime;
    private String description;

    public Event(int gameTime, String description) {
        if (gameTime >= 0  && description != null) {
            this.gameTime = gameTime;
            this.description = description;
        }
    }
}
