import java.util.Objects;

public abstract class User {
    private String name;
    private String password;

    protected User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract void add();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{name='" + name + "'}";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User other = (User) obj;
        return Objects.equals(name, other.name) && Objects.equals(password, other.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, password);
    }
}
