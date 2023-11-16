
![제목을-입력해주세요_ (1)](https://github.com/prgrms-be-devcourse/BE-04-HiMin/assets/29273437/b2f98c45-144a-424e-8177-fce4c5acb1e3)

## 히히의 민환족
> 배달 주문 서비스 API

## 📣 프로젝트 목적
> 배달의 민족 서비스를 클론 코딩하여 스프링 학습
> 
> 프로젝트 기간 : 2023/08/28 ~ 2023/09/22

## 🧐 팀원소개
|Product Owner|Scrum Master|                                   Developer                                    |
|:---:|:---:|:------------------------------------------------------------------------------:|
|[강병곤](https://github.com/Curry4182)|[구범모](https://github.com/BeommoKoo-dev)|                       [박이슬](https://github.com/Yiseull)                        |
|<img src="https://avatars.githubusercontent.com/u/29273437?v=4" width="300" />|<img src="https://avatars.githubusercontent.com/u/95630007?v=4" width="300" />| <img src="https://avatars.githubusercontent.com/u/98391539?v=4" width="300" /> |

## 🛠 기술스택
### 개발 환경
  <img src="https://img.shields.io/badge/Java17-007396?style=flat-square&logo=Java&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Spring Boot 3.1.3-6DB33F?style=flat-square&logo=Spring&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/-Spring Data JPA-gray?logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Query DSL-0078D4?style=flat-square&logo=Spring Data JPA&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/MySQL 8-4479A1?style=flat-square&logo=MySQL&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Gradle-4429A1?style=flat-square&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Junit-25A162?style=flat-&logo=JUnit5&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/AWS-%23FF9900.svg?style=flat-square&logo=amazon-aws&logoColor=white&style=flat"/>
  <img src="https://img.shields.io/badge/Spring Security-6DB33F?style=flat-square&logo=spring-security&logoColor=white&style=flat"/></a>
  
### 협업 툴
  <img src="https://img.shields.io/badge/Notion-FFFFFF?style=flat-square&logo=Notion&logoColor=black"/></a>
  <img src="https://img.shields.io/badge/slack-232F3E?style=flat-square&logo=slack&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Github-000000?style=flat-square&logo=Github&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/Jira-0052CC?style=flat-square&logo=Jira%20software&logoColor=white&style=flat"/></a>

### 기타
  <img src="https://img.shields.io/badge/IntelliJ IDEA-8A3391?style=flat-square&logo=IntelliJ IDEA&logoColor=black&style=flat"/></a>
  <img src="https://img.shields.io/badge/ERDCloud-4429A7?style=flat-square&logoColor=white&style=flat"/></a>
  <img src="https://img.shields.io/badge/REST Docs-8CA1AF?style=flat-square&logo=Read the Docs&logoColor=white&style=flat">

## 📋 시스템 구조
<p align="center">
 <img src="https://github.com/prgrms-be-devcourse/BE-04-HiMin/assets/29273437/cb86fe6e-a530-4285-9ce3-f7bde484aa7a" width="900" height="400"/>
</p>

## 📋 클래스 의존성 다이어그램
<p align="center">
  <img src="https://github.com/prgrms-be-devcourse/BE-04-HiMin/assets/29273437/94f09c83-7028-495a-8cfa-2ac4d40adacd" width="900" height="400"/>
</p>

## 📋 ERD 다이어 그램
<p align="center">
  <img src="https://github.com/prgrms-be-devcourse/BE-04-HiMin/assets/29273437/ed8025c8-e3e6-43da-93d3-29c187bb6482" width="900" height="400"/>
</p>

## 🎋 Git 브랜치 전략
<p align="center">
    <img src="https://github.com/prgrms-be-devcourse/BE-04-HiMin/assets/29273437/ceeda2bb-cc43-441d-aadb-a373d2e53c33" width="400" height="200"/>
</p>
<ul>
  <li>
    저장소를 효과적으로 활용하기 위해 곤모슬팀은 Github-Flow 전략 브랜치를 생성한다.
  </li>
  <li>
    Main : 프로덕션 코드. 항상 Main을 기준으로 branch 생성
  </li>
  <li>
    {이슈번호}-{개발자 이름}-{개발할 기능 이름} : 이슈마다 브랜치를 생성하여 기능 개발 완료 이후 main에 merge
  </li>
</ul>

## 📁 패키지 구조
- `global` : 도메인 전체에 적용되는 base entity, config 등을 담고있는 패키지
- `api` : 레이어 아키텍쳐 중 컨트롤러가 위치한 패키지.
- `application` : 레이어 아키텍쳐 중 서비스가 위치한 패키지.
- `domain` : 각 도메인 엔티티와 레포지토리, 도메인에 필요한 enum들을 갖고 있는 패키지.
- `dto` : `request`, `response` 두개로 패키지가 나뉘며, 각각 요청, 응답 dto를 갖고 있음.
<details>
<summary><h4> 📌 상세 보기</h4></summary>

```bash
.
├── main
│   ├── java
│   │   └── com
│   │       └── prgrms
│   │           └── himin
│   │               ├── delivery
│   │               │   ├── api
│   │               │   ├── application
│   │               │   ├── domain
│   │               │   └── dto
│   │               │       ├── request
│   │               │       └── response
│   │               ├── global
│   │               │   ├── common
│   │               │   ├── config
│   │               │   │   └── security
│   │               │   │       └── jwt
│   │               │   ├── error
│   │               │   │   └── exception
│   │               │   └── util
│   │               ├── member
│   │               │   ├── api
│   │               │   ├── application
│   │               │   ├── domain
│   │               │   └── dto
│   │               │       ├── request
│   │               │       └── response
│   │               ├── menu
│   │               │   ├── api
│   │               │   ├── application
│   │               │   ├── domain
│   │               │   └── dto
│   │               │       ├── request
│   │               │       └── response
│   │               ├── order
│   │               │   ├── api
│   │               │   ├── application
│   │               │   ├── domain
│   │               │   ├── dto
│   │               │   │   ├── request
│   │               │   │   └── response
│   │               │   └── event
│   │               └── shop
│   │                   ├── api
│   │                   ├── application
│   │                   ├── dao
│   │                   ├── domain
│   │                   └── dto
│   │                       ├── request
│   │                       └── response
│   └── resources
│       ├── static
│       └── template
└── test
    ├── java
    │   └── com
    │       └── prgrms
    │           └── himin
    │               ├── delivery
    │               │   └── application
    │               ├── member
    │               │   ├── api
    │               │   ├── application
    │               │   └── domain
    │               ├── menu
    │               │   ├── api
    │               │   ├── application
    │               │   └── domain
    │               ├── order
    │               │   ├── api
    │               │   └── application
    │               ├── setup
    │               │   ├── domain
    │               │   ├── factory
    │               │   └── request
    │               └── shop
    │                   ├── api
    │                   └── application
    └── resources
```
</details>

## 🔑 Github Secret

| AWS_ACCESS_KEY_ID | 액세스 키 ID |
| --- | --- |
| AWS_PRIVATE_ACCESS_KEY | 비밀 액세스 키 |
| DATABASE_PROPERTIES | datasource url, user 등을 포함한 파일 |
| MYSQL_PASSWORD | datasource password 포함한 파일 |

## 📚 팀 노션
### [노션 페이지 바로가기](https://www.notion.so/Team-5ea5574118ad4bec8ce2f8505f6494d4?pvs=21)

## 👓 Api 명세서
### [Api 명세서 바로가기](https://prgrms-be-devcourse.github.io/BE-04-HiMin/)
