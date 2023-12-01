package prodcons;

import prodcons.v1.Consomateur;

public class Message{
    String msg ;
    Object creator;
    public Message(String msg) {
        this.msg = msg;
    }
    public Message(String msg, Object sender) {
        this.msg = msg;
        this.creator = sender;
    }
    public String getMsg() {
        return msg;
    }
    public Object getCreator() {
        return creator;
    }
    // public String toString() {
    //     return msg.toString();
    // }
}
