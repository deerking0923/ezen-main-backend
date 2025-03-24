# 다독다독 — Layered Architecture 버전 (Backend)

## 한 줄 소개
Layered Architecture로 리팩토링된 읽기 기록 서비스 백엔드 (Production Deployment)

## 프로젝트 상태
✅ Production: AWS EC2에 GitHub Actions CI/CD 파이프라인으로 자동 배포 완료. 로컬 및 프로덕션 환경 모두 정상 작동 중.

## 배포 링크
- API Base URL: https://dadoklog.com/

## 기능 목록
| 기능 | 설명 | 엔드포인트 |
|------|-------|------------|
| 회원 가입 | 신규 사용자 등록 | POST /api/v1/auth/signup |
| 로그인 | JWT HTTP‑Only Cookie 발급 | POST /api/v1/auth/login |
| 로그아웃 | JWT 쿠키 삭제 | POST /api/v1/auth/logout |
| 현재 사용자 조회 | 인증된 사용자 정보 반환 | GET /api/v1/auth/me |
| 질문 CRUD | 질문 생성·조회·수정·삭제 | GET /api/v1/questions, POST /api/v1/questions, PUT /api/v1/questions/{id}, DELETE /api/v1/questions/{id} |
| 답변 CRUD | 답변 생성·수정·삭제 | POST /api/v1/answers, PUT /api/v1/answers/{id}, DELETE /api/v1/answers/{id} |
| 도서 기록 CRUD | 독서 기록 생성·조회·수정·삭제 | GET /api/v1/books, POST /api/v1/books, PUT /api/v1/books/{id}, DELETE /api/v1/books/{id} |
| 개인 도서관 | 사용자별 도서 목록 조회/관리 | GET /mylibrary-service/booklist, POST /mylibrary-service/{userId}/create |
| 최신 리뷰 조회 | 최근 리뷰 목록 조회 | GET /mylibrary-service/reviews/recent |


## 기술 스택
| Category | Technologies |
|----------|--------------|
| Backend | Java 17, Spring Boot, Spring Data JPA|
| Architecture | Layered Architecture (Controller, Service, Repository) |
| Database | MySQL (RDS) |
| Security | JWT (HTTP-Only Cookie) |
| Deployment & CI/CD | Docker, GitHub Actions, AWS EC2 |
| Build Tools | Maven |
| Collaboration | Git/GitHub, Jira, Notion, Postman |
| Dev Environment | IntelliJ IDEA, VSCODE |

## ⚙️ 로컬 실행
1️⃣ 리포지토리 클론
```bash
git clone https://github.com/deerking0923/ezen-main-backend.git
cd ezen-main-springcloud
```
2️⃣ 환경 변수 설정 (`.env`)
```dotenv
DB_URL=jdbc:mysql://localhost:3306/dadok           # 로컬 MySQL 연결 URL
DB_USERNAME=root                                  # 데이터베이스 사용자명
DB_PASSWORD=your_password                         # 데이터베이스 비밀번호
BASE_IP=http://localhost:8080                     # 애플리케이션 기본 URL
SERVER_PORT=8080                                  # 애플리케이션 포트
JWT_SECRET=your_jwt_secret                        # JWT 서명 키 (복잡하고 안전하게 설정)
JWT_EXPIRATION_MS=86400000                        # JWT 만료 시간(ms), 예: 24시간 = 86,400,000
SPRING_PROFILES_ACTIVE=local                      # Spring 프로파일 (local 또는 prod)
```
3️⃣ 애플리케이션 실행
```bash
./mvnw spring-boot:run
```

## 학습 포인트
- Layered Architecture 적용으로 코드 구조 개선
- JWT HTTP-Only Cookie 인증 구현
- GitHub Actions를 활용한 Docker 기반 CI/CD 파이프라인 구축
- AWS EC2 프로덕션 배포 경험


