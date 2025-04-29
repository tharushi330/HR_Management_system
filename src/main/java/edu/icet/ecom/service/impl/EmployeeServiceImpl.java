package edu.icet.ecom.service.impl;

import edu.icet.ecom.dto.EmployeeDTO;
import edu.icet.ecom.entity.EmployeeEntity;
import edu.icet.ecom.repository.EmployeeRepository;
import edu.icet.ecom.service.EmployeeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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
        EmployeeEntity entity = modelMapper.map(employeeDTO, EmployeeEntity.class);
        return modelMapper.map(employeeRepository.save(entity), EmployeeDTO.class);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(emp -> modelMapper.map(emp, EmployeeDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        Long id = employeeDTO.getId();
        Optional<EmployeeEntity> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isEmpty()) {
            throw new RuntimeException("Employee not found with id: " + id);
        }

        EmployeeEntity existing = optionalEmployee.get();
        existing.setName(employeeDTO.getName());
        existing.setEmail(employeeDTO.getEmail());
        existing.setDepartment(employeeDTO.getDepartment());

        EmployeeEntity updated = employeeRepository.save(existing);
        return modelMapper.map(updated, EmployeeDTO.class);
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

