public class Staff extends User {
    public Staff(String name, String password) {
        super(name, password);
    }

    @Override
    public void add() {
        System.out.println("Adding staff member: " + getName());
    }
}