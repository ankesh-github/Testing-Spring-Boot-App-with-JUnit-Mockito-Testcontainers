package net.junit.springboot.repository;

import net.junit.springboot.integration.AbstractionContainerBaseTest;
import net.junit.springboot.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace=AutoConfigureTestDatabase.Replace.NONE)
public class EmployeeRepositoryITTests extends AbstractionContainerBaseTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setUp(){
        employee = Employee.builder()
                .firstName("Ank01")
                .lastName("Tiwari")
                .email("ank01@gmail.com")
                .build();
    }

    // JUnit test for save employee operation
    //@DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isGreaterThan(0);

    }

    // Junit test for get all employees operation
    @DisplayName("Junit test for get all employees operation")
    @Test
    public void givenEmployeeList_whenFindAll_thenEmployeeList() {
        // given- precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();

        Employee employee1 = Employee.builder()
                .firstName("akt")
                .lastName("wl")
                .email("akt@gmail.com")
                .build();
        employeeRepository.save(employee);
        employeeRepository.save(employee1);

        // when - action or the behaviour that we are going test

        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);

    }

    // Junit test for get employee by id operation
    @DisplayName("Junit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {
        // given- precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee employeeDB = employeeRepository.findById(employee.getId()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();


    }

    // Junit test for get employee by email operation
    @DisplayName("Junit test for get employee by email operation")
    @Test
    public void givenEmployeeEnail_whenFindByEmail_thenEmployeeObject() {
        // given- precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee employeeDB = employeeRepository.findByEmail(employee.getEmail()).get();

        // then - verify the output
        assertThat(employeeDB).isNotNull();


    }

    // Junit test for update employee operation
    @DisplayName("Junit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {
        // given- precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("ram@gmail.com");
        savedEmployee.setFirstName("Ram");

        Employee updatedEmployee = employeeRepository.save(savedEmployee);


        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("ram@gmail.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Ram");


    }

    // Junit test for delete employee operation
    @Test
    public void givenEmployeeObject_whenDelete_thenRemoveEmployee() {
        // given- precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        employeeRepository.delete(employee);
        Optional<Employee> employeeOptional = employeeRepository.findById(employee.getId());

        // then - verify the output
        assertThat(employeeOptional).isEmpty();


    }

    // Junit test for custom query using JPQL with index
    @DisplayName("Junit test for custom query using JPQL with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQL_thenReturnEmployeeObject() {
        // given- precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();
        employeeRepository.save(employee);

        String firstName = "Ank01";
        String lastName = "Tiwari";

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQL(firstName, lastName);


        // then - verify the output
        assertThat(savedEmployee).isNotNull();


    }

    // Junit test for custom query using JPQL with Named params
    @DisplayName("Junit test for custom query using JPQL with Named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {
        // given- precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();
        employeeRepository.save(employee);

        String firstName = "Ank01";
        String lastName = "Tiwari";

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();


    }

    // Junit test for custom query using native sql with index
    @DisplayName("Junit test for custom query using native sql with index")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQL_thenReturnEmployeeObject() {
        // given- precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQL(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();

    }

    // Junit test for custom query using native sql with named params
    @DisplayName("Junit test for custom query using native sql with named params")
    @Test
    public void givenFirstNameAndLastName_whenFindByNativeSQLNamed_thenReturnEmployeeObject() {
        // given- precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("Ankesh")
//                .lastName("Tiwari")
//                .email("ankesh@gmail.com")
//                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findByNativeSQLNamed(employee.getFirstName(), employee.getLastName());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();

    }


}
