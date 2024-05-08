package net.junit.springboot.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.junit.springboot.model.Employee;
import net.junit.springboot.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
public class EmployeeControllerITTest_Old {

    @Container
    private static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest")
            .withUsername("username")
            .withPassword("password")
            .withDatabaseName("ems");

    public static void dynamicPropertySource(DynamicPropertyRegistry registry){
        registry.add("spring.dataSource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.dataSource.password",mySQLContainer::getPassword);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        employeeRepository.deleteAll();
    }

    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {

        System.out.println(mySQLContainer.getUsername());
        System.out.println(mySQLContainer.getPassword());
        System.out.println(mySQLContainer.getDatabaseName());
        System.out.println(mySQLContainer.getJdbcUrl());

        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("Ankesh")
                .lastName("Tiwari")
                .email("ankesh@gmail.com")
                .build();

        // when - action or behaviour that we are going to test

        ResultActions response = mockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the result or output using assert statements
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName",
                        is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName",
                        is(employee.getLastName())))
                .andExpect(jsonPath("$.email",
                        is(employee.getEmail())));

    }

    // Junit test for Get all employees REST API
    @Test
    public void givenListOfEmployees_whenGetAllEmployees_thenReturnEmployeesList() throws Exception {
        // given- precondition or setup
        List<Employee> listOfEmployees = new ArrayList<>();
        listOfEmployees.add(Employee.builder().firstName("Ankesh").lastName("Tiwari").email("ankesh@gmail.com").build());
        listOfEmployees.add(Employee.builder().firstName("Akt").lastName("Tiwari").email("akt@gmail.com").build());

        employeeRepository.saveAll(listOfEmployees);


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees"));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(listOfEmployees.size())));


    }

    // Junit test for GET employee by id REST API
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() throws Exception {
        // given- precondition or setup
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .firstName("Ankesh")
                .lastName("Tiwari")
                .email("ankesh@gmail.com")
                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", employee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(employee.getLastName())))
                .andExpect(jsonPath("$.email", is(employee.getEmail())));
    }

    // Junit test for GET employee by id REST API (negative scenario)
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenReturnEmppty() throws Exception {
        // given- precondition or setup
        long employeeId = 1L;

        Employee employee = Employee.builder()
                .firstName("Ankesh")
                .lastName("Tiwari")
                .email("ankesh@gmail.com")
                .build();

        employeeRepository.save(employee);
        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(get("/api/employees/{id}", 1));

        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // Junit test for update employee REST API
    @Test
    public void givenUpdatedEmployee_whenUpdatedEmployee_thenReturnUpdateEmployeeObject() throws Exception {
        // given- precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Ankesh")
                .lastName("Tiwari")
                .email("ankesh@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Akt")
                .lastName("Tiwari")
                .email("akt@gmail.com")
                .build();


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", savedEmployee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));


        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.firstName", is(updatedEmployee.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(updatedEmployee.getLastName())))
                .andExpect(jsonPath("$.email", is(updatedEmployee.getEmail())));

    }

    // Junit test for update employee REST API (for negative scenario)
    @Test
    public void givenUpdatedEmployee_whenUpdatedEmployee_thenReturn404() throws Exception {
        // given- precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Ankesh")
                .lastName("Tiwari")
                .email("ankesh@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        Employee updatedEmployee = Employee.builder()
                .firstName("Akt")
                .lastName("Tiwari")
                .email("akt@gmail.com")
                .build();


        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(put("/api/employees/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)));


        // then - verify the output
        response.andExpect(status().isNotFound())
                .andDo(print());

    }

    // Junit test for delete employee REST API
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenReturn200() throws Exception {
        // given- precondition or setup
        Employee savedEmployee = Employee.builder()
                .firstName("Ankesh")
                .lastName("Tiwari")
                .email("ankesh@gmail.com")
                .build();
        employeeRepository.save(savedEmployee);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(delete("/api/employees/{id}", savedEmployee.getId()));

        // then - verify the output
        response.andExpect(status().isOk())
                .andDo(print());
    }

}
