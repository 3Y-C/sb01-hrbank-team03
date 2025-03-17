package com.sprint.part2.sb1hrbankteam03.controller;

import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentCreateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentGetRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.request.DepartmentUpdateRequest;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.CursorPageResponseChangeLogDto;
import com.sprint.part2.sb1hrbankteam03.dto.department.respons.DepartmentDto;
import com.sprint.part2.sb1hrbankteam03.service.DepartmentService;
import java.util.Map;
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
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
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
    DepartmentDto Response= departmentService.update(id,departmentUpdateRequest);
    return ResponseEntity.status(HttpStatus.OK).body(Response);
  }

//  @GetMapping
//  public ResponseEntity<Map<String, Object>> findDepartments(@ModelAttribute DepartmentGetRequest departmentGetRequest) {
//    Map<String, Object> response = departmentService.findDepartments(
//        departmentGetRequest.nameOrDescription(),
//        departmentGetRequest.idAfter(),
//        departmentGetRequest.cursor(),
//        departmentGetRequest.size(),
//        departmentGetRequest.sortField(),
//        departmentGetRequest.sortDirection()
//    );
//    return ResponseEntity.status(HttpStatus.OK).body(response);
//  }

  @GetMapping
  public ResponseEntity<CursorPageResponseChangeLogDto> findDepartments(@ModelAttribute DepartmentGetRequest departmentGetRequest) {
    CursorPageResponseChangeLogDto response = departmentService.findDepartments(
        departmentGetRequest.nameOrDescription(),
        departmentGetRequest.idAfter(),
        departmentGetRequest.cursor(),
        departmentGetRequest.size(),
        departmentGetRequest.sortField(),
        departmentGetRequest.sortDirection());
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable Long id){

  }




}
