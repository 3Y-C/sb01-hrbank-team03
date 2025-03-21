# 1-HRBank-3Y-C
https://angry-mile-4b9.notion.site/Team-3-19aa788b97e0802b91d9c7cf63aafb61?pvs=4

## 팀원 구성
| 이름 | 역할 |
| --- | --- |
| 유진 | 팀장 백업 관리 기능 구현 |
| 용구 | 이력 관리 및 파일 기능 구현 |
| 유일 | 부서 관리 기능 구현 |
| 창우 | 직원 정보 관리 구현 |
### 🔗 개인 Github 링크

- 유진: [github.com/yujin-dev](https://github.com/yujin-dev)
- 용구: [github.com/yonggu-dev](https://github.com/yonggu-dev)
- 유일: [github.com/youil-dev](https://github.com/youil-dev)
- 창우: [github.com/changwoo-dev](https://github.com/changwoo-dev)
## 프로젝트 소개
- **프로젝트 명:** HR Bank
- **설명:** 기업의 인사 관리를 위한 Spring 기반 백엔드 시스템 개발
- **주요 기능:** 부서/직원 관리, 수정 이력, 파일 관리, 대시보드, 정기 백업 시스템 등
- **프로젝트 기간:** 2024.03.14 ~ 2024.03.24
## 기술 스택
- **Backend:** Spring Boot, Spring Data JPA, MapStruct, Spring Scheduler
- **Database:** PostgreSQL (배포), H2 (개발)
- **Tool:** Git & GitHub, Discord, Notion, Railway.io, Swagger, Figma, rander

## 팀원별 구현 기능 상세
#### 유진
- 데이터 백업 생성 (POST /api/backups)
- 백업 이력 목록 조회 (GET /api/backups)
- 마지막 백업 시간 조회 (GET /api/backups/latest)
- 데이터 백업 및 로그 저장 배치 처리
- Spring Scheduler 기반 주기적 백업 구현
#### 용구
- 파일 저장 및 다운로드 (GET /api/files/{id}/download)
- 직원 이력 등록/상세 조회 (GET /api/change-logs/{id}/diffs)
- 수정 이력 건수 조회 (GET /api/change-logs/count)
- 직원 정보 수정 이력 관리 기능
#### 유일
- Swagger 기반 API 명세서 설계 및 문서화
- 부서 등록 - POST /api/departments부서
- 수정 - PATCH /api/departments/{id}
- 부서 삭제 - DELETE /api/departments/{id}
- 부서 목록 조회 - GET /api/departments
- 부서 상세 조회 - GET /api/departments/{id}
#### 창우
- 직원 등록/수정/삭제/조회 기능
- 직원 통계 API
    - 직원수 추이 조회 (GET /api/employees/stats/trend)
    - 직원 분포 조회 (GET /api/employees/stats/distribution)
    - 직원 수 조회 (GET /api/employees/count)
## 파일 구조 
```
com.sprint.part2.sb1hrbankteam03
├── Sb1HrbankTeam03Application.java
├── config
│   ├── SwaggerConfig.java
│   └── api
│       └── DepartmentApi.java
├── controller
│   ├── BackupController.java
│   ├── DepartmentController.java
│   ├── EmployeeController.java
│   ├── EmployeeHistoryController.java
│   └── FileMetaDataController.java
├── dto
│   ├── ErrorResponse.java
│   ├── backup
│   │   ├── BackupDto.java
│   │   ├── CursorPageResponseBackupDto.java
│   │   ├── ParsedBackupDto.java
│   │   └── RequestBackupDto.java
│   ├── department
│   │   ├── request
│   │   │   ├── DepartmentCreateRequest.java
│   │   │   ├── DepartmentGetRequest.java
│   │   │   └── DepartmentUpdateRequest.java
│   │   └── respons
│   │       ├── CursorPageResponseDepartmentDto.java
│   │       └── DepartmentDto.java
│   ├── employee
│   │   ├── CursorPageResponseEmployeeDto.java
│   │   ├── EmployeeCreateRequest.java
│   │   ├── EmployeeDistributionDto.java
│   │   ├── EmployeeDto.java
│   │   ├── EmployeeTrendDto.java
│   │   └── EmployeeUpdateRequest.java
│   └── employeeHistory
│       ├── ChangeLogDto.java
│       ├── CursorPageResponseChangeLogDto.java
│       ├── DiffDto.java
│       ├── EmployeeChangeInfo.java
│       └── EmployeeSnapshotDto.java
├── entity
│   ├── Backup.java
│   ├── BackupStatus.java
│   ├── BaseEntity.java
│   ├── BaseUpdatableEntity.java
│   ├── ChangeType.java
│   ├── Department.java
│   ├── Employee.java
│   ├── EmployeeChangeDetail.java
│   ├── EmployeeHistory.java
│   ├── FileCategory.java
│   ├── FileMetaData.java
│   └── Status.java
├── exception
│   └── GlobalExceptionHandler.java
├── mapper
│   ├── BackupMapper.java
│   ├── DepartmentMapper.java
│   ├── EmployeeChangeDetailMapper.java
│   ├── EmployeeHistoryMapper.java
│   └── EmployeeMapper.java
├── repository
│   ├── BackupRepository.java
│   ├── DepartmentRepository.java
│   ├── EmployeeChangeDetailRepository.java
│   ├── EmployeeHistoryRepository.java
│   ├── EmployeeHistoryRepositoryCustom.java
│   ├── EmployeeHistoryRepositoryImpl.java
│   ├── EmployeeRepository.java
│   ├── EmployeeRepositoryCustom.java
│   ├── EmployeeRepositoryCustomImpl.java
│   └── FileMetaDataRepository.java
├── scheduler
│   └── BackupScheduler.java
├── service
│   ├── BackupService.java
│   ├── BackupServiceImpl.java
│   ├── DepartmentService.java
│   ├── DepartmentServiceImpl.java
│   ├── EmployeeHistoryService.java
│   ├── EmployeeHistoryServiceImpl.java
│   ├── EmployeeService.java
│   ├── EmployeeServiceImpl.java
│   ├── FileMetaDataService.java
│   └── FileMetaDataServiceImpl.java
└── stroage
    ├── FileStorage.java
    └── LocalFileStorage.java

```

## 구현 홈페이지
- https://sb01-hrbank-team03.onrender.com/ 
- https://powerful-elegance-production.up.railway.app/
## 프로젝트 회고
(제작한 발표자료 링크 혹은 첨부파일 첨부)
