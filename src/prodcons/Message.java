package prodcons;


public class Message{
    Object msg ;
    Object creator;

    public Message(Object msg) {
        this.msg = msg;
    }
    public Message(Object msg, Object sender) {
        this.msg = msg;
        this.creator = sender;
    }
    public Object getMsg() {
        return msg;
    }
    public Object getCreator() {
        return creator;
    }
    // public String toString() {
    //     return msg.toString();
    // }
}
