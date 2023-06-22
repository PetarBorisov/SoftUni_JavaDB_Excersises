import entities.Address;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static java.lang.Integer.parseInt;

public class Engin implements Runnable {

    private final EntityManager entityManager;
    private BufferedReader bufferedReader;

    public Engin(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.bufferedReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        System.out.println("Select ex number");

        try {
            int exNum = Integer.parseInt(bufferedReader.readLine());
            switch (exNum) {
                case 2: exTwo();
               case 3: exThree();
                case 4: exFour();
                case 5: exFive();
                case 6: exSix();
                case 7: exSeven();
                case 10: exTen();

            }
        } catch (IOException e) {

            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    private void exTen() {
        entityManager.getTransaction().begin();
      int affectedRows =  entityManager.createQuery("update Employee e " +
                "set e.salary = e.salary * 1.2 " +
                "where e.department.id in :ids")
              .setParameter("ids", Set.of(1,2,4,11))
                .executeUpdate();
      entityManager.getTransaction().commit();
        System.out.println(affectedRows);

    }


    private void exSeven() {
        List<Address> addresses = entityManager.createQuery("select a from  Address a " +
                "order by a.employees.size desc ",Address.class)
                .setMaxResults(10)
                .getResultList();

        addresses.forEach(address -> {
            System.out.printf("%s , %s - %d employee%n",
                    address.getText(),
                    address.getTown() == null
                            ? "Unknown" :address.getTown().getName(),
                    address.getEmployees().size());
        });
    }

    private void exSix() throws IOException {
        System.out.println("Enter employee last name:");
        String lastName = bufferedReader.readLine();
        Employee employee = entityManager.createQuery("select e from Employee e " +
                "where e.lastName = :l_name",Employee.class)
                .setParameter("l_name",lastName)
                .getSingleResult();

        Address address = createAddress("Vitoshka 15");
        entityManager.getTransaction().begin();
        employee.setAddress(address);
        entityManager.getTransaction().commit();
    }

    private Address createAddress(String addressText) {
        Address address = new Address();
        address.setText(addressText);

        entityManager.getTransaction().begin();
        entityManager.persist(address);
        entityManager.getTransaction().commit();

        return address;
    }

    private void exFive() {
        entityManager
                .createQuery("select e from Employee e " +
                        "where e.department.name = 'Research and Development' " +
                        "order by e.salary, e.id ",Employee.class)
                //.setParameter("d_name","Research and Development")
                .getResultStream()
                .forEach(employee -> {
                    System.out.printf("%s %s from %s - $%.2f%n",employee.getFirstName(),
                            employee.getLastName(),
                            employee.getDepartment().getName(),
                            employee.getSalary());
                });

    }

    private void exFour() {
        entityManager
                .createQuery("select e from Employee e " +
                        "where e.salary >:min_salary",Employee.class)
                .setParameter("min_salary", BigDecimal.valueOf(50000L))
                .getResultStream()
                .map(Employee::getFirstName)
                .forEach(System.out::println);
    }

    private void exThree() throws IOException {
        System.out.println("Enter employee full name");
        String[] fullName = bufferedReader.readLine().split("\\s+");
        String firstName = fullName[0];
        String lastName = fullName[1];

        Long singleResult = entityManager.createQuery("select count (e) from Employee e " +
                        "where e.firstName = :f_name and e.lastName = :l_name", Long.class)
                .setParameter("f_name",firstName)
                .setParameter("l_name",lastName)
                .getSingleResult();

        System.out.println(singleResult == 0 ? "No" : "Yes" );

    }

    private void exTwo() {
        entityManager.getTransaction().begin();
        Query query = entityManager.createQuery("UPDATE Town t " +
                "set t.name = upper(t.name)" +
                "WHERE length(t.name) <= 5 ");

        System.out.println(query.executeUpdate());
        entityManager.getTransaction().commit();
    }
}
