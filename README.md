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
- 직원 정보 수정 이력 목록 조회 (GET /api/change-logs)
- 직원 상세 조회 (GET /api/change-logs/{id}/diffs)
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
sb1hrbankteam03
            ├─common
            │  └─util
            ├─config
            │  └─api
            ├─controller
            ├─dto
            │  ├─backup
            │  ├─department
            │  │  ├─request
            │  │  └─respons
            │  ├─employee
            │  └─employeeHistory
            ├─entity
            │  ├─base
            │  └─enums
            ├─exception
            ├─mapper
            ├─repository
            │  └─custom
            ├─scheduler
            ├─service
            │  └─implement
            └─stroage

```

## 구현 홈페이지
- https://sb01-hrbank-team03.onrender.com/ 
- https://powerful-elegance-production.up.railway.app/
## 프로젝트 회고
- https://angry-mile-4b9.notion.site/1bda788b97e08060a498da599856397d?pvs=4
- ppt
https://www.figma.com/slides/wJR7TTpLLigGLSkipIKbzB/hrbank-3y-c?node-id=26-606&t=9hJTr6VY08hB0x00-1
- 시연영상
