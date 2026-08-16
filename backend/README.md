# WorkSaathi Backend

A comprehensive Spring Boot backend for the WorkSaathi local worker hiring and service platform.

## 🚀 Features

- **User Management**: Customer, Worker, and Admin roles with comprehensive authentication
- **Worker Profiles**: Professional profiles with skills, availability, and verification
- **Job Management**: Complete job lifecycle from request to completion
- **Real-time Updates**: WebSocket support for live job tracking and notifications
- **Payment System**: Payment tracking and management
- **Review System**: Customer reviews and worker ratings
- **Admin Dashboard**: User management, worker verification, and report handling
- **Location Services**: Nearby worker search with geospatial queries
- **Security**: JWT authentication, role-based access control, and rate limiting

## 🛠️ Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.2.0
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **Cache**: Redis
- **Security**: Spring Security with JWT
- **Real-time**: WebSocket/STOMP
- **API Documentation**: Swagger/OpenAPI
- **File Storage**: Cloudinary
- **Testing**: JUnit, Mockito, MockMvc
- **Containerization**: Docker

## 📋 Prerequisites

- Java 21 or higher
- Maven 3.6+
- PostgreSQL 15+
- Redis 7+
- Docker (optional, for containerized deployment)

## 🔧 Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd worksaathi-backend
```

### 2. Configure Environment Variables

Create a `.env` file in the project root (copy from `.env.example`):

```bash
cp .env.example .env
```

Update the following environment variables in `.env`:

```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/worksaathi
DB_USERNAME=postgres
DB_PASSWORD=your_secure_password

# JWT
JWT_SECRET=your-very-secure-jwt-secret-key-at-least-256-bits
JWT_EXPIRATION=86400000
JWT_REFRESH_EXPIRATION=604800000

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379
REDIS_PASSWORD=

# Cloudinary (for file storage)
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Email
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# Server
SERVER_PORT=8080
```

### 3. Setup Database

#### Option A: Using Docker (Recommended)

```bash
docker-compose up -d postgres redis
```

#### Option B: Manual Setup

1. Install PostgreSQL and Redis locally
2. Create a database named `worksaathi`
3. Update the database credentials in `application.properties`

### 4. Build and Run

#### Using Maven

```bash
# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

#### Using Docker

```bash
# Build and run all services
docker-compose up -d

# View logs
docker-compose logs -f backend
```

### 5. Access API Documentation

Once the application is running, access Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

## 📁 Project Structure

```
worksaathi-backend/
├── src/
│   ├── main/
│   │   ├── java/com/worksaathi/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Exception handling
│   │   │   ├── repository/      # JPA repositories
│   │   │   ├── security/        # Security configuration
│   │   │   ├── service/         # Business logic
│   │   │   ├── websocket/       # WebSocket controllers
│   │   │   └── WorkSaathiApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       └── application-dev.properties
│   └── test/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## 🔌 API Endpoints

### Authentication
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/refresh` - Refresh access token
- `POST /api/v1/auth/forgot-password` - Request password reset
- `POST /api/v1/auth/reset-password` - Reset password

### Users
- `GET /api/v1/users/me` - Get current user profile
- `PUT /api/v1/users/me` - Update current user profile
- `DELETE /api/v1/users/me` - Delete account
- `GET /api/v1/users/{id}` - Get user by ID

### Workers
- `GET /api/v1/workers/me` - Get worker profile
- `POST /api/v1/workers/profile` - Create worker profile
- `PUT /api/v1/workers/profile` - Update worker profile
- `PUT /api/v1/workers/availability` - Update availability
- `GET /api/v1/workers` - Search workers
- `GET /api/v1/workers/nearby` - Find nearby workers
- `GET /api/v1/workers/{id}` - Get worker by ID

