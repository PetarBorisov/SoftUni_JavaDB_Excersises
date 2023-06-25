import entities.Address;
import entities.Employee;
import entities.Project;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
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
                break;
               case 3: exThree();
               break;
                case 4: exFour();
                break;
                case 5: exFive();
                break;
                case 6: exSix();
                break;
                case 7: exSeven();
                break;
                case 8: exEight();
                break;
                case 9: exNine();
                break;
                case 10: exTen();
                break;
                case 11: exEleven();
                break;
                case 12: exTwelve();
                break;
                case 13: exThirteen();
                break;

            }
        } catch (IOException e) {

            e.printStackTrace();
        }finally {
            entityManager.close();
        }
    }

    private void exEleven() throws IOException {
        System.out.println("Enter pattern for Employee name");
        System.out.println("Exapmle : SA");
         String pattern = bufferedReader.readLine();

        List<Employee> employees = entityManager.createQuery( "SELECT e FROM Employee e " +
                "WHERE e.firstName LIKE :p_pattern",Employee.class)
                .setParameter("p_pattern",pattern.concat("%"))
                .getResultList();

        for (Employee employee:employees
             ) {
            System.out.printf("%s %s - %s - (%.2f)%n",
                    employee.getFirstName(),
                    employee.getLastName(),
                    employee.getJobTitle(),
                    employee.getSalary());

        }

    }

    private void exNine() {
        List<Project> projects = entityManager.createQuery(
                        "SELECT p FROM Project p " +
                                "ORDER BY p.startDate DESC", Project.class)
                .setMaxResults(10)
                .getResultList();

        projects.stream()
                .sorted(Comparator.comparing(Project::getName))
                .forEach(p -> {
                    System.out.printf("Project name: %s%n", p.getName());
                    System.out.printf(" \tProject Description: %s%n", p.getDescription());
                    System.out.printf(" \tProject Start Date:%s%n", p.getStartDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s")));
                    System.out.printf(" \tProject End Date:%s%n", p.getEndDate() == null ? "null" : p.getEndDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.s")));
                });

    }

    private void exEight() throws IOException {
        System.out.println("Enter employee ID:");
        Scanner scanner = new Scanner(System.in);
        int employeeId = Integer.parseInt(scanner.nextLine());

        Employee employee = entityManager.find(Employee.class, employeeId);

        System.out.printf("%s %s - %s%n",
                employee.getFirstName(),
                employee.getLastName(),
                employee.getJobTitle());

        employee.getProjects().stream()
                .map(Project::getName)
                .sorted(String::compareTo)
                .forEach(p -> System.out.printf("\t%s%n", p));






    }

    private void exThirteen() throws IOException {
        System.out.println("Enter town name");
        String townName = bufferedReader.readLine();

        Town town = entityManager.createQuery("select t from Town t where t.name = :t_name",
                Town.class)
                .setParameter("t_name",townName)
                .getSingleResult();

        int affected = removeAdresesByTownId(town.getId());
        entityManager.getTransaction().begin();
        entityManager.remove(town);
        entityManager.getTransaction().commit();

        System.out.printf("%d address in %s is deleted",affected,townName);
    }

    private int removeAdresesByTownId(Integer id) {
             List<Address> addresses = entityManager
                     .createQuery("SELECT  a FROM Address a " +
                             "WHERE a.town.id = :p_id", Address.class)
                     .setParameter("p_id", id)
                     .getResultList();

             entityManager.getTransaction().begin();
             addresses.forEach(entityManager::remove);
             entityManager.getTransaction().commit();
                 return addresses.size();
    }

    private void exTwelve() {
        List<Object[]> rows = entityManager
                .createNativeQuery("select d.name,MAX(e.salary) AS 'm_salary' from departments d " +
                        "join employees e on d.department_id = e.department_id " +
                        "GROUP BY d.name " +
                        "HAVING m_salary NOT BETWEEN  30000 AND 70000 ")
                .getResultList();

        rows.forEach(row -> System.out.println(row[0] + " " + row[1]));
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
