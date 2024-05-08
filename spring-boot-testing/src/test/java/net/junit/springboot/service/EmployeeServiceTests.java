package net.junit.springboot.service;

import net.junit.springboot.exception.ResourceNotFoundException;
import net.junit.springboot.model.Employee;
import net.junit.springboot.repository.EmployeeRepository;
import net.junit.springboot.service.impl.EmployeeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setup() {
//        employeeRepository = Mockito.mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("Ankesh")
                .lastName("Tiwari")
                .email("ankesh@gmail.com")
                .build();
    }

    // Junit test for saveEmployee method
    @DisplayName("Junit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given- precondition or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.empty());

        given(employeeRepository.save(employee)).willReturn(employee);
        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);
        System.out.println(savedEmployee);

        // then - verify the output
        Assertions.assertThat(savedEmployee).isNotNull();


    }

    // Junit test for saveEmployee method which throw exception
    @DisplayName("Junit test for saveEmployee method which throw exception")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenThrowException() {
        // given- precondition or setup

        given(employeeRepository.findByEmail(employee.getEmail()))
                .willReturn(Optional.of(employee));

        //given(employeeRepository.save(employee)).willReturn(employee);
        System.out.println(employeeRepository);
        System.out.println(employeeService);

        // when - action or the behaviour that we are going test
        org.junit.jupiter.api.Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });
        // then
        verify(employeeRepository, never()).save(any(Employee.class));

    }

    // Junit test for getAllEmployees method
    @DisplayName("Junit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnAllEmployeesList() {
        // given- precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("akt")
                .lastName("Tiwari")
                .email("akt@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(List.of(employee, employee1));

        // when - action or the behaviour that we are going test

        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isGreaterThan(1);

    }


    // Junit test for getAllEmployees method (for negative scenario)
    @DisplayName("Junit test for getAllEmployees method (for negative scenario)")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyEmployeesList() {
        // given- precondition or setup
        Employee employee1 = Employee.builder()
                .id(2L)
                .firstName("akt")
                .lastName("Tiwari")
                .email("akt@gmail.com")
                .build();
        given(employeeRepository.findAll()).willReturn(Collections.emptyList());

        // when - action or the behaviour that we are going test

        List<Employee> employeeList = employeeService.getAllEmployees();

        // then - verify the output
        Assertions.assertThat(employeeList).isNotNull();
        Assertions.assertThat(employeeList.size()).isEqualTo(0);

    }

    // JUnit test for getEmployeeById
    @DisplayName("JUnit test for getEmployeeById")
    @Test
    public void givenEmployeeId_whenGetEmployeeByID_thenReturnEmployeeObject() {
        //given
        given(employeeRepository.findById(1L)).willReturn(Optional.of(employee));

        //when
        Employee savedEmployee = employeeService.getEmployeeById(employee.getId()).get();

        //then
        Assertions.assertThat(savedEmployee).isNotNull();


    }

    // Junit test for updateEmployee method
    @DisplayName("Junit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmplyee_thenReturnEmployeeObject() {
        // given- precondition or setup
        given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("shyam@gmail.com");
        employee.setFirstName("Shyam");

        // when - action or the behaviour that we are going test
        Employee updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        Assertions.assertThat(updatedEmployee.getFirstName()).isEqualTo("Shyam");
        Assertions.assertThat(updatedEmployee.getEmail()).isEqualTo("shyam@gmail.com");


    }

    // Junit test for deleteEmployee method
    @DisplayName("Junit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNothing() {
        // given- precondition or setup
        long employeeId = 1L;
        willDoNothing().given(employeeRepository).deleteById(employeeId);

        // when - action or the behaviour that we are going test
        employeeService.deleteEmployee(employeeId);

        // then - verify the output
        verify(employeeRepository, times(1)).deleteById(employeeId);

    }

}