### Jobs
- `POST /api/v1/jobs` - Create job
- `GET /api/v1/jobs` - Get customer jobs
- `POST /api/v1/jobs/{id}/accept` - Accept job
- `POST /api/v1/jobs/{id}/reject` - Reject job
- `POST /api/v1/jobs/{id}/start` - Start job
- `POST /api/v1/jobs/{id}/complete` - Complete job
- `POST /api/v1/jobs/{id}/cancel` - Cancel job
- `POST /api/v1/jobs/{id}/review` - Review job

### Payments
- `POST /api/v1/payments` - Create payment
- `GET /api/v1/payments/job/{jobId}` - Get payment by job
- `GET /api/v1/payments/my-payments` - Get user payments

### Admin
- `GET /api/v1/admin/dashboard` - Dashboard statistics
- `GET /api/v1/admin/users` - Get all users
- `GET /api/v1/admin/workers` - Get all workers
- `PUT /api/v1/admin/workers/{id}/verify` - Verify worker
- `PUT /api/v1/admin/workers/{id}/reject` - Reject worker
- `GET /api/v1/admin/reports` - Get reports
- `PUT /api/v1/admin/reports/{id}/resolve` - Resolve report

## 🔐 Security

### Authentication
- JWT-based authentication
- Access tokens (15 minutes expiration)
- Refresh tokens (7 days expiration)
- BCrypt password hashing

### Authorization
- Role-based access control (RBAC)
- Resource ownership verification
- Admin-only endpoints protection

### Security Headers
- CORS configuration
- Rate limiting (Redis-based)
- Input validation
- SQL injection prevention (JPA)

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=AuthServiceTest

# Run with coverage
mvn test jacoco:report
```

## 🚢 Deployment

### Docker Deployment

```bash
# Build Docker image
docker build -t worksaathi-backend .

# Run with Docker Compose
docker-compose up -d

# Check logs
docker-compose logs -f backend
```

### Environment-Specific Configuration

The application supports multiple profiles:

- `dev` - Development environment
- `prod` - Production environment

Set the active profile:

```bash
export SPRING_PROFILES_ACTIVE=prod
```

## 📊 Database Schema

### Core Tables
- `users` - User accounts
- `workers` - Worker profiles
- `services` - Service categories
- `worker_skills` - Worker-service mappings
- `jobs` - Job requests
- `job_status_history` - Job status tracking
- `reviews` - Customer reviews
- `payments` - Payment records
- `notifications` - User notifications
- `conversations` - Chat conversations
- `messages` - Chat messages
- `worker_documents` - Worker verification documents
- `reports` - User reports
- `audit_logs` - System audit trail

## 🔧 Configuration

### Application Properties

Key configuration options in `application.properties`:

```properties
# Server
server.port=8080

# Database
spring.datasource.url=jdbc:postgresql://localhost:5432/worksaathi
spring.datasource.username=postgres
spring.datasource.password=postgres

# JWT
jwt.secret=your-secret-key
jwt.expiration=86400000

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

## 🐛 Troubleshooting

### Common Issues

1. **Database Connection Error**
   - Ensure PostgreSQL is running
   - Check database credentials in `.env`
   - Verify database exists

2. **Redis Connection Error**
   - Ensure Redis is running
   - Check Redis configuration in `application.properties`

3. **JWT Token Issues**
   - Verify JWT_SECRET is set correctly
   - Check token expiration settings

4. **Port Already in Use**
   - Change `server.port` in `application.properties`
   - Or stop the process using port 8080

## 📝 Development Guidelines

### Code Style
- Follow Java naming conventions
- Use meaningful variable names
- Add Javadoc comments for public methods
- Keep methods focused and small

### Git Workflow
1. Create feature branch from `main`
2. Make changes and commit
3. Push and create pull request
4. Get approval and merge

### Commit Messages
```
feat: add worker profile creation
fix: resolve job status update bug
docs: update API documentation
test: add integration tests
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Team

WorkSaathi Development Team

## 📞 Support

For support, email support@worksaathi.com or create an issue in the repository.

---

**Note**: This is a backend-only project. For the complete WorkSaathi platform, you'll also need to set up the React frontend application.
