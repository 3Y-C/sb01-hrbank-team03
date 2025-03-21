package com.sprint.part2.sb1hrbankteam03.controller;

import com.sprint.part2.sb1hrbankteam03.config.api.DepartmentApi;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentGetRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.CursorPageResponseDepartmentDto;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentDto;
import com.sprint.part2.sb1hrbankteam03.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController implements DepartmentApi {
  private final DepartmentService departmentService;

  //부서 생성
  @PostMapping
  public ResponseEntity<DepartmentDto> create(@RequestBody DepartmentCreateRequest departmentCreateRequest){
    DepartmentDto department = departmentService.create(departmentCreateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(department);
  }

  //부서 수정
  @PatchMapping("/{id}")
  public ResponseEntity<DepartmentDto> update(@PathVariable Long id,
      @RequestBody DepartmentUpdateRequest departmentUpdateRequest){
    DepartmentDto response= departmentService.update(id,departmentUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseDepartmentDto> findDepartments(
      @RequestParam(required = false, defaultValue = "") String nameOrDescription,
      @RequestParam(required = false, defaultValue = "0") Long idAfter,
      @RequestParam(required = false, defaultValue = "") String cursor,
      @RequestParam(required = false, defaultValue = "20") int size,
      @RequestParam(required = false, defaultValue = "name") String sortField,
      @RequestParam(required = false, defaultValue = "asc") String sortDirection) {

    CursorPageResponseDepartmentDto response = departmentService.findDepartments(
        nameOrDescription, idAfter, cursor, size, sortField, sortDirection);

    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    departmentService.delete(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDto> findById(@PathVariable Long id){
    DepartmentDto response = departmentService.findById(id);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }





}
