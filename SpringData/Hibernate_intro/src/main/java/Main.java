import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence
                .createEntityManagerFactory("softuni");
        EntityManager entityManager = emf.createEntityManager();

        Engin engin = new Engin(entityManager);
        engin.run();
    }
}
