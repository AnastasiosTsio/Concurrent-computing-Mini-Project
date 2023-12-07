package prodcons;


public class Message{
    private Object msg;
    private IProducteur author;

    public Message(Object msg) {
        this.msg = msg;
    }

    public Object consumeMessage() {
        return msg;
    }
    public IProducteur getAuthor() {
        return author;
    }
    
}
