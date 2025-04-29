package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.EmployeeDTO;
import edu.icet.ecom.entity.EmployeeEntity;
import edu.icet.ecom.repository.EmployeeRepository;
import edu.icet.ecom.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    final EmployeeRepository employeeRepository;
    final ModelMapper modelMapper;


    @Override
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new IllegalArgumentException("Email already exists!");
        }
        EmployeeEntity entity = modelMapper.map(employeeDTO, EmployeeEntity.class);

        entity.setCreatedAt(LocalDateTime.now());
        EmployeeEntity savedEntity = employeeRepository.save(entity);
        return modelMapper.map(savedEntity, EmployeeDTO.class);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(emp -> modelMapper.map(emp, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        Optional<EmployeeEntity> optionalEmployee = employeeRepository.findById(employeeDTO.getId());
        if (optionalEmployee.isPresent()) {
            EmployeeEntity existingEmployee = optionalEmployee.get();

            if (!existingEmployee.getEmail().equals(employeeDTO.getEmail()) && employeeRepository.existsByEmail(employeeDTO.getEmail())) {
                throw new IllegalArgumentException("Email already exists!");
            }

            existingEmployee.setName(employeeDTO.getName());
            existingEmployee.setEmail(employeeDTO.getEmail());
            existingEmployee.setDepartment(employeeDTO.getDepartment());
            existingEmployee.setUpdatedAt(LocalDateTime.now());

            employeeRepository.save(existingEmployee);
            return modelMapper.map(existingEmployee, EmployeeDTO.class);
        } else {
            throw new RuntimeException("Employee with ID " + employeeDTO.getId() + " not found");
        }
    }

    @Override
        public void deleteEmployee (Long id){
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
            }
        employeeRepository.deleteById(id);
        }

        @Override
        public EmployeeDTO searchEmployeeById (Long id){
            EmployeeEntity entity = employeeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
            return modelMapper.map(entity, EmployeeDTO.class);
        }

    }

