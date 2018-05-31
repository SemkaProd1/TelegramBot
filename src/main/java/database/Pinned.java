package database;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity (name = "pinned")
public class Pinned {

    @Id
    private
    int id;

    @Column
    private
    String userName;

    @Column
    private
    int fromChatId;

    @Column
    private
    String forwardFrom;

    @Column
    private
    int messageId;

    @Column
    private
    String textMessage;

    public Pinned(int id, String userName, int fromChatId, String forwardFrom, int messageId, String textMessage) {
        this.id = id;
        this.userName = userName;
        this.fromChatId = fromChatId;
        this.forwardFrom = forwardFrom;
        this.messageId = messageId;
        this.textMessage = textMessage;
    }

    public Pinned() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public String getForwardFrom() {return forwardFrom;}

    public void setForwardFrom(String forwardFrom) {
        this.forwardFrom = forwardFrom;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getFromChatId() {
        return fromChatId;
    }

    public void setFromChatId(int fromChatId) {
        this.fromChatId = fromChatId;
    }



    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }
    public void reset() {
        // Setup the instance
        this.id = 0;
        this.forwardFrom = null;
        this.userName = null;
        this.fromChatId = 0;
        this.messageId = 0;
        this.textMessage = null;
    }

    @Override
    public String toString() {
        return "Pinned{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", fromChatId=" + fromChatId +
                ", forwardFrom='" + forwardFrom + '\'' +
                ", messageId=" + messageId +
                ", textMessage='" + textMessage + '\'' +
                '}';
    }
}
