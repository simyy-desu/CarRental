public class Customer extends User {
    
    public Customer(String name, String password) {
        super(name, password);
    }

    @Override
    public void add() {
        System.out.println("Adding customer: " + getName());
    }
}