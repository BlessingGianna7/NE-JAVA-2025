package com.example.erp.repository;

import com.example.erp.model.Employee;
import com.example.erp.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByEmployee(Employee employee);
    List<Message> findByEmployeeAndMonthAndYear(Employee employee, Integer month, Integer year);
    List<Message> findByMonthAndYear(Integer month, Integer year);
    List<Message> findBySent(Boolean sent);
}